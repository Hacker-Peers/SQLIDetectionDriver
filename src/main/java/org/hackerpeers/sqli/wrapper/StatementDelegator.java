package org.hackerpeers.sqli.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;

/**
 * Wrapper around the real statement.
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
class StatementDelegator<S extends Statement> implements InvocationHandler {

    // Attributes
    private Connection connection;
    private S realStatement;
    private List<String> batchSQL = new ArrayList<>();
    private Map<Object, Object> params = new TreeMap<>();
    private List<Map<Object, Object>> batchParams = new ArrayList<>();
    private String sql;
    private ISQLInjectionAnalyzer analyzer;
    private static final Set<String> OVERWRITTEN_METHODS = new HashSet<>();
    static {
        OVERWRITTEN_METHODS.add("addBatch");
        OVERWRITTEN_METHODS.add("clearBatch");
        OVERWRITTEN_METHODS.add("getConnection");
    }

    /**
     * Wrapper constructor on the real statement.
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

    public void addBatch(String sql) throws SQLException {
        batchSQL.add(sql);
        getRealStatement().addBatch(sql);
    }

    public void clearBatch() throws SQLException {
        batchSQL.clear();
        getRealStatement().clearBatch();
    }

    public Connection getConnection() throws SQLException {
        return new ConnectionWrapper(analyzer, realStatement.getConnection());
//        return connection;
    }

    /**
     * @return The stocked parameters for batch processing.
     */
    protected List<Map<Object, Object>> getBatchParameters() {
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (StringUtils.startsWith(method.getName(), "execute")) {
            return execute(method, args);
        } else if (StringUtils.startsWith(method.getName(), "getConnection")) {
            return getConnection();
        } else if (StringUtils.startsWith(method.getName(), "addBatch")) {
            addBatch((String) args[0]);
            return null;
        } else if (StringUtils.startsWith(method.getName(), "clearBatch")) {
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
        if (StringUtils.containsIgnoreCase(method.getName(), "batch")) {
            getAnalyzer().analyze(batchSQL, getBatchParameters(), start, end);
        } else {
            getAnalyzer().analyze((String) args[0], null, start, end);
        }
        return result;
    }
}
