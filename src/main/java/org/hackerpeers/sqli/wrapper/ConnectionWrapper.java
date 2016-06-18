package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * Wrapper around the real connection.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class ConnectionWrapper implements Connection {
    
    // Attributes.
    private Connection realConn;
    private ISQLInjectionAnalyzer analyzer;
    
    /**
     * Wrapper constructor on the real connection.
     * @param realConn The real connection.
     */
    public ConnectionWrapper(ISQLInjectionAnalyzer analyzer, Connection realConn) {
        super();
        this.realConn = realConn;
        this.analyzer = analyzer;
    }

    @Override
    public Statement createStatement() throws SQLException {
        Class[] proxyInterfaces = new Class[] {Statement.class};
        return (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                proxyInterfaces,
                new StatementDelegator<>(this, analyzer, realConn.createStatement()));
//        return new StatementWrapper(analyzer, realConn.createStatement());
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(this, analyzer, realConn.prepareStatement(sql), sql));
//        return new PreparedStatementWrapper(analyzer, realConn.prepareStatement(sql), sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return new CallableStatementWrapper(analyzer, realConn.prepareCall(sql), sql);
    }

    @Override
    public String nativeSQL(String string) throws SQLException {
        return realConn.nativeSQL(string);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return realConn.getAutoCommit();
    }

    @Override
    public void setAutoCommit(boolean bln) throws SQLException {
        realConn.setAutoCommit(bln);
    }

    @Override
    public void commit() throws SQLException {
        realConn.commit();
    }

    @Override
    public void rollback() throws SQLException {
        realConn.rollback();
    }

    @Override
    public void close() throws SQLException {
        realConn.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return realConn.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return realConn.getMetaData();
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return realConn.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean bln) throws SQLException {
        realConn.setReadOnly(bln);
    }

    @Override
    public String getCatalog() throws SQLException {
        return realConn.getCatalog();
    }

    @Override
    public void setCatalog(String string) throws SQLException {
        realConn.setCatalog(string);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return realConn.getTransactionIsolation();
    }

    @Override
    public void setTransactionIsolation(int i) throws SQLException {
        realConn.setTransactionIsolation(i);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return realConn.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        realConn.clearWarnings();
    }

    @Override
    public Statement createStatement(int i, int i1) throws SQLException {
        Class[] proxyInterfaces = new Class[]{Statement.class};
        return (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                proxyInterfaces,
                new StatementDelegator<>(this, analyzer, realConn.createStatement(i, i1)));
//        return new StatementWrapper(analyzer, realConn.createStatement(i, i1));
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int i, int i1) throws SQLException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(this, analyzer, realConn.prepareStatement(sql, i, i1), sql));
//        return new PreparedStatementWrapper(analyzer, realConn.prepareStatement(sql, i, i1), sql);
    }

    @Override
    public CallableStatement prepareCall(String string, int i, int i1) throws SQLException {
        return realConn.prepareCall(string, i, i1);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return realConn.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        realConn.setTypeMap(map);
    }

    @Override
    public int getHoldability() throws SQLException {
        return realConn.getHoldability();
    }

    @Override
    public void setHoldability(int i) throws SQLException {
        realConn.setHoldability(i);
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return realConn.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String string) throws SQLException {
        return realConn.setSavepoint(string);
    }

    @Override
    public void rollback(Savepoint svpnt) throws SQLException {
        realConn.rollback(svpnt);
    }

    @Override
    public void releaseSavepoint(Savepoint svpnt) throws SQLException {
        realConn.releaseSavepoint(svpnt);
    }

    @Override
    public Statement createStatement(int i, int i1, int i2) throws SQLException {
        Class[] proxyInterfaces = new Class[]{Statement.class};
        return (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                proxyInterfaces,
                new StatementDelegator<>(this, analyzer, realConn.createStatement(i, i1, i2)));
//        return new StatementWrapper(analyzer, realConn.createStatement(i, i1, i2));
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int i, int i1, int i2) throws SQLException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(this, analyzer, realConn.prepareStatement(sql, i, i1, i2), sql));
//        return new PreparedStatementWrapper(analyzer, realConn.prepareStatement(sql, i, i1, i2), sql);
    }

    @Override
    public CallableStatement prepareCall(String string, int i, int i1, int i2) throws SQLException {
        return realConn.prepareCall(string, i, i1, i2);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int i) throws SQLException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(this, analyzer, realConn.prepareStatement(sql, i), sql));
//        return new PreparedStatementWrapper(analyzer, realConn.prepareStatement(sql, i), sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] ints) throws SQLException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(this, analyzer, realConn.prepareStatement(sql, ints), sql));
//        return new PreparedStatementWrapper(analyzer, realConn.prepareStatement(sql, ints), sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] strings) throws SQLException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                new PreparedStatementDelegator<>(this, analyzer, realConn.prepareStatement(sql, strings), sql));
//        return new PreparedStatementWrapper(analyzer, realConn.prepareStatement(sql, strings), sql);
    }

    @Override
    public Clob createClob() throws SQLException {
        return realConn.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return realConn.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return realConn.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return realConn.createSQLXML();
    }

    @Override
    public boolean isValid(int i) throws SQLException {
        return realConn.isValid(i);
    }

    @Override
    public void setClientInfo(String string, String string1) throws SQLClientInfoException {
        realConn.setClientInfo(string, string1);
    }

    @Override
    public String getClientInfo(String string) throws SQLException {
        return realConn.getClientInfo(string);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return realConn.getClientInfo();
    }

    @Override
    public void setClientInfo(Properties prprts) throws SQLClientInfoException {
        realConn.setClientInfo(prprts);
    }

    @Override
    public Array createArrayOf(String string, Object[] os) throws SQLException {
        return realConn.createArrayOf(string, os);
    }

    @Override
    public Struct createStruct(String string, Object[] os) throws SQLException {
        return realConn.createStruct(string, os);
    }

    @Override
    public <T> T unwrap(Class<T> type) throws SQLException {
        return realConn.unwrap(type);
    }

    @Override
    public boolean isWrapperFor(Class<?> type) throws SQLException {
        return realConn.isWrapperFor(type);
    }

    @Override
    public String getSchema() throws SQLException {
        return realConn.getSchema();
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        realConn.setSchema(schema);
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        realConn.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        realConn.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
       return realConn.getNetworkTimeout();
    }
}
