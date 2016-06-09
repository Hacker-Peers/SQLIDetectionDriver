package org.hackerpeers.sqli.config;

import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.*;

/**
 * Test methods in SQLIAnalyzerConfig class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLIAnalyzerConfigHelperTest {

    @Test
    public void testDefaultState() {
        ISQLIAnalyzerConfig config = new SQLIAnalyzerConfigImpl();
        assertEquals(config.isLoaded(), false);
        assertEquals(config.isAnalyzerActive(), false);
        assertEquals(config.isAnalyzerUseDiskStorage(), false);
        assertEquals(config.getAnalyzerMaxSizeInMemory(), 0);
        assertNull(config.getAnalyzerStoragePath());
        assertNull(config.getAnalyzerRegexSimplifiers());
        assertNull(config.getAnalyzerRealDriver());
        assertNull(config.getAnalyzerRealJdbc());
        assertNull(config.getAnalyzerEntrypointPackages());
        assertNull(config.getAnalyzerEntrypointSafezones());
        assertEquals(config.getAnalyzerLogThresholdSevere(), -1L);
        assertEquals(config.getAnalyzerLogThresholdWarning(), -1L);
        assertEquals(config.getAnalyzerLogThresholdInfo(), -1L);
        assertEquals(config.getAnalyzerLogThresholdFine(), -1L);
    }

    @Test
    public void testWithEmptyPropertiesFile() throws IOException {

        ISQLIAnalyzerConfig config = SQLIAnalyzerConfigHelper.loadConfig(ClassLoader.getSystemResource("SQLIAnalyzer-empty.properties"));

        assertEquals(config.isLoaded(), true);
        assertEquals(config.isAnalyzerActive(), false);
        assertEquals(config.isAnalyzerUseDiskStorage(), false);
        assertEquals(config.getAnalyzerMaxSizeInMemory(), 100);
        assertNull(config.getAnalyzerStoragePath());
        assertNotNull(config.getAnalyzerRegexSimplifiers());
        assertTrue(config.getAnalyzerRegexSimplifiers().isEmpty());
        assertNull(config.getAnalyzerRealDriver());
        assertNull(config.getAnalyzerRealJdbc());
        assertNotNull(config.getAnalyzerEntrypointPackages());
        assertEquals(config.getAnalyzerEntrypointPackages().iterator(), Arrays.asList("com").iterator());
        assertNotNull(config.getAnalyzerEntrypointSafezones());
        assertEquals(config.getAnalyzerEntrypointSafezones().iterator(), Arrays.asList("").iterator());
        assertEquals(config.getAnalyzerLogThresholdSevere(), -1L);
        assertEquals(config.getAnalyzerLogThresholdWarning(), -1L);
        assertEquals(config.getAnalyzerLogThresholdInfo(), -1L);
        assertEquals(config.getAnalyzerLogThresholdFine(), -1L);
    }

    @Test
    public void testWithTestPropertiesFile() throws IOException {
        ISQLIAnalyzerConfig config = SQLIAnalyzerConfigHelper.loadConfig(SQLIAnalyzerConfigHelper.getPropertiesConfigFile());

        assertEquals(config.isLoaded(), true);
        assertEquals(config.isAnalyzerActive(), true);
        assertEquals(config.isAnalyzerUseDiskStorage(), true);
        assertEquals(config.getAnalyzerMaxSizeInMemory(), 100);
        assertEquals(config.getAnalyzerStoragePath(), "./target/SQLIAnalyzer");
        assertNotNull(config.getAnalyzerRegexSimplifiers());
        assertEquals(config.getAnalyzerRegexSimplifiers().size(), 2);
        assertEquals(config.getAnalyzerRegexSimplifiers().get("Stats_[0-9]+"), "Stats_");
        assertEquals(config.getAnalyzerRegexSimplifiers().get("Structs_[0-9]+"), "Structs_");
        assertEquals(config.getAnalyzerRealDriver(), "com.mysql.jdbc.Driver");
        assertEquals(config.getAnalyzerRealJdbc(), "mysql");
        assertNotNull(config.getAnalyzerEntrypointPackages());
        assertEquals(config.getAnalyzerEntrypointPackages().iterator(), Arrays.asList("com.pld.sqli.demo").iterator());
        assertNotNull(config.getAnalyzerEntrypointSafezones());
        assertEquals(config.getAnalyzerEntrypointSafezones().iterator(),
                Arrays.asList("com.pld.sqli.demo.test.", "com.pld.sqli.demo.test2.DemoIgnored#", "com.pld.sqli.demo.test3.DemoIngored#doDemo()").iterator());
        assertEquals(config.getAnalyzerLogThresholdSevere(), -1L);
        assertEquals(config.getAnalyzerLogThresholdWarning(), -1L);
        assertEquals(config.getAnalyzerLogThresholdInfo(), 1000L);
        assertEquals(config.getAnalyzerLogThresholdFine(), 0L);
    }
}
