package net.anzix.callcost.rulefile;

import net.anzix.callcost.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author elekma
 */
public class PojoCreator {

    Map<Class, Converter> converters = new HashMap<Class, Converter>();

    Map<String, Converter> customConverters = new HashMap<String, Converter>();

    private Logger logger = LoggerFactory.getLogger(PojoCreator.class);

    public PojoCreator() {
    }

    public void addConverter(Class type, Converter converters) {
        this.converters.put(type, converters);
    }

    public void addConverter(String className, String fieldName, Converter converter) {
        customConverters.put(className + "-" + fieldName, converter);
    }

    public <T> T create(Class<? extends T> type, Map<String, String> parameters) {
        try {
            T object = type.newInstance();

            Map<String, Map<String, String>> collections = new HashMap<String, Map<String, String>>();

            params:
            for (String parameter : parameters.keySet()) {
                if (parameter.contains(".")) {
                    storeValue(collections, parameter, parameters.get(parameter));
                } else {
                    try {
                        if (setSetter(object, parameter, parameters.get(parameter)) ||
                                setField(object, parameter, parameters.get(parameter))) continue params;
                        logger.error("Unknown parameter " + parameter + " in " + type.getSimpleName());
                    } catch (Exception ex) {
                        logger.error("Error on setting parameter " + parameter + " on " + type.getSimpleName() + " " + ex.getClass().getSimpleName() + " " + ex.getMessage(), ex);
                    }
                }
            }
            for (String parameter : collections.keySet()) {
                setCollectionField(object, parameter, collections.get(parameter));
            }
            return object;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }

    }

    private <T> void setCollectionField(T object, String parameter, Map<String, String> parameters) {
        Method addMethod = null;
        String methodName = "add" + parameter.substring(0, 1).toUpperCase() + parameter.substring(1);
        for (Method m : object.getClass().getMethods()) {
            if (m.getName().equals(methodName)) {
                addMethod = m;
            }
        }

        if (addMethod == null) {
            throw new IllegalArgumentException("No such adder " + methodName + " on " + object.getClass());
        }

        Map<String, Map<String, String>> indexedParameters = new TreeMap<String, Map<String, String>>();
        for (String t : parameters.keySet()) {
            String idx = t.substring(0, t.indexOf("."));
            String remain = t.substring(t.indexOf(".") + 1);

            if (!indexedParameters.containsKey(idx)) {
                indexedParameters.put(idx, new TreeMap<String, String>());
            }

            indexedParameters.get(idx).put(remain, parameters.get(t));
        }

        for (String idx : indexedParameters.keySet()) {
            Class type = addMethod.getParameterTypes()[0];
            try {
                Object childObject = create(type, indexedParameters.get(idx));
                addMethod.invoke(object, childObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void storeValue(Map<String, Map<String, String>> collections, String parameter, String s) {
        String key1 = parameter.substring(0, parameter.indexOf('.'));
        String param = parameter.substring(parameter.indexOf('.') + 1);

        if (!collections.containsKey(key1)) {
            collections.put(key1, new TreeMap<String, String>());
        }

        collections.get(key1).put(param, s);
    }

    private boolean setSetter(Object c, String parameter, Object value) {
        try {
            for (Method m : c.getClass().getMethods()) {
                if (m.getName().equals("set" + parameter.substring(0, 1).toUpperCase() + parameter.substring(1))) {
                    m.invoke(c, convert(c.getClass(), parameter, m.getParameterTypes()[0], value));
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private boolean setField(Object c, String parameter, Object value) {
        for (Field field : c.getClass().getDeclaredFields()) {
            if (field.getName().equals(parameter)) {
                field.setAccessible(true);
                try {
                    field.set(c, convert(c.getClass(), field.getName(), field.getType(), value));
                } catch (IllegalAccessException e) {
                    throw new AssertionError(e);
                }
                return true;
            }
        }
        return false;
    }

    public Object convert(Class<?> parentType, String fieldName, Class<?> fieldType, Object value) {
        if (customConverters.containsKey(parentType.getCanonicalName() + "-" + fieldName)) {
            return customConverters.get(parentType.getCanonicalName() + "-" + fieldName).convert((String) value);
        } else if (converters.containsKey(fieldType)) {
            return converters.get(fieldType).convert((String) value);
        } else if (fieldType.equals(Boolean.class)) {
            return Boolean.valueOf((String) value);
        } else if (fieldType.equals(boolean.class)) {
            return Boolean.parseBoolean((String) value);
        } else if (fieldType.equals(Integer.class)) {
            return Integer.valueOf((String) value);
        } else if (fieldType.equals(int.class)) {
            return Integer.parseInt((String) value);
        }
        return value;
    }
}
