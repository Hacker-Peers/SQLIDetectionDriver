package org.hackerpeers.sqli.config;

import java.util.List;
import java.util.Map;

/**
 * Configuration handler of the SQLInjection Analyzer jdbc driver.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
class SQLIAnalyzerConfigImpl implements ISQLIAnalyzerConfig {
    private boolean loaded;
    private boolean analyzerActive;
    private boolean analyzerUseDiskStorage;
    private int analyzerMaxSizeInMemory;
    private String analyzerStoragePath;
    private Map<String, String> analyzerRegexSimplifiers;
    private String analyzerRealDriver;
    private String analyzerRealJdbc;
    private List<String> analyzerEntrypointPackages;
    private List<String> analyzerEntrypointSafezones;
    private long analyzerLogThresholdSevere = -1L;
    private long analyzerLogThresholdWarning = -1L;
    private long analyzerLogThresholdInfo = -1L;
    private long analyzerLogThresholdFine = -1L;

    @Override
    public List<String> getAnalyzerEntrypointPackages() {
        return analyzerEntrypointPackages;
    }

    protected void setAnalyzerEntrypointPackages(List<String> analyzerEntrypointPackages) {
        this.analyzerEntrypointPackages = analyzerEntrypointPackages;
    }

    @Override
    public long getAnalyzerLogThresholdFine() {
        return analyzerLogThresholdFine;
    }

    protected void setAnalyzerLogThresholdFine(long analyzerLogThresholdFine) {
        this.analyzerLogThresholdFine = analyzerLogThresholdFine;
    }

    @Override
    public long getAnalyzerLogThresholdInfo() {
        return analyzerLogThresholdInfo;
    }

    protected void setAnalyzerLogThresholdInfo(long analyzerLogThresholdInfo) {
        this.analyzerLogThresholdInfo = analyzerLogThresholdInfo;
    }

    @Override
    public long getAnalyzerLogThresholdWarning() {
        return analyzerLogThresholdWarning;
    }

    protected void setAnalyzerLogThresholdWarning(long analyzerLogThresholdWarning) {
        this.analyzerLogThresholdWarning = analyzerLogThresholdWarning;
    }

    @Override
    public long getAnalyzerLogThresholdSevere() {
        return analyzerLogThresholdSevere;
    }

    protected void setAnalyzerLogThresholdSevere(long analyzerLogThresholdSevere) {
        this.analyzerLogThresholdSevere = analyzerLogThresholdSevere;
    }

    @Override
    public String getAnalyzerRealDriver() {
        return analyzerRealDriver;
    }

    protected void setAnalyzerRealDriver(String analyzerRealDriver) {
        this.analyzerRealDriver = analyzerRealDriver;
    }

    @Override
    public String getAnalyzerRealJdbc() {
        return analyzerRealJdbc;
    }

    protected void setAnalyzerRealJdbc(String analyzerRealJdbc) {
        this.analyzerRealJdbc = analyzerRealJdbc;
    }

    @Override
    public boolean isAnalyzerActive() {
        return analyzerActive;
    }

    protected void setAnalyzerActive(boolean analyzerActive) {
        this.analyzerActive = analyzerActive;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    protected void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public List<String> getAnalyzerEntrypointSafezones() {
        return analyzerEntrypointSafezones;
    }

    protected void setAnalyzerEntrypointSafezones(List<String> analyzerEntrypointSafezones) {
        this.analyzerEntrypointSafezones = analyzerEntrypointSafezones;
    }

    @Override
    public Map<String, String> getAnalyzerRegexSimplifiers() {
        return analyzerRegexSimplifiers;
    }

    protected void setAnalyzerRegexSimplifiers(Map<String, String> analyzerRegexSimplifiers) {
        this.analyzerRegexSimplifiers = analyzerRegexSimplifiers;
    }

    @Override
    public int getAnalyzerMaxSizeInMemory() {
        return analyzerMaxSizeInMemory;
    }

    protected void setAnalyzerMaxSizeInMemory(int analyzerMaxSizeInMemory) {
        this.analyzerMaxSizeInMemory = analyzerMaxSizeInMemory;
    }

    @Override
    public String getAnalyzerStoragePath() {
        return analyzerStoragePath;
    }

    protected void setAnalyzerStoragePath(String analyzerStoragePath) {
        this.analyzerStoragePath = analyzerStoragePath;
    }

    @Override
    public boolean isAnalyzerUseDiskStorage() {
        return analyzerUseDiskStorage;
    }

    protected void setAnalyzerUseDiskStorage(boolean analyzerUseDiskStorage) {
        this.analyzerUseDiskStorage = analyzerUseDiskStorage;
    }
}
