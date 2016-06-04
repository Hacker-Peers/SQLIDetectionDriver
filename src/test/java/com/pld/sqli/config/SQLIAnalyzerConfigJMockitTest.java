package com.pld.sqli.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import mockit.Mocked;
import org.junit.After;
import java.io.InputStream;
import mockit.NonStrictExpectations;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mockit.Expectations;
import org.junit.Test;
import static org.junit.Assert.*;
import static mockit.Deencapsulation.*;

/**
 * Test methods in SQLIAnalyzerConfig class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLIAnalyzerConfigJMockitTest {
    
    @After
    public void tearDown() {
        setField(SQLIAnalyzerConfig.class, "initialized", false);
        setField(SQLIAnalyzerConfig.class, "propertiesFile", "SQLIAnalyzer.properties");
        invoke(SQLIAnalyzerConfig.class, "initialize");
    }
    
    /**
     * Test constructor, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testConstructor() {
        Object o = newInstance(SQLIAnalyzerConfig.class);
        assertNotNull(o);
    }
    
    /**
     * Test of initialize method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testInitialize(@Mocked final Logger log, @Mocked final InputStream is) throws IOException {
        // default initialization
        invoke(SQLIAnalyzerConfig.class, "initialize");
        assertArrayEquals(Arrays.asList("com.pld.sqli.demo").toArray(),
                SQLIAnalyzerConfig.getAnalyzerEntrypointPackages().toArray());
        assertEquals("mysql", SQLIAnalyzerConfig.getAnalyzerRealJdbc());
        assertEquals("com.mysql.jdbc.Driver", SQLIAnalyzerConfig.getAnalyzerRealDriver());
        assertEquals(0L, SQLIAnalyzerConfig.getAnalyzerLogThresholdFine());
        assertEquals(1000L, SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
        assertEquals(-1L, SQLIAnalyzerConfig.getAnalyzerLogThresholdWarning());
        assertEquals(-1L, SQLIAnalyzerConfig.getAnalyzerLogThresholdSevere());
        assertTrue(SQLIAnalyzerConfig.isAnalyzerActive());
        assertTrue(SQLIAnalyzerConfig.isLoaded());
        
        // already initialize
        invoke(SQLIAnalyzerConfig.class, "initialize");
        assertArrayEquals(Arrays.asList("com.pld.sqli.demo").toArray(),
                SQLIAnalyzerConfig.getAnalyzerEntrypointPackages().toArray());
        assertEquals("mysql", SQLIAnalyzerConfig.getAnalyzerRealJdbc());
        assertEquals("com.mysql.jdbc.Driver", SQLIAnalyzerConfig.getAnalyzerRealDriver());
        assertTrue(SQLIAnalyzerConfig.isAnalyzerActive());
        assertTrue(SQLIAnalyzerConfig.isLoaded());
       
        // File not found.
        new Expectations() {
            {
                Logger.getLogger(SQLIAnalyzerConfig.class.getName());
                result = log;
                times = 1;
                
                log.log(Level.SEVERE, "Unable to load SQLIAnalyzerConfig properties. SQLIAnalyzer.properties not found.");
                times = 1;
            }
        };
        setField(SQLIAnalyzerConfig.class, "initialized", false);
        setField(SQLIAnalyzerConfig.class, "propertiesFile", "JUnit.properties.fileNotFound");
        invoke(SQLIAnalyzerConfig.class, "initialize");
        assertFalse(SQLIAnalyzerConfig.isAnalyzerActive());
        assertFalse(SQLIAnalyzerConfig.isLoaded());

        // Driver invalid
        new NonStrictExpectations() {
            {
                is.close();
                result = new Exception();
            }
        };
        
        new Expectations() {
            {
                Logger.getLogger(SQLIAnalyzerConfig.class.getName());
                result = new Exception();
                result = log;
                times = 2;
                
                log.log(Level.SEVERE, "Unable to load SQLIAnalyzerConfig properties. Failed with exception.", (Throwable) any);
                times = 1;
            }
        };
        setField(SQLIAnalyzerConfig.class, "initialized", false);
        invoke(SQLIAnalyzerConfig.class, "initialize");
    }

    /**
     * Test of getEntryPointPackage method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testGetEntryPointPackage() {
        setField(SQLIAnalyzerConfig.class, "analyzerEntrypointPackages", Arrays.asList("com.pld.sqli"));
        assertArrayEquals(Arrays.asList("com.pld.sqli").toArray(),
                SQLIAnalyzerConfig.getAnalyzerEntrypointPackages().toArray());
    }

    /**
     * Test of getAnalyzerLogThresholdFine method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testgetAnalyzerLogThresholdFine() {
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdFine", 10L);
        Long result = SQLIAnalyzerConfig.getAnalyzerLogThresholdFine();
        assertEquals((Long) 10L, result);
    }

    /**
     * Test of getAnalyzerLogThresholdInfo method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testgetAnalyzerLogThresholdInfo() {
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdInfo", 10L);
        Long result = SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo();
        assertEquals((Long) 10L, result);
    }

    /**
     * Test of getAnalyzerLogThresholdWarning method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testgetAnalyzerLogThresholdWarning() {
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdWarning", 10L);
        Long result = SQLIAnalyzerConfig.getAnalyzerLogThresholdWarning();
        assertEquals((Long) 10L, result);
    }

    /**
     * Test of getAnalyzerLogThresholdSevere method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testgetAnalyzerLogThresholdSevere() {
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdSevere", 10L);
        Long result = SQLIAnalyzerConfig.getAnalyzerLogThresholdSevere();
        assertEquals((Long) 10L, result);
    }

    /**
     * Test of getRealDriverClass method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testGetRealDriverClass() {
        setField(SQLIAnalyzerConfig.class, "analyzerRealDriver", "com.driver");
        String result = SQLIAnalyzerConfig.getAnalyzerRealDriver();
        assertEquals("com.driver", result);
    }

    /**
     * Test of getRealJDBCType method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testGetRealJDBCType() {
        setField(SQLIAnalyzerConfig.class, "analyzerRealJdbc", "mysql");
        String result = SQLIAnalyzerConfig.getAnalyzerRealJdbc();
        assertEquals("mysql", result);
    }

    /**
     * Test of isAnalyzerActive method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testIsAnalyzerActive() {
        Boolean result = SQLIAnalyzerConfig.isAnalyzerActive();
        assertTrue(result);
    }

    /**
     * Test of isLoaded method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testIsLoaded() {
        Boolean result = SQLIAnalyzerConfig.isAnalyzerActive();
        assertTrue(result);
    }

    /**
     * Test of getAnalyzerEntrypointSafezones method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testGetAnalyzerEntrypointSafezones() {
        List<String> safezones = SQLIAnalyzerConfig.getAnalyzerEntrypointSafezones();
        assertArrayEquals(new String[]{
            "com.pld.sqli.demo.test.",
            "com.pld.sqli.demo.test2.DemoIgnored#",
            "com.pld.sqli.demo.test3.DemoIngored#doDemo()"
        }, safezones.toArray());
    }
    
    /**
     * Test of getAnalyzerEntrypointSafezones method, of class SQLIAnalyzerConfig.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testGetAnalyzerEntrypointSafezones_TryToModify() {
        SQLIAnalyzerConfig.getAnalyzerEntrypointSafezones().add("1");
    }

    /**
     * Test of getAnalyzerRegexSimplifiers method, of class SQLIAnalyzerConfig.
     */
    @Test
    public void testGetAnalyzerRegexSimplifiers() {
        Map<String, String> regexes = SQLIAnalyzerConfig.getAnalyzerRegexSimplifiers();
        assertEquals(2, regexes.size());
        assertEquals("Stats_", regexes.get("Stats_[0-9]+"));
        assertEquals("Structs_", regexes.get("Structs_[0-9]+"));

    }

    /**
     * Test of getAnalyzerRegexSimplifiers method, of class SQLIAnalyzerConfig.
     */
    @Test(expected=UnsupportedOperationException.class)
    public void testGetAnalyzerRegexSimplifiers_TryToModify() {
        SQLIAnalyzerConfig.getAnalyzerRegexSimplifiers().put("1", "2");
    }
}
