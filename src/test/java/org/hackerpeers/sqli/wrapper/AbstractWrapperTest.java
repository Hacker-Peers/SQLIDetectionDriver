package org.hackerpeers.sqli.wrapper;

import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * @author pldupont@gmail.com
 * 06/06/2016.
 */
public abstract class AbstractWrapperTest<B extends Wrapper, W extends B> {
    private static final Set<String> SETTER_EXCEPTIONS = new HashSet<>(Arrays.asList(
            "setCharacterStream",
            "setAsciiStream",
            "setBinaryStream",
            "setBlob",
            "setClob",
            "setNCharacterStream",
            "setNClob",
            "setUnicodeStream"
    ));

    @DataProvider(name = "methods")
    public Iterator<Object[]> methods() throws ClassNotFoundException {
        List<Object[]> result = new ArrayList<>();
        addMethodsFromInterface(getBasicClass(), result);
        addMethodsFromInterface(Wrapper.class, result);
        return result.iterator();
    }

    @Test(dataProvider = "methods")
    public void testWrappedMethodIsCalled(Object wrappedMethod) throws InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, SQLException, ClassNotFoundException {
        B mockBasicClass = mock(getBasicClass());
        ISQLInjectionAnalyzer mockAnalyzer = mock(ISQLInjectionAnalyzer.class);
        W wrapper = getWrapperInstance(mockAnalyzer, mockBasicClass);
        Object[] params = getMethodParams((Method) wrappedMethod);

        ((Method) wrappedMethod).invoke(wrapper, params);

        int invokeCount = 1;
//        if (((Method) wrappedMethod).getName().equals("getConnection")) {
//            invokeCount = 0;
//        }
        ((Method) wrappedMethod).invoke(verify(mockBasicClass, times(invokeCount)), params);
        assertSetterCatchParameters(wrapper, ((Method) wrappedMethod).getName(), params);
    }

    private void assertSetterCatchParameters(W wrapper, String methodName, Object[] params) {
        if (methodName.startsWith("set")
                && wrapper instanceof PreparedStatementWrapper) {
            assertFalse(((PreparedStatementWrapper) wrapper).getParameters().isEmpty());

            if (methodName.equals("setNull")) {
                assertNull(((PreparedStatementWrapper) wrapper).getParameters().get(params[0]));
            } else if (SETTER_EXCEPTIONS.contains(methodName)
                    && !(params[1] instanceof Blob || params[1] instanceof Clob)) {
                StringBuilder expResult = new StringBuilder(methodName.replaceFirst("set", ""));
                for (int i = 1; i < params.length; i++) {
                    expResult.append("-");
                    if (i == 1) {
                        String paramClassName = params[i].getClass().getSimpleName();
                        int mockSuffix = paramClassName.indexOf("$$EnhancerByMockito");
                        if (mockSuffix > 0) {
                            expResult.append(paramClassName.substring(0, mockSuffix));
                        } else {
                            expResult.append(paramClassName);
                        }
                    } else {
                        expResult.append(params[i]);
                    }
                }
                assertEquals(((PreparedStatementWrapper) wrapper).getParameters().get(params[0]), "[" + expResult.toString() + "]");
            } else {
                assertEquals(((PreparedStatementWrapper) wrapper).getParameters().get(params[0]), params[1]);
            }
        }

    }

    protected abstract W getWrapperInstance(ISQLInjectionAnalyzer analyzer, B mockBasicClass);

    private Class<B> getBasicClass() throws ClassNotFoundException {
        Type mySuperclass = this.getClass().getGenericSuperclass();
        Type basicType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[0];
        return (Class<B>) Class.forName(basicType.getTypeName());
    }

    private Object[] getMethodParams(Method method) throws IllegalAccessException, InstantiationException, MalformedURLException, SQLException {
        List<Object> params = new ArrayList<>();
        for (Class<?> clazz : method.getParameterTypes()) {
            if (clazz.isPrimitive()) {
                if (clazz.toString().equals("boolean")) {
                    params.add(true);
                } else if (clazz.toString().equals("byte")) {
                    params.add((byte) 1);
                } else if (clazz.toString().equals("short")) {
                    params.add((short) 1);
                } else if (clazz.toString().equals("int")) {
                    params.add(1);
                } else if (clazz.toString().equals("long")) {
                    params.add(1L);
                } else if (clazz.toString().equals("float")) {
                    params.add(1.0f);
                } else if (clazz.toString().equals("double")) {
                    params.add(1.0d);
                } else {
                    fail("" + clazz + " " + (clazz.toString().equals("int")));
                }
            } else if (clazz == Class.class) {
                params.add(null);
            } else if (clazz.isArray()) {
                params.add(java.lang.reflect.Array.newInstance(clazz.getComponentType(), 0));
            } else if (clazz.isInterface()) {
                params.add(mock(clazz));
            } else if (clazz.isAssignableFrom(Calendar.class)) {
                params.add(Calendar.getInstance());
            } else if (clazz.isAssignableFrom(URL.class)) {
                params.add(new URL("http://www.test.com"));
            } else {
                try {
                    params.add(clazz.newInstance());
                } catch (Exception e) {
                    params.add(mock(clazz));
                }
            }
        }
        return params.toArray();
    }

    private void addMethodsFromInterface(Class<?> clazz, List<Object[]> result) {
        Arrays.asList(clazz.getDeclaredMethods())
                .stream()
                .forEach(m -> result.add(new Object[]{m}));
    }
}
