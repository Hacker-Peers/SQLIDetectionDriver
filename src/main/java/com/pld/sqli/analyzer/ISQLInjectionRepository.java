package com.pld.sqli.analyzer;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public interface ISQLInjectionRepository extends Closeable {

    /**
     * Add the statement to the cache of statements by entry point.
     * @param entryPoint The entry point of the statement.
     * @param statement The statement to cache.
     * @param inClauseVariant The id of the IN Clause variant.
     */
    void addStatement(String entryPoint, String statement, int inClauseVariant) throws IOException;

    /**
     * @return All entries created by the SQLInjectionAnalyzer.
     * @throws Exception If an error occurs while retrieving entries.
     */
    Collection<SQLInjectionAnalyzerEntry> getEntries() throws IOException;
}
