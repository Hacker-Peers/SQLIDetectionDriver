package org.hackerpeers.sqli.analyzer;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionAnalyzerEntryMatcher extends TypeSafeMatcher<SQLInjectionAnalyzerEntry> {
    private String entryPoint;
    private String statement;
    private Set<Integer> variationINClause = new TreeSet<>();
    private int occurrence = 1;

    public SQLInjectionAnalyzerEntryMatcher(String entryPoint, String statement, int occurrence, Integer ... variationINClause) {
        this.entryPoint = entryPoint;
        this.statement = statement;
        this.variationINClause.addAll(Arrays.asList(variationINClause));
        this.occurrence = occurrence;
    }

    private static String setToCSVString(Collection<?> col) {
        return col.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    @Override
    protected boolean matchesSafely(SQLInjectionAnalyzerEntry that) {
        if (!Objects.equals(entryPoint, that.getEntryPoint())) return false;
        if (!Objects.equals(statement, that.getStatement())) return false;
        if (occurrence != that.getCount()) return false;
        return Objects.equals(setToCSVString(variationINClause), that.getVariationList());

    }

    @Override
    public void describeTo(Description description) {
        //TODO: Code this :-/
    }
}
