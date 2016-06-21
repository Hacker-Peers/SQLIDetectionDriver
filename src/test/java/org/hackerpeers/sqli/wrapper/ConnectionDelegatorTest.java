package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class ConnectionDelegatorTest extends AbstractDelegatorTest<Connection> {

    @Override
    protected Connection getDelegatorInstance(ISQLInjectionAnalyzer analyzer, Connection mockBasicClass) {
        Class[] proxyInterfaces = new Class[] {Connection.class};
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), proxyInterfaces, new ConnectionDelegator(analyzer, mockBasicClass));
    }
}