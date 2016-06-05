package com.pld.sqli.driver;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;
import com.pld.sqli.analyzer.ISQLInjectionRepository;
import com.pld.sqli.analyzer.SQLInjectionAnalyzerEntry;
import com.pld.sqli.analyzer.SQLInjectionAnalyzerImpl;
import com.pld.sqli.analyzer.SQLInjectionRepositoryImpl;
import com.pld.sqli.config.ISQLIAnalyzerConfig;
import com.pld.sqli.config.SQLIAnalyzerConfigHelper;
import com.pld.sqli.wrapper.ConnectionWrapper;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper around a JDBC driver to detect SQLInjection.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class AnalyzerDriver implements Driver {

    // Class variable.
    private Driver realDriver;
    private ISQLIAnalyzerConfig cfg;
    private ISQLInjectionAnalyzer analyzer;
    private ISQLInjectionRepository repo;

    static {
        try {
            DriverManager.registerDriver(new AnalyzerDriver());
        } catch (SQLException
                    |IOException
                    |IllegalAccessException
                    |InstantiationException
                    |ClassNotFoundException ex) {
            Logger.getLogger(AnalyzerDriver.class.getName()).log(Level.SEVERE,
                        "Unable to load the real driver. Failed with exception.", ex);
        }
    }

    public AnalyzerDriver() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this(SQLIAnalyzerConfigHelper.loadConfig(SQLIAnalyzerConfigHelper.getPropertiesConfigFile()));
    }

    AnalyzerDriver(ISQLIAnalyzerConfig cfg) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.cfg = cfg;
        if (cfg.isAnalyzerUseDiskStorage()) {
            File location = new File(cfg.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml");
            if (location.exists()) {
                File archive = new File(cfg.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml." + System.currentTimeMillis());
                location.renameTo(archive);
            }
        }
        realDriver = (Driver) Class.forName(cfg.getAnalyzerRealDriver()).newInstance();
        repo = new SQLInjectionRepositoryImpl(cfg);
        analyzer = new SQLInjectionAnalyzerImpl(cfg, repo, Executors.newFixedThreadPool(50));
    }

    @Override
    public Connection connect(String string, Properties prprts) throws SQLException {
        Connection conn = realDriver.connect(string.replaceFirst("sqli", cfg.getAnalyzerRealJdbc()), prprts);
        if (cfg.isAnalyzerActive()) {
            conn = new ConnectionWrapper(analyzer, conn);
        }
        return conn;
    }

    @Override
    public boolean acceptsURL(String string) throws SQLException {
        boolean result = string.startsWith("jdbc:sqli")
                && realDriver.acceptsURL(string.replaceFirst("sqli", cfg.getAnalyzerRealJdbc()));
        return result;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String string, Properties prprts) throws SQLException {
        return realDriver.getPropertyInfo(string, prprts);
    }

    @Override
    public int getMajorVersion() {
        return realDriver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return realDriver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return realDriver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return realDriver.getParentLogger();
    }

    private static AnalyzerDriver getDriver() throws SQLException {
        return (AnalyzerDriver) DriverManager.getDriver("jdbc:sqli://localhost/sqliDriver");
    }

    public static void shutdown() throws SQLException {
        getDriver().analyzer.shutdown();
    }

    public static void shutdownNow() throws SQLException {
        getDriver().analyzer.shutdownNow();
    }

    public static Map<String, Map<String, SQLInjectionAnalyzerEntry>> getAllEntries() throws SQLException, IOException {
        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result = new HashMap<>();
        getDriver().repo.getEntries().stream()
                .forEach((e) -> {
                    Map<String, SQLInjectionAnalyzerEntry> entry = result.get(e.getEntryPoint());
                    if (entry == null) {
                        entry = new HashMap<>();
                        result.put(e.getEntryPoint(), entry);
                    }
                    entry.put(e.getStatement(), e); //there should never be duplicates anyway
                });
        return result;
    }
}
