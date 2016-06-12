package org.hackerpeers.sqli.spec

import org.hackerpeers.sqli.analyzer.SQLInjectionAnalyzerEntry
import org.hackerpeers.sqli.driver.AnalyzerDriver
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

/**
 * @author pldupont@gmail.com
 * 09/06/2016.
 */
class PreparedStatementSpec extends Specification {
    @Shared
    def Connection conn;

    def setupSpec() {
        Properties properties = new Properties();
        properties.put("user", "sqli");
        properties.put("password", "sqli");
        String url = "jdbc:sqli:mem:sqliDriver;MV_STORE=FALSE;MVCC=FALSE"

        Class.forName("org.hackerpeers.sqli.driver.AnalyzerDriver");
        this.conn = DriverManager.getConnection(url, properties);
        conn.createStatement().execute("CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))")
    }

    // WARNING, the order of the test methods is important. Do not change the order.

    def "validate that the query is trap and there is no variation"() {
        when:
        AnalyzerDriver.allEntries.clear()
        PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM TEST WHERE NAME = ? AND ID = ?")
        ps.setString(1, "1");
        ps.setInt(2, 2);
        ps.execute();
        Thread.sleep(1000L) // Since the work is threaded, we need to wait a little bit.

        String entryPoint = AnalyzerDriver.allEntries.keySet().find { k -> k.contains("spock_feature_0_0") } // Current test
        def queryMap = AnalyzerDriver.allEntries.get(entryPoint)
        SQLInjectionAnalyzerEntry entry = queryMap.get("SELECT 1 FROM TEST WHERE NAME = ? AND ID = ?")

        then:
        !AnalyzerDriver.allEntries.isEmpty()
        entryPoint != null
        queryMap != null
        queryMap.size() == 1
        queryMap.containsKey("SELECT 1 FROM TEST WHERE NAME = ? AND ID = ?")
        entry != null
        entry.getCount() == 1
        entry.getVariationList() == "0"

    }

    def "validate that the query is trap and there is 3 IN Clause variation"() {
        when:
        AnalyzerDriver.allEntries.clear()
        (1..3).each { inVariation ->
            PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM TEST WHERE NAME = ? AND ID IN (?${", ?" * inVariation})")
            ps.setString(1, "1");
            for (int i = 0; i <= inVariation; i++) {
                ps.setInt(2 + i, inVariation);
            }
            ps.execute();
        }
        Thread.sleep(1000L) // Since the work is threaded, we need to wait a little bit.

        String entryPoint = AnalyzerDriver.allEntries.keySet().find { k -> k.contains("spock_feature_0_1") } // Current test
        def queryMap = AnalyzerDriver.allEntries.get(entryPoint)
        SQLInjectionAnalyzerEntry entry = queryMap.get("SELECT 1 FROM TEST WHERE NAME = ? AND ID IN (?)")

        then:
        !AnalyzerDriver.allEntries.isEmpty()
        entryPoint != null
        queryMap != null
        queryMap.size() == 1
        queryMap.containsKey("SELECT 1 FROM TEST WHERE NAME = ? AND ID IN (?)")
        entry != null
        entry.getCount() == 3
        entry.getVariationList() == "2,3,4"

    }

    def "validate that the query is trap and there is 3 different queries from the same entry point"() {
        when:
        def queries = ["SELECT 1 FROM TEST WHERE NAME = 'ABC'", "SELECT 1 FROM TEST WHERE ID = 123", "SELECT 1 FROM TEST"]
        AnalyzerDriver.allEntries.clear()
        queries.each { q ->
            PreparedStatement ps = conn.prepareStatement(q)
            ps.execute();
        }
        Thread.sleep(1000L) // Since the work is threaded, we need to wait a little bit.

        String entryPoint = AnalyzerDriver.allEntries.keySet().find { k -> k.contains("spock_feature_0_2") } // Current test
        def queryMap = AnalyzerDriver.allEntries.get(entryPoint)

        then:
        !AnalyzerDriver.allEntries.isEmpty()
        entryPoint != null
        queryMap != null
        queryMap.size() == 3
        queryMap.containsKey("SELECT 1 FROM TEST WHERE NAME = 'ABC'")
        queryMap.containsKey("SELECT 1 FROM TEST WHERE ID = 123")
        queryMap.containsKey("SELECT 1 FROM TEST")

    }
//
//    @Unroll
//    def "validate that the skus are all in the inmemory datastore"() {
//
//        expect:
//        Sku sku = this.skuRepository.findBySkuname(skuname)
//        sku != null
//        sku.skuname == skuname
//
//        where:
//        skuname << ["SKU01","SKU02","SKU11","SKU12","SKU21","SKU22","SKU31","SKU32","SKU41","SKU42"]
//    }
//
//    def "validate that an unknown skuname returns null"() {
//
//        when:
//        Sku shouldBeNull = this.skuRepository.findBySkuname("UNKNOWN")
//
//        then:
//        shouldBeNull == null
//    }
}
