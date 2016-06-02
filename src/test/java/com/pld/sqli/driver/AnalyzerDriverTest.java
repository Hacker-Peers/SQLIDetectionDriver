package com.pld.sqli.driver;

import com.pld.sqli.config.SQLIAnalyzerConfig;
import com.pld.sqli.wrapper.ConnectionWrapper;
import mockit.Expectations;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static mockit.Deencapsulation.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test methods in AnalyzerDriver class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class AnalyzerDriverTest {

    /**
     * Test of initialize method, of class AnalyzerDriver.
     */
    @Test
    public void testInitialize() throws IOException {
        // default initialization
        setField(AnalyzerDriver.class, "initialized", false);
        invoke(AnalyzerDriver.class, "initialize");
        Driver realDriver = getField(AnalyzerDriver.class, "realDriver");
        assertTrue(realDriver instanceof com.mysql.jdbc.Driver);

        // already initialize
        invoke(AnalyzerDriver.class, "initialize");
        realDriver = getField(AnalyzerDriver.class, "realDriver");
        assertTrue(realDriver instanceof com.mysql.jdbc.Driver);

        // Archive last file
        File storageFile = new File("./target/SQLIAnalyzer/SQLIAnalyzerDiskStorage.xml");
        storageFile.getParentFile().mkdirs();
        storageFile.createNewFile();
        assertTrue(storageFile.exists());
        setField(AnalyzerDriver.class, "initialized", false);
        invoke(AnalyzerDriver.class, "initialize");
        assertFalse(storageFile.exists());
    }

    /**
     * Test of connect method, of class AnalyzerDriver.
     */
    @Test
    public void testConnect(@Mocked final Driver driver, @Mocked final SQLIAnalyzerConfig config) throws Exception {
        AnalyzerDriver instance = new AnalyzerDriver();
        setField(AnalyzerDriver.class, "realDriver", driver);

        new Expectations() {
            {
                SQLIAnalyzerConfig.getAnalyzerRealJdbc();
                result = "junit";
                times = 1;

                driver.connect("jdbc:junit://localhost:3306/sqliDriver", (Properties) any);
                times = 1;

                SQLIAnalyzerConfig.isAnalyzerActive();
                result = false;
                times = 1;
            }
        };
        Connection result = instance.connect("jdbc:sqli://localhost:3306/sqliDriver", new Properties());
        assertFalse(result instanceof ConnectionWrapper);

        new Expectations() {
            SQLIAnalyzerConfig config;

            {
                SQLIAnalyzerConfig.getAnalyzerRealJdbc();
                result = "junit";
                times = 1;

                driver.connect("jdbc:junit://localhost:3306/sqliDriver", (Properties) any);
                times = 1;

                SQLIAnalyzerConfig.isAnalyzerActive();
                result = true;
                times = 1;
            }
        };
        result = instance.connect("jdbc:sqli://localhost:3306/sqliDriver", new Properties());
        assertTrue(result instanceof ConnectionWrapper);
    }

    /**
     * Test of acceptsURL method, of class AnalyzerDriver.
     */
    @Test
    public void testAcceptsURL(@Mocked final Driver driver, @Mocked final SQLIAnalyzerConfig config) throws Exception {
        AnalyzerDriver instance = new AnalyzerDriver();
        setField(AnalyzerDriver.class, "realDriver", driver);

        new Expectations() {
            {
                SQLIAnalyzerConfig.getAnalyzerRealJdbc();
                result = "junit";
                times = 1;

                driver.acceptsURL("jdbc:junit://localhost:3306/sqliDriver");
                result = true;
                times = 1;

                SQLIAnalyzerConfig.getAnalyzerRealJdbc();
                result = "junit";
                times = 1;

                driver.acceptsURL("jdbc:junit://localhost:3306/sqliDriver");
                result = false;
                times = 1;
            }
        };
        assertTrue(instance.acceptsURL("jdbc:sqli://localhost:3306/sqliDriver"));
        assertFalse(instance.acceptsURL("jdbc:sqli://localhost:3306/sqliDriver"));
        assertFalse(instance.acceptsURL("jdbc:junit://localhost:3306/sqliDriver"));
    }

    /**
     * Test of getPropertyInfo method, of class AnalyzerDriver.
     */
    @Test
    public void testGetPropertyInfo(@Mocked final Driver driver) throws Exception {
        new Expectations() {
            {
                driver.getPropertyInfo("abc", (Properties) any);
                times = 1;
            }
        };
        AnalyzerDriver instance = new AnalyzerDriver();
        setField(AnalyzerDriver.class, "realDriver", driver);
        instance.getPropertyInfo("abc", new Properties());
    }

    /**
     * Test of getMajorVersion method, of class AnalyzerDriver.
     */
    @Test
    public void testGetMajorVersion(@Mocked final Driver driver) throws Exception {
        new Expectations() {
            {
                driver.getMajorVersion();
                times = 1;
            }
        };
        AnalyzerDriver instance = new AnalyzerDriver();
        setField(AnalyzerDriver.class, "realDriver", driver);
        instance.getMajorVersion();
    }

    /**
     * Test of getMinorVersion method, of class AnalyzerDriver.
     */
    @Test
    public void testGetMinorVersion(@Mocked final Driver driver) throws Exception {
        new Expectations() {
            {
                driver.getMinorVersion();
                times = 1;
            }
        };
        AnalyzerDriver instance = new AnalyzerDriver();
        setField(AnalyzerDriver.class, "realDriver", driver);
        instance.getMinorVersion();
    }

    /**
     * Test of jdbcCompliant method, of class AnalyzerDriver.
     */
    @Test
    public void testJdbcCompliant(@Mocked final Driver driver) throws Exception {
        new Expectations() {
            {
                driver.jdbcCompliant();
                times = 1;
            }
        };
        AnalyzerDriver instance = new AnalyzerDriver();
        setField(AnalyzerDriver.class, "realDriver", driver);
        instance.jdbcCompliant();
    }
}
