package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.sql.CallableStatement;

/**
 * Test methods in CallableStatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class CallableStatementWrapperTest extends AbstractWrapperTest<CallableStatement, CallableStatementWrapper> {

    @Override
    protected CallableStatementWrapper getWrapperInstance(ISQLInjectionAnalyzer analyzer, CallableStatement mockBasicClass) {
        return new CallableStatementWrapper(analyzer, mockBasicClass, "select 1 from dual");
    }
}
