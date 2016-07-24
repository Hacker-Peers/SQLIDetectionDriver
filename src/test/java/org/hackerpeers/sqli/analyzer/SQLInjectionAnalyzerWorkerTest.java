package org.hackerpeers.sqli.analyzer;

import org.hackerpeers.sqli.config.ISQLIAnalyzerConfig;
import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Simon on 7/24/2016.
 */
public class SQLInjectionAnalyzerWorkerTest {
    @Mock
    ISQLIAnalyzerConfig cfg;
    @Mock
    ISQLInjectionRepository repo;
    @Mock
    StackTraceSnapshot entryPoint;
    @Mock
    Thread sourceThread;

    SQLInjectionAnalyzerWorker worker;

    @BeforeMethod
    void givenAnAnalyzer() {
        initMocks(this); //or else mocks are going to stay null when setting up the analyzer.

        worker = spy(new SQLInjectionAnalyzerWorker(cfg, repo, entryPoint, null, null, 0l, 0l, sourceThread));
    }


    @Test
    public void givenARegexMapBuildSQLStatementRegexReplaceChainRetunsAFunctionThatAppliesThemAllInOrder() {
        // Given
        final String A_STRING = "abcde";
        Map<String, String> cfg = new LinkedHashMap<>();
        cfg.put("a", "b");
        cfg.put("b", "c");
        cfg.put("c", "d");
        cfg.put("d", "e");
        cfg.put("e", "-");

        // When
        String result = worker.buildSQLStatementRegexReplaceChain(cfg).apply(A_STRING);

        // Then
        assertThat(result, equalTo("-----"));
    }
}