package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;

import java.sql.PreparedStatement;

/**
 * Test methods in PreparedStatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class PreparedStatementWrapperTest extends AbstractWrapperTest<PreparedStatement, PreparedStatementWrapper> {

    @Override
    protected PreparedStatementWrapper getWrapperInstance(ISQLInjectionAnalyzer analyzer, PreparedStatement mockBasicClass) {
        return new PreparedStatementWrapper(analyzer, mockBasicClass, "select 1 from dual");
    }
}
