[![CircleCI](https://circleci.com/gh/Hacker-Peers/SQLIDetectionDriver.svg?style=shield)](https://circleci.com/gh/Hacker-Peers/SQLIDetectionDriver) [![Coverage Status](https://coveralls.io/repos/github/Hacker-Peers/SQLIDetectionDriver/badge.svg?branch=master)](https://coveralls.io/github/Hacker-Peers/SQLIDetectionDriver)

## Description
This project is a JDBC Driver wrapper that help to detect potential SQLInjection security issue.
By analyzing each SQL structure by code entry point, the library can detect variation coming from
the same source code.

The purpose of this library is not to be deployed in production but in QA environment.

In order to control the SQLIDetectionDriver, you need to add a SQLIAnalyzer.properties file in
your ClassLoader resource path.

## Configuration
Once you have configured the SQLIDectectionDriver (see below), you can start using it like this:
```java
        Properties properties = new Properties();
        properties.put("user", "sqli");
        properties.put("password", "sqli");
        String url = "jdbc:sqli:mem:sqliDriver;MV_STORE=FALSE;MVCC=FALSE"

        Class.forName("org.hackerpeers.sqli.driver.AnalyzerDriver");
        this.conn = DriverManager.getConnection(url, properties);
```
The user/password correspond to your real database. 
The URL is also the same as the real database, except that you need to change the database type (h2, mysql, oracle) by
sqli. Make sure set the property **analyzer.real.jdbc** to the right value.

---

You can look at the example used for the tests : [SQLIAnalyzer.properties](src/test/resources/SQLIAnalyzer.properties)

```properties
##########
# Possible values : true|false
# If false, the connection won't be wrapped into the Analyzer.
##########
analyzer.active=true

##########
# Possible values : true|false
# If true, the analyzer.storage.path and analyzer.maxSizeInMemory are required.
# Otherwise, all statements will be cached in memory.
# The maxSizeInMemory is the maximum number of statement to keep in memory before
# saving the result on disk.
##########
analyzer.useDiskStorage=false
analyzer.maxSizeInMemory=100
analyzer.storage.path=./target/SQLIAnalyzer

##########
# In some case, the application might generate different query intentionally
# to optimize the database. This property allows to simplify the SQL statement
# to avoid raising a flag.
# For example, if the application split the clients data into multiple tables
# like this : Stats_<ClientID>, we might have multiple query from the same
# entry point. The following regex should solve the issue.
# analyzer.regex.simplifiers=Stats_[0-9]+=Stats_
#
# Comma separated list.
##########
analyzer.regex.simplifiers=Stats_[0-9]+=Stats_,\
                           Structs_[0-9]+=Structs_,\
                           notvalid

##########
# The configuration to the real JDBC driver.
# Both properties are required for the Analyzer to work.
# -----
# Example:
#   - MySQL:
#analyzer.real.driver=com.mysql.jdbc.Driver
#analyzer.real.jdbc=mysql
# 
#   - Oracle:
#realDriver=oracle.jdbc.OracleDriver
#realJDBCType=oracle
##########
analyzer.real.driver=org.h2.Driver
analyzer.real.jdbc=h2

##########
# Define with package should be considered as the entry point 
# of the generated Statement.
# If, for example, you specify com.pld.sqli.demo as the entry point package,
# the first class in the Thread.currentThread().getStackTrace() that match it
# will be the design entry point.
##########
analyzer.entrypoint.packages=org.hackerpeers.sqli.spec

##########
# The safe zone is a list of prefixes that we trust as entry points and
# that we don't need to check.
# For example:
#   analyzer.entrypoint.safezones=com.pld.sqli.demo.test,\
#                                 com.pld.sqli.demo.test2.DemoIgnored#,\
#                                 com.pld.sqli.demo.test3.DemoIngored#doDemo()
#
# In the previous example, all the following entry points will be ignored:
#   packages/classes starting with 'com.pld.sqli.demo.test'
#   all methods from class 'com.pld.sqli.demo.test2.DemoIgnored'
#   all methods named 'doDemo' in class 'com.pld.sqli.demo.test3.DemoIngored'
##########
analyzer.entrypoint.safezones=org.hackerpeers.sqli.demo.test.,\
                              org.hackerpeers.sqli.demo.test2.DemoIgnored#,\
                              org.hackerpeers.sqli.demo.test3.DemoIngored#doDemo()

##########
# Define the minimum time in millisecond that a statement must take to be logged.
# We can than manage to information that we want to log through the logging configuration.
# IMPORTANT: Except if the analyzer.active is false, the log setting will always applied.
##########
analyzer.log.threshold.warning=ABC
analyzer.log.threshold.info=1000
analyzer.log.threshold.fine=0

```

## Sample

```java
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

        Class.forName("org.hackerpeers.sqli.driver.AnalyzerDriver");
        Connection c = DriverManager.getConnection(url, properties);
        return c;
    }

    /**
     * @return A SQL Injection Wrapper connection over an Oracle connection.
     * @throws Exception If an error occurs while loading the AnalyzerDriver.
     */
    private Connection openSQLIConnection_Oracle() throws Exception {
        Properties properties = new Properties();
        properties.put("user", "sqli");
        properties.put("password", "sqli");
        String url = "jdbc:sqli:thin:@localhost:1521:orcl";

        Class.forName("org.hackerpeers.sqli.driver.AnalyzerDriver");
        Connection c = DriverManager.getConnection(url, properties);
        return c;
    }

}
```