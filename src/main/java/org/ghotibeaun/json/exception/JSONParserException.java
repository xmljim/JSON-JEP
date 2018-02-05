/**
 *
 */
package org.ghotibeaun.json.exception;

/**
 * @author jearley
 *
 */
public class JSONParserException extends JSONException {


    private static final long serialVersionUID = 1L;


    public JSONParserException() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 the message
     */
    public JSONParserException(String arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 the exception
     */
    public JSONParserException(Throwable arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param arg0 the message
     * @param arg1 the exception
     */
    public JSONParserException(String arg0, Throwable arg1) {
        super(arg0, arg1);

    }

}
