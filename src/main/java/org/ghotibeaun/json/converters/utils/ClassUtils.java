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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import org.ghotibeaun.json.converters.Converter;
import org.ghotibeaun.json.converters.options.ConverterOption;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public final class ClassUtils {

    /**
     * Create a new class instance
     * @param <T> the class type
     * @param classToCreate The class
     * @return a new instance of the class;
     * @throws JSONConversionException
     */
    public static <T> T createInstance(Class<T> classToCreate) throws JSONConversionException {
        final Optional<Constructor<?>> constructor = getDefaultConstructor(classToCreate);

        if (constructor.isPresent()) {
            try {
                constructor.get().setAccessible(true);
                @SuppressWarnings("unchecked")
                final T instance = (T) constructor.get().newInstance();
                return instance;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                throw new JSONConversionException("Error creating instance: " + classToCreate.getName(), e);
            }
        } else {
            throw new JSONConversionException("No default constructor found for class: " + classToCreate.getName());
        }
    }

    private static <T> Optional<Constructor<?>> getDefaultConstructor(Class<T> classToCreate) throws JSONConversionException {
        return Arrays.stream(classToCreate.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0).findFirst();
    }

    public static <T> T createInstance(Class<T> classToCreate, Class<?>[] argTypes, Object...args) throws JSONConversionException {

        try {
            Constructor<T> constructor;
            constructor = classToCreate.getConstructor(argTypes);
            final T instance = constructor.newInstance(args);
            return instance;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONConversionException("Error creating class: " + classToCreate + ": " + e.getMessage(), e);
        }
    }

    public static ValueConverter<?> createValueConverter(Class<?> classToCreate, String... args) throws JSONConversionException {
        try {
            Constructor<?> con;
            con = classToCreate.getConstructor(String[].class);
            con.setAccessible(true);
            return (ValueConverter<?>) con.newInstance((Object)args);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new JSONConversionException("Error creating class: " + classToCreate + ": " + e.getMessage(), e);
        }

    }

    public static Converter createConverter(Class<?> converterClass, ConverterOption<?>...option) throws JSONConversionException {
        try {
            final Constructor<?> constructor = converterClass.getConstructor(ConverterOption[].class);
            constructor.setAccessible(true);
            return (Converter)constructor.newInstance((Object)option);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
            throw new JSONConversionException("Error creating class: " + converterClass + ": " + e.getMessage(), e);
        }
    }

    private ClassUtils() {
        // TODO Auto-generated constructor stub
    }

}
