package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Test methods in CallableStatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class CallableStatementWrapperTest extends AbstractWrapperTest {

    @DataProvider(name = "methods")
    public Iterator<Object[]> methods() {
        List<Object[]> result = new ArrayList<>();
        Arrays.asList(CallableStatement.class.getDeclaredMethods())
                .stream()
                // Filter out missing implementation at the moment
                .filter(m -> !m.getName().equals("registerOutParameter") && !m.getName().equals("setObject"))
                .forEach(m -> result.add(new Object[]{m}));
        return result.iterator();
    }

    @Test(dataProvider = "methods")
    public void testWrappedMethodIsCalled(Object wrappedMethod) throws InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, SQLException {
        CallableStatement mockCallableStatement = mock(CallableStatement.class);
        CallableStatementWrapper wrapper = new CallableStatementWrapper(mock(ISQLInjectionAnalyzer.class), mockCallableStatement, "select 1 from dual");
        Object[] params = getMethodParams((Method) wrappedMethod);

        ((Method) wrappedMethod).invoke(wrapper, params);

        ((Method) wrappedMethod).invoke(verify(mockCallableStatement, times(1)), params);
    }
}
