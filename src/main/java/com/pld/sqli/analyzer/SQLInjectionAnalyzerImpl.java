package com.pld.sqli.analyzer;

import com.pld.sqli.config.ISQLIAnalyzerConfig;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Analyzer of SQL to find out possible injection issue.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionAnalyzerImpl implements ISQLInjectionAnalyzer {
    private ExecutorService analyzerPool;
    private ISQLInjectionRepository repo;
    private ISQLIAnalyzerConfig cfg;

    public SQLInjectionAnalyzerImpl(ISQLIAnalyzerConfig cfg, ISQLInjectionRepository repo, ExecutorService analyzerPool) {
        this.analyzerPool = analyzerPool;
        this.repo = repo;
        this.cfg = cfg;
    }

    @Override
    public void analyze(String sql, Map<Object, Object> params, long start, long end) {
        analyze(Arrays.asList(sql), Arrays.asList(params), start, end);
    }

    @Override
    public void analyze(List<String> sql, List<Map<Object, Object>> params, long start, long end) {
        submitThread(getEntryPoint(), sql, params, start, end);
    }

    protected StackTraceSnapshot getEntryPoint() {
        return new StackTraceSnapshot();
    }

    /**
     * Log SQL statement(s) with parameters and the time in millisecond to run it (them).
     * @param entryPoint The entry point where the query originated.
     * @param sql The statement(s) to log.
     * @param params The parameters to log.
     * @param start The start time to calculate the execution time,
     * @param end The end time to calculate the execution time,
     */
    private void submitThread(StackTraceSnapshot entryPoint, List<String> sql, List<Map<Object, Object>> params, long start, long end) {
        analyzerPool.submit(new SQLInjectionAnalyzerWorker(cfg, repo, entryPoint, sql, params, start, end, Thread.currentThread()));
    }

    @Override
    public void shutdown() {
        analyzerPool.shutdown();
        try {
            repo.flush();
        } catch (IOException ex) {
            Logger.getLogger(SQLInjectionAnalyzerImpl.class.getName()).log(Level.SEVERE, "Unable to store result on disk", ex);
        }
    }

    @Override
    public void shutdownNow() {
        analyzerPool.shutdownNow();
        try {
            repo.flush();
        } catch (IOException ex) {
            Logger.getLogger(SQLInjectionAnalyzerImpl.class.getName()).log(Level.SEVERE, "Unable to store result on disk", ex);
        }
    }
}
