package org.hackerpeers.sqli.analyzer;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionRepositoryH2Test extends ISQLInjectionRepositoryTest<SQLInjectionRepositoryH2> {
    private static final String AN_H2_DB_FILENAME = "mem:"; //In-memory


    @AfterMethod
    void resetDb() throws IOException {
        repo.close();
    }

    @Override
    SQLInjectionRepositoryH2 createRepo() throws Exception {
        return new SQLInjectionRepositoryH2("mem:");
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Error updating the database")
    void whenThereIsALiquibaseErrorConstructorReportsAnIOException() throws Exception {

    }
}