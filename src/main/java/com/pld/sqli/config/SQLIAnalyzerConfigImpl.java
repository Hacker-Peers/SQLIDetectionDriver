package com.pld.sqli.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuration handler of the SQLInjection Analyzer jdbc driver.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLIAnalyzerConfigImpl implements ISQLIAnalyzerConfig {
    private boolean initialized = false;
    private boolean loaded = false;
    private String propertiesFile = "SQLIAnalyzer.properties";
    private boolean analyzerActive;
    private boolean analyzerUseDiskStorage;
    private int analyzerMaxSizeInMemory;
    private String analyzerStoragePath;
    private Map<String, String> analyzerRegexSimplifiers = new HashMap<String, String>();
    private String analyzerRealDriver;
    private String analyzerRealJdbc;
    private List<String> analyzerEntrypointPackages;
    private List<String> analyzerEntrypointSafezones = new ArrayList<String>();
    private long analyzerLogThresholdSevere = -1;
    private long analyzerLogThresholdWarning = -1;
    private long analyzerLogThresholdInfo = -1;
    private long analyzerLogThresholdFine = -1;


    protected void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    protected void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    protected void setPropertiesFile(String propertiesFile) {
        this.propertiesFile = propertiesFile;
    }

    protected void setAnalyzerActive(boolean analyzerActive) {
        this.analyzerActive = analyzerActive;
    }

    protected void setAnalyzerUseDiskStorage(boolean analyzerUseDiskStorage) {
        this.analyzerUseDiskStorage = analyzerUseDiskStorage;
    }

    protected void setAnalyzerMaxSizeInMemory(int analyzerMaxSizeInMemory) {
        this.analyzerMaxSizeInMemory = analyzerMaxSizeInMemory;
    }

    protected void setAnalyzerStoragePath(String analyzerStoragePath) {
        this.analyzerStoragePath = analyzerStoragePath;
    }

    protected void setAnalyzerRegexSimplifiers(Map<String, String> analyzerRegexSimplifiers) {
        this.analyzerRegexSimplifiers = analyzerRegexSimplifiers;
    }

    protected void setAnalyzerRealDriver(String analyzerRealDriver) {
        this.analyzerRealDriver = analyzerRealDriver;
    }

    protected void setAnalyzerRealJdbc(String analyzerRealJdbc) {
        this.analyzerRealJdbc = analyzerRealJdbc;
    }

    protected void setAnalyzerEntrypointPackages(List<String> analyzerEntrypointPackages) {
        this.analyzerEntrypointPackages = analyzerEntrypointPackages;
    }

    protected void setAnalyzerEntrypointSafezones(List<String> analyzerEntrypointSafezones) {
        this.analyzerEntrypointSafezones = analyzerEntrypointSafezones;
    }

    protected void setAnalyzerLogThresholdSevere(long analyzerLogThresholdSevere) {
        this.analyzerLogThresholdSevere = analyzerLogThresholdSevere;
    }

    protected void setAnalyzerLogThresholdWarning(long analyzerLogThresholdWarning) {
        this.analyzerLogThresholdWarning = analyzerLogThresholdWarning;
    }

    protected void setAnalyzerLogThresholdInfo(long analyzerLogThresholdInfo) {
        this.analyzerLogThresholdInfo = analyzerLogThresholdInfo;
    }

    protected void setAnalyzerLogThresholdFine(long analyzerLogThresholdFine) {
        this.analyzerLogThresholdFine = analyzerLogThresholdFine;
    }

    @Override
    public List<String> getAnalyzerEntrypointPackages() {
        return analyzerEntrypointPackages;
    }

    @Override
    public long getAnalyzerLogThresholdFine() {
        return analyzerLogThresholdFine;
    }

    @Override
    public long getAnalyzerLogThresholdInfo() {
        return analyzerLogThresholdInfo;
    }

    @Override
    public long getAnalyzerLogThresholdWarning() {
        return analyzerLogThresholdWarning;
    }

    @Override
    public long getAnalyzerLogThresholdSevere() {
        return analyzerLogThresholdSevere;
    }

    @Override
    public String getAnalyzerRealDriver() {
        return analyzerRealDriver;
    }

    @Override
    public String getAnalyzerRealJdbc() {
        return analyzerRealJdbc;
    }

    @Override
    public boolean isAnalyzerActive() {
        return analyzerActive;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public List<String> getAnalyzerEntrypointSafezones() {
        return analyzerEntrypointSafezones;
    }

    @Override
    public Map<String, String> getAnalyzerRegexSimplifiers() {
        return analyzerRegexSimplifiers;
    }

    @Override
    public int getAnalyzerMaxSizeInMemory() {
        return analyzerMaxSizeInMemory;
    }

    @Override
    public String getAnalyzerStoragePath() {
        return analyzerStoragePath;
    }

    @Override
    public boolean isAnalyzerUseDiskStorage() {
        return analyzerUseDiskStorage;
    }
}
