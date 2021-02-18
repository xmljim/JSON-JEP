package org.ghotibeaun.json.converters.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that a JSON element should be ignored.
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface JSONIgnore {
    /**
     * Specifies whether the element should be ignored. By default it's set to true. If set to false, 
     * then the element will be used. 
     * @return true (default) if element should be ignored. If overridden and set to false, the element will be used. 
     */
    boolean value() default true;
}
