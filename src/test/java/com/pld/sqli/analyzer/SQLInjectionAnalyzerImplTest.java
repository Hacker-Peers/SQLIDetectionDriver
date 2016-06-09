package com.pld.sqli.analyzer;

import com.pld.sqli.config.ISQLIAnalyzerConfig;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionAnalyzerImplTest {

    private static final String A_SIMPLE_SQL = "select timestamp from dual";
    private static final long A_START_TIMESTAMP = 123456789l;
    private static final long AN_END_TIMESTAMP = 987654321l;
    private static final Map<Object, Object> A_SET_OF_PARAMS = new HashMap<>();
    private static final StackTraceSnapshot AN_ENTRY_POINT = new StackTraceSnapshot();

    @Mock
    private ISQLIAnalyzerConfig cfg;
    @Mock
    private ISQLInjectionRepository repo;
    @Mock
    private ExecutorService analyzerPool;

    private SQLInjectionAnalyzerImpl analyzer;

    static {
        A_SET_OF_PARAMS.put("param1", "value1");
        A_SET_OF_PARAMS.put("param2", "value2");
        A_SET_OF_PARAMS.put("param3", "value3");
    }

    @BeforeMethod
    void givenAnAnalyzer() {
        initMocks(this); //or else mocks are going to stay null when setting up the analyzer.

        analyzer = spy(new SQLInjectionAnalyzerImpl(cfg, repo, analyzerPool));
    }

    @Test
    public void whenAnalyzeIsCalledWithNonCollectionsTheyAreProperlyWrapped() throws Exception {
        doNothing().when(analyzer).analyze(anyList(), anyList(), anyLong(), anyLong());
        analyzer.analyze(A_SIMPLE_SQL, A_SET_OF_PARAMS, A_START_TIMESTAMP, AN_END_TIMESTAMP);

        verify(analyzer).analyze(Arrays.asList(A_SIMPLE_SQL), Arrays.asList(A_SET_OF_PARAMS), A_START_TIMESTAMP, AN_END_TIMESTAMP);
    }

    @Test
    public void whenAnalyzeIsCalledParamsAreProperlyGivenToWorker() throws Exception {
        when(analyzer.getEntryPoint()).thenReturn(AN_ENTRY_POINT);
        analyzer.analyze(Arrays.asList(A_SIMPLE_SQL), Arrays.asList(A_SET_OF_PARAMS), A_START_TIMESTAMP, AN_END_TIMESTAMP);

        verify(analyzerPool).submit(argThat(new SQLInjectionAnalyzerWorkerArgumentMatcher(cfg, repo, Arrays.asList(A_SIMPLE_SQL), Arrays.asList(A_SET_OF_PARAMS), A_START_TIMESTAMP, AN_END_TIMESTAMP, AN_ENTRY_POINT)));
    }

    @Test
    public void whenShutdownIsCalledExecutorPoolIsShutdownAndRepoIsFlushed() throws Exception {
        analyzer.shutdown();

        verify(analyzerPool, times(1)).shutdown();
        verify(repo, times(1)).close();
    }

    @Test
    public void whenShutdownNowIsCalledExecutorPoolIsShutdownNowAndRepoIsFlushed() throws Exception {
        analyzer.shutdownNow();

        verify(analyzerPool, times(1)).shutdownNow();
        verify(repo, times(1)).close();
    }

    @Test
    public void whenShutdownIsCalledFlushIOExceptionsAreTrapped() throws Exception {
        doThrow(new IOException("Should not be raised!")).when(repo).close();
        analyzer.shutdown();

        verify(analyzerPool, times(1)).shutdown();
        verify(repo, times(1)).close();
    }

    @Test
    public void whenShutdownNowIsCalledFlushIOExceptionsAreTrapped() throws Exception {
        doThrow(new IOException("Should not be raised!")).when(repo).close();
        analyzer.shutdownNow();

        verify(analyzerPool, times(1)).shutdownNow();
        verify(repo, times(1)).close();
    }
}