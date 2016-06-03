package com.pld.sqli.driver;

import com.pld.sqli.config.SQLIAnalyzerConfig;
import com.pld.sqli.wrapper.ConnectionWrapper;
import java.io.File;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Wrapper around a JDBC driver to detect SQLInjection.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class AnalyzerDriver implements Driver {

    // Class variable.
    private static boolean initialized = false;
    private static Driver realDriver;

    static {
        initialize();
    }

    /**
     * Initialize the driver.
     */
    private static synchronized void initialize() {
        if (!initialized) {
            try {
                if (SQLIAnalyzerConfig.isLoaded()) {
                    DriverManager.registerDriver(new AnalyzerDriver());
                    if (SQLIAnalyzerConfig.isAnalyzerUseDiskStorage()) {
                        File location = new File(SQLIAnalyzerConfig.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml");
                        if (location.exists()) {
                            File archive = new File(SQLIAnalyzerConfig.getAnalyzerStoragePath(), "SQLIAnalyzerDiskStorage.xml." + System.currentTimeMillis());
                            location.renameTo(archive);
                        }
                    }
                    realDriver = (Driver) Class.forName(SQLIAnalyzerConfig.getAnalyzerRealDriver()).newInstance();
                } else {
                    Logger.getLogger(AnalyzerDriver.class.getName()).log(Level.SEVERE,
                            "The SQLIAnalyzer configuration is not loaded properly.");
                }
            } catch (Exception ex) {
                Logger.getLogger(AnalyzerDriver.class.getName()).log(Level.SEVERE,
                        "Unable to load the real driver. Failed with exception.", ex);
            }

            initialized = true;
        }
    }

    @Override
    public Connection connect(String string, Properties prprts) throws SQLException {
        Connection conn = realDriver.connect(string.replaceFirst("sqli", SQLIAnalyzerConfig.getAnalyzerRealJdbc()), prprts);
        if (SQLIAnalyzerConfig.isAnalyzerActive()) {
            conn = new ConnectionWrapper(conn);
        }
        return conn;
    }

    @Override
    public boolean acceptsURL(String string) throws SQLException {
        boolean result = string.startsWith("jdbc:sqli")
                && realDriver.acceptsURL(string.replaceFirst("sqli", SQLIAnalyzerConfig.getAnalyzerRealJdbc()));
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
}
