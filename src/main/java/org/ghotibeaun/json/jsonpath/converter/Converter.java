
package org.ghotibeaun.json.jsonpath.converter;

/**
 * @author Jim Earley
 *
 */
public interface Converter<T> {

    T convert(Object data);
}
