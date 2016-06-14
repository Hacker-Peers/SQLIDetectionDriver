package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.sql.Connection;

/**
 * Test methods in ConnectionWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class ConnectionWrapperTest extends AbstractWrapperTest<Connection, ConnectionWrapper> {

    @Override
    protected ConnectionWrapper getWrapperInstance(ISQLInjectionAnalyzer analyzer, Connection mockBasicClass) {
        return new ConnectionWrapper(analyzer, mockBasicClass);
    }
}
