package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;

import static org.mockito.Mockito.mock;

/**
 * Test methods in PreparedStatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class PreparedStatementDelegatorTest extends AbstractWrapperTest<PreparedStatement, PreparedStatement> {

    @Override
    protected PreparedStatement getWrapperInstance(ISQLInjectionAnalyzer analyzer, PreparedStatement mockBasicClass) {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(mock(Connection.class), analyzer, mockBasicClass, "SELECT 1 FROM DUAL"));
    }
}