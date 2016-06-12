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
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class SQLInjectionRepositoryH2 implements ISQLInjectionRepository {
    private static final String CHANGELOG_FILENAME = "db-changelog.xml";
    private static final String GET_ALL_DATA_SQL =  "SELECT e.value AS entryPoint, s.value AS statement, i.value AS variation\n" +
                                                    "FROM EntryPoints e\n" +
                                                    "   INNER JOIN Statements s ON s.entryPointId = e.id\n" +
                                                    "   INNER JOIN InClauseVariations i ON i.statementId = s.id";
    private static final String INSERT_ENTRY_POINT_SQL = "INSERT INTO EntryPoints(value) VALUES(?)";
    private static final String FIND_ENTRY_POINT_SQL = "SELECT id FROM EntryPoints WHERE value = ?";
    private static final String INSERT_STATEMENT_SQL = "INSERT INTO Statements(entryPointId, value) VALUES(?, ?)";
    private static final String FIND_STATEMENT_SQL = "SELECT id FROM Statements WHERE entryPointId = ? AND value = ?";
    private static final String INSERT_IN_CLAUSE_SQL = "INSERT INTO InClauseVariations(statementId, value) VALUES(?, ?)";
    private static final String FIND_IN_CLAUSE_SQL = "SELECT value FROM InClauseVariations WHERE statementId = ? AND value = ?";
    private Connection conn;

    public SQLInjectionRepositoryH2(String dbFilename) throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        try {
            conn = DriverManager.getConnection("jdbc:h2:" + dbFilename + ";MV_STORE=FALSE;MVCC=FALSE");
            initSchema(conn);
        } catch (LiquibaseException e) {
            DbUtils.closeQuietly(conn);
            throw new SQLException("Error updating the database", e);
        }
    }

    void initSchema(Connection conn) throws LiquibaseException, SQLException {
        //http://www.liquibase.org/2015/07/executing-liquibase.html
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
        Liquibase liquibase = new Liquibase(CHANGELOG_FILENAME, new ClassLoaderResourceAccessor(SQLInjectionRepositoryH2.class.getClassLoader()), database);
        liquibase.update(new Contexts(), new LabelExpression());
    }

    @Override
    public synchronized void addStatement(String entryPoint, String statement, int inClauseVariant) throws IOException {
        failIfClosed();
        try {
            persistInClauseVariant(persistStatement(persistEntryPoint(entryPoint), statement), inClauseVariant);
            conn.commit();
        } catch (SQLException ex) {
            throw new IOException("Error persisting data", ex);
        }
    }

    int persistEntryPoint(final String entryPoint) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer entryPointId = qr.query(conn, FIND_ENTRY_POINT_SQL, new ScalarHandler<Integer>(1), entryPoint);
        if (entryPointId == null) {
            entryPointId = qr.insert(conn, INSERT_ENTRY_POINT_SQL, new ScalarHandler<Integer>(), entryPoint);
        }

        return entryPointId;
    }

    int persistStatement(final int entryPointId, final String statement) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer statementId = qr.query(conn, FIND_STATEMENT_SQL, new ScalarHandler<Integer>(1), entryPointId, statement);
        if (statementId == null) {
            statementId = qr.insert(conn, INSERT_STATEMENT_SQL, new ScalarHandler<Integer>(), entryPointId, statement);
        }

        return statementId;
    }

    void persistInClauseVariant(final int statementId, final int inClauseVariant) throws SQLException {
        QueryRunner qr = new QueryRunner();

        Integer variant = qr.query(conn, FIND_IN_CLAUSE_SQL, new ScalarHandler<Integer>(1), statementId, inClauseVariant);
        if (variant == null) {
            qr.insert(conn, INSERT_IN_CLAUSE_SQL, new ScalarHandler<Integer>(), statementId, inClauseVariant);
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
