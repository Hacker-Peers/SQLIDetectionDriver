package com.pld.sqli.analyzer;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test methods in SQLInjectionAnalyzerEntry class.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerEntryTest {

    private static final String AN_ENTRY_POINT = "my.entry.point";
    private static final String ANOTHER_ENTRY_POINT = "another.entry.point";
    private static final String A_SIMPLE_SQL = "select sysdate from dual";
    private static final String ANOTHER_SIMPLE_SQL = "select systimestamp from dual";
    private static final Integer ZERO_VARIATION = 0;
    private static final Integer ONE_VARIATION = 1;
    private static final Integer THREE_VARIATIONS = 3;

    private SQLInjectionAnalyzerEntry anEntryWithZeroVariation;
    private SQLInjectionAnalyzerEntry anotherEntryWithZeroVariation;
    private SQLInjectionAnalyzerEntry anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree;

    @BeforeMethod
    public void givenAnEntry() {
        anEntryWithZeroVariation = new SQLInjectionAnalyzerEntry(AN_ENTRY_POINT, A_SIMPLE_SQL, ZERO_VARIATION);

        anotherEntryWithZeroVariation = new SQLInjectionAnalyzerEntry(AN_ENTRY_POINT, A_SIMPLE_SQL, ZERO_VARIATION);

        anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree = new SQLInjectionAnalyzerEntry(AN_ENTRY_POINT, A_SIMPLE_SQL, ZERO_VARIATION);
        anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree.addStatementCall(ONE_VARIATION);
        anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree.addStatementCall(THREE_VARIATIONS);
    }


    /**
     * Test constructor, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void whenConstructorIsUsedValuesAreAsExpected() {
        SQLInjectionAnalyzerEntry entry = new SQLInjectionAnalyzerEntry(AN_ENTRY_POINT, A_SIMPLE_SQL, ZERO_VARIATION);

        assertThat(entry.getEntryPoint(), equalTo(AN_ENTRY_POINT));
        assertThat(entry.getStatement(), equalTo(A_SIMPLE_SQL));
        assertThat(entry.getVariationList(), equalTo(ZERO_VARIATION.toString()));
        assertThat(entry.getCount(), equalTo(1));
    }

    /**
     * Test of addStatementCall method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void whenAddingAVariationCountIsIncreasedByOne() {
        int initialCount = anEntryWithZeroVariation.getCount();

        anEntryWithZeroVariation.addStatementCall(ZERO_VARIATION);

        assertThat(anEntryWithZeroVariation.getCount(), equalTo(initialCount + 1));
    }

    @Test
    public void whenAddingAnExistingVariationVariationListIsUnchanged() throws Exception {
        String initialList = anEntryWithZeroVariation.getVariationList();

        anEntryWithZeroVariation.addStatementCall(ZERO_VARIATION);

        assertThat(anEntryWithZeroVariation.getVariationList(), equalTo(initialList));
    }

    @Test
    public void whenAddingANewVariationVariationListIsUpdated() throws Exception {
        String initialList = anEntryWithZeroVariation.getVariationList();

        anEntryWithZeroVariation.addStatementCall(ONE_VARIATION);

        assertThat(anEntryWithZeroVariation.getVariationList(), equalTo(initialList + "," + ONE_VARIATION));
    }

    @Test
    public void whenMergingEntriesCountsAreAdded() throws Exception {
        int firstCount = anEntryWithZeroVariation.getCount();
        int secondCount = anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree.getCount();

        anEntryWithZeroVariation.mergeStatementCall(anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree);

        assertThat(anEntryWithZeroVariation.getCount(), equalTo(firstCount + secondCount));
    }

    @Test
    public void whenMergingEntriesUniqueVariationsAreCombined() throws Exception {
        String expected = String.join(",", ZERO_VARIATION.toString(), ONE_VARIATION.toString(), THREE_VARIATIONS.toString());

        anEntryWithZeroVariation.mergeStatementCall(anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree);

        assertThat(anEntryWithZeroVariation.getVariationList(), equalTo(expected));
    }

    /**
     * Test of toString method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void toStringIsAsExpected() {
        String expected = String.format("SQLInjectionAnalyzerEntry{entryPoint=%s statement=%s}", anEntryWithZeroVariation.getEntryPoint(), anEntryWithZeroVariation.getStatement());

        assertThat(anEntryWithZeroVariation.toString(), equalTo(expected));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void twoSimilarEntriesEqual() {
        assertThat(anEntryWithZeroVariation.equals(anotherEntryWithZeroVariation), is(true));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void entriesWithDifferentEntryPointsDiffer() {
        SQLInjectionAnalyzerEntry different = new SQLInjectionAnalyzerEntry(ANOTHER_ENTRY_POINT, A_SIMPLE_SQL, ZERO_VARIATION);

        assertThat(anEntryWithZeroVariation.equals(different), is(false));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void entriesWithDifferentStatementsDiffer() {
        SQLInjectionAnalyzerEntry different = new SQLInjectionAnalyzerEntry(ANOTHER_ENTRY_POINT, ANOTHER_SIMPLE_SQL, ZERO_VARIATION);

        assertThat(anEntryWithZeroVariation.equals(different), is(false));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void entriesWithDifferentVariationsAndCountEquals() {
        assertThat(anEntryWithZeroVariation.equals(anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree), is(true));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void anEntryAlwaysEqualsItself() throws Exception {
        assertThat(anEntryWithZeroVariation.equals(anEntryWithZeroVariation), is(true));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void anEntryNeverEqualsToNull() throws Exception {
        assertThat(anEntryWithZeroVariation.equals(null), is(false));
    }

    /**
     * Test of equals method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void anEntryNeverEqualsAnotherClass() throws Exception {
        assertThat(anEntryWithZeroVariation.equals("A String"), is(false));
    }

    /**
     * Test of hashCode method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void entriesWithSameEntryPointAndStatementsHaveSameHashCode() {
        assertThat(anEntryWithZeroVariation.hashCode(), equalTo(anEntryWithVariationsOfZeroOneAndThreeAndCountOfThree.hashCode()));
    }

    /**
     * Test of hashCode method, of class SQLInjectionAnalyzerEntry.
     */
    @Test
    public void aChangeOfVariationAndCountDoesNotChangeHashCode() {
        int initial = anEntryWithZeroVariation.hashCode();

        anEntryWithZeroVariation.addStatementCall(THREE_VARIATIONS);

        assertThat(anEntryWithZeroVariation.hashCode(), equalTo(initial));
    }
}
