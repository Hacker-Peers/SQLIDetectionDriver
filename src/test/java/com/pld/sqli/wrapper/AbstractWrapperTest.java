package com.pld.sqli.wrapper;

import com.pld.sqli.analyzer.ISQLInjectionAnalyzer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.testng.Assert.fail;

/**
 * @author pldupont@gmail.com
 * 06/06/2016.
 */
public abstract class AbstractWrapperTest<B extends Wrapper, W extends B> {

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
        W wrapper = getWrapperInstance(mock(ISQLInjectionAnalyzer.class), mockBasicClass);
        Object[] params = getMethodParams((Method) wrappedMethod);

        ((Method) wrappedMethod).invoke(wrapper, params);

        ((Method) wrappedMethod).invoke(verify(mockBasicClass, times(1)), params);
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
