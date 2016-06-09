package org.hackerpeers.sqli.spec

import org.hackerpeers.sqli.driver.AnalyzerDriver
import org.junit.Ignore
import spock.lang.Specification

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

/**
 * @author pldupont@gmail.com
 * 09/06/2016.
 */
@Ignore
class PreparedStatementSpec extends Specification {
    def Connection conn;

    def setup() {
        Properties properties = new Properties();
        properties.put("user", "sqli");
        properties.put("password", "sqli");
        String url = "jdbc:sqli://localhost/sqliDriver";

        Class.forName("org.hackerpeers.sqli.driver.AnalyzerDriver");
        this.conn = DriverManager.getConnection(url, properties);
    }

    def "validate that all parameters are trapped and reported"() {
        when:
        PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM dual WHERE :A = :A AND :B = :B")
        ps.setString("A", "1");
        ps.setInt("B", 2);
        ps.execute();

        then:
        AnalyzerDriver.allEntries.isEmpty()

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
