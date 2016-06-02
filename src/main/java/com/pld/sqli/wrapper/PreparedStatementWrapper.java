package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.SQLInjectionAnalyzer;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Wrapper around the real prepared statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class PreparedStatementWrapper extends StatementWrapper implements PreparedStatement {

    // Attributes
    private PreparedStatement realPreparedStatement;
    private Map<Object, Object> params = new TreeMap<Object, Object>();
    private List<Map<Object, Object>> batchParams = new ArrayList<Map<Object, Object>>();
    private String sql;

    /**
     * Wrapper constructor on the real prepared statement.
     * @param realPreparedStatement The real prepared statement.
     */
    PreparedStatementWrapper(PreparedStatement realPreparedStatement, String sql) {
        super(realPreparedStatement);
        this.realPreparedStatement = realPreparedStatement;
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        long start = System.currentTimeMillis();
        ResultSet result = realPreparedStatement.executeQuery();
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, params, start, end);
        return result;
    }

    @Override
    public int executeUpdate() throws SQLException {
        long start = System.currentTimeMillis();
        int result = realPreparedStatement.executeUpdate();
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, params, start, end);
        return result;
    }

    @Override
    public void setNull(int index, int type) throws SQLException {
        addParams(index, null);
        realPreparedStatement.setNull(index, type);
    }

    @Override
    public void setBoolean(int index, boolean value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setBoolean(index, value);
    }

    @Override
    public void setByte(int index, byte value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setByte(index, value);
    }

    @Override
    public void setShort(int index, short value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setShort(index, value);
    }

    @Override
    public void setInt(int index, int value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setInt(index, value);
    }

    @Override
    public void setLong(int index, long value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setLong(index, value);
    }

    @Override
    public void setFloat(int index, float value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setFloat(index, value);
    }

    @Override
    public void setDouble(int index, double value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setDouble(index, value);
    }

    @Override
    public void setBigDecimal(int index, BigDecimal value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setBigDecimal(index, value);
    }

    @Override
    public void setString(int index, String value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setString(index, value);
    }

    @Override
    public void setBytes(int index, byte[] value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setBytes(index, value);
    }

    @Override
    public void setDate(int index, Date value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setDate(index, value);
    }

    @Override
    public void setTime(int index, Time value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setTime(index, value);
    }

    @Override
    public void setTimestamp(int index, Timestamp value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setTimestamp(index, value);
    }

    @Override
    public void setAsciiStream(int index, InputStream in, int i1) throws SQLException {
        addParams(index, String.format("[AsciiStream-InputStream-%s]", i1));
        realPreparedStatement.setAsciiStream(index, in, i1);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int index, InputStream in, int i1) throws SQLException {
        addParams(index, String.format("[UnicodeStream-InputStream-%s]", i1));
        realPreparedStatement.setUnicodeStream(index, in, i1);
    }

    @Override
    public void setBinaryStream(int index, InputStream in, int i1) throws SQLException {
        addParams(index, String.format("[BinaryStream-InputStream-%s]", i1));
        realPreparedStatement.setBinaryStream(index, in, i1);
    }

    @Override
    public void clearParameters() throws SQLException {
        params.clear();
        realPreparedStatement.clearParameters();
    }

    @Override
    public void setObject(int index, Object value, int type) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setObject(index, value, type);
    }

    @Override
    public void setObject(int index, Object value) throws SQLException {
        addParams(index, value);
        realPreparedStatement.setObject(index, value);
    }

    @Override
    public boolean execute() throws SQLException {
        long start = System.currentTimeMillis();
        boolean result = realPreparedStatement.execute();
        long end = System.currentTimeMillis();
        SQLInjectionAnalyzer.analyze(sql, params, start, end);
        return result;
    }

    @Override
    public void addBatch() throws SQLException {
        batchParams.add(new TreeMap<Object, Object>(params));
        getBatchSQL().add(sql);
        realPreparedStatement.addBatch();
    }

    @Override
    public void setCharacterStream(int index, Reader reader, int i1) throws SQLException {
        addParams(index, String.format("[CharacterStream-Reader-%s]", i1));
        realPreparedStatement.setCharacterStream(index, reader, i1);
    }

    @Override
    public void setRef(int index, Ref ref) throws SQLException {
        addParams(index, ref);
        realPreparedStatement.setRef(index, ref);
    }

    @Override
    public void setBlob(int index, Blob blob) throws SQLException {
        addParams(index, blob);
        realPreparedStatement.setBlob(index, blob);
    }

    @Override
    public void setClob(int index, Clob clob) throws SQLException {
        addParams(index, clob);
        realPreparedStatement.setClob(index, clob);
    }

    @Override
    public void setArray(int index, Array array) throws SQLException {
        addParams(index, array);
        realPreparedStatement.setArray(index, array);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return realPreparedStatement.getMetaData();
    }

    @Override
    public void setDate(int index, Date date, Calendar clndr) throws SQLException {
        addParams(index, date);
        realPreparedStatement.setDate(index, date, clndr);
    }

    @Override
    public void setTime(int index, Time time, Calendar clndr) throws SQLException {
        addParams(index, time);
        realPreparedStatement.setTime(index, time, clndr);
    }

    @Override
    public void setTimestamp(int index, Timestamp tmstmp, Calendar clndr) throws SQLException {
        addParams(index, tmstmp);
        realPreparedStatement.setTimestamp(index, tmstmp, clndr);
    }

    @Override
    public void setNull(int index, int i1, String string) throws SQLException {
        addParams(index, null);
        realPreparedStatement.setNull(index, i1, string);
    }

    @Override
    public void setURL(int index, URL url) throws SQLException {
        addParams(index, url);
        realPreparedStatement.setURL(index, url);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return realPreparedStatement.getParameterMetaData();
    }

    @Override
    public void setRowId(int index, RowId rowid) throws SQLException {
        addParams(index, rowid);
        realPreparedStatement.setRowId(index, rowid);
    }

    @Override
    public void setNString(int index, String string) throws SQLException {
        addParams(index, string);
        realPreparedStatement.setNString(index, string);
    }

    @Override
    public void setNCharacterStream(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[NCharacterStream-Reader-%s]", l));
        realPreparedStatement.setNCharacterStream(index, reader, l);
    }

    @Override
    public void setNClob(int index, NClob nclob) throws SQLException {
        addParams(index, nclob);
        realPreparedStatement.setNClob(index, nclob);
    }

    @Override
    public void setClob(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[Clob-Reader-%s]", l));
        realPreparedStatement.setClob(index, reader, l);
    }

    @Override
    public void setBlob(int index, InputStream in, long l) throws SQLException {
        addParams(index, String.format("[Blob-InputStream-%s]", l));
        realPreparedStatement.setBlob(index, in, l);
    }

    @Override
    public void setNClob(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[NClob-Reader-%s]", l));
        realPreparedStatement.setNClob(index, reader, l);
    }

    @Override
    public void setSQLXML(int index, SQLXML sqlxml) throws SQLException {
        addParams(index, sqlxml);
        realPreparedStatement.setSQLXML(index, sqlxml);
    }

    @Override
    public void setObject(int index, Object o, int i1, int i2) throws SQLException {
        addParams(index, o);
        realPreparedStatement.setObject(index, o, i1, i2);
    }

    @Override
    public void setAsciiStream(int index, InputStream in, long l) throws SQLException {
        addParams(index, String.format("[AsciiStream-InputStream-%s]", l));
        realPreparedStatement.setAsciiStream(index, in, l);
    }

    @Override
    public void setBinaryStream(int index, InputStream in, long l) throws SQLException {
        addParams(index, String.format("[BinaryStream-InputStream-%s]", l));
        realPreparedStatement.setBinaryStream(index, in, l);
    }

    @Override
    public void setCharacterStream(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[CharacterStream-Reader-%s]", l));
        realPreparedStatement.setCharacterStream(index, reader, l);
    }

    @Override
    public void setAsciiStream(int index, InputStream in) throws SQLException {
        addParams(index, "[AsciiStream-InputStream]");
        realPreparedStatement.setAsciiStream(index, in);
    }

    @Override
    public void setBinaryStream(int index, InputStream in) throws SQLException {
        addParams(index, "[BinaryStream-InputStream]");
        realPreparedStatement.setBinaryStream(index, in);
    }

    @Override
    public void setCharacterStream(int index, Reader reader) throws SQLException {
        addParams(index, "[CharacterStream-Reader]");
        realPreparedStatement.setCharacterStream(index, reader);
    }

    @Override
    public void setNCharacterStream(int index, Reader reader) throws SQLException {
        addParams(index, "[NCharacterStream-Reader]");
        realPreparedStatement.setNCharacterStream(index, reader);
    }

    @Override
    public void setClob(int index, Reader reader) throws SQLException {
        addParams(index, "[Clob-Reader]");
        realPreparedStatement.setClob(index, reader);
    }

    @Override
    public void setBlob(int index, InputStream in) throws SQLException {
        addParams(index, "[Blob-InputStream]");
        realPreparedStatement.setBlob(index, in);
    }

    @Override
    public void setNClob(int index, Reader reader) throws SQLException {
        addParams(index, "[NClob-Reader]");
        realPreparedStatement.setNClob(index, reader);
    }

    @Override
    protected List<Map<Object, Object>> getBatchParameters() {
        return batchParams;
    }
    
    void addParams(Object k, Object v) {
        params.put(k, v);
    }
}
