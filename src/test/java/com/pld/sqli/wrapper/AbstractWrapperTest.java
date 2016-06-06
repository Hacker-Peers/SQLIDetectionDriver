package com.pld.sqli.wrapper;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.fail;

/**
 * Created by pldupont@gmail.com on 06/06/2016.
 */
public class AbstractWrapperTest {
    protected Object[] getMethodParams(Method method) throws IllegalAccessException, InstantiationException, MalformedURLException, SQLException {
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
}
