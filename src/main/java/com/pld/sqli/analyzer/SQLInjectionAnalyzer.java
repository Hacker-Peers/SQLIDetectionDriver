package com.pld.sqli.analyzer;

import com.pld.sqli.config.SQLIAnalyzerConfig;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Analyzer of SQL to find out possible injection issue.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzer {

    // Class attributes
    private static ExecutorService analyzerPool = Executors.newFixedThreadPool(50);

    /**
     * Hidden constructor.
     */
    private SQLInjectionAnalyzer() {
    }

    /**
     * Retrieve the SQL request entry point based on the Thread stack trace
     * and the SQLIDriver configuration.
     * @return The SQL request entry : <classname> (method: <methodname>, line: <line>)
     */
    private static String getEntryClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        String className = null;
        for (StackTraceElement stackTraceElement : stackTrace) {
            for (String entryPoint : SQLIAnalyzerConfig.getAnalyzerEntrypointPackages()) {
                if (stackTraceElement.getClassName().startsWith(entryPoint)) {
                    className = String.format("%s#%s() (line: %s)",
                            stackTraceElement.getClassName(),
                            stackTraceElement.getMethodName(),
                            stackTraceElement.getLineNumber());
                    break;
                }
            }
            if (StringUtils.isNotBlank(className)) {
                break;
            }
        }

        return (className == null ? "Not found" : className);
    }

    /**
     * Analyze a single SQL statement.
     * @param sql The SQL statement to analyze.
     * @param params The parameters used with the SQL statement, mostly for debugging purpose.
     * @param start The SQL statement start time in millisecond.
     * @param end The SQL statement end time in millisecond.
     */
    public static void analyze(String sql, Map<Object, Object> params, long start, long end) {
        analyze(Arrays.asList(sql), Arrays.asList(params), start, end);
    }

    /**
     * Analyze a batch of SQL statement, running from the same entry point.
     * @param sql The list of SQL statement to analyze.
     * @param params The list of parameters used with SQL statements, mostly for debugging purpose.
     * @param start The SQL statement start time in millisecond.
     * @param end The SQL statement end time in millisecond.
     */
    public static void analyze(List<String> sql, List<Map<Object, Object>> params, long start, long end) {
        String entryPoint = getEntryClass();
        submitThread(entryPoint, sql, params, start, end);
    }

    /**
     * Log SQL statement(s) with parameters and the time in millisecond to run it (them).
     * @param entryPoint The entry point where the query originated.
     * @param sql The statement(s) to log.
     * @param params The parameters to log.
     * @param start The start time to calculate the execution time,
     * @param end The end time to calculate the execution time,
     */
    private static void submitThread(
            String entryPoint, List<String> sql, List<Map<Object, Object>> params, long start, long end) {
        analyzerPool.submit(new SQLInjectionAnalyzerRunnable(
                entryPoint, sql, params, start, end, Thread.currentThread()));
    }

    /**
     * Shutdown the thread pool, trying to complete pending thread.
     */
    public static void shutdown() {
        analyzerPool.shutdown();
        try {
            SQLInjectionAnalyzerRunnable.storeEntriesToDisk();
        } catch (IOException ex) {
            Logger.getLogger(SQLInjectionAnalyzer.class.getName()).log(Level.SEVERE, "Unable to store result on disk", ex);
        }
    }

    /**
     * Shutdown the thread pool immediately.
     */
    public static void shutdownNow() {
        analyzerPool.shutdownNow();
        try {
            SQLInjectionAnalyzerRunnable.storeEntriesToDisk();
        } catch (IOException ex) {
            Logger.getLogger(SQLInjectionAnalyzer.class.getName()).log(Level.SEVERE, "Unable to store result on disk", ex);
        }
    }
    
    /**
     * @return All entries created by the SQLInjectionAnalyzer.
     * @throws Exception If an error occurs while retrieving entries.
     */
    public static Map<String, Map<String, SQLInjectionAnalyzerEntry>> getAllEntries() throws Exception {
        return SQLInjectionAnalyzerRunnable.getEntries();
    }
}
