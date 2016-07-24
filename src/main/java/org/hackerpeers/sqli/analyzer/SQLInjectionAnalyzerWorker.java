package org.hackerpeers.sqli.analyzer;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.hackerpeers.sqli.config.ISQLIAnalyzerConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Asynchronous analyzer of SQL statement.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionAnalyzerWorker implements Runnable {

    private final Logger logger = Logger.getLogger(SQLInjectionAnalyzerWorker.class.getName());
    private ISQLIAnalyzerConfig cfg;
    private ISQLInjectionRepository repo;
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
        this.statements = new ArrayList<>(CollectionUtils.emptyIfNull(sql));
        this.params = CollectionUtils.emptyIfNull(params).stream()
                            .map(p -> new TreeMap<>(MapUtils.emptyIfNull(p)))
                            .collect(Collectors.toList());
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
    String getEntryClass(StackTraceSnapshot entryPoint) {
        StackTraceElement[] stackTrace = entryPoint.getStackTrace();

        Optional<StackTraceElement> found = Arrays.stream(stackTrace)
                                                .filter(s -> StringUtils.startsWithAny(s.getClassName(), cfg.getAnalyzerEntrypointPackages().stream().toArray(size -> new String[size])))
                                                .findFirst();

        if (found.isPresent()) {
            return String.format("%s#%s() (line: %s)",
                    found.get().getClassName(),
                    found.get().getMethodName(),
                    found.get().getLineNumber());
        }

        return "Not found";
    }

    @Override
    public void run() {
        log();

        String entryPoint = getEntryClass(this.entryPoint);
        // Skip elements from safezone
        if (StringUtils.startsWithAny(entryPoint, cfg.getAnalyzerEntrypointSafezones().toArray(new String[0]))) {
            return;
        }

        statements.stream()
                .map(buildSQLStatementRegexReplaceChain(cfg.getAnalyzerRegexSimplifiers()))
                .map(s -> s.replaceAll("[\\s]*,[\\s]*\\?", ",?"))
                .forEach(s ->
                        {
                            int inClauseVariant = StringUtils.countMatches(s, ",?");
                            s = s.replaceAll(",\\?", ""); // Remove extra ,? in IN clause.
                            inClauseVariant = s.matches("^(.+)[iI][nN][\\s]*\\((.*)$") ? inClauseVariant + 1 : 0;

                            // Store statement for the entry point.
                            try {
                                repo.addStatement(entryPoint, s, inClauseVariant);
                            } catch (IOException ioe) {
                                Logger.getLogger(SQLInjectionAnalyzerImpl.class.getName()).log(Level.SEVERE, "Failed to store result on disk.", ioe);
                            }
                        }
                );
    }

    /**
     * This weird part creates a chain of "regex replace" and chains them into one big function to be reused a few lines down.
     * @param regexes Map containing the list of string to map from (key) and to (value).
     * @return Function that will apply all configured regex to the given input SQL string.
     */
    Function<String, String> buildSQLStatementRegexReplaceChain(Map<String, String> regexes) {
        return regexes.entrySet().stream()
                .map(entry -> (Function<String, String>) (e -> e.replaceAll(entry.getKey(), entry.getValue())))
                .reduce(
                        Function.identity(),
                        (k, v) -> k.andThen(v));
    }

    /**
     * Log the SQL statement if matching logging criteria.
     */
    private void log() {
        long elapse = end - start;
        Level level = getLogLevel(elapse);
        if (logger.isLoggable(level) && ! statements.isEmpty()) {
            String logTemplate = String.format("\t%s (thread id:%s) -- %%s %s", sourceThread, sourceThreadId, System.getProperty("line.separator"));
            StringBuilder buf = new StringBuilder();
            buf.append(String.format(logTemplate, String.format("============= Begin : SQL Injection Analyzer <%s> =================", entryPoint)));
            buf.append(String.format(logTemplate, "Query total time (millisecond) : " + elapse));

            Iterator<String> sIterator = statements.iterator();
            Iterator<Map<Object, Object>> pIterator = params.iterator();
            while(sIterator.hasNext() && pIterator.hasNext()) {
                buf.append(String.format(logTemplate, sIterator.next()));
                for (Map.Entry<Object, Object> entry : pIterator.next().entrySet()) {
                    buf.append(String.format(logTemplate,
                            String.format("\t%s => [%s] (%s)", entry.getKey(), entry.getValue(), (entry.getValue() == null ? "unknown" : entry.getValue().getClass().getName()))));
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
        if (isGreaterThanThreshold(cfg.getAnalyzerLogThresholdSevere(), elapse)) {
            level = Level.SEVERE;
        } else if (isGreaterThanThreshold(cfg.getAnalyzerLogThresholdWarning(), elapse)) {
            level = Level.WARNING;
        } else if (isGreaterThanThreshold(cfg.getAnalyzerLogThresholdInfo(), elapse)) {
            level = Level.INFO;
        } else if (isGreaterThanThreshold(cfg.getAnalyzerLogThresholdFine(), elapse)) {
            level = Level.FINE;
        }
        return level;
    }

    private boolean isGreaterThanThreshold(long threshold, long value) {
        return threshold != -1 && value >= threshold;
    }

    ISQLIAnalyzerConfig getCfg() {
        return cfg;
    }

    ISQLInjectionRepository getRepo() {
        return repo;
    }

    StackTraceSnapshot getEntryPoint() {
        return entryPoint;
    }

    List<String> getStatements() {
        return statements;
    }

    List<Map<Object, Object>> getParams() {
        return params;
    }

    long getStart() {
        return start;
    }

    long getEnd() {
        return end;
    }
}
