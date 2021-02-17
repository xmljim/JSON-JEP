
package org.ghotibeaun.json.jsonpath.converter;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

/**
 * @author Jim Earley
 *
 */
public class StringConverter extends AbstractConverter<String> {

    /**
     *
     */
    public StringConverter() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.jsonpath.converter.AbstractConverter#convert(java.lang.Object)
     */

    @Override
    public String convert(Object data) {
        if (data == null) {
            return null;
        }

        if (isJSONArray(data)) {
            final JSONArray instance = (JSONArray)data;

            if (instance.size() > 0) {
                return instance.getValue(0);
            } else {
                return null;
            }
        } else if (isJSONValue(data)) {
            return ((JSONValue<?>)data).toString();
        } else if (isPrimitive(data)) {
            return data.toString();
        }

        throw new JSONInvalidValueTypeException("Cannot find String value for data: [" + data.getClass().getName() + "]");
    }

}
