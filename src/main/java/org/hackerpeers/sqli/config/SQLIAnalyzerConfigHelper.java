package org.hackerpeers.sqli.config;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLIAnalyzerConfigHelper {
    private static final String CONFIG_FILE_NAME = "SQLIAnalyzer.properties";

    /**
     * Search for the first resource matching the propertiesFile name.
     * @return The first resource found.
     */
    public static URL getPropertiesConfigFile() {
        URL resource = ClassLoader.getSystemResource(CONFIG_FILE_NAME);
        if (resource == null) {
            resource = ClassLoader.getSystemResource("META-INF/" + CONFIG_FILE_NAME);
        }
        if (resource == null) {
            resource = SQLIAnalyzerConfigImpl.class.getClassLoader().getResource(CONFIG_FILE_NAME);
        }
        if (resource == null) {
            resource = SQLIAnalyzerConfigImpl.class.getClassLoader().getResource("META-INF/" + CONFIG_FILE_NAME);
        }

        return resource;
    }

    public static ISQLIAnalyzerConfig loadConfig(URL configLocation) throws IOException {
        SQLIAnalyzerConfigImpl cfg = new SQLIAnalyzerConfigImpl();

        Properties props = new Properties();
        InputStream openStream = configLocation.openStream();
        props.load(openStream);
        openStream.close();
        cfg.setAnalyzerActive(Boolean.parseBoolean(props.getProperty("analyzer.active", "false")));

        // Load storage settings.
        cfg.setAnalyzerUseDiskStorage(Boolean.parseBoolean(props.getProperty("analyzer.active", "false")));
        cfg.setAnalyzerMaxSizeInMemory(NumberUtils.toInt(props.getProperty("analyzer.maxSizeInMemory"), 100));
        cfg.setAnalyzerStoragePath(props.getProperty("analyzer.storage.path", null));

        // Load RegEx simplifiers.
        String regexSimplifiers = props.getProperty("analyzer.regex.simplifiers", "");
        Map<String, String> analyzerRegexSimplifiers = new HashMap<>();
        for (String regex : regexSimplifiers.split(",")) {
            String[] expr = regex.split("=");
            if (expr.length == 2) {
                analyzerRegexSimplifiers.put(expr[0], expr[1]);
            }
        }
        cfg.setAnalyzerRegexSimplifiers(Collections.unmodifiableMap(analyzerRegexSimplifiers));

        // Load real driver parameters.
        cfg.setAnalyzerRealDriver(props.getProperty("analyzer.real.driver"));
        cfg.setAnalyzerRealJdbc(props.getProperty("analyzer.real.jdbc"));

        // Load analyzer entry point detection.
        String packages = props.getProperty("analyzer.entrypoint.packages", "com");
        List<String> analyzerEntrypointPackages = new ArrayList<>();
        analyzerEntrypointPackages.addAll(Arrays.asList(packages.split(",")));
        cfg.setAnalyzerEntrypointPackages(Collections.unmodifiableList(analyzerEntrypointPackages));

        // Load safe zones.
        String safezones = props.getProperty("analyzer.entrypoint.safezones", "");
        List<String> analyzerEntrypointSafezones = new ArrayList<>();
        analyzerEntrypointSafezones.addAll(Arrays.asList(safezones.split(",")));
        cfg.setAnalyzerEntrypointSafezones(Collections.unmodifiableList(analyzerEntrypointSafezones));

        // Load logging configuration.
        cfg.setAnalyzerLogThresholdSevere(NumberUtils.toLong(props.getProperty("analyzer.log.threshold.severe"), -1L));
        cfg.setAnalyzerLogThresholdWarning(NumberUtils.toLong(props.getProperty("analyzer.log.threshold.warning"), -1L));
        cfg.setAnalyzerLogThresholdInfo(NumberUtils.toLong(props.getProperty("analyzer.log.threshold.info"), -1L));
        cfg.setAnalyzerLogThresholdFine(NumberUtils.toLong(props.getProperty("analyzer.log.threshold.fine"), -1L));

        cfg.setLoaded(true);

        return cfg;
    }
}
