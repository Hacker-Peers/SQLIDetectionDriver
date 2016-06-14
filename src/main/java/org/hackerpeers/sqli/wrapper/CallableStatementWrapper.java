package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Map;

/**
 * Wrapper around the real callable statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
class CallableStatementWrapper<S extends CallableStatement> extends PreparedStatementWrapper<S> implements CallableStatement {

    /**
     * Wrapper constructor on the real prepared statement.
     * @param realCallableStatement The real prepared statement.
     */
    CallableStatementWrapper(ISQLInjectionAnalyzer analyzer, S realCallableStatement, String sql) {
        super(analyzer, realCallableStatement, sql);
    }


    @Override
    public void setNull(String paramName, int type) throws SQLException {
        addParams(paramName, null);
        getRealStatement().setNull(paramName, type);
    }

    @Override
    public void setBoolean(String paramName, boolean value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setBoolean(paramName, value);
    }

    @Override
    public void setByte(String paramName, byte value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setByte(paramName, value);
    }

    @Override
    public void setShort(String paramName, short value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setShort(paramName, value);
    }

    @Override
    public void setInt(String paramName, int value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setInt(paramName, value);
    }

    @Override
    public void setLong(String paramName, long value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setLong(paramName, value);
    }

    @Override
    public void setFloat(String paramName, float value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setFloat(paramName, value);
    }

    @Override
    public void setDouble(String paramName, double value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setDouble(paramName, value);
    }

    @Override
    public void setBigDecimal(String paramName, BigDecimal value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setBigDecimal(paramName, value);
    }

    @Override
    public void setString(String paramName, String value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setString(paramName, value);
    }

    @Override
    public void setBytes(String paramName, byte[] value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setBytes(paramName, value);
    }

    @Override
    public void setDate(String paramName, Date value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setDate(paramName, value);
    }

    @Override
    public void setTime(String paramName, Time value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setTime(paramName, value);
    }

    @Override
    public void setTimestamp(String paramName, Timestamp value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setTimestamp(paramName, value);
    }

    @Override
    public void setAsciiStream(String paramName, InputStream in, int i1) throws SQLException {
        addParams(paramName, String.format("[AsciiStream-InputStream-%s]", i1));
        getRealStatement().setAsciiStream(paramName, in, i1);
    }

    @Override
    public void setBinaryStream(String paramName, InputStream in, int i1) throws SQLException {
        addParams(paramName, String.format("[BinaryStream-InputStream-%s]", i1));
        getRealStatement().setBinaryStream(paramName, in, i1);
    }

    @Override
    public void setObject(String paramName, Object value, int type) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setObject(paramName, value, type);
    }

    @Override
    public void setObject(String paramName, Object value) throws SQLException {
        addParams(paramName, value);
        getRealStatement().setObject(paramName, value);
    }

    @Override
    public void setRowId(String paramName, RowId rowid) throws SQLException {
        addParams(paramName, rowid);
        getRealStatement().setRowId(paramName, rowid);
    }

    @Override
    public void setNString(String paramName, String string) throws SQLException {
        addParams(paramName, string);
        getRealStatement().setNString(paramName, string);
    }

    @Override
    public void setNCharacterStream(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[NCharacterStream-Reader-%s]", l));
        getRealStatement().setNCharacterStream(paramName, reader, l);
    }

    @Override
    public void setNClob(String paramName, NClob nclob) throws SQLException {
        addParams(paramName, nclob);
        getRealStatement().setNClob(paramName, nclob);
    }

    @Override
    public void setClob(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[Clob-Reader-%s]", l));
        getRealStatement().setClob(paramName, reader, l);
    }

    @Override
    public void setBlob(String paramName, InputStream in, long l) throws SQLException {
        addParams(paramName, String.format("[Blob-InputStream-%s]", l));
        getRealStatement().setBlob(paramName, in, l);
    }

    @Override
    public void setNClob(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[NClob-Reader-%s]", l));
        getRealStatement().setNClob(paramName, reader, l);
    }

    @Override
    public void setSQLXML(String paramName, SQLXML sqlxml) throws SQLException {
        addParams(paramName, sqlxml);
        getRealStatement().setSQLXML(paramName, sqlxml);
    }

    @Override
    public void setObject(String paramName, Object o, int i1, int i2) throws SQLException {
        addParams(paramName, o);
        getRealStatement().setObject(paramName, o, i1, i2);
    }

    @Override
    public void setAsciiStream(String paramName, InputStream in, long l) throws SQLException {
        addParams(paramName, String.format("[AsciiStream-InputStream-%s]", l));
        getRealStatement().setAsciiStream(paramName, in, l);
    }

    @Override
    public void setBinaryStream(String paramName, InputStream in, long l) throws SQLException {
        addParams(paramName, String.format("[BinaryStream-InputStream-%s]", l));
        getRealStatement().setBinaryStream(paramName, in, l);
    }

    @Override
    public void setCharacterStream(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[CharacterStream-Reader-%s]", l));
        getRealStatement().setCharacterStream(paramName, reader, l);
    }

    @Override
    public void setAsciiStream(String paramName, InputStream in) throws SQLException {
        addParams(paramName, "[AsciiStream-InputStream]");
        getRealStatement().setAsciiStream(paramName, in);
    }

    @Override
    public void setBinaryStream(String paramName, InputStream in) throws SQLException {
        addParams(paramName, "[BinaryStream-InputStream]");
        getRealStatement().setBinaryStream(paramName, in);
    }

    @Override
    public void setCharacterStream(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[CharacterStream-Reader]");
        getRealStatement().setCharacterStream(paramName, reader);
    }

    @Override
    public void setNCharacterStream(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[NCharacterStream-Reader]");
        getRealStatement().setNCharacterStream(paramName, reader);
    }

    @Override
    public void setClob(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[Clob-Reader]");
        getRealStatement().setClob(paramName, reader);
    }

    @Override
    public void setBlob(String paramName, InputStream in) throws SQLException {
        addParams(paramName, "[Blob-InputStream]");
        getRealStatement().setBlob(paramName, in);
    }

    @Override
    public void setNClob(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[NClob-Reader]");
        getRealStatement().setNClob(paramName, reader);
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return getRealStatement().getObject(parameterIndex, type);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return getRealStatement().getObject(parameterName, type);
    }

    @Override
    public void setCharacterStream(String paramName, Reader reader, int i1) throws SQLException {
        addParams(paramName, String.format("[CharacterStream-Reader-%s]", i1));
        getRealStatement().setCharacterStream(paramName, reader, i1);
    }

    @Override
    public void setBlob(String paramName, Blob blob) throws SQLException {
        addParams(paramName, blob);
        getRealStatement().setBlob(paramName, blob);
    }

    @Override
    public void setClob(String paramName, Clob clob) throws SQLException {
        addParams(paramName, clob);
        getRealStatement().setClob(paramName, clob);
    }

    @Override
    public void setDate(String paramName, Date date, Calendar clndr) throws SQLException {
        addParams(paramName, date);
        getRealStatement().setDate(paramName, date, clndr);
    }

    @Override
    public void setTime(String paramName, Time time, Calendar clndr) throws SQLException {
        addParams(paramName, time);
        getRealStatement().setTime(paramName, time, clndr);
    }

    @Override
    public void setTimestamp(String paramName, Timestamp tmstmp, Calendar clndr) throws SQLException {
        addParams(paramName, tmstmp);
        getRealStatement().setTimestamp(paramName, tmstmp, clndr);
    }

    @Override
    public void setNull(String paramName, int i1, String string) throws SQLException {
        addParams(paramName, null);
        getRealStatement().setNull(paramName, i1, string);
    }

    @Override
    public void setURL(String paramName, URL url) throws SQLException {
        addParams(paramName, url);
        getRealStatement().setURL(paramName, url);
    }
    
    @Override
    public void registerOutParameter(int i, int i1) throws SQLException {
        getRealStatement().registerOutParameter(i, i1);
    }

    @Override
    public void registerOutParameter(int i, int i1, int i2) throws SQLException {
        getRealStatement().registerOutParameter(i, i1, i2);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return getRealStatement().wasNull();
    }

    @Override
    public String getString(int i) throws SQLException {
        return getRealStatement().getString(i);
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        return getRealStatement().getBoolean(i);
    }

    @Override
    public byte getByte(int i) throws SQLException {
        return getRealStatement().getByte(i);
    }

    @Override
    public short getShort(int i) throws SQLException {
        return getRealStatement().getShort(i);
    }

    @Override
    public int getInt(int i) throws SQLException {
        return getRealStatement().getInt(i);
    }

    @Override
    public long getLong(int i) throws SQLException {
        return getRealStatement().getLong(i);
    }

    @Override
    public float getFloat(int i) throws SQLException {
        return getRealStatement().getFloat(i);
    }

    @Override
    public double getDouble(int i) throws SQLException {
        return getRealStatement().getDouble(i);
    }

    @Override
    @Deprecated
    public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
        return getRealStatement().getBigDecimal(i, i1);
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        return getRealStatement().getBytes(i);
    }

    @Override
    public Date getDate(int i) throws SQLException {
        return getRealStatement().getDate(i);
    }

    @Override
    public Time getTime(int i) throws SQLException {
        return getRealStatement().getTime(i);
    }

    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        return getRealStatement().getTimestamp(i);
    }

    @Override
    public Object getObject(int i) throws SQLException {
        return getRealStatement().getObject(i);
    }

    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        return getRealStatement().getBigDecimal(i);
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return getRealStatement().getObject(i, map);
    }

    @Override
    public Ref getRef(int i) throws SQLException {
        return getRealStatement().getRef(i);
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return getRealStatement().getBlob(i);
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        return getRealStatement().getClob(i);
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return getRealStatement().getArray(i);
    }

    @Override
    public Date getDate(int i, Calendar clndr) throws SQLException {
        return getRealStatement().getDate(i, clndr);
    }

    @Override
    public Time getTime(int i, Calendar clndr) throws SQLException {
        return getRealStatement().getTime(i, clndr);
    }

    @Override
    public Timestamp getTimestamp(int i, Calendar clndr) throws SQLException {
        return getRealStatement().getTimestamp(i, clndr);
    }

    @Override
    public void registerOutParameter(int i, int i1, String string) throws SQLException {
        getRealStatement().registerOutParameter(i, i1, string);
    }

    @Override
    public void registerOutParameter(String string, int i) throws SQLException {
        getRealStatement().registerOutParameter(string, i);
    }

    @Override
    public void registerOutParameter(String string, int i, int i1) throws SQLException {
        getRealStatement().registerOutParameter(string, i, i1);
    }

    @Override
    public void registerOutParameter(String string, int i, String string1) throws SQLException {
        getRealStatement().registerOutParameter(string, i, string1);
    }

    @Override
    public URL getURL(int i) throws SQLException {
        return getRealStatement().getURL(i);
    }

    @Override
    public String getString(String string) throws SQLException {
        return getRealStatement().getString(string);
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        return getRealStatement().getBoolean(string);
    }

    @Override
    public byte getByte(String string) throws SQLException {
        return getRealStatement().getByte(string);
    }

    @Override
    public short getShort(String string) throws SQLException {
        return getRealStatement().getShort(string);
    }

    @Override
    public int getInt(String string) throws SQLException {
        return getRealStatement().getInt(string);
    }

    @Override
    public long getLong(String string) throws SQLException {
        return getRealStatement().getLong(string);
    }

    @Override
    public float getFloat(String string) throws SQLException {
        return getRealStatement().getFloat(string);
    }

    @Override
    public double getDouble(String string) throws SQLException {
        return getRealStatement().getDouble(string);
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        return getRealStatement().getBytes(string);
    }

    @Override
    public Date getDate(String string) throws SQLException {
        return getRealStatement().getDate(string);
    }

    @Override
    public Time getTime(String string) throws SQLException {
        return getRealStatement().getTime(string);
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        return getRealStatement().getTimestamp(string);
    }

    @Override
    public Object getObject(String string) throws SQLException {
        return getRealStatement().getObject(string);
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        return getRealStatement().getBigDecimal(string);
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        return getRealStatement().getObject(string, map);
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        return getRealStatement().getRef(string);
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        return getRealStatement().getBlob(string);
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        return getRealStatement().getClob(string);
    }

    @Override
    public Array getArray(String string) throws SQLException {
        return getRealStatement().getArray(string);
    }

    @Override
    public Date getDate(String string, Calendar clndr) throws SQLException {
        return getRealStatement().getDate(string, clndr);
    }

    @Override
    public Time getTime(String string, Calendar clndr) throws SQLException {
        return getRealStatement().getTime(string, clndr);
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar clndr) throws SQLException {
        return getRealStatement().getTimestamp(string, clndr);
    }

    @Override
    public URL getURL(String string) throws SQLException {
        return getRealStatement().getURL(string);
    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        return getRealStatement().getRowId(i);
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        return getRealStatement().getRowId(string);
    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        return getRealStatement().getNClob(i);
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        return getRealStatement().getNClob(string);
    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        return getRealStatement().getSQLXML(i);
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        return getRealStatement().getSQLXML(string);
    }

    @Override
    public String getNString(int i) throws SQLException {
        return getRealStatement().getNString(i);
    }

    @Override
    public String getNString(String string) throws SQLException {
        return getRealStatement().getNString(string);
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        return getRealStatement().getNCharacterStream(i);
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        return getRealStatement().getNCharacterStream(string);
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        return getRealStatement().getCharacterStream(i);
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        return getRealStatement().getCharacterStream(string);
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        addParams(parameterName, x);
        getRealStatement().setObject(parameterName, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setObject(String parameterName, Object x, SQLType targetSqlType) throws SQLException {
        addParams(parameterName, x);
        getRealStatement().setObject(parameterName, x, targetSqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType) throws SQLException {
        getRealStatement().registerOutParameter(parameterIndex, sqlType);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType, int scale) throws SQLException {
        getRealStatement().registerOutParameter(parameterIndex, sqlType, scale);
    }

    @Override
    public void registerOutParameter(int parameterIndex, SQLType sqlType, String typeName) throws SQLException {
        getRealStatement().registerOutParameter(parameterIndex, sqlType, typeName);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType) throws SQLException {
        getRealStatement().registerOutParameter(parameterName, sqlType);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType, int scale) throws SQLException {
        getRealStatement().registerOutParameter(parameterName, sqlType, scale);
    }

    @Override
    public void registerOutParameter(String parameterName, SQLType sqlType, String typeName) throws SQLException {
        getRealStatement().registerOutParameter(parameterName, sqlType, typeName);
    }
}
