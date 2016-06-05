package com.pld.sqli.analyzer;

import java.util.List;
import java.util.Map;

/**
 * Analyzer of SQL to find out possible injection issue.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public interface ISQLInjectionAnalyzer {
    /**
     * Analyze a single SQL statement.
     * @param sql The SQL statement to analyze.
     * @param params The parameters used with the SQL statement, mostly for debugging purpose.
     * @param start The SQL statement start time in millisecond.
     * @param end The SQL statement end time in millisecond.
     */
    void analyze(String sql, Map<Object, Object> params, long start, long end);

    /**
     * Analyze a batch of SQL statement, running from the same entry point.
     * @param sql The list of SQL statement to analyze.
     * @param params The list of parameters used with SQL statements, mostly for debugging purpose.
     * @param start The SQL statement start time in millisecond.
     * @param end The SQL statement end time in millisecond.
     */
    void analyze(List<String> sql, List<Map<Object, Object>> params, long start, long end);

    /**
     * Shutdown the thread pool, trying to complete pending thread.
     */
    void shutdown();

    /**
     * Shutdown the thread pool immediately.
     */
    void shutdownNow();
}
