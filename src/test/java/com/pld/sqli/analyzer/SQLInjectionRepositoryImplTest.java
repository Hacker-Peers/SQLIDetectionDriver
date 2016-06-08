package com.pld.sqli.analyzer;

import com.pld.sqli.config.ISQLIAnalyzerConfig;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionRepositoryImplTest {
    private static final String AN_ENTRY_POINT = "my.entry.point";
    private static final String ANOTHER_ENTRY_POINT = "another.entry.point";
    private static final String A_SIMPLE_SQL = "select sysdate from dual";
    private static final String ANOTHER_SIMPLE_SQL = "select systimestamp from dual";
    private static final Integer THREE_VARIATIONS = 3;
    private static final Integer ONE_VARIATIONS = 1;

    @Mock
    private ISQLIAnalyzerConfig cfg;

    private SQLInjectionRepositoryImpl repo;


    @BeforeMethod
    void givenARepo() {
        initMocks(this); //or else mocks are going to stay null when setting up the analyzer.

        repo = spy(new SQLInjectionRepositoryImpl(cfg));
    }

    @Test
    public void whenRepoIsNewItContainsNoEntries() throws Exception {
        assertThat(repo.getCurrentBufferSize(), is(0));
    }

    @Test
    public void whenRepoIsNewItIsNotFlushing() throws Exception {
        assertThat(repo.isFlushing(), is(false));
    }

    @Test
    public void whenAddingEntryToRepoItIsAdded() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);

        assertThat(repo.getCurrentBufferSize(), is(1));
    }

    @Test
    public void whenAddingEntryToRepoItContainsProperValues() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);

        assertThat(repo.getEntries().iterator().next(), new SQLInjectionAnalyzerEntryMatcher(AN_ENTRY_POINT, A_SIMPLE_SQL, 1, THREE_VARIATIONS));
    }

    @Test
    public void whenAddingMatchingEntryToRepoItUpdatesExistingEntry() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATIONS);

        assertThat(repo.getCurrentBufferSize(), is(1));
        SQLInjectionAnalyzerEntry entry = repo.getEntries().iterator().next();
        assertThat(entry, new SQLInjectionAnalyzerEntryMatcher(AN_ENTRY_POINT, A_SIMPLE_SQL, 2, ONE_VARIATIONS, THREE_VARIATIONS));
    }

    @Test
    public void whenAddingEntryRepoChecksIfItNeedsToFlushBufferWithoutForcing() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);

        verify(repo, times(1)).flushIfNecessary(false);
    }

    @Test
    public void whenRepoIsSetFlushingItReportsFlushing() throws Exception {
        repo.setFlushing(true);

        assertThat(repo.isFlushing(), is(true));
    }

    @Test
    public void whenRepoIsNotUsingDiskStorageItNeverReportsHavingToFlush() throws Exception {
        when(cfg.isAnalyzerUseDiskStorage()).thenReturn(false);

        when(cfg.getAnalyzerMaxSizeInMemory()).thenReturn(2);
        when(repo.getCurrentBufferSize()).thenReturn(3);
        when(repo.isFlushing()).thenReturn(false);

        assertThat(repo.needsToFlush(false), is(false));
        assertThat(repo.needsToFlush(true), is(false));
    }

    @Test
    public void whenRepoIsFlushingItNeverReportsHavingToFlush() throws Exception {
        when(repo.isFlushing()).thenReturn(true);

        when(cfg.isAnalyzerUseDiskStorage()).thenReturn(true);
        when(cfg.getAnalyzerMaxSizeInMemory()).thenReturn(2);
        when(repo.getCurrentBufferSize()).thenReturn(3);

        assertThat(repo.needsToFlush(false), is(false));
        assertThat(repo.needsToFlush(true), is(false));
    }

    @Test
    public void whenBufferOverCapacityItReportsHavingToFlush() throws Exception {
        when(repo.isFlushing()).thenReturn(false);
        when(cfg.isAnalyzerUseDiskStorage()).thenReturn(true);
        when(cfg.getAnalyzerMaxSizeInMemory()).thenReturn(2);
        when(repo.getCurrentBufferSize()).thenReturn(3);

        assertThat(repo.needsToFlush(false), is(true));
    }

    @Test
    public void whenBufferUnderCapacityItDoesNotReportsHavingToFlush() throws Exception {
        when(repo.isFlushing()).thenReturn(false);
        when(cfg.isAnalyzerUseDiskStorage()).thenReturn(true);
        when(cfg.getAnalyzerMaxSizeInMemory()).thenReturn(3);
        when(repo.getCurrentBufferSize()).thenReturn(2);

        assertThat(repo.needsToFlush(false), is(false));
    }

    @Test
    public void whenBufferUnderCapacityItReportsHavingToFlushIfForced() throws Exception {
        when(repo.isFlushing()).thenReturn(false);
        when(cfg.isAnalyzerUseDiskStorage()).thenReturn(true);
        when(cfg.getAnalyzerMaxSizeInMemory()).thenReturn(3);
        when(repo.getCurrentBufferSize()).thenReturn(2);

        assertThat(repo.needsToFlush(true), is(true));
    }

    @Test
    public void whenDoingFlushCheckingItPassesTheRightFlushValueToReportingFunction() throws Exception {
        repo.flushIfNecessary(true);

        verify(repo).needsToFlush(true);

        repo.flushIfNecessary(false);

        verify(repo).needsToFlush(false);
    }

    @Test
    public void whenNeedingToFlushItSetsFlushingAndWritesToDisk() throws Exception {
        InOrder inOrder = inOrder(repo);
        when(repo.needsToFlush(anyBoolean())).thenReturn(true);
        doNothing().when(repo).storeEntriesToDisk();
        repo.flushIfNecessary(anyBoolean());

        inOrder.verify(repo, times(1)).setFlushing(true);
        inOrder.verify(repo, times(1)).storeEntriesToDisk();
        inOrder.verifyNoMoreInteractions();
    }
}