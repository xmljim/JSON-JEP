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
