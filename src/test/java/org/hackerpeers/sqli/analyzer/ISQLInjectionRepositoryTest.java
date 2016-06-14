package org.hackerpeers.sqli.analyzer;

import org.hackerpeers.sqli.config.ISQLIAnalyzerConfig;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public abstract class ISQLInjectionRepositoryTest<R extends ISQLInjectionRepository> {
    static final String AN_ENTRY_POINT = "my.entry.point";
    static final String ANOTHER_ENTRY_POINT = "another.entry.point";
    static final String A_SIMPLE_SQL = "select sysdate from dual";
    static final Integer ONE_VARIATION = 1;
    static final Integer A_BUFFER_SIZE = 2;
    private static final String ANOTHER_SIMPLE_SQL = "select systimestamp from dual";
    private static final Integer THREE_VARIATIONS = 3;
    @Mock
    protected ISQLIAnalyzerConfig cfg;

    R repo;


    /**
     * Create the concrete repo. Do NOT create a mock or add a spy, it will be taken care of.
     * @return A new repo instance.
     */
    abstract R createRepo() throws Exception;

    @BeforeMethod
    void givenARepo() throws Exception {
        initMocks(this); //or else mocks are going to stay null when setting up the analyzer.

        repo = spy(createRepo());
    }

    @Test
    public void whenAddingEntryToRepoItIsAdded() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);

        assertThat(repo.getEntries().size(), is(1));
    }

    @Test
    public void whenAddingEntryToRepoItContainsProperValues() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);

        assertThat(repo.getEntries(), hasItem(new SQLInjectionAnalyzerEntryMatcher(AN_ENTRY_POINT, A_SIMPLE_SQL, 1, THREE_VARIATIONS)));
    }

    @Test
    public void whenAddingMatchingEntryToRepoItUpdatesExistingEntry() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, THREE_VARIATIONS);
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);

        assertThat(repo.getEntries().size(), is(1));
        assertThat(repo.getEntries(), hasItem(new SQLInjectionAnalyzerEntryMatcher(AN_ENTRY_POINT, A_SIMPLE_SQL, 2, ONE_VARIATION, THREE_VARIATIONS)));
    }

    @Test
    public void whenAddingEntryWithDifferentEntryPointToRepoItAddsIt() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);
        repo.addStatement(ANOTHER_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);

        assertThat(repo.getEntries().size(), is(2));
        assertThat(repo.getEntries(), hasItem(new SQLInjectionAnalyzerEntryMatcher(ANOTHER_ENTRY_POINT, A_SIMPLE_SQL, 1, ONE_VARIATION)));
    }

    @Test
    public void whenAddingEntryWithDifferentStatementToRepoItAddsIt() throws Exception {
        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);
        repo.addStatement(AN_ENTRY_POINT, ANOTHER_SIMPLE_SQL, ONE_VARIATION);

        assertThat(repo.getEntries().size(), is(2));
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Repository closed")
    public void whenClosedAddingEntryFailsWithIOException() throws Exception {
        repo.close();

        repo.addStatement(AN_ENTRY_POINT, ANOTHER_SIMPLE_SQL, ONE_VARIATION);
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Repository closed")
    public void whenClosedGettingEntriesFailsWithIOException() throws Exception {
        repo.close();

        repo.getEntries();
    }

    @Test
    public void whenClosedClosingDoesNotFail() throws Exception {
        repo.close();

        repo.close();
    }

    @Test
    public void whenGettingEntriesCollectionIsUnmodifiable() throws Exception {
        //Ideally we should test all possible methods that can modify a collection, including Iterator.remove, but too long :P
        assertThat(repo.getEntries().getClass().getCanonicalName(), is("java.util.Collections.UnmodifiableCollection"));
    }
}