package com.pld.sqli.analyzer;

import com.pld.sqli.config.ISQLIAnalyzerConfig;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Asynchronous analyzer of SQL statement.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionAnalyzerWorker implements Runnable {

    private ISQLIAnalyzerConfig cfg;
    private ISQLInjectionRepository repo;
    private final Logger logger = Logger.getLogger(SQLInjectionAnalyzerWorker.class.getName());

    // Attributes
    private StackTraceSnapshot entryPoint;
    private List<String> statements;
    private List<Map<Object, Object>> params;
    private long start;
    private long end;
    private String sourceThread;
    private long sourceThreadId;

    /**
     * Constructor with all parameters to analyze the SQL statement.
     * @param cfg Injected config.
     * @param repo Injected repo.
     * @param entryPoint Snapshot of there the call was originally made.
     * @param sql The SQL statement to analyze.
     * @param params The parameters associated to the SQL statement.
     * @param start The SQL statement start date.
     * @param end The SQL statement end date.
     */
    SQLInjectionAnalyzerWorker(ISQLIAnalyzerConfig cfg, ISQLInjectionRepository repo,
            StackTraceSnapshot entryPoint, List<String> sql, List<Map<Object, Object>> params, long start, long end,
            Thread sourceThread) {
        this.cfg = cfg;
        this.repo = repo;
        this.entryPoint = entryPoint;
        if (sql != null) {
            this.statements = new ArrayList<>(sql);
        }
        if (params != null) {
            this.params = new ArrayList<>();
            for (Map<Object, Object> p : params) {
                if (p == null) {
                    this.params.add(null);
                } else {
                    this.params.add(new TreeMap<>(p));
                }
            }
        }
        this.start = start;
        this.end = end;
        this.sourceThread = sourceThread.toString();
        this.sourceThreadId = sourceThread.getId();
    }

    /**
     * Retrieve the SQL request entry point based on the Thread stack trace
     * and the SQLIDriver configuration.
     * @return The SQL request entry : <classname> (method: <methodname>, line: <line>)
     */
    private String getEntryClass(StackTraceSnapshot entryPoint) {
        StackTraceElement[] stackTrace = entryPoint.getStackTrace();
        String className = null;
        for (StackTraceElement stackTraceElement : stackTrace) {
            for (String packages : cfg.getAnalyzerEntrypointPackages()) {
                if (stackTraceElement.getClassName().startsWith(packages)) {
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

    @Override
    public void run() {
        if (statements != null) {
            log();

            String entryPoint = getEntryClass(this.entryPoint);
            // Skip elements from safezone
            for (String safezone : cfg.getAnalyzerEntrypointSafezones()) {
                if (StringUtils.isNotBlank(safezone)
                        && StringUtils.startsWith(entryPoint, safezone)) {
                    return; // The element does not need to be analyzed.
                }
            }

            for (String statement : statements) {
                // Apply RegEx simplifiers
                for (Map.Entry<String, String> regex : cfg.getAnalyzerRegexSimplifiers().entrySet()) {
                    statement = statement.replaceAll(regex.getKey(), regex.getValue());
                }

                // Apply IN Clause simplifier
                statement = statement.replaceAll("[\\s]*,[\\s]*\\?", ",?");
                int inClauseVariant = StringUtils.countMatches(statement, ",?");
                statement = statement.replaceAll(",\\?", ""); // Remove extra ,? in IN clause.
                inClauseVariant = statement.matches("^(.+)[iI][nN][\\s]*\\((.*)$") ? inClauseVariant + 1 : 0;

                // Store statement for the entry point.
                try {
                    repo.addStatement(entryPoint, statement, inClauseVariant);
                } catch (IOException ioe) {
                    Logger.getLogger(SQLInjectionAnalyzerImpl.class.getName()).log(Level.SEVERE, "Failed to store result on disk.", ioe);
                }
            }
        }
    }

    /**
     * Log the SQL statement if matching logging criteria.
     */
    private void log() {
        long elapse = end - start;
        Level level = getLogLevel(elapse);
        if (logger.isLoggable(level)) {
            String logTemplate = String.format("\t%s (thread id:%s) -- %%s %s",
                    sourceThread, sourceThreadId,
                    System.getProperty("line.separator"));
            StringBuilder buf = new StringBuilder();
            buf.append(String.format(logTemplate, String.format("============= Begin : SQL Injection Analyzer <%s> =================", entryPoint)));
            buf.append(String.format(logTemplate, "Query total time (millisecond) : " + elapse));
            for (int i = 0; i < statements.size(); i++) {
                buf.append(String.format(logTemplate, statements.get(i)));
                if (params != null && params.get(i) != null) {
                    Map<Object, Object> p = params.get(i);
                    for (Map.Entry<Object, Object> entry : p.entrySet()) {
                        buf.append(String.format(logTemplate,
                                String.format("\t%s => [%s] (%s)",
                                entry.getKey(), entry.getValue(), (entry.getValue() == null ? "unknown" : entry.getValue().getClass().getName()))));
                    }
                }
            }

            buf.append(String.format(logTemplate, String.format("============= End : SQL Injection Analyzer <%s> =================", entryPoint)));
            logger.log(level, buf.toString());
        }
    }

    /**
     * Get the log level required by the SQL statement elapse time.
     * @param elapse The SQL statement elapse time.
     * @return The associated log level.
     */
    private Level getLogLevel(long elapse) {
        Level level = Level.OFF;
        if (cfg.getAnalyzerLogThresholdSevere() != -1
                && elapse >= cfg.getAnalyzerLogThresholdSevere()) {
            level = Level.SEVERE;
        } else if (cfg.getAnalyzerLogThresholdWarning() != -1
                && elapse >= cfg.getAnalyzerLogThresholdWarning()) {
            level = Level.WARNING;
        } else if (cfg.getAnalyzerLogThresholdInfo() != -1
                && elapse >= cfg.getAnalyzerLogThresholdInfo()) {
            level = Level.INFO;
        } else if (cfg.getAnalyzerLogThresholdFine() != -1
                && elapse >= cfg.getAnalyzerLogThresholdFine()) {
            level = Level.FINE;
        }
        return level;
    }

    protected ISQLIAnalyzerConfig getCfg() {
        return cfg;
    }

    protected ISQLInjectionRepository getRepo() {
        return repo;
    }

    protected StackTraceSnapshot getEntryPoint() {
        return entryPoint;
    }

    protected List<String> getStatements() {
        return statements;
    }

    protected List<Map<Object, Object>> getParams() {
        return params;
    }

    protected long getStart() {
        return start;
    }

    protected long getEnd() {
        return end;
    }
}
