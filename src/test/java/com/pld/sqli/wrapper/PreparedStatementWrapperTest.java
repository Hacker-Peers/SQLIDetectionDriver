package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test methods in StatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class PreparedStatementWrapperTest extends AbstractWrapperTest {

    @DataProvider(name = "methods")
    public Iterator<Object[]> methods() {
        List<Object[]> result = new ArrayList<>();
        Arrays.asList(Statement.class.getDeclaredMethods())
                .stream()
                // Filter out missing implementation at the moment
                .filter(m -> !m.getName().contains("Large"))
                .forEach(m -> result.add(new Object[]{m}));
        return result.iterator();
    }

    @Test(dataProvider = "methods")
    public void testWrappedMethodIsCalled(Object wrappedMethod) throws InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, SQLException {
        Statement mockStatement = mock(Statement.class);
        StatementWrapper wrapper = new StatementWrapper<Statement>(mock(ISQLInjectionAnalyzer.class), mockStatement);
        Object[] params = getMethodParams((Method) wrappedMethod);

        ((Method) wrappedMethod).invoke(wrapper, params);

        ((Method) wrappedMethod).invoke(verify(mockStatement, times(1)), params);
    }
}
