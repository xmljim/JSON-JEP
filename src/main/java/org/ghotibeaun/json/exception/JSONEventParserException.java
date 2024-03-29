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
