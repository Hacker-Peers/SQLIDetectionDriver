package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;

import static org.mockito.Mockito.mock;

/**
 * Test methods in PreparedStatementDelegator class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class CallableStatementDelegatorTest extends AbstractDelegatorTest<CallableStatement> {

    @Override
    protected CallableStatement getDelegatorInstance(ISQLInjectionAnalyzer analyzer, CallableStatement mockBasicClass) {
        Class[] proxyInterfaces = new Class[]{CallableStatement.class};
        return (CallableStatement) Proxy.newProxyInstance(
                CallableStatement.class.getClassLoader(),
                proxyInterfaces,
                new CallableStatementDelegator<>(mock(Connection.class), analyzer, mockBasicClass, "SELECT 1 FROM DUAL"));
    }
}
