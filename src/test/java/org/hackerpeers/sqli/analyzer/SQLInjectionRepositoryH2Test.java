package org.hackerpeers.sqli.analyzer;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com)
 */
public class SQLInjectionRepositoryH2Test extends ISQLInjectionRepositoryTest<SQLInjectionRepositoryH2> {
    private static final String AN_H2_IN_MEMORY_DB_FILENAME = "mem:"; //In-memory
    private static final String AN_H2_DB_FILENAME = "./src/test/resources/EmptyDB";

    private Connection connection;

    @AfterMethod
    void resetDb() throws IOException {
        repo.close();
    }

    @Override
    SQLInjectionRepositoryH2 createRepo() throws Exception {
        SQLInjectionRepositoryH2 r = new SQLInjectionRepositoryH2(AN_H2_IN_MEMORY_DB_FILENAME);
        connection = spy(r.getConnection());
        r.setConnection(connection);
        return r;
    }

    @Test(expectedExceptions = SQLException.class, expectedExceptionsMessageRegExp = "Error setting up or upgrading the database")
    void whenALiquibaseErrorHappensInitThrowsASQLException() throws SQLException {
        when(connection.getMetaData()).thenThrow(new SQLException("Error!!!!"));

        repo.initSchema();
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Error persisting data")
    void whenASQLExceptionOccursWhenAddingAStatementAnIOExceptionIsThrown() throws SQLException, IOException {
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Error!!!!"));

        repo.addStatement(AN_ENTRY_POINT, A_SIMPLE_SQL, ONE_VARIATION);
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Error reading data")
    void whenASQLExceptionOccursWhenGettingEntriesAnIOExceptionIsThrown() throws SQLException, IOException {
        when(connection.createStatement()).thenThrow(new SQLException("Error!!!!"));

        repo.getEntries();
    }

    @Test(expectedExceptions = IOException.class, expectedExceptionsMessageRegExp = "Error closing DB connection")
    void whenASQLExceptionOccursWhenClosingAnIOExceptionIsThrown() throws SQLException, IOException {
        doThrow(new SQLException("Error!!!!"))
                .doCallRealMethod() // So test cleanup method won't fail
                .when(connection).close();

        repo.close();
    }
}