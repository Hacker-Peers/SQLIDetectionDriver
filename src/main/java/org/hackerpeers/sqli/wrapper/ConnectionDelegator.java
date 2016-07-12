package org.hackerpeers.sqli.wrapper;

import org.apache.commons.lang3.StringUtils;
import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Proxy around the real connection.
 * @author Simon Berthiaume (sberthiaume@gmail.com) based on Pierre-Luc Dupont (pldupont@gmail.com) work
 */
public class ConnectionDelegator implements InvocationHandler {

    private Connection realConnection;
    private ISQLInjectionAnalyzer analyzer;

    public ConnectionDelegator(ISQLInjectionAnalyzer analyzer, Connection realConnection) {
        this.realConnection = realConnection;
        this.analyzer = analyzer;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        if (StringUtils.startsWith(method.getName(), "createStatement")) {
            return createStatement((Connection) proxy, method, args);
        } else if (StringUtils.equals(method.getName(), "prepareStatement")) {
            return prepareStatement((Connection) proxy, method, args);
        } else if (StringUtils.equals(method.getName(), "prepareCall")) {
            return prepareCall((Connection) proxy, method, args);
        } else {
            return method.invoke(getRealConnection(), args);
        }
    }

    private Statement createStatement(Connection proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Class[] proxyInterfaces = new Class[]{Statement.class};
        StatementDelegator<Statement> delegator =
                new StatementDelegator<>(proxy, analyzer, (Statement) method.invoke(getRealConnection(), args));
        return (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                proxyInterfaces,
                delegator);
    }

    private PreparedStatement prepareStatement(Connection proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Class[] proxyInterfaces = new Class[]{PreparedStatement.class};
        PreparedStatementDelegator<PreparedStatement> delegator =
                new PreparedStatementDelegator<>(proxy, analyzer, (PreparedStatement) method.invoke(getRealConnection(), args), (String) args[0]);
        return (PreparedStatement) Proxy.newProxyInstance(
                PreparedStatement.class.getClassLoader(),
                proxyInterfaces,
                delegator);
    }

    private CallableStatement prepareCall(Connection proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Class[] proxyInterfaces = new Class[]{CallableStatement.class};
        CallableStatementDelegator<CallableStatement> delegator =
                new CallableStatementDelegator<>(proxy, analyzer, (CallableStatement) method.invoke(getRealConnection(), args), (String) args[0]);
        return (CallableStatement) Proxy.newProxyInstance(
                CallableStatement.class.getClassLoader(),
                proxyInterfaces,
                delegator);
    }

    private Connection getRealConnection() {
        return realConnection;
    }
}
