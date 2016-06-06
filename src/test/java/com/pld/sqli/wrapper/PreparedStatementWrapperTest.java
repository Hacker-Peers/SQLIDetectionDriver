package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Test methods in PreparedStatementWrapper class.
 *
 * @author Pierre-Luc Dupont (pldupont@gmail.com)
 */
public class PreparedStatementWrapperTest extends AbstractWrapperTest {

    @DataProvider(name = "methods")
    public Iterator<Object[]> methods() {
        List<Object[]> result = new ArrayList<>();
        Arrays.asList(PreparedStatement.class.getDeclaredMethods())
                .stream()
                // Filter out missing implementation at the moment
                .filter(m -> !m.getName().equals("executeLargeUpdate"))
                .forEach(m -> result.add(new Object[]{m}));
        return result.iterator();
    }

    @Test(dataProvider = "methods")
    public void testWrappedMethodIsCalled(Object wrappedMethod) throws InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, SQLException {
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        PreparedStatementWrapper wrapper = new PreparedStatementWrapper<PreparedStatement>(mock(ISQLInjectionAnalyzer.class), mockPreparedStatement, "SELECT 1 FROM dual");
        Object[] params = getMethodParams((Method) wrappedMethod);

        ((Method) wrappedMethod).invoke(wrapper, params);

        ((Method) wrappedMethod).invoke(verify(mockPreparedStatement, times(1)), params);
    }
}
