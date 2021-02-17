package org.ghotibeaun.json.converters.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Specifies a concrete target class to apply to or retrieve from a field or method.
 * This is typically used for converting a JSONArray of JSONObjects, or to specify
 * a mapping of a specific JSON key to a JSONObject/Class mapping.
 * This also supports allowing Fields to be assigned to an Interface or Abstract Class while defining
 * the concrete class.
 * 
 * <h2>IMPORTANT NOTES</h2>
 * <p>The concrete class must include a default, zero-parameter Constructor. Otherwise, a JSONConversionException will
 * be thrown. If you wish to convert to a class that doesn't include a default constructor, then use a
 * {@link JSONValueConverter} annotation instead.</p>
 * <p>Do <em><u>not</u></em> use with either a {@link JSONValueConverter} or {@link ClassValueConverter}</p>
 * @author Jim Earley (jim.earley@fdiinc.com)
 *
 */
@Documented
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface TargetClass {

    /**
     * Specifes the concrete target class to use
     * @return the Class 
     */
    Class<?> value();
}
