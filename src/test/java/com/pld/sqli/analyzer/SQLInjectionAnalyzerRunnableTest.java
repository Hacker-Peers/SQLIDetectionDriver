package com.pld.sqli.analyzer;

import java.util.logging.Logger;
import java.io.IOException;

import com.pld.sqli.driver.AnalyzerDriver;
import mockit.Expectations;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.junit.After;
import com.pld.sqli.config.SQLIAnalyzerConfig;
import java.util.logging.Level;
import org.junit.Test;
import static org.junit.Assert.*;
import static mockit.Deencapsulation.*;

/**
 * Test methods in SQLInjectionAnalyzerRunnable class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerRunnableTest {

    @After
    public void tearDown() {
        setField(SQLIAnalyzerConfig.class, "initialized", false);
        invoke(SQLIAnalyzerConfig.class, "initialize");
    }

    /**
     * Test of run method, of class SQLInjectionAnalyzerRunnable.
     */
    @Test
    public void testRun() throws Exception {
        System.out.println(SQLIAnalyzerConfig.getAnalyzerStoragePath());
        System.out.println(new File(SQLIAnalyzerConfig.getAnalyzerStoragePath()).getAbsolutePath());
        new File("./target/SQLIAnalyzer/SQLIAnalyzerDiskStorage.xml").delete();
        List<String> backup = new ArrayList<String>(SQLIAnalyzerConfig.getAnalyzerEntrypointSafezones());
        try {
            // No statement
            SQLInjectionAnalyzerRunnable runnable = new SQLInjectionAnalyzerRunnable("junit", null, null, 0L, 0L, Thread.currentThread());
            assertEquals(0, SQLInjectionAnalyzerRunnable.getEntries().size());
            runnable.run();
            assertEquals(0, SQLInjectionAnalyzerRunnable.getEntries().size());

            // 1 statement, from the safe zone (com.safe.zone)
            SQLInjectionAnalyzerRunnable runnable2 = new SQLInjectionAnalyzerRunnable(
                    "com.pld.sqli.demo.test.Test#doSomething()", Arrays.asList("select sysdate from dual"), null, 0L, 0L, Thread.currentThread());
            runnable2.run();
            assertEquals(0, SQLInjectionAnalyzerRunnable.getEntries().size());

            // 1 statement, from the safe zone (com.safe.zone)
            SQLInjectionAnalyzerRunnable runnable3 = new SQLInjectionAnalyzerRunnable(
                    "com.pld.sqli.demo.test2.Test#doSomething()", Arrays.asList("select sysdate from dual WHERE id IN (?,?)"), null, 0L, 0L, Thread.currentThread());
            runnable3.run();
            assertEquals(1, SQLInjectionAnalyzerRunnable.getEntries().size());
            
            // Clear safe zone
            setField(SQLIAnalyzerConfig.class, "analyzerEntrypointSafezones", Arrays.asList(""));
            runnable2.run();
            assertEquals(2, SQLInjectionAnalyzerRunnable.getEntries().size());
            
            runnable2.run();
            
        } finally {
            new File("./target/SQLIAnalyzer/SQLIAnalyzerDiskStorage.xml").delete();
            setField(SQLInjectionAnalyzerRunnable.class, "entries", new TreeMap<String, Map<String, SQLInjectionAnalyzerEntry>>());
            setField(SQLIAnalyzerConfig.class, "analyzerEntrypointSafezones", backup);
        }
    }

    /**
     * Test of getLogLevel method, of class SQLInjectionAnalyzer.
     */
    @Test
    public void testGetLogLevelAllOff() {
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdFine", -1);
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdInfo", -1);
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdWarning", -1);
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdSevere", -1);
        SQLInjectionAnalyzerRunnable runnable = new SQLInjectionAnalyzerRunnable("junit", null, null, 0L, 0L, Thread.currentThread());

        Level result = invoke(runnable, "getLogLevel", 0L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", 1000L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", 10000L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", 100000L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", 1000000L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", 10000000L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", 100000000L);
        assertEquals(Level.OFF, result);

        result = invoke(runnable, "getLogLevel", System.currentTimeMillis());
        assertEquals(Level.OFF, result);
    }

    /**
     * Test of getLogLevel method, of class SQLInjectionAnalyzer.
     */
    @Test
    public void testGetLogLevel() {
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdFine", 0);
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdInfo", 10);
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdWarning", 20);
        setField(SQLIAnalyzerConfig.class, "analyzerLogThresholdSevere", 30);
        SQLInjectionAnalyzerRunnable runnable = new SQLInjectionAnalyzerRunnable("junit", null, null, 0L, 0L, Thread.currentThread());

        Level result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdFine());
        assertEquals(Level.FINE, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo() - 1L);
        assertEquals(Level.FINE, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdInfo());
        assertEquals(Level.INFO, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdWarning() - 1L);
        assertEquals(Level.INFO, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdWarning());
        assertEquals(Level.WARNING, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdSevere() - 1L);
        assertEquals(Level.WARNING, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdSevere());
        assertEquals(Level.SEVERE, result);

        result = invoke(runnable, "getLogLevel", SQLIAnalyzerConfig.getAnalyzerLogThresholdFine() - 1L);
        assertEquals(Level.OFF, result);
    }

    /**
     * Test of getEntries method, of class SQLInjectionAnalyzer.
     */
    @Test
    public void testGetEntries() throws Exception {
        setField(SQLIAnalyzerConfig.class, "analyzerUseDiskStorage", false);
        setField(SQLIAnalyzerConfig.class, "analyzerMaxSizeInMemory", 5);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "abc", "select1", 0);

        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result = SQLInjectionAnalyzerRunnable.getEntries();
        assertTrue(result.containsKey("abc"));

        setField(SQLIAnalyzerConfig.class, "analyzerUseDiskStorage", true);
        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result2 = SQLInjectionAnalyzerRunnable.getEntries();
        assertTrue(result2.containsKey("abc"));

        setField(SQLInjectionAnalyzerRunnable.class, "entries", new TreeMap<String, Map<String, SQLInjectionAnalyzerEntry>>());
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "def", "select2", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "def", "select3", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "def", "select3", 1);

        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result3 = SQLInjectionAnalyzerRunnable.getEntries();
        assertTrue(result3.containsKey("abc"));
        assertTrue(result3.containsKey("def"));
        assertEquals(2, result3.size());

        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "g", "select4", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "h", "select5", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "i", "select6", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "j", "select6", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "k", "select6", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "l", "select6", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "def", "select7", 0);
        invoke(SQLInjectionAnalyzerRunnable.class, "addStatement", "def", "select3", 2);

        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result4 = SQLInjectionAnalyzerRunnable.getEntries();
        assertTrue(result4.containsKey("abc"));
        assertTrue(result4.containsKey("def"));
        assertEquals(3, result4.get("def").size());
        assertTrue(result4.containsKey("g"));
        assertTrue(result4.containsKey("h"));
        assertTrue(result4.containsKey("i"));
        assertTrue(result4.containsKey("j"));
        assertTrue(result4.containsKey("k"));
        assertTrue(result4.containsKey("l"));
        assertEquals(8, result4.size());
    }
}
