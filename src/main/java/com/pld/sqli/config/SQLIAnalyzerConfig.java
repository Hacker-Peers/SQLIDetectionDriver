package com.pld.sqli.config;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Configuration handler of the SQLInjection Analyzer jdbc driver.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLIAnalyzerConfig {

    // Class variable.
    private static boolean initialized = false;
    private static boolean loaded = false;
    private static String propertiesFile = "SQLIAnalyzer.properties";
    private static boolean analyzerActive;
    private static boolean analyzerUseDiskStorage;
    private static int analyzerMaxSizeInMemory;
    private static String analyzerStoragePath;
    private static Map<String, String> analyzerRegexSimplifiers = new HashMap<String, String>();
    private static String analyzerRealDriver;
    private static String analyzerRealJdbc;
    private static List<String> analyzerEntrypointPackages;
    private static List<String> analyzerEntrypointSafezones = new ArrayList<String>();
    private static long analyzerLogThresholdSevere = -1;
    private static long analyzerLogThresholdWarning = -1;
    private static long analyzerLogThresholdInfo = -1;
    private static long analyzerLogThresholdFine = -1;

    static {
        initialize();
    }

    /**
     * Initialize the SQLIAnalyzer configuration.
     */
    private static synchronized void initialize() {
        if (!initialized) {
            analyzerActive = false;
            loaded = false;
            try {
                URL config = getResource();
                if (config != null) {
                    Properties props = new Properties();
                    InputStream openStream = config.openStream();
                    props.load(openStream);
                    openStream.close();
                    analyzerActive = Boolean.parseBoolean(props.getProperty("analyzer.active", "false"));

                    // Load storage settings.
                    analyzerUseDiskStorage = Boolean.parseBoolean(props.getProperty("analyzer.active", "false"));
                    analyzerMaxSizeInMemory = NumberUtils.toInt(props.getProperty("analyzer.maxSizeInMemory"), 100);
                    analyzerStoragePath = props.getProperty("analyzer.storage.path", null);

                    // Load RegEx simplifiers.
                    String regexSimplifiers = props.getProperty("analyzer.regex.simplifiers", "");
                    analyzerRegexSimplifiers = new HashMap<String, String>();
                    for (String regex : regexSimplifiers.split(",")) {
                        String[] expr = regex.split("=");
                        if (expr.length == 2) {
                            analyzerRegexSimplifiers.put(expr[0], expr[1]);
                        }
                    }
                    analyzerRegexSimplifiers = Collections.unmodifiableMap(analyzerRegexSimplifiers);

                    // Load real driver parameters.
                    analyzerRealDriver = props.getProperty("analyzer.real.driver");
                    analyzerRealJdbc = props.getProperty("analyzer.real.jdbc");

                    // Load analyzer entry point detection.
                    String packages = props.getProperty("analyzer.entrypoint.packages", "com");
                    analyzerEntrypointPackages = new ArrayList<String>();
                    analyzerEntrypointPackages.addAll(Arrays.asList(packages.split(",")));
                    analyzerEntrypointPackages = Collections.unmodifiableList(analyzerEntrypointPackages);

                    // Load safe zones.
                    String safezones = props.getProperty("analyzer.entrypoint.safezones", "");
                    analyzerEntrypointSafezones = new ArrayList<String>();
                    analyzerEntrypointSafezones.addAll(Arrays.asList(safezones.split(",")));
                    analyzerEntrypointSafezones = Collections.unmodifiableList(analyzerEntrypointSafezones);

                    // Load logging configuration.
                    analyzerLogThresholdSevere = NumberUtils.toLong(props.getProperty("analyzer.log.threshold.severe"), -1L);
                    analyzerLogThresholdWarning = NumberUtils.toLong(props.getProperty("analyzer.log.threshold.warning"), -1L);
                    analyzerLogThresholdInfo = NumberUtils.toLong(props.getProperty("analyzer.log.threshold.info"), -1L);
                    analyzerLogThresholdFine = NumberUtils.toLong(props.getProperty("analyzer.log.threshold.fine"), -1L);
                    loaded = true;
                } else {
                    Logger.getLogger(SQLIAnalyzerConfig.class.getName()).log(Level.SEVERE,
                            "Unable to load SQLIAnalyzerConfig properties. SQLIAnalyzer.properties not found.");
                }
            } catch (Throwable ex) {
                Logger.getLogger(SQLIAnalyzerConfig.class.getName()).log(Level.SEVERE,
                        "Unable to load SQLIAnalyzerConfig properties. Failed with exception.", ex);
            }

            initialized = true;
        }
    }

    /**
     * Search for the first resource matching the propertiesFile name.
     * @return The first resource found.
     */
    private static URL getResource() {
        URL resource = ClassLoader.getSystemResource(propertiesFile);
        if (resource == null) {
            resource = ClassLoader.getSystemResource("META-INF/" + propertiesFile);
        }
        if (resource == null) {
            resource = SQLIAnalyzerConfig.class.getClassLoader().getResource(propertiesFile);
        }
        if (resource == null) {
            resource = SQLIAnalyzerConfig.class.getClassLoader().getResource("META-INF/" + propertiesFile);
        }

        return resource;
    }

    /**
     * @return The list of packages use to detect the sql statement entry point.
     */
    public static List<String> getAnalyzerEntrypointPackages() {
        return analyzerEntrypointPackages;
    }

    /**
     * @return The threshold in millisecond to log a statement at the fine level.
     */
    public static long getAnalyzerLogThresholdFine() {
        return analyzerLogThresholdFine;
    }

    /**
     * @return The threshold in millisecond to log a statement at the info level.
     */
    public static long getAnalyzerLogThresholdInfo() {
        return analyzerLogThresholdInfo;
    }

    /**
     * @return The threshold in millisecond to log a statement at the warning level.
     */
    public static long getAnalyzerLogThresholdWarning() {
        return analyzerLogThresholdWarning;
    }

    /**
     * @return The threshold in millisecond to log a statement at the severe level.
     */
    public static long getAnalyzerLogThresholdSevere() {
        return analyzerLogThresholdSevere;
    }

    /**
     * @return The real JDBC Driver to wrap.
     */
    public static String getAnalyzerRealDriver() {
        return analyzerRealDriver;
    }

    /**
     * @return The real JDBC Type, use to replace the sqli type.
     */
    public static String getAnalyzerRealJdbc() {
        return analyzerRealJdbc;
    }

    /**
     * @return True if the analyzer is turn on.
     */
    public static boolean isAnalyzerActive() {
        return analyzerActive;
    }

    /**
     * @return True if the configuration is successfully loaded.
     */
    public static boolean isLoaded() {
        return loaded;
    }

    /**
     * @return The list of safe zones to ignore when analyzing SQL statements.
     */
    public static List<String> getAnalyzerEntrypointSafezones() {
        return analyzerEntrypointSafezones;
    }

    /**
     * @return The RegEx to apply on SQL statements before analyzing it.
     */
    public static Map<String, String> getAnalyzerRegexSimplifiers() {
        return analyzerRegexSimplifiers;
    }

    /**
     * @return The maximum number of statement to keep in memory before saving the result on disk.
     */
    public static int getAnalyzerMaxSizeInMemory() {
        return analyzerMaxSizeInMemory;
    }

    /**
     * @return The path to store statements.
     */
    public static String getAnalyzerStoragePath() {
        return analyzerStoragePath;
    }

    /**
     * @return If true, the Analyzer will use local disk storage.
     */
    public static boolean isAnalyzerUseDiskStorage() {
        return analyzerUseDiskStorage;
    }
}
