package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.sql.Statement;

/**
 * Test methods in StatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class StatementWrapperTest extends AbstractWrapperTest<Statement, StatementWrapper> {

    @Override
    protected StatementWrapper getWrapperInstance(ISQLInjectionAnalyzer analyzer, Statement mockBasicClass) {
        return new StatementWrapper(analyzer, mockBasicClass);
    }
}
