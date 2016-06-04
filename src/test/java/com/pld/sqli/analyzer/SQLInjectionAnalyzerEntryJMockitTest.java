package com.pld.sqli.analyzer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test methods in SQLInjectionAnalyzerEntry class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerEntryJMockitTest {
    
    /**
     * Test constructor, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void testConstructor() {
        SQLInjectionAnalyzerEntry entry = new SQLInjectionAnalyzerEntry("select sysdate from dual", 0);
        assertEquals("select sysdate from dual", entry.getStatement());
        assertEquals("0", entry.getVariationList());
        assertEquals(1, entry.getCount());
    }
    
    /**
     * Test of addStatementCall method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void testAddStatementCall() {
        SQLInjectionAnalyzerEntry entry = new SQLInjectionAnalyzerEntry("select sysdate from dual", 0);
        assertEquals("select sysdate from dual", entry.getStatement());
        assertEquals("0", entry.getVariationList());
        assertEquals(1, entry.getCount());
        
        entry.addStatementCall(0);
        assertEquals("select sysdate from dual", entry.getStatement());
        assertEquals("0", entry.getVariationList());
        assertEquals(2, entry.getCount());
        
        entry.addStatementCall(1);
        assertEquals("select sysdate from dual", entry.getStatement());
        assertEquals("0,1", entry.getVariationList());
        assertEquals(3, entry.getCount());
    }

    /**
     * Test of mergeStatementCall method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void testMergeStatementCall() {
        SQLInjectionAnalyzerEntry entry1 = new SQLInjectionAnalyzerEntry("select sysdate from dual", 1);
        entry1.addStatementCall(2);
        assertEquals("select sysdate from dual", entry1.getStatement());
        assertEquals("1,2", entry1.getVariationList());
        assertEquals(2, entry1.getCount());

        SQLInjectionAnalyzerEntry entry2 = new SQLInjectionAnalyzerEntry("select sysdate from dual", 3);
        entry2.addStatementCall(4);
        entry2.addStatementCall(1);
        assertEquals("select sysdate from dual", entry2.getStatement());
        assertEquals("1,3,4", entry2.getVariationList());
        assertEquals(3, entry2.getCount());
        
        entry1.mergeStatementCall(entry2);
        assertEquals("select sysdate from dual", entry1.getStatement());
        assertEquals("1,2,3,4", entry1.getVariationList());
        assertEquals(5, entry1.getCount());
    }

    /**
     * Test of toString method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void testToString() {
        SQLInjectionAnalyzerEntry entry = new SQLInjectionAnalyzerEntry("select sysdate from dual", 1);
        assertEquals("SQLInjectionAnalyzerEntry{statement=select sysdate from dual}", entry.toString());
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void testEquals() {
        SQLInjectionAnalyzerEntry entry1 = new SQLInjectionAnalyzerEntry("select sysdate from dual", 1);
        SQLInjectionAnalyzerEntry entry2 = new SQLInjectionAnalyzerEntry("select sysdate from dual", 1);
        SQLInjectionAnalyzerEntry entry3 = new SQLInjectionAnalyzerEntry("select systimestamp from dual", 1);
        SQLInjectionAnalyzerEntry entry4 = new SQLInjectionAnalyzerEntry(null, 1);
        assertFalse(entry1.equals(null));
        assertFalse(entry1.equals(new Object()));
        assertFalse(entry4.equals(entry1));
        assertFalse(entry1.equals(entry4));
        assertFalse(entry1.equals(entry3));
        assertTrue(entry1.equals(entry2));
        assertTrue(entry4.equals(entry4));
    }

    /**
     * Test of hashCode method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void testHashCode() {
        SQLInjectionAnalyzerEntry entry1 = new SQLInjectionAnalyzerEntry("select sysdate from dual", 1);
        assertEquals("select sysdate from dual".hashCode(), entry1.hashCode());
        SQLInjectionAnalyzerEntry entry2 = new SQLInjectionAnalyzerEntry(null, 1);
        assertEquals(0, entry2.hashCode());
    }
}
