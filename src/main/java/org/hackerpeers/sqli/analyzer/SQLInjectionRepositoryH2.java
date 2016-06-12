package org.hackerpeers.sqli.analyzer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 * An H2 DB implementation of the repository; if created in-memory, all data is lost when the JVM shuts down.
 * If persisted to a file then the data is preserved from execution to execution.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionRepositoryH2 implements ISQLInjectionRepository {
    private static final String CHANGELOG_FILENAME = "db-changelog.xml";
    private static final String GET_ALL_DATA_SQL =  "SELECT e.value AS entryPoint, s.value AS statement, i.value AS variation\n" +
                                                    "FROM EntryPointsStatements es\n" +
                                                    "   INNER JOIN EntryPoints e ON es.entryPointId = e.id\n" +
                                                    "   INNER JOIN Statements s ON es.statementId = s.id\n" +
                                                    "   INNER JOIN InClauseVariations i ON i.entryPointStatementId = es.id";

    private static final String FIND_ENTRY_POINT_SQL = "SELECT id FROM EntryPoints WHERE value = ?";
    private static final String INSERT_ENTRY_POINT_SQL = "INSERT INTO EntryPoints(value) VALUES(?)";

    private static final String FIND_STATEMENT_SQL = "SELECT id FROM Statements WHERE value = ?";
    private static final String INSERT_STATEMENT_SQL = "INSERT INTO Statements(value) VALUES(?)";

    private static final String FIND_ENTRY_POINT_STATEMENT_SQL = "SELECT id FROM EntryPointsStatements WHERE entryPointId = ? AND statementId = ?";
    private static final String INSERT_ENTRY_POINT_STATEMENT_SQL = "INSERT INTO EntryPointsStatements(entryPointId, statementId) VALUES(?, ?)";

    private static final String FIND_IN_CLAUSE_SQL = "SELECT value FROM InClauseVariations WHERE entryPointStatementId = ? AND value = ?";
    private static final String INSERT_IN_CLAUSE_SQL = "INSERT INTO InClauseVariations(entryPointStatementId, value) VALUES(?, ?)";

    private Connection conn;

    /**
     * Repo connected to the given H2 DB.
     * @param dbFilename Path the the H2 database (excluding any .h2.db extension)
     * @throws SQLException If there is any database-related issue.
     * @throws ClassNotFoundException If unable to load the H2 JDBC drivers.
     */
    public SQLInjectionRepositoryH2(final String dbFilename) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        try {
            conn = DriverManager.getConnection("jdbc:h2:" + dbFilename + ";MV_STORE=FALSE;MVCC=FALSE");
            initSchema(conn);
        } catch (SQLException e) {
            DbUtils.closeQuietly(conn);
            throw e;
        }
    }

    /**
     * Initialize and updates the database schema if needed.
     * @param conn The connection to the DB
     * @throws SQLException If Liquibase experiences any issues.
     */
    private void initSchema(final Connection conn) throws SQLException {
        try {
            //http://www.liquibase.org/2015/07/executing-liquibase.html
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(CHANGELOG_FILENAME, new ClassLoaderResourceAccessor(SQLInjectionRepositoryH2.class.getClassLoader()), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException e) {
            throw new SQLException("Error updating the database", e);
        }
    }

    @Override
    public synchronized void addStatement(final String entryPoint, final String statement, final int inClauseVariant) throws IOException {
        failIfClosed();
        try {
            persistInClauseVariant(persistEntryPointStatement(persistEntryPoint(entryPoint), persistStatement(statement)), inClauseVariant);
            conn.commit();
        } catch (SQLException ex) {
            throw new IOException("Error persisting data", ex);
        }
    }

    /**
     * Persists the given entry point; will create the record if it doesn't exist, but will always return the record's ID.
     * @param entryPoint The entry point to persist
     * @return The entry point record ID.
     * @throws SQLException If there is any database issues.
     */
    private int persistEntryPoint(final String entryPoint) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer entryPointId = qr.query(conn, FIND_ENTRY_POINT_SQL, new ScalarHandler<Integer>(1), entryPoint);
        if (entryPointId == null) {
            entryPointId = qr.insert(conn, INSERT_ENTRY_POINT_SQL, new ScalarHandler<Integer>(), entryPoint);
        }

        return entryPointId;
    }

    /**
     * Persists the given statement; will create the record if it doesn't exist, but will always return the record's ID.
     * @param statement The entry point to persist
     * @return The statement record ID.
     * @throws SQLException If there is any database issues.
     */
    private int persistStatement(final String statement) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer statementId = qr.query(conn, FIND_STATEMENT_SQL, new ScalarHandler<Integer>(1), statement);
        if (statementId == null) {
            statementId = qr.insert(conn, INSERT_STATEMENT_SQL, new ScalarHandler<Integer>(), statement);
        }

        return statementId;
    }

    /**
     * Persists the given entry point + statement relationship; will create the record if it doesn't exist, but will always return the record's ID.
     * @param entryPointId The entry point ID.
     * @param statementId The statement ID.
     * @return The entry point + statement relationship record ID.
     * @throws SQLException If there is any database issues.
     */
    private int persistEntryPointStatement(final int entryPointId, final int statementId) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer entryPintStatementId = qr.query(conn, FIND_ENTRY_POINT_STATEMENT_SQL, new ScalarHandler<Integer>(1), entryPointId, statementId);
        if (entryPintStatementId == null) {
            entryPintStatementId = qr.insert(conn, INSERT_ENTRY_POINT_STATEMENT_SQL, new ScalarHandler<Integer>(), entryPointId, statementId);
        }

        return entryPintStatementId;
    }

    /**
     * Persists the given 'IN CLAUSE' variant; will create the record if it doesn't exist or do nothing if it does.
     * @param entryPointStatementId The entry point + statement relationship record ID.
     * @param inClauseVariant The 'IN CLAUSE' variant value.
     * @throws SQLException If there is any database issues.
     */
    private void persistInClauseVariant(final int entryPointStatementId, final int inClauseVariant) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer variant = qr.query(conn, FIND_IN_CLAUSE_SQL, new ScalarHandler<Integer>(1), entryPointStatementId, inClauseVariant);
        if (variant == null) {
            qr.insert(conn, INSERT_IN_CLAUSE_SQL, new ScalarHandler<Integer>(), entryPointStatementId, inClauseVariant);
        }
    }

    @Override
    public Collection<SQLInjectionAnalyzerEntry> getEntries() throws IOException {
        failIfClosed();
        List<SQLInjectionAnalyzerEntry> uncombinedEntries = getUncombinedEntriesFromDB();
        Table<String, String, SQLInjectionAnalyzerEntry> buffer = HashBasedTable.create();

        uncombinedEntries.stream()
                .forEach((e) -> {
                    SQLInjectionAnalyzerEntry oldEntry = buffer.get(e.getEntryPoint(), e.getStatement());
                    if (oldEntry != null) {
                        oldEntry.mergeStatementCall(e);
                    } else {
                        buffer.put(e.getEntryPoint(), e.getStatement(), e);
                    }
                });
        return Collections.unmodifiableCollection(buffer.values());
    }

    /**
     * Get raw, uncombined data from the DB
     * @return List of raw, uncombined data from the DB.
     * @throws IOException If there is any issues reading the data from the DB.
     */
    private List<SQLInjectionAnalyzerEntry> getUncombinedEntriesFromDB() throws IOException {
        List<SQLInjectionAnalyzerEntry> uncombinedEntries = new ArrayList<>();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(GET_ALL_DATA_SQL)) {
            while (rs.next()) {
                String entryPoint = rs.getString("entryPoint");
                String statement = rs.getString("statement");
                int variation = rs.getInt("variation");
                uncombinedEntries.add(new SQLInjectionAnalyzerEntry(entryPoint, statement, variation));
            }
        } catch (SQLException ex) {
            throw new IOException("Error reading data", ex);
        }
        return uncombinedEntries;
    }

    @Override
    public synchronized void close() throws IOException {
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            throw new IOException("Error closing DB connection", e);
        }
    }

    /**
     * Just checks if the repo was closed and throw an Exception if it was.
     * @throws IOException If the repo was closed.
     */
    private synchronized void failIfClosed() throws IOException {
        boolean isClosed = true;
        try {
            isClosed = conn.isClosed();
        } catch (SQLException e) {/* Do Nothing */ }
        if (isClosed) {
            throw new IOException("Repository closed");
        }
    }
}
