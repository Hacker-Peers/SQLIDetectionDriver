package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.SQLInjectionAnalyzer;

import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.RowId;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import mockit.Expectations;
import mockit.Mocked;

/**
 * Test methods in CallableStatementWrapper class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class CallableStatementWrapperTest {

    /**
     * Test of setNull method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNull_int_int(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNull("p1", 2);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNull("p1", 2);
    }

    /**
     * Test of setBoolean method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBoolean(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBoolean("p1", true);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBoolean("p1", true);
    }

    /**
     * Test of setByte method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetByte(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setByte("p1", (byte) 2);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setByte("p1", (byte) 2);
    }

    /**
     * Test of setShort method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetShort(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setShort("p1", (short) 2);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setShort("p1", (short) 2);
    }

    /**
     * Test of setInt method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetInt(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setInt("p1", 2);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setInt("p1", 2);
    }

    /**
     * Test of setLong method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetLong(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setLong("p1", 2L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setLong("p1", 2L);
    }

    /**
     * Test of setFloat method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetFloat(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setFloat("p1", 2.2f);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setFloat("p1", 2.2f);
    }

    /**
     * Test of setDouble method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetDouble(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setDouble("p1", 2.1d);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setDouble("p1", 2.1d);
    }

    /**
     * Test of setBigDecimal method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBigDecimal(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBigDecimal("p1", BigDecimal.TEN);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBigDecimal("p1", BigDecimal.TEN);
    }

    /**
     * Test of setString method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetString(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setString("p1", "abc");
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setString("p1", "abc");
    }

    /**
     * Test of setBytes method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBytes(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBytes("p1", new byte[]{1, 2, 3});
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBytes("p1", new byte[]{1, 2, 3});
    }

    /**
     * Test of setDate method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetDate_int_Date(@Mocked final CallableStatement ps) throws Exception {
        final Date value = new Date(System.currentTimeMillis());
        new Expectations() {

            {
                ps.setDate("p1", value);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setDate("p1", value);
    }

    /**
     * Test of setTime method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetTime_int_Time(@Mocked final CallableStatement ps) throws Exception {
        final Time value = new Time(System.currentTimeMillis());
        new Expectations() {

            {
                ps.setTime("p1", value);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setTime("p1", value);
    }

    /**
     * Test of setTimestamp method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetTimestamp_int_Timestamp(@Mocked final CallableStatement ps) throws Exception {
        final Timestamp value = new Timestamp(System.currentTimeMillis());
        new Expectations() {

            {
                ps.setTimestamp("p1", value);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setTimestamp("p1", value);
    }

    /**
     * Test of setAsciiStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetAsciiStream_3args_1(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setAsciiStream("p1", (InputStream) any, 3);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setAsciiStream("p1", (InputStream) null, 3);
    }

    /**
     * Test of setBinaryStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBinaryStream_3args_1(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBinaryStream("p1", (InputStream) any, 3);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBinaryStream("p1", (InputStream) null, 3);
    }

    /**
     * Test of clearParameters method, of class CallableStatementWrapper.
     */
    @Test
    public void testClearParameters(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.clearParameters();
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.clearParameters();
    }

    /**
     * Test of setObject method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetObject_3args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setObject("p1", "hello world", 3);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setObject("p1", "hello world", 3);
    }

    /**
     * Test of setObject method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetObject_int_Object(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setObject("p1", "Hello world");
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setObject("p1", "Hello world");
    }

    /**
     * Test of execute method, of class CallableStatementWrapper.
     */
    @Test
    public void testExecute(@Mocked final CallableStatement ps, @Mocked final Logger log) throws Exception {
        new Expectations() {

            {
                ps.execute();
                times = 1;

                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
                result = log;

                log.isLoggable(Level.FINE);
                result = true;
                times = 1;

                log.log(Level.FINE, anyString);
                times = 1;
            }
        };
        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.execute();
        Thread.sleep(100L);
    }

    /**
     * Test of addBatch method, of class CallableStatementWrapper.
     */
    @Test
    public void testAddBatch(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.addBatch();
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.addBatch();
    }

    /**
     * Test of setCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetCharacterStream_3args_1(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setCharacterStream("p1", (Reader) any, 3);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setCharacterStream("p1", (Reader) null, 3);
    }

    /**
     * Test of setBlob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBlob_int_Blob(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBlob("p1", (Blob) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBlob("p1", (Blob) null);
    }

    /**
     * Test of setClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetClob_int_Clob(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setClob("p1", (Clob) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setClob("p1", (Clob) null);
    }

    /**
     * Test of getMetaData method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetMetaData(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getMetaData();
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getMetaData();
    }

    /**
     * Test of setDate method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetDate_3args(@Mocked final CallableStatement ps) throws Exception {
        final Date value = new Date(System.currentTimeMillis());
        final Calendar c = Calendar.getInstance();
        new Expectations() {

            {
                ps.setDate("p1", value, c);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setDate("p1", value, c);
    }

    /**
     * Test of setTime method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetTime_3args(@Mocked final CallableStatement ps) throws Exception {
        final Time value = new Time(System.currentTimeMillis());
        final Calendar c = Calendar.getInstance();
        new Expectations() {

            {
                ps.setTime("p1", value, c);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setTime("p1", value, c);
    }

    /**
     * Test of setTimestamp method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetTimestamp_3args(@Mocked final CallableStatement ps) throws Exception {
        final Timestamp value = new Timestamp(System.currentTimeMillis());
        final Calendar c = Calendar.getInstance();
        new Expectations() {

            {
                ps.setTimestamp("p1", value, c);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setTimestamp("p1", value, c);
    }

    /**
     * Test of setNull method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNull_3args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNull("p1", 2, "abc");
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNull("p1", 2, "abc");
    }

    /**
     * Test of setURL method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetURL(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setURL("p1", (URL) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setURL("p1", null);
    }

    /**
     * Test of getParameterMetaData method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetParameterMetaData(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getParameterMetaData();
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getParameterMetaData();
    }

    /**
     * Test of setRowId method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetRowId(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setRowId("p1", (RowId) null);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setRowId("p1", null);
    }

    /**
     * Test of setNString method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNString(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNString("p1", "abc");
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNString("p1", "abc");
    }

    /**
     * Test of setNCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNCharacterStream_3args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNCharacterStream("p1", (Reader) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNCharacterStream("p1", null, 4L);
    }

    /**
     * Test of setNClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNClob_int_NClob(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNClob("p1", (NClob) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNClob("p1", (NClob) null);
    }

    /**
     * Test of setClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetClob_3args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setClob("p1", (Reader) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setClob("p1", null, 4L);
    }

    /**
     * Test of setBlob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBlob_3args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBlob("p1", (InputStream) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBlob("p1", null, 4L);
    }

    /**
     * Test of setNClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNClob_3args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNClob("p1", (Reader) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNClob("p1", null, 4L);
    }

    /**
     * Test of setSQLXML method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetSQLXML(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setSQLXML("p1", (SQLXML) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setSQLXML("p1", null);
    }

    /**
     * Test of setObject method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetObject_4args(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setObject("p1", "abc", 3, 4);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setObject("p1", "abc", 3, 4);
    }

    /**
     * Test of setAsciiStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetAsciiStream_3args_2(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setAsciiStream("p1", (InputStream) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setAsciiStream("p1", null, 4L);
    }

    /**
     * Test of setBinaryStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBinaryStream_3args_2(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBinaryStream("p1", (InputStream) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBinaryStream("p1", null, 4L);
    }

    /**
     * Test of setCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetCharacterStream_3args_2(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setCharacterStream("p1", (Reader) any, 4L);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setCharacterStream("p1", null, 4L);
    }

    /**
     * Test of setAsciiStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetAsciiStream_int_InputStream(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setAsciiStream("p1", (InputStream) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setAsciiStream("p1", null);
    }

    /**
     * Test of setBinaryStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBinaryStream_int_InputStream(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBinaryStream("p1", (InputStream) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBinaryStream("p1", null);
    }

    /**
     * Test of setCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetCharacterStream_int_Reader(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setCharacterStream("p1", (Reader) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setCharacterStream("p1", null);
    }

    /**
     * Test of setNCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNCharacterStream_int_Reader(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNCharacterStream("p1", (Reader) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNCharacterStream("p1", (Reader) null);
    }

    /**
     * Test of setClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetClob_int_Reader(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setClob("p1", (Reader) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setClob("p1", (Reader) null);
    }

    /**
     * Test of setBlob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetBlob_int_InputStream(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setBlob("p1", (InputStream) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setBlob("p1", (InputStream) null);
    }

    /**
     * Test of setNClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testSetNClob_int_Reader(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.setNClob("p1", (Reader) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.setNClob("p1", (Reader) null);
    }

    /**
     * Test of registerOutParameter method, of class CallableStatementWrapper.
     */
    @Test
    public void testRegisterOutParameter(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.registerOutParameter("a", 1);
                ps.registerOutParameter("a", 1, 1);
                ps.registerOutParameter("a", 1, "b");
                ps.registerOutParameter(1, 1);
                ps.registerOutParameter(1, 1, 2);
                ps.registerOutParameter(1, 1, "c");
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.registerOutParameter("a", 1);
        wrapper.registerOutParameter("a", 1, 1);
        wrapper.registerOutParameter("a", 1, "b");
        wrapper.registerOutParameter(1, 1);
        wrapper.registerOutParameter(1, 1, 2);
        wrapper.registerOutParameter(1, 1, "c");
    }

    /**
     * Test of getBoolean method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetBoolean(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getBoolean("p1");
                times = 1;
                ps.getBoolean(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getBoolean("p1");
        wrapper.getBoolean(1);
    }

    /**
     * Test of getByte method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetByte(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getByte("p1");
                times = 1;
                ps.getByte(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getByte("p1");
        wrapper.getByte(1);
    }

    /**
     * Test of getShort method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetShort(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getShort("p1");
                times = 1;
                ps.getShort(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getShort("p1");
        wrapper.getShort(1);
    }

    /**
     * Test of getInt method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetInt(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getInt("p1");
                times = 1;
                ps.getInt(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getInt("p1");
        wrapper.getInt(1);
    }

    /**
     * Test of getLong method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetLong(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getLong("p1");
                times = 1;
                ps.getLong(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getLong("p1");
        wrapper.getLong(1);
    }

    /**
     * Test of getFloat method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetFloat(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getFloat("p1");
                times = 1;
                ps.getFloat(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getFloat("p1");
        wrapper.getFloat(1);
    }

    /**
     * Test of getDouble method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetDouble(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getDouble("p1");
                times = 1;
                ps.getDouble(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getDouble("p1");
        wrapper.getDouble(1);
    }

    /**
     * Test of getBigDecimal method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetBigDecimal(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getBigDecimal("p1");
                times = 1;
                ps.getBigDecimal(1);
                times = 1;
                ps.getBigDecimal(1, 2);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getBigDecimal("p1");
        wrapper.getBigDecimal(1);
        wrapper.getBigDecimal(1, 2);
    }

    /**
     * Test of getString method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetString(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getString("p1");
                times = 1;
                ps.getString(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getString("p1");
        wrapper.getString(1);
    }

    /**
     * Test of getBytes method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetBytes(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getBytes("p1");
                times = 1;
                ps.getBytes(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getBytes("p1");
        wrapper.getBytes(1);
    }

    /**
     * Test of getDate method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetDate(@Mocked final CallableStatement ps) throws Exception {
        final Date value = new Date(System.currentTimeMillis());
        new Expectations() {

            {
                ps.getDate("p1");
                times = 1;
                ps.getDate(1);
                times = 1;
                ps.getDate("p1", (Calendar) any);
                times = 1;
                ps.getDate(1, (Calendar) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getDate("p1");
        wrapper.getDate(1);
        wrapper.getDate("p1", Calendar.getInstance());
        wrapper.getDate(1, Calendar.getInstance());
    }

    /**
     * Test of getTime method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetTime(@Mocked final CallableStatement ps) throws Exception {
        final Time value = new Time(System.currentTimeMillis());
        new Expectations() {

            {
                ps.getTime("p1");
                times = 1;
                ps.getTime(1);
                times = 1;
                ps.getTime("p1", (Calendar) any);
                times = 1;
                ps.getTime(1, (Calendar) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getTime("p1");
        wrapper.getTime(1);
        wrapper.getTime("p1", Calendar.getInstance());
        wrapper.getTime(1, Calendar.getInstance());
    }

    /**
     * Test of getTimestamp method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetTimestamp(@Mocked final CallableStatement ps) throws Exception {
        final Timestamp value = new Timestamp(System.currentTimeMillis());
        new Expectations() {

            {
                ps.getTimestamp("p1");
                times = 1;
                ps.getTimestamp(1);
                times = 1;
                ps.getTimestamp("p1", (Calendar) any);
                times = 1;
                ps.getTimestamp(1, (Calendar) any);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getTimestamp("p1");
        wrapper.getTimestamp(1);
        wrapper.getTimestamp("p1", Calendar.getInstance());
        wrapper.getTimestamp(1, Calendar.getInstance());
    }

    /**
     * Test of getObject method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetObject(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getObject("p1");
                times = 1;
                ps.getObject(1);
                times = 1;
                ps.getObject("p1", (Class) null);
                times = 1;
                ps.getObject(1, (Class) null);
                times = 1;
                ps.getObject("p1", (Map<String, Class<?>>) null);
                times = 1;
                ps.getObject(1, (Map<String, Class<?>>) null);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getObject("p1");
        wrapper.getObject(1);
        wrapper.getObject("p1", (Class) null);
        wrapper.getObject(1, (Class) null);
        wrapper.getObject("p1", (Map<String, Class<?>>) null);
        wrapper.getObject(1, (Map<String, Class<?>>) null);
    }

    /**
     * Test of getCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetCharacterStream(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getCharacterStream("p1");
                times = 1;
                ps.getCharacterStream(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getCharacterStream("p1");
        wrapper.getCharacterStream(1);
    }

    /**
     * Test of getBlob method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetBlob(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getBlob("p1");
                times = 1;
                ps.getBlob(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getBlob("p1");
        wrapper.getBlob(1);
    }

    /**
     * Test of getClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetClob(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getClob("p1");
                times = 1;
                ps.getClob(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getClob("p1");
        wrapper.getClob(1);
    }

    /**
     * Test of getURL method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetURL(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getURL("p1");
                times = 1;
                ps.getURL(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getURL("p1");
        wrapper.getURL(1);
    }

    /**
     * Test of getRowId method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetRowId(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getRowId("p1");
                times = 1;
                ps.getRowId(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getRowId("p1");
        wrapper.getRowId(1);
    }

    /**
     * Test of getNString method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetNString(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getNString("p1");
                times = 1;
                ps.getNString(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getNString("p1");
        wrapper.getNString(1);
    }

    /**
     * Test of getNCharacterStream method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetNCharacterStream(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getNCharacterStream("p1");
                times = 1;
                ps.getNCharacterStream(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getNCharacterStream("p1");
        wrapper.getNCharacterStream(1);
    }

    /**
     * Test of getNClob method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetNClob(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getNClob("p1");
                times = 1;
                ps.getNClob(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getNClob("p1");
        wrapper.getNClob(1);
    }

    /**
     * Test of getSQLXML method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetSQLXML(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getSQLXML("p1");
                times = 1;
                ps.getSQLXML(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getSQLXML("p1");
        wrapper.getSQLXML(1);
    }

    /**
     * Test of getArray method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetArray(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getArray("p1");
                times = 1;
                ps.getArray(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getArray("p1");
        wrapper.getArray(1);
    }

    /**
     * Test of getRef method, of class CallableStatementWrapper.
     */
    @Test
    public void testGetRef(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.getRef("p1");
                times = 1;
                ps.getRef(1);
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.getRef("p1");
        wrapper.getRef(1);
    }

    /**
     * Test of wasNull method, of class CallableStatementWrapper.
     */
    @Test
    public void testWasNull(@Mocked final CallableStatement ps) throws Exception {
        new Expectations() {

            {
                ps.wasNull();
                times = 1;
            }
        };

        CallableStatementWrapper wrapper = new CallableStatementWrapper(ps, "select 1 from dual");
        wrapper.wasNull();
    }
}
