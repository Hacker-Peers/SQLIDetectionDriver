package org.hackerpeers.sqli.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper around the real statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
class StatementDelegator<S extends Statement> implements InvocationHandler {

    // Attributes
    private Connection connection;
    private S realStatement;
    private List<String> batchSQL = new ArrayList<>();
    private ISQLInjectionAnalyzer analyzer;

    /**
     * Wrapper constructor on the real statement.
     * @param conn The connection calling this statement.
     * @param analyzer The SQLInjectionAnalyzer service.
     * @param realStatement The real statement.
     */
    StatementDelegator(Connection conn, ISQLInjectionAnalyzer analyzer, S realStatement) {
        super();
        this.connection = conn;
        this.realStatement = realStatement;
        this.analyzer = analyzer;
    }

    protected final ISQLInjectionAnalyzer getAnalyzer() {
        return analyzer;
    }

    final S getRealStatement() {
        return realStatement;
    }

    private void addBatch(String sql) throws SQLException {
        batchSQL.add(sql);
        getRealStatement().addBatch(sql);
    }

    List<String> getBatchSQL() {
        return batchSQL;
    }

    private void clearBatch() throws SQLException {
        batchSQL.clear();
        getRealStatement().clearBatch();
    }

    Connection getConnection() throws SQLException {
        return new ConnectionWrapper(analyzer, realStatement.getConnection());
//        return connection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (StringUtils.startsWith(method.getName(), "execute")) {
            return execute(method, args);
        } else if (StringUtils.equals(method.getName(), "getConnection")) {
            return getConnection();
        } else if (StringUtils.equals(method.getName(), "addBatch") && args.length == 1) {
            addBatch((String) args[0]);
            return null;
        } else if (StringUtils.equals(method.getName(), "clearBatch")) {
            clearBatch();
            return null;
        } else {
            return method.invoke(getRealStatement(), args);
        }
    }

    private Object execute(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        long start = System.currentTimeMillis();
        Object result = method.invoke(getRealStatement(), args);
        long end = System.currentTimeMillis();
        analyze(method, args, start, end);
        return result;
    }

    void analyze(Method method, Object[] args, long start, long end) {
        if (StringUtils.containsIgnoreCase(method.getName(), "batch")) {
            getAnalyzer().analyze(batchSQL, null, start, end);
        } else {
            getAnalyzer().analyze((String) args[0], null, start, end);
        }
    }
}
