package com.pld.sqli.analyzer;

import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;

/**
 * Entry to distinguish different statement for the same entry point.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerEntry {

    // Attributes
    private String statement;
    private Set<Integer> variationINClause = new TreeSet<Integer>();
    private int occurence = 0;

    /**
     * Constructor for statement containing IN clause.
     * @param statement The base statement for this entry.
     * @param variationINClause The IN clause variation count.
     */
    public SQLInjectionAnalyzerEntry(String statement, int variationINClause) {
        this.statement = statement;
        this.variationINClause.add(variationINClause);
        this.occurence = 1;
    }

    /**
     * Add the statement invocation count.
     * @param variationINClause The IN clause variation number.
     */
    void addStatementCall(int variationINClause) {
        this.occurence++;
        this.variationINClause.add(variationINClause);
    }

    /**
     * Add the statement invocation count.
     * @param variationINClause The IN clause variation number.
     */
    void mergeStatementCall(SQLInjectionAnalyzerEntry toMerge) {
        this.occurence += toMerge.occurence;
        this.variationINClause.addAll(toMerge.variationINClause);
    }

    /**
     * @return The statement that is analyzed.
     */
    public String getStatement() {
        return this.statement;
    }

    /**
     * @return The number of variations that was used for this specific statement.
     */
    public String getVariationList() {
        return StringUtils.join(variationINClause, ",");
    }

    /**
     * @return The number of occurrences that this statement was called.
     */
    public int getCount() {
        return this.occurence;
    }

    @Override
    public String toString() {
        return "SQLInjectionAnalyzerEntry{" + "statement=" + statement + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        final SQLInjectionAnalyzerEntry other = (SQLInjectionAnalyzerEntry) obj;
        if ((this.statement == null) ? (other.statement != null) : !this.statement.equals(other.statement)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return (this.statement != null ? this.statement.hashCode() : 0);
    }
}
