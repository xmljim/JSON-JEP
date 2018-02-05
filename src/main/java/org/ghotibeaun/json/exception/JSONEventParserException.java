package org.ghotibeaun.json.exception;

public class JSONEventParserException extends JSONParserException {

    private long line;
    private long linePos;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public JSONEventParserException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public JSONEventParserException(String message, Throwable t) {
        super(message, t);
        // TODO Auto-generated constructor stub
    }

    public JSONEventParserException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public JSONEventParserException(Throwable t) {
        super(t);
        // TODO Auto-generated constructor stub
    }

    public JSONEventParserException(long line, long linePos, String message) {
        super(message + " [at line: " + line + "; col: " + linePos +"]");
        this.line = line;
        this.linePos = linePos;
    }
    
    public JSONEventParserException(long line, long linePos, String message, Throwable t) {
        super(message + " [at line: " + line + "; col: " + linePos +"]", t);
    }

    public long getLineNumber() {
        return line;
    }

    public long getLinePosition() {
        return linePos;
    }


    
    
    
}
