package org.hackerpeers.sqli.analyzer;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.apache.commons.dbutils.DbUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    private static final String INSERT_STATEMENT_SQL = "INSERT INTO Statements(entryPointId, value) VALUES(?, ?)";
    private static final String INSERT_IN_CLAUSE_SQL = "INSERT INTO InClauseVariations(statementId, value) VALUES(?, ?)";
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
    public void addStatement(String entryPoint, String statement, int inClauseVariant) throws IOException {
        failIfClosed();
        try {
            persistInClauseVariant(persistStatement(persistEntryPoint(entryPoint), statement), inClauseVariant);
            conn.commit();
        } catch (SQLException ex) {
            throw new IOException("Error persisting data", ex);
        }
    }

    int persistEntryPoint(String entryPoint) throws SQLException {
        int entryPointId;
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_ENTRY_POINT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entryPoint);
            stmt.execute();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entryPointId = rs.getInt(1);
                } else {
                    throw new SQLException("No value inserted!");
                }
            }

        }
        return entryPointId;
    }

    int persistStatement(int entryPointId, String statement) throws SQLException {
        int statementId;
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_STATEMENT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, entryPointId);
            stmt.setString(2, statement);
            stmt.execute();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    statementId = rs.getInt(1);
                } else {
                    throw new SQLException("No value inserted!");
                }
            }

        }
        return statementId;
    }

    void persistInClauseVariant(int statementId, int inClauseVariant) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_IN_CLAUSE_SQL)) {
            stmt.setInt(1, statementId);
            stmt.setInt(2, inClauseVariant);
            stmt.execute();
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
    public void close() throws IOException {
        try {
            DbUtils.close(conn);
        } catch (SQLException e) {
            throw new IOException("Error closing DB connection", e);
        }
    }

    private void failIfClosed() throws IOException {
        synchronized(conn) {
            boolean isClosed = true;
            try {
                isClosed = conn.isClosed();
            } catch (SQLException e) {/* Do Nothing */ }
            if (isClosed) {
                throw new IOException("Repository closed");
            }
        }
    }
}
