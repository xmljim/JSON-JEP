package org.ghotibeaun.json.converters.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.ghotibeaun.json.converters.valueconverter.ValueConverter;

/**
 * Specifies a {@link ValueConverter} to be applied to a JSONValue before setting the value on the target class.
 * These can be used for situations where underlying Class does not have a default, zero-parameter constructor, for
 * converting a primitive value to a specific class (e.g., a Long JSON value to a LocalDate or LocalDateTime), or 
 * for formatting values from JSON before setting them in the target class (e.g., formatting a double with commas and 
 * decimal places)
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface JSONValueConverter {

    /**
     * Sets the ValueConverter class to be applied to the JSONValue
     * @return
     */
    Class<? extends ValueConverter<?>> converter();

    String[] args();
}
