package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;

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
