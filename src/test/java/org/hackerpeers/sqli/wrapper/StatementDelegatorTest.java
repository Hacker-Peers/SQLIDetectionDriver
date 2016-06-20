package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Statement;

import static org.mockito.Mockito.mock;

/**
 * Test methods in StatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class StatementDelegatorTest extends AbstractDelegatorTest<Statement> {

    @Override
    protected Statement getDelegatorInstance(ISQLInjectionAnalyzer analyzer, Statement mockBasicClass) {
        Class[] proxyInterfaces = new Class[] {Statement.class};
        return (Statement) Proxy.newProxyInstance(Statement.class.getClassLoader(), proxyInterfaces, new StatementDelegator<Statement>(mock(Connection.class), analyzer, mockBasicClass));

    }
}
