package org.ghotibeaun.json.factory;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ghotibeaun.json.JSONValueType;

class JSONDateValueImpl extends AbstractJSONValue<Date> {

    /**
     *
     */
    private static final long serialVersionUID = 3450708256376075217L;

    public JSONDateValueImpl() {
        super();
    }

    public JSONDateValueImpl(Date d) {
        super(d);
    }

    @Override
    public JSONValueType getType() {
        return JSONValueType.DATE;
    }

    @Override
    public boolean isPrimitive() {
        return true;
    }

    @Override
    public boolean isArray() {
        return false;
    }

    @Override
    public String toString() {

        final SimpleDateFormat sdf = new SimpleDateFormat(FactorySettings.getSetting(Setting.DATE_FORMAT));
        return "\"" + sdf.format(getValue()) + "\"";
    }

    @Override
    public String prettyPrint() {
        return toString();
    }

    @Override
    public String prettyPrint(int indent) {
        return toString();
    }
}
