package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.SQLInjectionAnalyzer;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test methods in StatementWrapper class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class StatementWrapperTest {

    /**
     * Test of executeQuery method, of class StatementWrapper.
     */
    @Test
    public void testExecuteQuery(@Mocked final Statement s) throws Exception {
        final String sql = "select 1 from dual";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.executeQuery(sql);
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.executeQuery(sql);
    }

    /**
     * Test of executeUpdate method, of class StatementWrapper.
     */
    @Test
    public void testExecuteUpdate_String(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.executeUpdate(sql);
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.executeUpdate(sql);
    }

    /**
     * Test of close method, of class StatementWrapper.
     */
    @Test
    public void testClose(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.close();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.close();
    }

    /**
     * Test of getMaxFieldSize method, of class StatementWrapper.
     */
    @Test
    public void testGetMaxFieldSize(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getMaxFieldSize();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getMaxFieldSize();
    }

    /**
     * Test of setMaxFieldSize method, of class StatementWrapper.
     */
    @Test
    public void testSetMaxFieldSize(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.setMaxFieldSize(10);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setMaxFieldSize(10);
    }

    /**
     * Test of getMaxRows method, of class StatementWrapper.
     */
    @Test
    public void testGetMaxRows(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getMaxRows();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getMaxRows();
    }

    /**
     * Test of setMaxRows method, of class StatementWrapper.
     */
    @Test
    public void testSetMaxRows(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.setMaxRows(10);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setMaxRows(10);
    }

    /**
     * Test of setEscapeProcessing method, of class StatementWrapper.
     */
    @Test
    public void testSetEscapeProcessing(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.setEscapeProcessing(true);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setEscapeProcessing(true);
    }

    /**
     * Test of getQueryTimeout method, of class StatementWrapper.
     */
    @Test
    public void testGetQueryTimeout(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getQueryTimeout();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getQueryTimeout();
    }

    /**
     * Test of setQueryTimeout method, of class StatementWrapper.
     */
    @Test
    public void testSetQueryTimeout(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.setQueryTimeout(15);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setQueryTimeout(15);
    }

    /**
     * Test of cancel method, of class StatementWrapper.
     */
    @Test
    public void testCancel(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.cancel();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.cancel();
    }

    /**
     * Test of getWarnings method, of class StatementWrapper.
     */
    @Test
    public void testGetWarnings(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getWarnings();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getWarnings();
    }

    /**
     * Test of clearWarnings method, of class StatementWrapper.
     */
    @Test
    public void testClearWarnings(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.clearWarnings();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.clearWarnings();
    }

    /**
     * Test of setCursorName method, of class StatementWrapper.
     */
    @Test
    public void testSetCursorName(@Mocked final Statement s) throws Exception {
        final String cursorName = "cursor name";
        new Expectations() {

            {
                s.setCursorName(cursorName);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setCursorName(cursorName);
    }

    /**
     * Test of execute method, of class StatementWrapper.
     */
    @Test
    public void testExecute_String(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.execute(sql);
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.execute(sql);
    }

    /**
     * Test of getResultSet method, of class StatementWrapper.
     */
    @Test
    public void testGetResultSet(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getResultSet();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getResultSet();
    }

    /**
     * Test of getUpdateCount method, of class StatementWrapper.
     */
    @Test
    public void testGetUpdateCount(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getUpdateCount();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getUpdateCount();
    }

    /**
     * Test of getMoreResults method, of class StatementWrapper.
     */
    @Test
    public void testGetMoreResults_0args(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getMoreResults();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getMoreResults();
    }

    /**
     * Test of setFetchDirection method, of class StatementWrapper.
     */
    @Test
    public void testSetFetchDirection(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.setFetchDirection(10);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setFetchDirection(10);
    }

    /**
     * Test of getFetchDirection method, of class StatementWrapper.
     */
    @Test
    public void testGetFetchDirection(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getFetchDirection();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getFetchDirection();
    }

    /**
     * Test of setFetchSize method, of class StatementWrapper.
     */
    @Test
    public void testSetFetchSize(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.setFetchSize(10);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setFetchSize(10);
    }

    /**
     * Test of getFetchSize method, of class StatementWrapper.
     */
    @Test
    public void testGetFetchSize(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getFetchSize();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getFetchSize();
    }

    /**
     * Test of getResultSetConcurrency method, of class StatementWrapper.
     */
    @Test
    public void testGetResultSetConcurrency(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getResultSetConcurrency();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getResultSetConcurrency();
    }

    /**
     * Test of getResultSetType method, of class StatementWrapper.
     */
    @Test
    public void testGetResultSetType(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getResultSetType();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getResultSetType();
    }

    /**
     * Test of addBatch method, of class StatementWrapper.
     */
    @Test
    public void testAddBatch(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.addBatch(sql);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.addBatch(sql);
        assertArrayEquals(new String[]{sql}, wrapper.getBatchSQL().toArray());
    }

    /**
     * Test of clearBatch method, of class StatementWrapper.
     */
    @Test
    public void testClearBatch(@Mocked final Statement s) throws Exception {
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.clearBatch();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getBatchSQL().add("a");
        wrapper.clearBatch();
        assertArrayEquals(new String[0], wrapper.getBatchSQL().toArray());
    }

    /**
     * Test of executeBatch method, of class StatementWrapper.
     */
    @Test
    public void testExecuteBatch(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        final List<String> list = new ArrayList<String>();
        list.add(sql);

        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.addBatch(sql);
                times = 1;

                s.executeBatch();
                times = 1;

                SQLInjectionAnalyzer.analyze(list, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.addBatch(sql);
        wrapper.executeBatch();
    }

    /**
     * Test of getConnection method, of class StatementWrapper.
     */
    @Test
    public void testGetConnection(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getConnection();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        assertTrue(wrapper.getConnection() instanceof ConnectionWrapper);
    }

    /**
     * Test of getMoreResults method, of class StatementWrapper.
     */
    @Test
    public void testGetMoreResults_int(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getMoreResults(10);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getMoreResults(10);
    }

    /**
     * Test of getGeneratedKeys method, of class StatementWrapper.
     */
    @Test
    public void testGetGeneratedKeys(@Mocked final Statement s) throws Exception {
        new Expectations() {

            {
                s.getGeneratedKeys();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getGeneratedKeys();
    }

    /**
     * Test of executeUpdate method, of class StatementWrapper.
     */
    @Test
    public void testExecuteUpdate_String_int(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.executeUpdate(sql, 10);
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.executeUpdate(sql, 10);
    }

    /**
     * Test of executeUpdate method, of class StatementWrapper.
     */
    @Test
    public void testExecuteUpdate_String_intArr(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.executeUpdate(sql, new int[]{1, 2, 3});
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.executeUpdate(sql, new int[]{1, 2, 3});
    }

    /**
     * Test of executeUpdate method, of class StatementWrapper.
     */
    @Test
    public void testExecuteUpdate_String_StringArr(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.executeUpdate(sql, new String[]{"a", "b"});
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.executeUpdate(sql, new String[]{"a", "b"});
    }

    /**
     * Test of execute method, of class StatementWrapper.
     */
    @Test
    public void testExecute_String_int(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.execute(sql, 10);
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.execute(sql, 10);
    }

    /**
     * Test of execute method, of class StatementWrapper.
     */
    @Test
    public void testExecute_String_intArr(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.execute(sql, new int[]{1, 2, 3});
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.execute(sql, new int[]{1, 2, 3});
    }

    /**
     * Test of execute method, of class StatementWrapper.
     */
    @Test
    public void testExecute_String_StringArr(@Mocked final Statement s) throws Exception {
        final String sql = "update test set col1 = 1";
        new Expectations() {

            SQLInjectionAnalyzer analyzer;

            {
                s.execute(sql, new String[]{"a", "b"});
                times = 1;

                SQLInjectionAnalyzer.analyze(sql, null, anyInt, anyInt);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.execute(sql, new String[]{"a", "b"});
    }

    /**
     * Test of getResultSetHoldability method, of class StatementWrapper.
     */
    @Test
    public void testGetResultSetHoldability(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.getResultSetHoldability();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.getResultSetHoldability();
    }

    /**
     * Test of isClosed method, of class StatementWrapper.
     */
    @Test
    public void testIsClosed(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.isClosed();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.isClosed();
    }

    /**
     * Test of setPoolable method, of class StatementWrapper.
     */
    @Test
    public void testSetPoolable(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.setPoolable(true);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.setPoolable(true);
    }

    /**
     * Test of isPoolable method, of class StatementWrapper.
     */
    @Test
    public void testIsPoolable(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.isPoolable();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.isPoolable();
    }

    /**
     * Test of unwrap method, of class StatementWrapper.
     */
    @Test
    public void testUnwrap(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.unwrap(Object.class);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.unwrap(Object.class);
    }

    /**
     * Test of isWrapperFor method, of class StatementWrapper.
     */
    @Test
    public void testIsWrapperFor(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.isWrapperFor(Object.class);
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.isWrapperFor(Object.class);
    }

    /**
     * Test of getBatchParameters method, of class StatementWrapper.
     */
    @Test
    public void testGetBatchParameters(@Mocked final Statement s) throws Exception {
        StatementWrapper wrapper = new StatementWrapper(s);
        assertNull(wrapper.getBatchParameters());
    }

    /**
     * Test of getBatchSQL method, of class StatementWrapper.
     */
    @Test
    public void testGetBatchSQL(@Mocked final Statement s) throws Exception {
        StatementWrapper wrapper = new StatementWrapper(s);
        assertEquals(0, wrapper.getBatchSQL().size());
    }

    /**
     * Test of isCloseOnCompletion method, of class StatementWrapper.
     */
    @Test
    public void testIsCloseOnCompletion(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.isCloseOnCompletion();
                result = true;
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        assertTrue(wrapper.isCloseOnCompletion());
    }

    /**
     * Test of closeOnCompletion method, of class StatementWrapper.
     */
    @Test
    public void testCloseOnCompletion(@Mocked final Statement s) throws Exception {
        new Expectations() {
            {
                s.closeOnCompletion();
                times = 1;
            }
        };

        StatementWrapper wrapper = new StatementWrapper(s);
        wrapper.closeOnCompletion();
    }
}
