package com.pld.sqli.analyzer;

import com.pld.sqli.config.SQLIAnalyzerConfig;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * Asynchronous analyzer of SQL statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerRunnable implements Runnable {

    // Class attributes
    private static Map<String, Map<String, SQLInjectionAnalyzerEntry>> entries = new TreeMap<String, Map<String, SQLInjectionAnalyzerEntry>>();
    // Attributes
    private String entryPoint;
    private List<String> statements;
    private List<Map<Object, Object>> params;
    private long start;
    private long end;
    private String sourceThread;
    private long sourceThreadId;

    /**
     * Constructor with all parameters to analyze the SQL statement.
     * @param sql The SQL statement to analyze.
     * @param params The parameters associated to the SQL statement.
     * @param start The SQL statement start date.
     * @param end The SQL statement end date.
     */
    SQLInjectionAnalyzerRunnable(
            String entryPoint, List<String> sql, List<Map<Object, Object>> params, long start, long end,
            Thread sourceThread) {
        this.entryPoint = entryPoint;
        if (sql != null) {
            this.statements = new ArrayList<String>(sql);
        }
        if (params != null) {
            this.params = new ArrayList<Map<Object, Object>>();
            for (Map<Object, Object> p : params) {
                if (p == null) {
                    this.params.add(null);
                } else {
                    this.params.add(new TreeMap<Object, Object>(p));
                }
            }
        }
        this.start = start;
        this.end = end;
        this.sourceThread = sourceThread.toString();
        this.sourceThreadId = sourceThread.getId();
    }

    @Override
    public void run() {
        if (statements != null) {
            log();

            // Skip elements from safezone
            for (String safezone : SQLIAnalyzerConfig.getAnalyzerEntrypointSafezones()) {
                if (StringUtils.isNotBlank(safezone)
                        && StringUtils.startsWith(this.entryPoint, safezone)) {
                    return; // The element does not need to be analyzed.
                }
            }

            for (String statement : statements) {
                // Apply RegEx simplifiers
                for (Map.Entry<String, String> regex : SQLIAnalyzerConfig.getAnalyzerRegexSimplifiers().entrySet()) {
                    statement = statement.replaceAll(regex.getKey(), regex.getValue());
                }

                // Apply IN Clause simplifier
                statement = statement.replaceAll("[\\s]*,[\\s]*\\?", ",?");
                int inClauseVariant = StringUtils.countMatches(statement, ",?");
                statement = statement.replaceAll(",\\?", ""); // Remove extra ,? in IN clause.
                inClauseVariant = statement.matches("^(.+)[iI][nN][\\s]*\\((.*)$") ? inClauseVariant + 1 : 0;

                // Store statement for the entry point.
                try {
                    addStatement(entryPoint, statement, inClauseVariant);
                } catch (IOException ioe) {
                    Logger.getLogger(SQLInjectionAnalyzer.class.getName()).log(Level.SEVERE, "Failed to store result on disk.", ioe);
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
        Logger logger = Logger.getLogger(SQLInjectionAnalyzer.class.getName());
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
        if (SQLIAnalyzerConfig.getAnalyzerLogThresholdSevere() != -1
                && elapse >= SQLIAnalyzerConfig.getAnalyzerLogThresholdSevere()) {
            level = Level.SEVERE;
        } else if (SQLIAnalyzerConfig.getAnalyzerLogThresholdWarning() != -1
                && elapse >= SQLIAnalyzerConfig.getAnalyzerLogThresholdWarning()) {
            level = Level.WARNING;
        } else if (SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() != -1
                && elapse >= SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo()) {
            level = Level.INFO;
        } else if (SQLIAnalyzerConfig.getAnalyzerLogThresholdFine() != -1
                && elapse >= SQLIAnalyzerConfig.getAnalyzerLogThresholdFine()) {
            level = Level.FINE;
        }
        return level;
    }

    /**
     * Add the statement to the cache of statements by entry point.
     * @param entryPoint The entry point of the statement.
     * @param statement The statement to cache.
     * @param inClauseVariant The id of the IN Clause variant.
     */
    private static synchronized void addStatement(
            String entryPoint, String statement, int inClauseVariant) throws IOException {
        Map<String, SQLInjectionAnalyzerEntry> statements = entries.get(entryPoint);
        if (statements == null) {
            statements = new TreeMap<String, SQLInjectionAnalyzerEntry>();
            statements.put(statement, new SQLInjectionAnalyzerEntry(statement, inClauseVariant));
            entries.put(entryPoint, statements);
        } else {
            if (statements.containsKey(statement)) {
                statements.get(statement).addStatementCall(inClauseVariant);
            } else {
                statements.put(statement, new SQLInjectionAnalyzerEntry(statement, inClauseVariant));
            }
        }
        if (SQLIAnalyzerConfig.isAnalyzerUseDiskStorage()
                && entries.size() >= SQLIAnalyzerConfig.getAnalyzerMaxSizeInMemory()) {
            storeEntriesToDisk();
        }
    }

    /**
     * Store current entries to disk.
     * If there already is a disk file, merge the results.
     */
    static synchronized void storeEntriesToDisk() throws IOException {
        File location = new File(SQLIAnalyzerConfig.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml");
        location.getParentFile().mkdirs();

        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result;
        if (location.exists()) {
            FileInputStream is = new FileInputStream(location);
            result = (Map<String, Map<String, SQLInjectionAnalyzerEntry>>) new XStream().fromXML(is);
            is.close();
            for (Map.Entry<String, Map<String, SQLInjectionAnalyzerEntry>> entry : entries.entrySet()) {
                Map<String, SQLInjectionAnalyzerEntry> existingEntry = result.get(entry.getKey());
                if (existingEntry == null) {
                    result.put(entry.getKey(), entry.getValue());
                } else {
                    for (Map.Entry<String, SQLInjectionAnalyzerEntry> statement : entry.getValue().entrySet()) {
                        SQLInjectionAnalyzerEntry existingStatement = existingEntry.get(statement.getKey());
                        if (existingStatement == null) {
                            existingEntry.put(statement.getKey(), statement.getValue());
                        } else {
                            existingStatement.mergeStatementCall(statement.getValue());
                        }
                    }
                }
            }
        } else {
            result = entries;
        }

        // Store merge result.
        FileOutputStream os = new FileOutputStream(location, false);
        new XStream().toXML(result, os);
        os.close();

        entries.clear();
    }

    /**
     * @return All entries created by the SQLInjectionAnalyzer.
     * @throws Exception If an error occurs while retrieving entries.
     */
    static synchronized Map<String, Map<String, SQLInjectionAnalyzerEntry>> getEntries() throws Exception {
        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result;
        if (SQLIAnalyzerConfig.isAnalyzerUseDiskStorage())  {
            storeEntriesToDisk();
            File location = new File(SQLIAnalyzerConfig.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml");
            FileInputStream is = new FileInputStream(location);
            result = (Map<String, Map<String, SQLInjectionAnalyzerEntry>>) new XStream().fromXML(is);
            is.close();
        } else {
            result = new TreeMap<String, Map<String, SQLInjectionAnalyzerEntry>>(entries);
        }
        return result;

    }
}
