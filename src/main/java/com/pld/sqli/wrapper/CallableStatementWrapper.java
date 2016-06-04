package com.pld.sqli.wrapper;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Wrapper around the real callable statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class CallableStatementWrapper extends PreparedStatementWrapper implements CallableStatement {

    // Attributes
    private CallableStatement realCallableStatement;

    /**
     * Wrapper constructor on the real prepared statement.
     * @param realCallableStatement The real prepared statement.
     */
    CallableStatementWrapper(CallableStatement realCallableStatement, String sql) {
        super(realCallableStatement, sql);
        this.realCallableStatement = realCallableStatement;
    }


    @Override
    public void setNull(String paramName, int type) throws SQLException {
        addParams(paramName, null);
        realCallableStatement.setNull(paramName, type);
    }

    @Override
    public void setBoolean(String paramName, boolean value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setBoolean(paramName, value);
    }

    @Override
    public void setByte(String paramName, byte value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setByte(paramName, value);
    }

    @Override
    public void setShort(String paramName, short value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setShort(paramName, value);
    }

    @Override
    public void setInt(String paramName, int value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setInt(paramName, value);
    }

    @Override
    public void setLong(String paramName, long value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setLong(paramName, value);
    }

    @Override
    public void setFloat(String paramName, float value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setFloat(paramName, value);
    }

    @Override
    public void setDouble(String paramName, double value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setDouble(paramName, value);
    }

    @Override
    public void setBigDecimal(String paramName, BigDecimal value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setBigDecimal(paramName, value);
    }

    @Override
    public void setString(String paramName, String value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setString(paramName, value);
    }

    @Override
    public void setBytes(String paramName, byte[] value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setBytes(paramName, value);
    }

    @Override
    public void setDate(String paramName, Date value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setDate(paramName, value);
    }

    @Override
    public void setTime(String paramName, Time value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setTime(paramName, value);
    }

    @Override
    public void setTimestamp(String paramName, Timestamp value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setTimestamp(paramName, value);
    }

    @Override
    public void setAsciiStream(String paramName, InputStream in, int i1) throws SQLException {
        addParams(paramName, String.format("[AsciiStream-InputStream-%s]", i1));
        realCallableStatement.setAsciiStream(paramName, in, i1);
    }

    @Override
    public void setBinaryStream(String paramName, InputStream in, int i1) throws SQLException {
        addParams(paramName, String.format("[BinaryStream-InputStream-%s]", i1));
        realCallableStatement.setBinaryStream(paramName, in, i1);
    }

    @Override
    public void setObject(String paramName, Object value, int type) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setObject(paramName, value, type);
    }

    @Override
    public void setObject(String paramName, Object value) throws SQLException {
        addParams(paramName, value);
        realCallableStatement.setObject(paramName, value);
    }

    @Override
    public void setRowId(String paramName, RowId rowid) throws SQLException {
        addParams(paramName, rowid);
        realCallableStatement.setRowId(paramName, rowid);
    }

    @Override
    public void setNString(String paramName, String string) throws SQLException {
        addParams(paramName, string);
        realCallableStatement.setNString(paramName, string);
    }

    @Override
    public void setNCharacterStream(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[NCharacterStream-Reader-%s]", l));
        realCallableStatement.setNCharacterStream(paramName, reader, l);
    }

    @Override
    public void setNClob(String paramName, NClob nclob) throws SQLException {
        addParams(paramName, nclob);
        realCallableStatement.setNClob(paramName, nclob);
    }

    @Override
    public void setClob(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[Clob-Reader-%s]", l));
        realCallableStatement.setClob(paramName, reader, l);
    }

    @Override
    public void setBlob(String paramName, InputStream in, long l) throws SQLException {
        addParams(paramName, String.format("[Blob-InputStream-%s]", l));
        realCallableStatement.setBlob(paramName, in, l);
    }

    @Override
    public void setNClob(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[NClob-Reader-%s]", l));
        realCallableStatement.setNClob(paramName, reader, l);
    }

    @Override
    public void setSQLXML(String paramName, SQLXML sqlxml) throws SQLException {
        addParams(paramName, sqlxml);
        realCallableStatement.setSQLXML(paramName, sqlxml);
    }

    @Override
    public void setObject(String paramName, Object o, int i1, int i2) throws SQLException {
        addParams(paramName, o);
        realCallableStatement.setObject(paramName, o, i1, i2);
    }

    @Override
    public void setAsciiStream(String paramName, InputStream in, long l) throws SQLException {
        addParams(paramName, String.format("[AsciiStream-InputStream-%s]", l));
        realCallableStatement.setAsciiStream(paramName, in, l);
    }

    @Override
    public void setBinaryStream(String paramName, InputStream in, long l) throws SQLException {
        addParams(paramName, String.format("[BinaryStream-InputStream-%s]", l));
        realCallableStatement.setBinaryStream(paramName, in, l);
    }

    @Override
    public void setCharacterStream(String paramName, Reader reader, long l) throws SQLException {
        addParams(paramName, String.format("[CharacterStream-Reader-%s]", l));
        realCallableStatement.setCharacterStream(paramName, reader, l);
    }

    @Override
    public void setAsciiStream(String paramName, InputStream in) throws SQLException {
        addParams(paramName, "[AsciiStream-InputStream]");
        realCallableStatement.setAsciiStream(paramName, in);
    }

    @Override
    public void setBinaryStream(String paramName, InputStream in) throws SQLException {
        addParams(paramName, "[BinaryStream-InputStream]");
        realCallableStatement.setBinaryStream(paramName, in);
    }

    @Override
    public void setCharacterStream(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[CharacterStream-Reader]");
        realCallableStatement.setCharacterStream(paramName, reader);
    }

    @Override
    public void setNCharacterStream(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[NCharacterStream-Reader]");
        realCallableStatement.setNCharacterStream(paramName, reader);
    }

    @Override
    public void setClob(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[Clob-Reader]");
        realCallableStatement.setClob(paramName, reader);
    }

    @Override
    public void setBlob(String paramName, InputStream in) throws SQLException {
        addParams(paramName, "[Blob-InputStream]");
        realCallableStatement.setBlob(paramName, in);
    }

    @Override
    public void setNClob(String paramName, Reader reader) throws SQLException {
        addParams(paramName, "[NClob-Reader]");
        realCallableStatement.setNClob(paramName, reader);
    }

    @Override
    public <T> T getObject(int parameterIndex, Class<T> type) throws SQLException {
        return realCallableStatement.getObject(parameterIndex, type);
    }

    @Override
    public <T> T getObject(String parameterName, Class<T> type) throws SQLException {
        return realCallableStatement.getObject(parameterName, type);
    }

    @Override
    public void setCharacterStream(String paramName, Reader reader, int i1) throws SQLException {
        addParams(paramName, String.format("[CharacterStream-Reader-%s]", i1));
        realCallableStatement.setCharacterStream(paramName, reader, i1);
    }

    @Override
    public void setBlob(String paramName, Blob blob) throws SQLException {
        addParams(paramName, blob);
        realCallableStatement.setBlob(paramName, blob);
    }

    @Override
    public void setClob(String paramName, Clob clob) throws SQLException {
        addParams(paramName, clob);
        realCallableStatement.setClob(paramName, clob);
    }

    @Override
    public void setDate(String paramName, Date date, Calendar clndr) throws SQLException {
        addParams(paramName, date);
        realCallableStatement.setDate(paramName, date, clndr);
    }

    @Override
    public void setTime(String paramName, Time time, Calendar clndr) throws SQLException {
        addParams(paramName, time);
        realCallableStatement.setTime(paramName, time, clndr);
    }

    @Override
    public void setTimestamp(String paramName, Timestamp tmstmp, Calendar clndr) throws SQLException {
        addParams(paramName, tmstmp);
        realCallableStatement.setTimestamp(paramName, tmstmp, clndr);
    }

    @Override
    public void setNull(String paramName, int i1, String string) throws SQLException {
        addParams(paramName, null);
        realCallableStatement.setNull(paramName, i1, string);
    }

    @Override
    public void setURL(String paramName, URL url) throws SQLException {
        addParams(paramName, url);
        realCallableStatement.setURL(paramName, url);
    }
    
    @Override
    public void registerOutParameter(int i, int i1) throws SQLException {
        realCallableStatement.registerOutParameter(i, i1);
    }

    @Override
    public void registerOutParameter(int i, int i1, int i2) throws SQLException {
        realCallableStatement.registerOutParameter(i, i1, i2);
    }

    @Override
    public boolean wasNull() throws SQLException {
        return realCallableStatement.wasNull();
    }

    @Override
    public String getString(int i) throws SQLException {
        return realCallableStatement.getString(i);
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        return realCallableStatement.getBoolean(i);
    }

    @Override
    public byte getByte(int i) throws SQLException {
        return realCallableStatement.getByte(i);
    }

    @Override
    public short getShort(int i) throws SQLException {
        return realCallableStatement.getShort(i);
    }

    @Override
    public int getInt(int i) throws SQLException {
        return realCallableStatement.getInt(i);
    }

    @Override
    public long getLong(int i) throws SQLException {
        return realCallableStatement.getLong(i);
    }

    @Override
    public float getFloat(int i) throws SQLException {
        return realCallableStatement.getFloat(i);
    }

    @Override
    public double getDouble(int i) throws SQLException {
        return realCallableStatement.getDouble(i);
    }

    @Override
    public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
        return realCallableStatement.getBigDecimal(i, i1);
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        return realCallableStatement.getBytes(i);
    }

    @Override
    public Date getDate(int i) throws SQLException {
        return realCallableStatement.getDate(i);
    }

    @Override
    public Time getTime(int i) throws SQLException {
        return realCallableStatement.getTime(i);
    }

    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        return realCallableStatement.getTimestamp(i);
    }

    @Override
    public Object getObject(int i) throws SQLException {
        return realCallableStatement.getObject(i);
    }

    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        return realCallableStatement.getBigDecimal(i);
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return realCallableStatement.getObject(i, map);
    }

    @Override
    public Ref getRef(int i) throws SQLException {
        return realCallableStatement.getRef(i);
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return realCallableStatement.getBlob(i);
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        return realCallableStatement.getClob(i);
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return realCallableStatement.getArray(i);
    }

    @Override
    public Date getDate(int i, Calendar clndr) throws SQLException {
        return realCallableStatement.getDate(i, clndr);
    }

    @Override
    public Time getTime(int i, Calendar clndr) throws SQLException {
        return realCallableStatement.getTime(i, clndr);
    }

    @Override
    public Timestamp getTimestamp(int i, Calendar clndr) throws SQLException {
        return realCallableStatement.getTimestamp(i, clndr);
    }

    @Override
    public void registerOutParameter(int i, int i1, String string) throws SQLException {
        realCallableStatement.registerOutParameter(i, i1, string);
    }

    @Override
    public void registerOutParameter(String string, int i) throws SQLException {
        realCallableStatement.registerOutParameter(string, i);
    }

    @Override
    public void registerOutParameter(String string, int i, int i1) throws SQLException {
        realCallableStatement.registerOutParameter(string, i, i1);
    }

    @Override
    public void registerOutParameter(String string, int i, String string1) throws SQLException {
        realCallableStatement.registerOutParameter(string, i, string1);
    }

    @Override
    public URL getURL(int i) throws SQLException {
        return realCallableStatement.getURL(i);
    }

    @Override
    public String getString(String string) throws SQLException {
        return realCallableStatement.getString(string);
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        return realCallableStatement.getBoolean(string);
    }

    @Override
    public byte getByte(String string) throws SQLException {
        return realCallableStatement.getByte(string);
    }

    @Override
    public short getShort(String string) throws SQLException {
        return realCallableStatement.getShort(string);
    }

    @Override
    public int getInt(String string) throws SQLException {
        return realCallableStatement.getInt(string);
    }

    @Override
    public long getLong(String string) throws SQLException {
        return realCallableStatement.getLong(string);
    }

    @Override
    public float getFloat(String string) throws SQLException {
        return realCallableStatement.getFloat(string);
    }

    @Override
    public double getDouble(String string) throws SQLException {
        return realCallableStatement.getDouble(string);
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        return realCallableStatement.getBytes(string);
    }

    @Override
    public Date getDate(String string) throws SQLException {
        return realCallableStatement.getDate(string);
    }

    @Override
    public Time getTime(String string) throws SQLException {
        return realCallableStatement.getTime(string);
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        return realCallableStatement.getTimestamp(string);
    }

    @Override
    public Object getObject(String string) throws SQLException {
        return realCallableStatement.getObject(string);
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        return realCallableStatement.getBigDecimal(string);
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        return realCallableStatement.getObject(string, map);
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        return realCallableStatement.getRef(string);
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        return realCallableStatement.getBlob(string);
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        return realCallableStatement.getClob(string);
    }

    @Override
    public Array getArray(String string) throws SQLException {
        return realCallableStatement.getArray(string);
    }

    @Override
    public Date getDate(String string, Calendar clndr) throws SQLException {
        return realCallableStatement.getDate(string, clndr);
    }

    @Override
    public Time getTime(String string, Calendar clndr) throws SQLException {
        return realCallableStatement.getTime(string, clndr);
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar clndr) throws SQLException {
        return realCallableStatement.getTimestamp(string, clndr);
    }

    @Override
    public URL getURL(String string) throws SQLException {
        return realCallableStatement.getURL(string);
    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        return realCallableStatement.getRowId(i);
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        return realCallableStatement.getRowId(string);
    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        return realCallableStatement.getNClob(i);
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        return realCallableStatement.getNClob(string);
    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        return realCallableStatement.getSQLXML(i);
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        return realCallableStatement.getSQLXML(string);
    }

    @Override
    public String getNString(int i) throws SQLException {
        return realCallableStatement.getNString(i);
    }

    @Override
    public String getNString(String string) throws SQLException {
        return realCallableStatement.getNString(string);
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        return realCallableStatement.getNCharacterStream(i);
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        return realCallableStatement.getNCharacterStream(string);
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        return realCallableStatement.getCharacterStream(i);
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        return realCallableStatement.getCharacterStream(string);
    }
}
