package org.hackerpeers.sqli.config;

import java.util.List;
import java.util.Map;

/**
 * Configuration handler of the SQLInjection Analyzer jdbc driver.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public interface ISQLIAnalyzerConfig {
    /**
     * @return The list of packages use to detect the sql statement entry point.
     */
    List<String> getAnalyzerEntrypointPackages();

    /**
     * @return The threshold in millisecond to log a statement at the fine level.
     */
    long getAnalyzerLogThresholdFine();

    /**
     * @return The threshold in millisecond to log a statement at the info level.
     */
    long getAnalyzerLogThresholdInfo();

    /**
     * @return The threshold in millisecond to log a statement at the warning level.
     */
    long getAnalyzerLogThresholdWarning();

    /**
     * @return The threshold in millisecond to log a statement at the severe level.
     */
    long getAnalyzerLogThresholdSevere();

    /**
     * @return The real JDBC Driver to wrap.
     */
    String getAnalyzerRealDriver();

    /**
     * @return The real JDBC Type, use to replace the sqli type.
     */
    String getAnalyzerRealJdbc();

    /**
     * @return True if the analyzer is turn on.
     */
    boolean isAnalyzerActive();

    /**
     * @return True if the configuration is successfully loaded.
     */
    boolean isLoaded();

    /**
     * @return The list of safe zones to ignore when analyzing SQL statements.
     */
    List<String> getAnalyzerEntrypointSafezones();

    /**
     * @return The RegEx to apply on SQL statements before analyzing it.
     */
    Map<String, String> getAnalyzerRegexSimplifiers();

    /**
     * @return The maximum number of statement to keep in memory before saving the result on disk.
     */
    int getAnalyzerMaxSizeInMemory();

    /**
     * @return The path to store statements.
     */
    String getAnalyzerStoragePath();

    /**
     * @return If true, the Analyzer will use local disk storage.
     */
    boolean isAnalyzerUseDiskStorage();
}
