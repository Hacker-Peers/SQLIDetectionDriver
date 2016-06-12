package org.hackerpeers.sqli.analyzer;

import org.testng.annotations.AfterMethod;

import java.io.IOException;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionRepositoryH2Test extends ISQLInjectionRepositoryTest<SQLInjectionRepositoryH2> {
    private static final String AN_H2_DB_FILENAME = "mem:"; //In-memory
    //private static final String AN_H2_DB_FILENAME = "./src/test/resources/EmptyDB";

    @AfterMethod
    void resetDb() throws IOException {
        repo.close();
    }

    @Override
    SQLInjectionRepositoryH2 createRepo() throws Exception {
        return new SQLInjectionRepositoryH2(AN_H2_DB_FILENAME);
    }
}