package org.hackerpeers.sqli.wrapper;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Wrapper around the real prepared statement.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
class PreparedStatementDelegator<PS extends PreparedStatement> extends StatementDelegator<PreparedStatement> {

    // Attributes
    private Map<Object, Object> params = new TreeMap<>();
    private List<Map<Object, Object>> batchParams = new ArrayList<>();
    private String sql;

    /**
     * Wrapper constructor on the real prepared statement.
     *
     * @param conn                  The connection calling this statement.
     * @param analyzer              The SQLInjectionAnalyzer service.
     * @param realPreparedStatement The real prepared statement.
     */
    PreparedStatementDelegator(Connection conn, ISQLInjectionAnalyzer analyzer, PS realPreparedStatement, String sql) {
        super(conn, analyzer, realPreparedStatement);
        this.sql = sql;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (StringUtils.startsWith(method.getName(), "set") && args.length >= 2) {
            setParameters(method, args);
            return null;
        } else if (StringUtils.equals(method.getName(), "addBatch") && ArrayUtils.isEmpty(args)) {
            addBatch();
            return null;
        } else if (StringUtils.equals(method.getName(), "clearParameters")) {
            clearParameters();
            return null;
        } else {
            return super.invoke(proxy, method, args);
        }
    }

    @Override
    void analyze(Method method, Object[] args, long start, long end) {
        if (StringUtils.containsIgnoreCase(method.getName(), "batch")) {
            getAnalyzer().analyze(getBatchSQL(), getBatchParameters(), start, end);
        } else {
            getAnalyzer().analyze(sql, getParameters(), start, end);
        }
    }

    private void setParameters(Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Object key = args[0];
        Object value;
        if (StringUtils.equals(method.getName(), "setNull")) {
            value = "null, type:" + args[1];
        } else if (args[1] instanceof InputStream) {
            value = "[" + method.getName().replaceFirst("set", "") + "-InputStream]";
            if (args.length > 2) {
                value += "-" + StringUtils.join(ArrayUtils.subarray(args, 2, args.length), "-");
            }
        } else if (args[1] instanceof Reader) {
            value = "[" + method.getName().replaceFirst("set", "") + "-Reader]";
            if (args.length > 2) {
                value += "-" + StringUtils.join(ArrayUtils.subarray(args, 2, args.length), "-");
            }
        } else {
            value = StringUtils.join(ArrayUtils.subarray(args, 1, args.length), "-");
        }
        addParams(key, value);
        method.invoke(getRealStatement(), args);
    }

    private void addBatch() throws SQLException {
        batchParams.add(new TreeMap<>(params));
        getBatchSQL().add(sql);
        getRealStatement().addBatch();
    }

    private void clearParameters() throws SQLException {
        params.clear();
        getRealStatement().clearParameters();
    }

    private List<Map<Object, Object>> getBatchParameters() {
        return batchParams;
    }

    /**
     * @return The map of current parameters passed to the prepared statement.
     */
    private Map<Object, Object> getParameters() {
        return params;
    }

    private void addParams(Object k, Object v) {
        params.put(k, v);
    }
}
