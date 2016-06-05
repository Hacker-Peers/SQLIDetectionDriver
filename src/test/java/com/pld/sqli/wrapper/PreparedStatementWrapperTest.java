package com.pld.sqli.wrapper;

/**
 * Test methods in PreparedStatementWrapper class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class PreparedStatementWrapperTest {
//
//    /**
//     * Test that all setter store the parameter in the Analyzer cache.
//     * @param ps Mock prepare statement.
//     * @param blob Mock Blob.
//     * @param nclob Mock NClob.
//     * @param clob Mock Clob.
//     * @param ref Mock Ref.
//     * @param array Mock Array.
//     * @param sqlxml Mock SQLXML.
//     * @param rowid Mock RowId.
//     * @throws Exception If an error occurs while calling methods to tests.
//     */
//    @Test
//    public void testParameterSetters(
//            @Mocked PreparedStatement ps,
//            @Mocked Blob blob,
//            @Mocked NClob nclob,
//            @Mocked Clob clob,
//            @Mocked Ref ref,
//            @Mocked Array array,
//            @Mocked SQLXML sqlxml,
//            @Mocked final RowId rowid) throws Exception {
//        new NonStrictExpectations() {
//
//            {
//                rowid.toString();
//                result = "rowid";
//            }
//        };
//        List<String> excluded = Arrays.asList(
//                "setPoolable", "setFetchSize", "setFetchDirection", "setCursorName",
//                "setQueryTimeout", "setEscapeProcessing", "setMaxRows", "setLargeMaxRows", "setMaxFieldSize");
//        Method[] methods = PreparedStatementWrapper.class.getMethods();
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        int index = 1;
//        for (Method m : methods) {
//            if (m.getName().startsWith("set")
//                    && !excluded.contains(m.getName())
//                    && m.getReturnType().toString().equals("void")) {
//                Class<?>[] parameterTypes = m.getParameterTypes();
//                Object[] params = new Object[m.getParameterTypes().length];
//                Object expValue = populateDefaultValues(
//                        index, params, parameterTypes, nclob, blob, clob, ref, array, rowid, sqlxml);
//                m.invoke(wrapper, params);
//                assertParameter(m, wrapper, index, expValue);
//                index++;
//
//            }
//        }
//    }
//
//    private Object populateDefaultValues(
//            int index, Object[] params, Class<?>[] paramTypes, NClob nclob, Blob blob, Clob clob, Ref ref,
//            Array array, RowId rowid, SQLXML sqlxml) throws IllegalAccessException, InstantiationException, MalformedURLException {
//        Object expValue = null;
//        params[0] = index;
//        for (int i = 1; i < params.length; i++) {
//            if (paramTypes[i].toString().equalsIgnoreCase("integer") || paramTypes[i].toString().equalsIgnoreCase("int")) {
//                params[i] = 8;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("short")) {
//                params[i] = (short) 9;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("boolean")) {
//                params[i] = true;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("long")) {
//                params[i] = 7L;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("double")) {
//                params[i] = 6.6d;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("float")) {
//                params[i] = 5.5f;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("byte")) {
//                params[i] = (byte) 4;
//            } else if (paramTypes[i].toString().equalsIgnoreCase("class [b")) {
//                params[i] = new byte[]{1, 2, 3};
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("string")) {
//                params[i] = "JUnit";
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("timestamp")) {
//                params[i] = new Timestamp(System.currentTimeMillis());
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("time")) {
//                params[i] = new Time(System.currentTimeMillis());
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("date")) {
//                params[i] = new Date(System.currentTimeMillis());
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("calendar")) {
//                params[i] = Calendar.getInstance();
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("url")) {
//                params[i] = new URL("http://junit.test.com");
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("bigdecimal")) {
//                params[i] = new BigDecimal("10");
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("object")) {
//                params[i] = "object";
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("blob")) {
//                params[i] = blob;
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("nclob")) {
//                params[i] = nclob;
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("clob")) {
//                params[i] = clob;
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("ref")) {
//                params[i] = ref;
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("rowid")) {
//                params[i] = rowid;
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("array")) {
//                params[i] = array;
//            } else if (paramTypes[i].toString().toLowerCase().endsWith("sqlxml")) {
//                params[i] = sqlxml;
//            }
//
//            if (i == 1) {
//                if (paramTypes[i].toString().toLowerCase().endsWith("inputstream")) {
//                    expValue = "InputStream";
//                } else if (paramTypes[i].toString().toLowerCase().endsWith("reader")) {
//                    expValue = "Reader";
//                } else {
//                    expValue = params[i];
//                }
//            } else if (expValue.toString().startsWith("InputStream") || expValue.toString().startsWith("Reader")) {
//                expValue = expValue + "-" + params[i].toString();
//            }
//        }
//        return expValue;
//    }
//
//    /**
//     * Assert parameter is store properly.
//     * @param m The method tested.
//     * @param wrapper The wrapper containing parameters set.
//     * @param index The parameter index.
//     * @param value The value expected.
//     */
//    private void assertParameter(Method m, PreparedStatementWrapper wrapper, int index, Object value) {
//        Map<Integer, Object> params = getField(wrapper, "params");
//        if (value.toString().startsWith("InputStream") || value.toString().startsWith("Reader")) {
//            assertEquals(m.getName(), "[" + m.getName().replace("set", "") + "-" + value + "]", params.get(index));
//        } else if (m.getName().equals("setRowId")) {
//            assertEquals(m.getName(), value.toString(), params.get(index).toString());
//        } else {
//            assertEquals(m.getName(), (m.getName().equals("setNull") ? null : value), params.get(index));
//        }
//    }
//
//    /**
//     * Test of executeQuery method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testExecuteQuery(@Mocked final PreparedStatement ps, @Mocked final Logger log) throws Exception {
//        new Expectations() {
//
//            {
//                ps.executeQuery();
//                times = 1;
//
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//
//                log.isLoggable(Level.FINE);
//                result = true;
//                times = 1;
//
//                log.log(Level.FINE, anyString);
//                times = 1;
//            }
//        };
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.executeQuery();
//        Thread.sleep(100L);
//    }
//
//    /**
//     * Test of executeUpdate method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testExecuteUpdate(@Mocked final PreparedStatement ps, @Mocked final Logger log) throws Exception {
//        new Expectations() {
//
//            {
//                ps.executeUpdate();
//                times = 1;
//
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//
//                log.isLoggable(Level.FINE);
//                result = true;
//                times = 1;
//
//                log.log(Level.FINE, anyString);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.executeUpdate();
//        Thread.sleep(100L);
//    }
//
//    /**
//     * Test of setNull method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNull_int_int(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNull(1, 2);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNull(1, 2);
//    }
//
//    /**
//     * Test of setBoolean method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBoolean(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBoolean(1, true);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBoolean(1, true);
//    }
//
//    /**
//     * Test of setByte method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetByte(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setByte(1, (byte) 2);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setByte(1, (byte) 2);
//    }
//
//    /**
//     * Test of setShort method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetShort(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setShort(1, (short) 2);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setShort(1, (short) 2);
//    }
//
//    /**
//     * Test of setInt method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetInt(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setInt(1, 2);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setInt(1, 2);
//    }
//
//    /**
//     * Test of setLong method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetLong(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setLong(1, 2L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setLong(1, 2L);
//    }
//
//    /**
//     * Test of setFloat method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetFloat(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setFloat(1, 2.2f);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setFloat(1, 2.2f);
//    }
//
//    /**
//     * Test of setDouble method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetDouble(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setDouble(1, 2.1d);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setDouble(1, 2.1d);
//    }
//
//    /**
//     * Test of setBigDecimal method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBigDecimal(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBigDecimal(1, BigDecimal.TEN);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBigDecimal(1, BigDecimal.TEN);
//    }
//
//    /**
//     * Test of setString method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetString(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setString(1, "abc");
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setString(1, "abc");
//    }
//
//    /**
//     * Test of setBytes method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBytes(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBytes(1, new byte[]{1, 2, 3});
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBytes(1, new byte[]{1, 2, 3});
//    }
//
//    /**
//     * Test of setDate method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetDate_int_Date(@Mocked final PreparedStatement ps) throws Exception {
//        final Date value = new Date(System.currentTimeMillis());
//        new Expectations() {
//
//            {
//                ps.setDate(1, value);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setDate(1, value);
//    }
//
//    /**
//     * Test of setTime method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetTime_int_Time(@Mocked final PreparedStatement ps) throws Exception {
//        final Time value = new Time(System.currentTimeMillis());
//        new Expectations() {
//
//            {
//                ps.setTime(1, value);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setTime(1, value);
//    }
//
//    /**
//     * Test of setTimestamp method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetTimestamp_int_Timestamp(@Mocked final PreparedStatement ps) throws Exception {
//        final Timestamp value = new Timestamp(System.currentTimeMillis());
//        new Expectations() {
//
//            {
//                ps.setTimestamp(1, value);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setTimestamp(1, value);
//    }
//
//    /**
//     * Test of setAsciiStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetAsciiStream_3args_1(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setAsciiStream(1, (InputStream) any, 3);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setAsciiStream(1, (InputStream) null, 3);
//    }
//
//    /**
//     * Test of setUnicodeStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetUnicodeStream(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setUnicodeStream(1, (InputStream) any, 3);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setUnicodeStream(1, (InputStream) null, 3);
//    }
//
//    /**
//     * Test of setBinaryStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBinaryStream_3args_1(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBinaryStream(1, (InputStream) any, 3);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBinaryStream(1, (InputStream) null, 3);
//    }
//
//    /**
//     * Test of clearParameters method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testClearParameters(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.clearParameters();
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.clearParameters();
//    }
//
//    /**
//     * Test of setObject method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetObject_3args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setObject(1, "hello world", 3);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setObject(1, "hello world", 3);
//    }
//
//    /**
//     * Test of setObject method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetObject_int_Object(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setObject(1, "Hello world");
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setObject(1, "Hello world");
//    }
//
//    /**
//     * Test of execute method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testExecute(@Mocked final PreparedStatement ps, @Mocked final Logger log) throws Exception {
//        new Expectations() {
//
//            {
//                ps.execute();
//                times = 1;
//
//                Logger.getLogger(SQLInjectionAnalyzer.class.getName());
//                result = log;
//
//                log.isLoggable(Level.FINE);
//                result = true;
//                times = 1;
//
//                log.log(Level.FINE, anyString);
//                times = 1;
//            }
//        };
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.execute();
//        Thread.sleep(100L);
//    }
//
//    /**
//     * Test of addBatch method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testAddBatch(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.addBatch();
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.addBatch();
//    }
//
//    /**
//     * Test of setCharacterStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetCharacterStream_3args_1(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setCharacterStream(1, (Reader) any, 3);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setCharacterStream(1, (Reader) null, 3);
//    }
//
//    /**
//     * Test of setRef method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetRef(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setRef(1, null);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setRef(1, null);
//    }
//
//    /**
//     * Test of setBlob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBlob_int_Blob(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBlob(1, (Blob) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBlob(1, (Blob) null);
//    }
//
//    /**
//     * Test of setClob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetClob_int_Clob(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setClob(1, (Clob) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setClob(1, (Clob) null);
//    }
//
//    /**
//     * Test of setArray method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetArray(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setArray(1, (Array) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setArray(1, null);
//    }
//
//    /**
//     * Test of getMetaData method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testGetMetaData(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.getMetaData();
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.getMetaData();
//    }
//
//    /**
//     * Test of setDate method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetDate_3args(@Mocked final PreparedStatement ps) throws Exception {
//        final Date value = new Date(System.currentTimeMillis());
//        final Calendar c = Calendar.getInstance();
//        new Expectations() {
//
//            {
//                ps.setDate(1, value, c);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setDate(1, value, c);
//    }
//
//    /**
//     * Test of setTime method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetTime_3args(@Mocked final PreparedStatement ps) throws Exception {
//        final Time value = new Time(System.currentTimeMillis());
//        final Calendar c = Calendar.getInstance();
//        new Expectations() {
//
//            {
//                ps.setTime(1, value, c);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setTime(1, value, c);
//    }
//
//    /**
//     * Test of setTimestamp method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetTimestamp_3args(@Mocked final PreparedStatement ps) throws Exception {
//        final Timestamp value = new Timestamp(System.currentTimeMillis());
//        final Calendar c = Calendar.getInstance();
//        new Expectations() {
//
//            {
//                ps.setTimestamp(1, value, c);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setTimestamp(1, value, c);
//    }
//
//    /**
//     * Test of setNull method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNull_3args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNull(1, 2, "abc");
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNull(1, 2, "abc");
//    }
//
//    /**
//     * Test of setURL method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetURL(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setURL(1, (URL) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setURL(1, null);
//    }
//
//    /**
//     * Test of getParameterMetaData method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testGetParameterMetaData(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.getParameterMetaData();
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.getParameterMetaData();
//    }
//
//    /**
//     * Test of setRowId method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetRowId(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setRowId(1, (RowId) null);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setRowId(1, null);
//    }
//
//    /**
//     * Test of setNString method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNString(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNString(1, "abc");
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNString(1, "abc");
//    }
//
//    /**
//     * Test of setNCharacterStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNCharacterStream_3args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNCharacterStream(1, (Reader) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNCharacterStream(1, null, 4L);
//    }
//
//    /**
//     * Test of setNClob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNClob_int_NClob(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNClob(1, (NClob) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNClob(1, (NClob) null);
//    }
//
//    /**
//     * Test of setClob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetClob_3args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setClob(1, (Reader) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setClob(1, null, 4L);
//    }
//
//    /**
//     * Test of setBlob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBlob_3args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBlob(1, (InputStream) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBlob(1, null, 4L);
//    }
//
//    /**
//     * Test of setNClob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNClob_3args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNClob(1, (Reader) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNClob(1, null, 4L);
//    }
//
//    /**
//     * Test of setSQLXML method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetSQLXML(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setSQLXML(1, (SQLXML) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setSQLXML(1, null);
//    }
//
//    /**
//     * Test of setObject method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetObject_4args(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setObject(1, "abc", 3, 4);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setObject(1, "abc", 3, 4);
//    }
//
//    /**
//     * Test of setAsciiStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetAsciiStream_3args_2(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setAsciiStream(1, (InputStream) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setAsciiStream(1, null, 4L);
//    }
//
//    /**
//     * Test of setBinaryStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBinaryStream_3args_2(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBinaryStream(1, (InputStream) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBinaryStream(1, null, 4L);
//    }
//
//    /**
//     * Test of setCharacterStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetCharacterStream_3args_2(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setCharacterStream(1, (Reader) any, 4L);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setCharacterStream(1, null, 4L);
//    }
//
//    /**
//     * Test of setAsciiStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetAsciiStream_int_InputStream(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setAsciiStream(1, (InputStream) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setAsciiStream(1, null);
//    }
//
//    /**
//     * Test of setBinaryStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBinaryStream_int_InputStream(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBinaryStream(1, (InputStream) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBinaryStream(1, null);
//    }
//
//    /**
//     * Test of setCharacterStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetCharacterStream_int_Reader(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setCharacterStream(1, (Reader) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setCharacterStream(1, null);
//    }
//
//    /**
//     * Test of setNCharacterStream method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNCharacterStream_int_Reader(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNCharacterStream(1, (Reader) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNCharacterStream(1, (Reader) null);
//    }
//
//    /**
//     * Test of setClob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetClob_int_Reader(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setClob(1, (Reader) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setClob(1, (Reader) null);
//    }
//
//    /**
//     * Test of setBlob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetBlob_int_InputStream(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setBlob(1, (InputStream) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setBlob(1, (InputStream) null);
//    }
//
//    /**
//     * Test of setNClob method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testSetNClob_int_Reader(@Mocked final PreparedStatement ps) throws Exception {
//        new Expectations() {
//
//            {
//                ps.setNClob(1, (Reader) any);
//                times = 1;
//            }
//        };
//
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        wrapper.setNClob(1, (Reader) null);
//    }
//
//    /**
//     * Test of getBatchParameters method, of class PreparedStatementWrapper.
//     */
//    @Test
//    public void testGetBatchParameters(@Mocked final PreparedStatement ps) throws Exception {
//        PreparedStatementWrapper wrapper = new PreparedStatementWrapper(ps, "select 1 from dual");
//        assertNotNull(wrapper.getBatchParameters());
//    }
}
