package org.hackerpeers.sqli.analyzer;

import org.hackerpeers.sqli.config.ISQLIAnalyzerConfig;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
class SQLInjectionAnalyzerWorkerArgumentMatcher extends TypeSafeMatcher<SQLInjectionAnalyzerWorker> {
    private ISQLIAnalyzerConfig cfg;
    private ISQLInjectionRepository repo;

    // Attributes
    private List<String> statements;
    private List<Map<Object, Object>> params;
    private long start;
    private long end;
    private StackTraceSnapshot entryPoint;


    public SQLInjectionAnalyzerWorkerArgumentMatcher(ISQLIAnalyzerConfig cfg, ISQLInjectionRepository repo, String statement, Map<Object, Object> params, long start, long end, StackTraceSnapshot entryPoint) {
        this(cfg, repo, Arrays.asList(statement), Arrays.asList(params), start, end, entryPoint);
    }

    public SQLInjectionAnalyzerWorkerArgumentMatcher(ISQLIAnalyzerConfig cfg, ISQLInjectionRepository repo, List<String> statements, List<Map<Object, Object>> params, long start, long end, StackTraceSnapshot entryPoint) {
        this.cfg = cfg;
        this.repo = repo;
        this.statements = statements;
        this.params = params;
        this.start = start;
        this.end = end;
        this.entryPoint = entryPoint;
    }

    @Override
    protected boolean matchesSafely(SQLInjectionAnalyzerWorker that) {
        if (cfg != that.getCfg()) return false; // yes we just check instance equality
        if (repo != that.getRepo()) return false; // yes we just check instance equality
        if (entryPoint != that.getEntryPoint()) return false; // yes we just check instance equality
        if (start != that.getStart()) return false;
        if (end != that.getEnd()) return false;
        if (!that.getStatements().containsAll(statements)) return false;
        if (!that.getParams().containsAll(params)) return false;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        //TODO: Code this :-/
    }
}
