package org.ghotibeaun.json.marshalling;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ FIELD, METHOD })
@Inherited
public @interface JSONMapping {
    /**
     * The JSONObject key value
     * @return (Required) the key value
     */
    String key();

    String setterMethod() default "";

}
