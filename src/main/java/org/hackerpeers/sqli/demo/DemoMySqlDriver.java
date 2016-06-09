package org.hackerpeers.sqli.demo;

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.hackerpeers.sqli.analyzer.SQLInjectionAnalyzerEntry;
import org.hackerpeers.sqli.analyzer.SQLInjectionAnalyzerImpl;
import org.hackerpeers.sqli.driver.AnalyzerDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo to test the SQLJnjectionAnalyzerDriver.
 * @author pierre-lucdupont
 */
public class DemoMySqlDriver {

    /**
     * The Demo entry point.
     * @param args The command line arguments.
     * @throws Exception If an error occurs while doing the Demo.
     */
    public static void main(String[] args) throws Exception {
        Logger.getLogger(SQLInjectionAnalyzerImpl.class.getName()).setLevel(Level.FINE);
        DemoMySqlDriver demo = new DemoMySqlDriver();
        demo.testSimpleQuery(demo.openSQLIConnection_MySQL());
        demo.testINClauseQuery(demo.openSQLIConnection_MySQL());
        demo.testBatchQuery(demo.openSQLIConnection_MySQL());

        Thread.sleep(500L);
        AnalyzerDriver.shutdown();

        Map<String, Map<String, SQLInjectionAnalyzerEntry>> result = AnalyzerDriver.getAllEntries();
        System.out.println(new XStream().toXML(result));
    }

    /**
     * Test executing a batch query multiple time.
     * @param conn The connection used to do the batch query.
     * @throws Exception If an error occurs while accessing the database.
     */
    private void testBatchQuery(Connection conn) throws Exception {
        conn.createStatement().executeUpdate("delete from test where col1 > 3");
        PreparedStatement prepareStatement = conn.prepareStatement("INSERT INTO test (col2) VALUES (?)");
        for (int i = 10; i < 15; i++) {
            prepareStatement.setString(1, String.format("batch %s", i));
            prepareStatement.addBatch();
        }
        prepareStatement.executeBatch();
        prepareStatement.close();
        conn.close();
    }

    /**
     * Test executing a simple query.
     * @param conn The connection used to do the single query.
     * @throws Exception If an error occurs while accessing the database.
     */
    private void testSimpleQuery(Connection conn) throws Exception {
        PreparedStatement prepareStatement = conn.prepareStatement("select * from test where col1 = ?");
        prepareStatement.setInt(1, 2);
        prepareStatement.getConnection();
        ResultSet resultSet = prepareStatement.executeQuery();
        resultSet.close();
        prepareStatement.close();
        conn.close();
    }

    /**
     * Test executing an IN Clause query with multiple variation in the number of parameters.
     * @param conn The connection used to do the IN Clause query.
     * @throws Exception If an error occurs while accessing the database.
     */
    private void testINClauseQuery(Connection conn) throws Exception {
        for (int i = 1; i <= 9; i++) {
            PreparedStatement prepareStatement;
            if (i != 9) {
                prepareStatement = conn.prepareStatement("select * from test where col1 IN\n(?" + StringUtils.repeat("\r\n,\r\n?", (i - 1) % 3) + ")");
                for (int j = 1; j <= 1 + ((i - 1) % 3); j++) {
                    prepareStatement.setInt(j, j);
                }
            } else {
                prepareStatement = conn.prepareStatement("select * from test where col1 = 1");
            }
            ResultSet resultSet = prepareStatement.executeQuery();
            resultSet.close();
            prepareStatement.close();
        }
        conn.close();
    }

    /**
     * @return A basic MySQL connection.
     * @throws Exception If an error occurs while loading the MySQL connection.
     */
    private Connection openMySQLConnection() throws Exception {
        Properties properties = new Properties();
        properties.put("user", "sqli");
        properties.put("password", "sqli");
        String url = "jdbc:mysql://localhost:3306/sqliDriver";

        Class.forName("com.mysql.jdbc.Driver");
        Connection c = DriverManager.getConnection(url, properties);
        return c;
    }

    /**
     * @return A SQL Injection Wrapper connection over a MySQL connection.
     * @throws Exception If an error occurs while loading the AnalyzerDriver.
     */
    private Connection openSQLIConnection_MySQL() throws Exception {
        Properties properties = new Properties();
        properties.put("user", "sqli");
        properties.put("password", "sqli");
        String url = "jdbc:sqli://localhost/sqliDriver";

        Class.forName("AnalyzerDriver");
        Connection c = DriverManager.getConnection(url, properties);
        return c;
    }

    /**
     * @return A SQL Injection Wrapper connection over an Oracle connection.
     * @throws Exception If an error occurs while loading the AnalyzerDriver.
     */
    private Connection openSQLIConnection_Oracle() throws Exception {
        Properties properties = new Properties();
        properties.put("user", "DUMMY");
        properties.put("password", "DUMMY");
        String url = "jdbc:sqli:thin:@bouvmdb01:1521:devorcl";

        Class.forName("AnalyzerDriver");
        Connection c = DriverManager.getConnection(url, properties);
        return c;
    }

}
