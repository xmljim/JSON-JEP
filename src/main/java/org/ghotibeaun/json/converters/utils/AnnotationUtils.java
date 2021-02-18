package org.ghotibeaun.json.converters.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;

import org.ghotibeaun.json.converters.annotation.ClassValueConverter;
import org.ghotibeaun.json.converters.annotation.JSONElement;
import org.ghotibeaun.json.converters.annotation.JSONIgnore;
import org.ghotibeaun.json.converters.annotation.JSONValueConverter;
import org.ghotibeaun.json.converters.annotation.TargetClass;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;

public class AnnotationUtils {

    private AnnotationUtils() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Find and return an Optional containing an Annotation
     * @param <T> The annotation type
     * @param annotationClass the annotation class
     * @param classMember The class member (either a Field, Method or Class)
     * @return an Optional containing the Annotation
     */
    public static <T extends Annotation> Optional<T> findAnnotation(Class<? extends T> annotationClass, AnnotatedElement classMember) {
        T annotation = null;

        if (classMember.isAnnotationPresent(annotationClass)) {
            annotation = classMember.getAnnotation(annotationClass);
        }

        return Optional.ofNullable(annotation);
    }

    public static Optional<ValueConverter<?>> getJSONValueConverter(AnnotatedElement element) throws JSONConversionException {
        ValueConverter<?> convert = null;
        final Optional<JSONValueConverter> valueConverter = findAnnotation(JSONValueConverter.class, element);

        if (valueConverter.isPresent()) {
            convert = createValueConverter(valueConverter.get().converter(), valueConverter.get().args());
        } 

        return Optional.ofNullable(convert);
    }

    public static Optional<ValueConverter<?>> getClassValueConverter(AnnotatedElement element) throws JSONConversionException {
        ValueConverter<?> convert = null;
        final Optional<ClassValueConverter> valueConverter = findAnnotation(ClassValueConverter.class, element);

        if (valueConverter.isPresent()) {
            convert = createValueConverter(valueConverter.get().converter(), valueConverter.get().args());
        } 

        return Optional.ofNullable(convert);
    }

    private static ValueConverter<?> createValueConverter(Class<? extends ValueConverter<?>> converterClass, String... args) throws JSONConversionException {
        return ClassUtils.createValueConverter(converterClass, args);
    }

    public static boolean findJSONIgnore(AnnotatedElement element) {
        return findAnnotation(JSONIgnore.class, element).map(a -> a.value()).orElse(false);
    }

    public static Optional<String> findJSONElementKey(AnnotatedElement element) {
        return findAnnotation(JSONElement.class, element).filter(a -> a.key() != "").map(a -> a.key());
    }

    public static Optional<String> findJSONElementSetter(AnnotatedElement element) {
        return findAnnotation(JSONElement.class, element)
                .filter(a -> a.setterMethod().length() != 0)
                .map(a -> a.setterMethod());

        //return Optional.ofNullable(val);
    }

    public static Optional<String> findJSONElementGetter(AnnotatedElement element) {
        return findAnnotation(JSONElement.class, element).filter(a -> a.getterMethod() != "").map(a -> a.getterMethod());
    }

    public static Optional<Class<?>> findTargetClass(AnnotatedElement element) {
        return findAnnotation(TargetClass.class, element).map(a -> a.value());
    }




}
