package org.ghotibeaun.json.converters.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Identifies a JSON element mapping. Only required if a Field name is different than the mapped JSON element's key,
 * or if the value should be set or retrieved by a specified method
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface JSONElement {

    /**
     * Specifies the JSON key to use. Only required if the field name is different than the JSON element's key.
     * @return the JSON key mapped to this field. If empty, the Field name will be used, otherwise the key value will be used.
     */
    String key() default "";

    /**
     * Specifies a setter method to use to apply the JSON value to the Class value. 
     * Only required if the JSONValue should not be set directly to the Field
     * By default, the value is empty indicating that the value should be applied to the Field's set() method.
     * If the setterMethod value is set, then the JSONValue is applied to the method. 
     * <p><strong>NOTE:</strong> The method must be public and only accept one parameter, or it will throw a JSONConversionException
     * @return the methodName, or an empty String (default).</p> 
     */
    String setterMethod() default "";

    /**
     * Specifies a getter method to use to retrieve a Class value to be set in a JSON value.
     * Only required if the value should not be retrieved directly from the field.
     * <p><strong>NOTE: <strong>The method must be public and contain no parameters with a return parameter (can't be
     * void), or a JSONConversionException will be thrown</p>
     * @return
     */
    String getterMethod() default "";
}
