/**
 *
 */
package org.ghotibeaun.json.exception;

/**
 * @author jearley
 *
 */
public class JSONException extends RuntimeException {


    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public JSONException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 the message
     */
    public JSONException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 the throwable
     */
    public JSONException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 String message
     * @param arg1 the Exception
     */
    public JSONException(String arg0, Throwable arg1) {
        super(arg0, arg1);
        // TODO Auto-generated constructor stub
    }

}
