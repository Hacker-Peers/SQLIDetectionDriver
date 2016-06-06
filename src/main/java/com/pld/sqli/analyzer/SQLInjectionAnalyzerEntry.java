package com.pld.sqli.analyzer;

import com.google.common.base.MoreObjects;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Entry to distinguish different statement for the same entry point.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class SQLInjectionAnalyzerEntry {

    // Attributes
    private String entryPoint;
    private String statement;
    private Set<Integer> variationINClause = new TreeSet<>();
    private int occurrence = 1;

    /**
     * Constructor for statement containing IN clause.
     * @deprecated Should used {@link #SQLInjectionAnalyzerEntry(String, String, int)} instead.
     * @param statement The base statement for this entry.
     * @param variationINClause The IN clause variation count.
     */
    @Deprecated
    public SQLInjectionAnalyzerEntry(String statement, int variationINClause) {
        this.statement = statement;
        this.variationINClause.add(variationINClause);
    }

    /**
     * Constructor for statement containing IN clause.
     * @param entryPoint The entry point of the statement.
     * @param statement The base statement for this entry.
     * @param variationINClause The IN clause variation count.
     */
    public SQLInjectionAnalyzerEntry(String entryPoint, String statement, int variationINClause) {
        this(statement, variationINClause);

        this.entryPoint = entryPoint;
    }

    /**
     * Add the statement invocation count.
     * @param variationINClause The IN clause variation number.
     */
    void addStatementCall(int variationINClause) {
        this.occurrence++;
        this.variationINClause.add(variationINClause);
    }

    /**
     * Merge the SQLInjectionAnalyzerEntry.
     * @param toMerge the other SQLInjectionAnalyzerEntry to merge into this one.
     */
    void mergeStatementCall(SQLInjectionAnalyzerEntry toMerge) {
        this.occurrence += toMerge.occurrence;
        this.variationINClause.addAll(toMerge.variationINClause);
    }

    /**
     * @return The entry point for the statement that is analyzed.
     */
    public String getEntryPoint() {
        return entryPoint;
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
        return this.occurrence;
    }

    @Override
    public String toString() {
        return "SQLInjectionAnalyzerEntry{entryPoint=" + entryPoint + " statement=" + statement + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }
        final SQLInjectionAnalyzerEntry other = (SQLInjectionAnalyzerEntry) obj;

        return Objects.equals(this.statement, other.statement)
                && Objects.equals(this.entryPoint, other.entryPoint);
    }

    @Override
    public int hashCode() {
        int result = MoreObjects.firstNonNull(statement, "").hashCode();
        result = 31 * result + MoreObjects.firstNonNull(entryPoint, "").hashCode();
        return result;
    }
}
