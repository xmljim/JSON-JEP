
package org.ghotibeaun.json.jsonpath.converter;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONValue;
import org.ghotibeaun.json.factory.NodeFactory;

/**
 * @author Jim Earley
 *
 */
public class JSONArrayConverter extends AbstractConverter<JSONArray> {

    /* (non-Javadoc)
     * @see org.ghotibeaun.json.jsonpath.converter.AbstractConverter#convert(java.lang.Object)
     */
    @Override
    public JSONArray convert(Object data) {
        if (data == null) {
            return null;
        }

        if (isJSONArray(data)) {
            final JSONArray instance = (JSONArray)data;

            final JSONArray newInstance = NodeFactory.newJSONArray();
            if (instance.size() > 0) {
                for (final JSONValue<?> val : instance.getValues()) {
                    newInstance.add(val);
                }
            }
        }

        return null;
    }

}
