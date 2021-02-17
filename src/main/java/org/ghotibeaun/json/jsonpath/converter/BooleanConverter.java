package org.ghotibeaun.json.jsonpath.converter;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.exception.JSONInvalidValueTypeException;

/**
 * @author Jim Earley
 *
 */
public class BooleanConverter extends AbstractConverter<Boolean> {

    /**
     *
     */
    public BooleanConverter() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.jsonpath.converter.AbstractConverter#convert(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Boolean convert(Object data) {
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
            try {
                return ((JSONValue<Boolean>)data).getValue();
            } catch (final Exception e) {
                throw new JSONInvalidValueTypeException(e);
            }
        } else if (isPrimitive(data)) {
            return Boolean.valueOf(data.toString());
        }

        throw new JSONInvalidValueTypeException("Invalid Boolean Type: " + data.toString());
    }

}
