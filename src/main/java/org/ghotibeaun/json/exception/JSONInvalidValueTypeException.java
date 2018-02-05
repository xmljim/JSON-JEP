package org.ghotibeaun.json.exception;

import java.text.MessageFormat;

import org.ghotibeaun.json.JSONValueType;

public class JSONInvalidValueTypeException extends JSONException {

    private static final String INVALID_TYPE_MESSAGE = "Cannot retrieve value as {0}. Value is set as {1}";

    public static String getMessage(JSONValueType expectedValue, JSONValueType actualValue) {
        return MessageFormat.format(INVALID_TYPE_MESSAGE, expectedValue, actualValue);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JSONInvalidValueTypeException() {
        // TODO Auto-generated constructor stub
    }

    public JSONInvalidValueTypeException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public JSONInvalidValueTypeException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    public JSONInvalidValueTypeException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}
