package org.hackerpeers.sqli.analyzer;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionRepositoryH2 implements ISQLInjectionRepository {
    private static final String CHANGELOG_FILENAME = "db-changelog.xml";

    public SQLInjectionRepositoryH2(String dbFilename) throws SQLException, IOException, ClassNotFoundException {
        System.out.println("DBFile: " + new File(".").getCanonicalPath());
        //http://www.liquibase.org/2015/07/executing-liquibase.html
        Class.forName("org.h2.Driver");

        try (Connection conn = DriverManager.getConnection("jdbc:h2:file:" + dbFilename + ";MV_STORE=FALSE;MVCC=FALSE")) {

            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));

            Liquibase liquibase = new Liquibase(CHANGELOG_FILENAME, new ClassLoaderResourceAccessor(), database);

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            throw new SQLException("Error updating the database", e);
        }
    }

    @Override
    public void addStatement(String entryPoint, String statement, int inClauseVariant) throws IOException {

    }

    @Override
    public Collection<SQLInjectionAnalyzerEntry> getEntries() throws IOException {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        new SQLInjectionRepositoryH2("./src/test/resources/EmptyDB");
    }
}
