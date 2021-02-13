package org.ghotibeaun.json.marshalling;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.exception.JSONMarshallingException;

/**
 * Implementation 
 * @author Jim Earley (jim.earley@fdiinc.com)
 *
 */
class JSONMarshallerImpl extends AbstractJSONMarshaller  {
    private Class<?> targetClass;
    private JSONObject context;

    public JSONMarshallerImpl() {
        targetClass = null;
        context = null;
    }

    private JSONMarshallerImpl(Class<?> targetClass, JSONObject context) {
        this.targetClass = targetClass;
        this.context = context;
    }

    /**
     * Create class instance from JSONObject
     * @param <T> the class type
     * @param targetClass the class type
     * @param context the JSON Object
     * @return The class instance
     * @throws JSONMarshallingException thrown when an error occurs
     */
    @Override
    public <T> T marshall(Class<?> targetClass, JSONObject context) throws JSONMarshallingException {
        this.targetClass = targetClass;
        this.context = context;
        final T instance = create();
        return instance;
    }

    private Object createObject(Class<?> targetClass, JSONObject context) throws JSONMarshallingException {
        final Object instance = getInstance(targetClass);
        processJSONObject(instance, context);
        return instance;
    }

    /**
     * Create the marshalled object
     * @param <T> The class type
     * @return the marshalled object
     * @throws JSONMarshallingException
     */
    private <T> T create() throws JSONMarshallingException {
        final T instance = getInstance(targetClass);
        processJSONObject(instance, context);
        return instance;
    }

    /**
     * Creates the class instance that will be used to apply values from the JSONObject context
     * @param <T> The class type
     * @param typeClass the class
     * @return the class instance
     * @throws JSONMarshallingException
     */
    private <T> T getInstance(Class<?> typeClass) throws JSONMarshallingException {
        final Constructor<T> constructor = getDefaultConstructor(typeClass);

        if (constructor == null) {
            throw new JSONMarshallingException("No default (zero-parameter) constructor was found for class: " + typeClass.getName());
        }

        try {
            final T instance = constructor.newInstance();
            return instance;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new JSONMarshallingException("Could not instantiate class " + typeClass.getName(), e);
        }
    }

    /**
     * Locate the default, zero-parameter constructor
     * @param <T> The class type
     * @param targetClass the class containing the constructor
     * @return The default constructor if found, or null if not found
     */
    private <T> Constructor<T> getDefaultConstructor(Class<?> targetClass) {
        @SuppressWarnings("unchecked")
        final Constructor<T>[] constructors = (Constructor<T>[]) targetClass.getConstructors();

        for (final Constructor<T> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        return null;
    }

    /**
     * populate the class instance with the JSONObject values.
     * @param instance
     * @param context
     * @throws JSONMarshallingException
     */
    private void processJSONObject(Object instance, JSONObject context) throws JSONMarshallingException {
        for (final String key : context.keys()) {
            boolean setByField = false;
            final Optional<Field> optionalField = findFieldByKey(instance.getClass(), key);
            if (optionalField.isPresent()) {
                try {
                    // Field found. Now evaluate if we should ignore it
                    if (!ignoreField(instance.getClass(), optionalField.get())) {
                        setByField = setField(optionalField.get(), instance, context.get(key));
                    } else {
                        //ignore field. Set to true, so that we bypass attempt to locate a method
                        //instead.
                        setByField = true;
                    }
                } catch (final Exception e) {
                    throw new JSONMarshallingException(e);
                }
            }
            // couldn't locate a field, let's try a method
            if (!setByField) {
                final Optional<Method> optionalMethod = findMethod(targetClass, key);
                if (optionalMethod.isPresent()) {
                    final Method invokeMethod = optionalMethod.get();
                    try {
                        invokeMethod.invoke(instance, getValueToSet(invokeMethod, context.get(key)));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        throw new JSONMarshallingException("Error invoking method: " + invokeMethod.toString(), e);
                    }
                } else {
                    //nothing to relate to this key
                    final String message = "Expected find a field named " + key + " or method named " +
                            getMappedMethodName(key) + ", but not found for JSON key [" + key + "]";

                    throw new JSONMarshallingException(message);
                }
            }
        }
    }

    private boolean ignoreField(Class<?> targetClass, Field field) {
        boolean annotatedIgnore = false;
        if (field.isAnnotationPresent(JSONIgnore.class)) {
            annotatedIgnore = field.getAnnotation(JSONIgnore.class).value();
        }

        boolean typeIgnoreFields = false;
        if (targetClass.isAnnotationPresent(JSONMarshalling.class)) {
            final String[] ignoreKeys = targetClass.getAnnotation(JSONMarshalling.class).ignoreKeys();
            final String keyName = getMappedFieldName(field);
            typeIgnoreFields = Arrays.stream(ignoreKeys).anyMatch(val -> val.equals(keyName));
        }

        return annotatedIgnore && !typeIgnoreFields;
    }

    /**
     * Find the first method that matches setter name or linked to the jsonKey via a
     * {@link JSONMapping} annotation
     * @param targetClass the class containing the method to locate
     * @param jsonKey the JSONObject key in context
     * @return an Optional method wrapper
     */
    private Optional<Method> findMethod(Class<?> targetClass, String jsonKey) {
        return Arrays.stream(targetClass.getMethods())
                .filter(method -> methodMatches(method, jsonKey)).findFirst();
    }

    /**
     * Returns whether the method name matches the expected setter name, or has
     * a {@link JSONMapping} annotation with a key that matches the current JSONObject key
     * @param method the current method to evaluate
     * @param jsonKey the json key
     * @return true if either the method name or {@link JSONMapping#key()} value matches the json key
     */
    private boolean methodMatches(Method method, String jsonKey) {
        final boolean nameMatch = method.getName().equals(getMappedMethodName(jsonKey));
        final boolean keyMatch = getMappedMethodKey(method).equals(jsonKey);

        return nameMatch || keyMatch;
    }

    /**
     * Utility to build a setterMethod name from a string
     * @param key the key (typically a json key, but could be a field name)
     * @return a setter method name
     */
    private String getMappedMethodName(String key) {
        final String[] tokens = key.split("-");

        final StringBuilder builder = new StringBuilder();
        builder.append("set");

        for (final String token : tokens) {
            for (int i = 0; i < token.length(); i++) {
                if (i == 0) {
                    builder.append(Character.toTitleCase(token.charAt(i)));
                } else {
                    builder.append(token.charAt(i));
                }
            }
        }

        return builder.toString();
    }

    private String getMappedMethodKey(Method method) {
        String key = "";

        if (method.isAnnotationPresent(JSONMapping.class)) {
            key = method.getAnnotation(JSONMapping.class).key();
        }

        return key;
    }


    /**
     * Find the field associated with JSONObject key. By default it will use a field that has the same name
     * as the JSON key value.  However, if the field is annotated with the {@link JSONMapping} annotation,
     * it will use the {@link JSONMapping#key()} value if it's set (i.e., not an empty string)
     * @param targetClass the class containing the fields to scan
     * @param key the JSONObject key
     * @return An Optional that will contain the field if found, or empty if not found
     */
    private Optional<Field> findFieldByKey(Class<?> targetClass, String key) {
        return Arrays.stream(targetClass.getDeclaredFields())
                .filter(field -> getMappedFieldName(field).equals(key)).findFirst();
    }

    /**
     * Get the field name that will be associated with a JSONObject key value. By default, it will
     * return the field name. However if the field is annotated with the {@link JSONMapping} annotation,
     * it will evaluate if the {@link JSONMapping#key()} is set. If set the name will return the key value,
     * otherwise, it will return the field name
     * @param field the field to evaluate
     * @return the mapped field name
     */
    private String getMappedFieldName(Field field) {
        String name = field.getName();

        if (field.isAnnotationPresent(JSONMapping.class)) {
            final String keyValue = field.getAnnotation(JSONMapping.class).key();
            name = !keyValue.equals("") ? keyValue : name;
        }

        return name;
    }

    /**
     * Attempt to set a Field's value.  If the value is a JSONObject or JSONArray containing a list of JSONObjects,
     * then the field should be annotated with the {@link JSONTargetClass} annotation indicating how to instantiate 
     * each class. The annotation is not required for primitive and null types.
     * @param field the class field
     * @param instance the class instance
     * @param jsonValue the JSONValue. 
     * @return true if the value was set to the field; false otherwise
     * @throws JSONMarshallingException thrown if an error occurs during the setting of the field
     */
    private boolean setField(Field field, Object instance, JSONValue<?> jsonValue) throws JSONMarshallingException {
        boolean setField = false;

        try { 
            field.setAccessible(true);
            /*
            if (jsonValue.isPrimitive()) {
                field.set(instance, jsonValue.getValue());
            } else if (jsonValue.isArray()) {
                field.set(instance, createList(field, (JSONArray)jsonValue.getValue()));
            } else {
                final Object value = JSONMarshaller.marshall(getTargetType(field), (JSONObject)jsonValue.getValue());
                field.set(instance, value);
            }
             */
            field.set(instance, getValueToSet(field, jsonValue));
            setField = true;
        } catch (final Exception e) {
            throw new JSONMarshallingException(e);
        }

        return setField;
    }

    Object getValueToSet(Member member, JSONValue<?> jsonValue) {
        if (jsonValue.isPrimitive()) {
            return jsonValue.getValue();
        } else if (jsonValue.isArray()) {
            return createList(member, (JSONArray)jsonValue.getValue());
        } else {
            return createObject(getTargetType(member), (JSONObject)jsonValue.getValue());
        }
    }

    /**
     * Create a list of values from the JSONarray. Since JSONArrays can contain any JSONNode type, the 
     * {@link JSONTargetClass} annotation should be applied to the field containing the class type for JSONObject 
     * items. 
     * <strong>NOTE: Nested JSONArrays within JSONArrays is not supported. A JSONMarshallingException will be thrown</strong>
     * @param field
     * @param instance
     * @param jsonArrayValue
     * @return
     * @throws JSONMarshallingException
     */
    private List<?> createList(Member member, JSONArray jsonArrayValue) throws JSONMarshallingException {
        final List<?> valueList = createListOfType(getTargetType(member), jsonArrayValue);
        return valueList;
    }

    private Class<?> getTargetType(Member member) {
        if (member instanceof Field) {
            return getTargetType((Field)member);
        } else {
            return getTargetType((Method)member);
        }
    }

    /**
     * Identify the target class for a field. By default it will use {@link Field#getType()}. However,
     * if the {@link JSONTargetClass} annotation is present on the field, it will return that class value.
     * This annotation should be used for a JSONArray of JSONObjects to indicate the class to use to instantiate the
     * object.  It should also be used to specify a concrete implementation class for fields assigned to
     * an interface
     * @param field the current field
     * @return the class type to use for that field
     */
    private Class<?> getTargetType(Field field) {
        Class<?> clazz = field.getType();

        if (field.isAnnotationPresent(JSONTargetClass.class)) {
            clazz = field.getAnnotation(JSONTargetClass.class).value();
        }
        return clazz;
    }

    private Class<?> getTargetType(Method method) throws JSONMarshallingException {
        if (method.getParameterCount() != 1) {
            throw new JSONMarshallingException("Expected to find setter method with one parameter" + method.toString());
        }

        Class<?> targetType = method.getParameters()[0].getType();
        if (method.isAnnotationPresent(JSONTargetClass.class)) {
            targetType = method.getAnnotation(JSONTargetClass.class).value();
        }

        return targetType;
    }

    /**
     * Create and populate a generic list
     * <p><strong>NOTE: Nested arrays are not supported</strong></p>
     * @param <Type> The Class type
     * @param type the type
     * @param array the JSONArray containing the values to populate
     * @return returns the populated list
     * @throws JSONMarshallingException
     */
    @SuppressWarnings({ "unchecked" })
    private <Type> List<Type> createListOfType(Class<Type> type, JSONArray array) throws JSONMarshallingException {
        final List<Type> list = new ArrayList<>();

        for (final JSONValue<?> value : array) {
            if (value.isPrimitive()) {
                list.add((Type)value.getValue());
            } else if (value.isObject()) {
                //final JSONMarshallerImpl impl = new JSONMarshallerImpl();
                final Object t = createObject(type, (JSONObject)value.getValue());
                list.add((Type) t);
            } else {
                //another JSONArray
                throw new JSONMarshallingException("Nested JSONArrays with JSONArrays is not supported");
            }
        }

        return list;
    }

}
