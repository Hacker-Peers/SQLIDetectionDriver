package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.SQLInjectionAnalyzer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Wrapper around the real statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class StatementWrapper implements Statement {

    // Attributes
    private Statement realStatement;
    private List<String> batchSQL = new ArrayList<String>();

    /**
     * Wrapper constructor on the real statement.
     * @param realStatement The real statement.
     */
    public StatementWrapper(Statement realStatement) {
        super();
        this.realStatement = realStatement;
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        long start = System.currentTimeMillis();
        ResultSet result = realStatement.executeQuery(sql);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;

    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        long start = System.currentTimeMillis();
        int result = realStatement.executeUpdate(sql);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public void close() throws SQLException {
        realStatement.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return realStatement.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int i) throws SQLException {
        realStatement.setMaxFieldSize(i);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return realStatement.getMaxRows();
    }

    @Override
    public void setMaxRows(int i) throws SQLException {
        realStatement.setMaxRows(i);
    }

    @Override
    public void setEscapeProcessing(boolean bln) throws SQLException {
        realStatement.setEscapeProcessing(bln);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return realStatement.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int timeout) throws SQLException {
        realStatement.setQueryTimeout(timeout);
    }

    @Override
    public void cancel() throws SQLException {
        realStatement.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return realStatement.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        realStatement.clearWarnings();
    }

    @Override
    public void setCursorName(String cursorName) throws SQLException {
        realStatement.setCursorName(cursorName);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        long start = System.currentTimeMillis();
        boolean result = realStatement.execute(sql);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return realStatement.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return realStatement.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return realStatement.getMoreResults();
    }

    @Override
    public void setFetchDirection(int i) throws SQLException {
        realStatement.setFetchDirection(i);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return realStatement.getFetchDirection();
    }

    @Override
    public void setFetchSize(int i) throws SQLException {
        realStatement.setFetchSize(i);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return realStatement.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return realStatement.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return realStatement.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        batchSQL.add(sql);
        realStatement.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        batchSQL.clear();
        realStatement.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        long start = System.currentTimeMillis();
        int[] result = realStatement.executeBatch();
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(batchSQL, getBatchParameters(), start, end);
        return result;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = new ConnectionWrapper(realStatement.getConnection());
        return connection;
    }

    @Override
    public boolean getMoreResults(int i) throws SQLException {
        return realStatement.getMoreResults(i);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return realStatement.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        long start = System.currentTimeMillis();
        int result = realStatement.executeUpdate(sql, autoGeneratedKeys);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        long start = System.currentTimeMillis();
        int result = realStatement.executeUpdate(sql, columnIndexes);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        long start = System.currentTimeMillis();
        int result = realStatement.executeUpdate(sql, columnNames);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        long start = System.currentTimeMillis();
        boolean result = realStatement.execute(sql, autoGeneratedKeys);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        long start = System.currentTimeMillis();
        boolean result = realStatement.execute(sql, columnIndexes);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        long start = System.currentTimeMillis();
        boolean result = realStatement.execute(sql, columnNames);
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, null, start, end);
        return result;
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return realStatement.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return realStatement.isClosed();
    }

    @Override
    public void setPoolable(boolean bln) throws SQLException {
        realStatement.setPoolable(bln);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return realStatement.isPoolable();
    }

    @Override
    public <T> T unwrap(Class<T> type) throws SQLException {
        return realStatement.unwrap(type);
    }

    @Override
    public boolean isWrapperFor(Class<?> type) throws SQLException {
        return realStatement.isWrapperFor(type);
    }
    
    /**
     * @return The stocked parameters for batch processing.
     */
    protected List<Map<Object, Object>> getBatchParameters() {
        return null;
    }
    
    /**
     * @return The list of SQL used in batch.
     */
    protected List<String> getBatchSQL() {
        return batchSQL;
    }
}
