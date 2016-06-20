package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.sql.CallableStatement;
import java.sql.Connection;

/**
 * Wrapper around the real callable statement.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
class CallableStatementDelegator<CS extends CallableStatement> extends PreparedStatementDelegator<CallableStatement> {

    /**
     * Wrapper constructor on the real prepared statement.
     *
     * @param realCallableStatement The real prepared statement.
     */
    CallableStatementDelegator(Connection conn, ISQLInjectionAnalyzer analyzer, CS realCallableStatement, String sql) {
        super(conn, analyzer, realCallableStatement, sql);
    }
}
