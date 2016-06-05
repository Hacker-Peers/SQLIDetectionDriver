package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;

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
import java.sql.SQLType;
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
public class PreparedStatementWrapper<S extends PreparedStatement> extends StatementWrapper<S> implements PreparedStatement {

    // Attributes
    private Map<Object, Object> params = new TreeMap<>();
    private List<Map<Object, Object>> batchParams = new ArrayList<>();
    private String sql;

    /**
     * Wrapper constructor on the real prepared statement.
     * @param realPreparedStatement The real prepared statement.
     */
    PreparedStatementWrapper(ISQLInjectionAnalyzer analyzer, S realPreparedStatement, String sql) {
        super(analyzer, realPreparedStatement);
        this.sql = sql;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        long start = System.currentTimeMillis();
        ResultSet result = getRealStatement().executeQuery();
        long end = System.currentTimeMillis();
        getAnalyzer().analyze(sql, params, start, end);
        return result;
    }

    @Override
    public int executeUpdate() throws SQLException {
        long start = System.currentTimeMillis();
        int result = getRealStatement().executeUpdate();
        long end = System.currentTimeMillis();
        getAnalyzer().analyze(sql, params, start, end);
        return result;
    }

    @Override
    public void setNull(int index, int type) throws SQLException {
        addParams(index, null);
        getRealStatement().setNull(index, type);
    }

    @Override
    public void setBoolean(int index, boolean value) throws SQLException {
        addParams(index, value);
        getRealStatement().setBoolean(index, value);
    }

    @Override
    public void setByte(int index, byte value) throws SQLException {
        addParams(index, value);
        getRealStatement().setByte(index, value);
    }

    @Override
    public void setShort(int index, short value) throws SQLException {
        addParams(index, value);
        getRealStatement().setShort(index, value);
    }

    @Override
    public void setInt(int index, int value) throws SQLException {
        addParams(index, value);
        getRealStatement().setInt(index, value);
    }

    @Override
    public void setLong(int index, long value) throws SQLException {
        addParams(index, value);
        getRealStatement().setLong(index, value);
    }

    @Override
    public void setFloat(int index, float value) throws SQLException {
        addParams(index, value);
        getRealStatement().setFloat(index, value);
    }

    @Override
    public void setDouble(int index, double value) throws SQLException {
        addParams(index, value);
        getRealStatement().setDouble(index, value);
    }

    @Override
    public void setBigDecimal(int index, BigDecimal value) throws SQLException {
        addParams(index, value);
        getRealStatement().setBigDecimal(index, value);
    }

    @Override
    public void setString(int index, String value) throws SQLException {
        addParams(index, value);
        getRealStatement().setString(index, value);
    }

    @Override
    public void setBytes(int index, byte[] value) throws SQLException {
        addParams(index, value);
        getRealStatement().setBytes(index, value);
    }

    @Override
    public void setDate(int index, Date value) throws SQLException {
        addParams(index, value);
        getRealStatement().setDate(index, value);
    }

    @Override
    public void setTime(int index, Time value) throws SQLException {
        addParams(index, value);
        getRealStatement().setTime(index, value);
    }

    @Override
    public void setTimestamp(int index, Timestamp value) throws SQLException {
        addParams(index, value);
        getRealStatement().setTimestamp(index, value);
    }

    @Override
    public void setAsciiStream(int index, InputStream in, int i1) throws SQLException {
        addParams(index, String.format("[AsciiStream-InputStream-%s]", i1));
        getRealStatement().setAsciiStream(index, in, i1);
    }

    @Override
    @Deprecated
    public void setUnicodeStream(int index, InputStream in, int i1) throws SQLException {
        addParams(index, String.format("[UnicodeStream-InputStream-%s]", i1));
        getRealStatement().setUnicodeStream(index, in, i1);
    }

    @Override
    public void setBinaryStream(int index, InputStream in, int i1) throws SQLException {
        addParams(index, String.format("[BinaryStream-InputStream-%s]", i1));
        getRealStatement().setBinaryStream(index, in, i1);
    }

    @Override
    public void clearParameters() throws SQLException {
        params.clear();
        getRealStatement().clearParameters();
    }

    @Override
    public void setObject(int index, Object value, int type) throws SQLException {
        addParams(index, value);
        getRealStatement().setObject(index, value, type);
    }

    @Override
    public void setObject(int index, Object value) throws SQLException {
        addParams(index, value);
        getRealStatement().setObject(index, value);
    }

    @Override
    public void setObject(int index, Object value, SQLType type, int scaleOrLength) throws SQLException {
        addParams(index, value);
        getRealStatement().setObject(index, value, type, scaleOrLength);
    }

    @Override
    public void setObject(int index, Object value, SQLType type) throws SQLException {
        addParams(index, value);
        getRealStatement().setObject(index, value, type);
    }

    @Override
    public boolean execute() throws SQLException {
        long start = System.currentTimeMillis();
        boolean result = getRealStatement().execute();
        long end = System.currentTimeMillis();
        getAnalyzer().analyze(sql, params, start, end);
        return result;
    }

    @Override
    public void addBatch() throws SQLException {
        batchParams.add(new TreeMap<>(params));
        getBatchSQL().add(sql);
        getRealStatement().addBatch();
    }

    @Override
    public void setCharacterStream(int index, Reader reader, int i1) throws SQLException {
        addParams(index, String.format("[CharacterStream-Reader-%s]", i1));
        getRealStatement().setCharacterStream(index, reader, i1);
    }

    @Override
    public void setRef(int index, Ref ref) throws SQLException {
        addParams(index, ref);
        getRealStatement().setRef(index, ref);
    }

    @Override
    public void setBlob(int index, Blob blob) throws SQLException {
        addParams(index, blob);
        getRealStatement().setBlob(index, blob);
    }

    @Override
    public void setClob(int index, Clob clob) throws SQLException {
        addParams(index, clob);
        getRealStatement().setClob(index, clob);
    }

    @Override
    public void setArray(int index, Array array) throws SQLException {
        addParams(index, array);
        getRealStatement().setArray(index, array);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return getRealStatement().getMetaData();
    }

    @Override
    public void setDate(int index, Date date, Calendar clndr) throws SQLException {
        addParams(index, date);
        getRealStatement().setDate(index, date, clndr);
    }

    @Override
    public void setTime(int index, Time time, Calendar clndr) throws SQLException {
        addParams(index, time);
        getRealStatement().setTime(index, time, clndr);
    }

    @Override
    public void setTimestamp(int index, Timestamp tmstmp, Calendar clndr) throws SQLException {
        addParams(index, tmstmp);
        getRealStatement().setTimestamp(index, tmstmp, clndr);
    }

    @Override
    public void setNull(int index, int i1, String string) throws SQLException {
        addParams(index, null);
        getRealStatement().setNull(index, i1, string);
    }

    @Override
    public void setURL(int index, URL url) throws SQLException {
        addParams(index, url);
        getRealStatement().setURL(index, url);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return getRealStatement().getParameterMetaData();
    }

    @Override
    public void setRowId(int index, RowId rowid) throws SQLException {
        addParams(index, rowid);
        getRealStatement().setRowId(index, rowid);
    }

    @Override
    public void setNString(int index, String string) throws SQLException {
        addParams(index, string);
        getRealStatement().setNString(index, string);
    }

    @Override
    public void setNCharacterStream(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[NCharacterStream-Reader-%s]", l));
        getRealStatement().setNCharacterStream(index, reader, l);
    }

    @Override
    public void setNClob(int index, NClob nclob) throws SQLException {
        addParams(index, nclob);
        getRealStatement().setNClob(index, nclob);
    }

    @Override
    public void setClob(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[Clob-Reader-%s]", l));
        getRealStatement().setClob(index, reader, l);
    }

    @Override
    public void setBlob(int index, InputStream in, long l) throws SQLException {
        addParams(index, String.format("[Blob-InputStream-%s]", l));
        getRealStatement().setBlob(index, in, l);
    }

    @Override
    public void setNClob(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[NClob-Reader-%s]", l));
        getRealStatement().setNClob(index, reader, l);
    }

    @Override
    public void setSQLXML(int index, SQLXML sqlxml) throws SQLException {
        addParams(index, sqlxml);
        getRealStatement().setSQLXML(index, sqlxml);
    }

    @Override
    public void setObject(int index, Object o, int i1, int i2) throws SQLException {
        addParams(index, o);
        getRealStatement().setObject(index, o, i1, i2);
    }

    @Override
    public void setAsciiStream(int index, InputStream in, long l) throws SQLException {
        addParams(index, String.format("[AsciiStream-InputStream-%s]", l));
        getRealStatement().setAsciiStream(index, in, l);
    }

    @Override
    public void setBinaryStream(int index, InputStream in, long l) throws SQLException {
        addParams(index, String.format("[BinaryStream-InputStream-%s]", l));
        getRealStatement().setBinaryStream(index, in, l);
    }

    @Override
    public void setCharacterStream(int index, Reader reader, long l) throws SQLException {
        addParams(index, String.format("[CharacterStream-Reader-%s]", l));
        getRealStatement().setCharacterStream(index, reader, l);
    }

    @Override
    public void setAsciiStream(int index, InputStream in) throws SQLException {
        addParams(index, "[AsciiStream-InputStream]");
        getRealStatement().setAsciiStream(index, in);
    }

    @Override
    public void setBinaryStream(int index, InputStream in) throws SQLException {
        addParams(index, "[BinaryStream-InputStream]");
        getRealStatement().setBinaryStream(index, in);
    }

    @Override
    public void setCharacterStream(int index, Reader reader) throws SQLException {
        addParams(index, "[CharacterStream-Reader]");
        getRealStatement().setCharacterStream(index, reader);
    }

    @Override
    public void setNCharacterStream(int index, Reader reader) throws SQLException {
        addParams(index, "[NCharacterStream-Reader]");
        getRealStatement().setNCharacterStream(index, reader);
    }

    @Override
    public void setClob(int index, Reader reader) throws SQLException {
        addParams(index, "[Clob-Reader]");
        getRealStatement().setClob(index, reader);
    }

    @Override
    public void setBlob(int index, InputStream in) throws SQLException {
        addParams(index, "[Blob-InputStream]");
        getRealStatement().setBlob(index, in);
    }

    @Override
    public void setNClob(int index, Reader reader) throws SQLException {
        addParams(index, "[NClob-Reader]");
        getRealStatement().setNClob(index, reader);
    }

    @Override
    protected List<Map<Object, Object>> getBatchParameters() {
        return batchParams;
    }
    
    void addParams(Object k, Object v) {
        params.put(k, v);
    }
}
