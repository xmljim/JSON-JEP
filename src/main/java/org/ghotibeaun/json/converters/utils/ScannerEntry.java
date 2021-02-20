/*
 *
 * # Released under MIT License
 *
 * Copyright (c) 2016-2021 Jim Earley.
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */
package org.ghotibeaun.json.converters.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.converters.annotation.JSONElement;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.NodeFactory;

/**
 * Contains mapping data for converting to and from JSON and Class instances. 
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class ScannerEntry {

    private final Class<?> containerClass;
    private final boolean isAbstract;
    private final String jsonKey;
    private final String fieldName;
    private final Type fieldType;
    private final boolean accessible;
    private final boolean ignore;
    private final GetterMethodReference getterReference;
    private final SetterMethodReference setterReference;
    private final ValueConverter<?> getterValueConverter;
    private final ValueConverter<?> setterValueConverter;
    private final Class<?> targetClass;

    /**
     * Initializes the entry
     * @param containerClass the Class containing the Field 
     * @param theField the field
     */
    public ScannerEntry(Class<?> containerClass, Field theField)  {
        this.containerClass = containerClass;
        fieldName = theField.getName();
        isAbstract = Modifier.isAbstract(containerClass.getModifiers());
        jsonKey = AnnotationUtils.findJSONElementKey(theField).orElse(theField.getName());
        fieldType = theField.getGenericType();
        accessible = theField.trySetAccessible();
        ignore = AnnotationUtils.findJSONIgnore(theField);
        setterReference = (SetterMethodReference) findMethod(containerClass, theField, true);
        getterReference = (GetterMethodReference) findMethod(containerClass, theField, false);
        getterValueConverter = AnnotationUtils.getClassValueConverter(theField).orElse(null);
        setterValueConverter = AnnotationUtils.getJSONValueConverter(theField).orElse(null);
        targetClass = AnnotationUtils.findTargetClass(theField).orElse(evalTargetClass(theField));
    }

    /**
     * Some reflection magic to handle Generic types
     * @param field the field
     * @return the underlying field type
     */
    private Class<?> evalTargetClass(Field field) {
        if (field.getGenericType() instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType)field.getGenericType()).getActualTypeArguments()[0];
        }
        return field.getType();
    }

    /**
     * Creates a setter Method for invoking a set value
     * @return a setter Method
     * @throws JSONConversionException
     */
    public Optional<Method> getSetterMethod() throws JSONConversionException {
        Method method = null;

        if (getSetterReference() != null) {
            final SetterMethodReference ref = getSetterReference();

            try {
                method = getContainerClass().getMethod(ref.getName(), ref.getParameterClass());
            } catch (NoSuchMethodException | SecurityException e) {
                throw new JSONConversionException("Error fetching setter method " + ref.getName(), e);
            }
        }

        return Optional.ofNullable(method);
    }

    public Optional<Method> getGetterMethod() throws JSONConversionException {
        Method method = null;

        if (getGetterReference() != null) {
            final GetterMethodReference ref = getGetterReference();

            try {
                method = getContainerClass().getMethod(ref.getName());
            } catch (NoSuchMethodException | SecurityException e) {
                throw new JSONConversionException("Error fetching setter method " + ref.getName(), e);
            }
        }

        return Optional.ofNullable(method);
    }

    /**
     * Utility to build a setterMethod name from a string
     * @param key the key (typically a json key, but could be a field name)
     * @return a setter method name
     */
    private String getMappedMethodName(String key, boolean setter) {
        final String[] tokens = key.split("-");

        final StringBuilder builder = new StringBuilder();
        if (setter) {
            builder.append("set");
        } else {
            builder.append("get");
        }

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

    /**
     * Locate a method to set or get a value. Method names are determined either by explicitly setting the 
     * {@link JSONElement#getterMethod()} or {@link JSONElement#setterMethod()} values, or by implying the setter
     * or getter name from the field name, i.e., if a field is named <code>fooBar</code>, the setter name would be
     * <code>setFooBar</code> and the getter name would be <code>getFooBar</code>. Once the method name is determined,
     * it scans all methods matching that name, return type and parameter type signatures appropriate for setters 
     * and getters. The returnType for getters and parameter type for setters is mapped to the field's declared type.
     * @param clazz The current class
     * @param field the current field within the class
     * @param setter flag to indicate whether to search for setter or getter methods; if set to true, it searches for
     * setters; false searches for getters
     * @return An Optional of a Method that optionally contains a Method reference.
     */
    private MethodReference findMethod(Class<?> clazz, Field field, boolean setter)  {

        final String methodName = AnnotationUtils.findJSONElementSetter(field).orElse(getMappedMethodName(field.getName(), setter));
        final int paramCount = setter ? 1 : 0;
        final Type returnType = setter ? Void.TYPE : field.getType();//field.getGenericType() != null ? (ParameterizedType)field.getGenericType() : field.getType();
        final List<Method> methods = findMethods(clazz, methodName, paramCount, returnType);


        Optional<Method> m = Optional.empty();

        if (setter) {
            m = methods.stream().filter(method -> getMethodParameterType(method).equals(getFieldType(field)))
                    .findFirst();
        } else if (methods.size() == 0 && (field.getType().equals(boolean.class) || field.getType().equals(Boolean.class))) {
            final String isMethodName = methodName.replaceFirst("get", "is");
            m = findMethods(clazz, isMethodName, paramCount, returnType).stream().findFirst();
        } else {
            m = methods.stream().findFirst();
        }

        if (m.isPresent()) {
            if (setter) {
                return new SetterMethodReference(m.get().getName(), 
                        m.get().trySetAccessible(), 
                        getMethodParameterType(m.get()),
                        Modifier.isPublic(m.get().getModifiers()));
            } else {
                return new GetterMethodReference(m.get().getName(), 
                        m.get().trySetAccessible(), 
                        m.get().getGenericReturnType(),
                        Modifier.isPublic(m.get().getModifiers()));
            }
        } else {
            return null;
        }
    }

    private List<Method> findMethods(Class<?> clazz, String methodName, int paramCount, Type returnType) {
        final List<Method> methods = Arrays.stream(clazz.getMethods())
                .filter(method -> method.getName().equals(methodName))
                .filter(method -> method.getParameterCount() == paramCount)
                .filter(method -> method.getReturnType().equals(returnType))
                .collect(Collectors.toList());

        return methods;
    }

    /**
     * Reflection magic to determine the Field's type 
     * @param field the field to evaluate
     * @return the Field type
     */
    private Type getFieldType(Field field) {
        final Type paramType = field.getGenericType();
        final Type baseType = field.getType();

        if (paramType != null) { 
            return paramType;
        } else {
            return baseType;
        }
    }

    private Type getMethodParameterType(Method m) {
        final Type[] baseParam = m.getParameterTypes();
        final Type[] genericParam = m.getGenericParameterTypes();

        if (genericParam.length == 1) {
            return genericParam[0];
        } else {
            return baseParam[0];
        }
    }

    /**
     * The class that declares the current field
     * @return
     */
    public Class<?> getContainerClass() {
        return containerClass;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Type getFieldType() {
        return fieldType;
    }

    public boolean isAccessible() {
        return accessible;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public GetterMethodReference getGetterReference() {
        return getterReference;
    }

    public SetterMethodReference getSetterReference() {
        return setterReference;
    }

    public ValueConverter<?> getGetterValueConverter() {
        return getterValueConverter;
    }


    public ValueConverter<?> getSetterValueConverter() {
        return setterValueConverter;
    }


    public Class<?> getTargetClass() {
        return targetClass;
    }


    public JSONObject toJSON() {
        final JSONObject entry = NodeFactory.newJSONObject();
        entry.put("containerClass", containerClass.getName());
        entry.put("fieldName", fieldName);
        entry.put("accessible", accessible);
        entry.put("jsonKey", jsonKey);
        entry.put("fieldType", fieldType.toString());
        entry.put("isAbstract", isAbstract);
        entry.put("ignore", ignore);
        entry.put("getterReference", getterReference != null ? getterReference.getJSON() : null);
        entry.put("setterReference", setterReference != null ? setterReference.getJSON() : null);
        entry.put("getterValueConverter", getterValueConverter != null ? getterValueConverter.getClass().toString() : null);
        entry.put("setterValueConverter", setterValueConverter != null ? setterValueConverter.getClass().toString() : null);
        entry.put("targetClass", targetClass.toString());
        return entry;
    }

    @Override
    public String toString() {
        return toJSON().prettyPrint();
    }

}
