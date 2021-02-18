package org.ghotibeaun.json.converters.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.ghotibeaun.json.converters.valueconverter.ValueConverter;

@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface ClassValueConverter {

    /**
     * Sets the ValueConverter class to be applied to the JSONValue
     * @return
     */
    Class<? extends ValueConverter<?>> converter();

    String[] args();
}
