package org.hackerpeers.sqli.wrapper

import org.apache.commons.lang3.ArrayUtils
import org.hackerpeers.sqli.analyzer.ISQLInjectionAnalyzer
import org.testng.annotations.DataProvider
import org.testng.annotations.Test

import java.lang.reflect.*
import java.sql.SQLException
import java.sql.Wrapper

import static org.mockito.Mockito.*
import static org.testng.Assert.*

/**
 * @author pldupont@gmail.com
 *         06/06/2016.
 */
public abstract class AbstractDelegatorTest<B extends Wrapper> {
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
        Class clazz = getBasicClass();
        addInterfaces(clazz, result);
        addMethodsFromInterface(Wrapper.class, result);
        return result.iterator();
    }

    @Test(dataProvider = "methods")
    public void testWrappedMethodIsCalled(Object wrappedMethod) throws InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, SQLException, ClassNotFoundException {
        B mockBasicClass = mock(getBasicClass());
        ISQLInjectionAnalyzer mockAnalyzer = mock(ISQLInjectionAnalyzer.class);
        B delegator = getDelegatorInstance(mockAnalyzer, mockBasicClass);
        Object[] params = getMethodParams((Method) wrappedMethod);

        ((Method) wrappedMethod).invoke(delegator, params);

        int invokeCount = 1;
        if (((Method) wrappedMethod).getName().equals("getConnection")) {
            invokeCount = 0;
        }
        ((Method) wrappedMethod).invoke(verify(mockBasicClass, times(invokeCount)), params);
        assertSetterCatchParameters(delegator, ((Method) wrappedMethod).getName(), params);
    }

    private void assertSetterCatchParameters(B delegator, String methodName, Object[] params) {
        if (methodName.startsWith("set") && params.length >= 2) {
            if (((Proxy) delegator).h instanceof PreparedStatementDelegator) {
                PreparedStatementDelegator preparedStatementDelegator = (PreparedStatementDelegator) ((Proxy) delegator).h;
                assertFalse(preparedStatementDelegator.getParameters().isEmpty());

                if (methodName.equals("setNull")) {
                    assertEquals(preparedStatementDelegator.getParameters().get(params[0]), "null, type:${params[1]}");
                } else if (methodName.equals("setBytes")) {
                    assertEquals(preparedStatementDelegator.getParameters().get(params[0]), new String(params[1]));
                } else if (params[1] instanceof Reader) {
                    String expResult = "[${methodName.replaceFirst("set", "")}-Reader]"
                    if (params.length > 2) {
                        expResult += "-${params[2..params.length - 1].join("-")}"
                    }
                    assertEquals(preparedStatementDelegator.getParameters().get(params[0]), expResult);
                } else if (params[1] instanceof InputStream) {
                    String expResult = "[${methodName.replaceFirst("set", "")}-InputStream]"
                    if (params.length > 2) {
                        expResult += "-${params[2..params.length - 1].join("-")}"
                    }
                    assertEquals(preparedStatementDelegator.getParameters().get(params[0]), expResult);
                } else {
                    assertEquals(preparedStatementDelegator.getParameters().get(params[0]), params[1..params.length - 1].join("-"));
                }

            }
        }
    }

    protected abstract B getDelegatorInstance(ISQLInjectionAnalyzer analyzer, B mockBasicClass);

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

    private void addInterfaces(Class<?> clazz, List<Object[]> result) {
        if (clazz != null && clazz != Wrapper.class) {
            addMethodsFromInterface(clazz, result);
            if (ArrayUtils.isNotEmpty(clazz.getInterfaces())) {
                for (Class<?> superClazz : clazz.getInterfaces()) {
                    addInterfaces(superClazz, result);
                }
            }
        }
    }

    private void addMethodsFromInterface(Class<?> clazz, List<Object[]> result) {
        clazz.getDeclaredMethods().each { m -> result.add([m] as Object[]) };
    }
}
