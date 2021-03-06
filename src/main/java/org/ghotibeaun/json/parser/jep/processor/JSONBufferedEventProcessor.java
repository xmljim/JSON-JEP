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
package org.ghotibeaun.json.parser.jep.processor;

import static org.ghotibeaun.json.parser.jep.processor.ActionConstants.TOKEN_STATE_BOOLEAN;
import static org.ghotibeaun.json.parser.jep.processor.ActionConstants.TOKEN_STATE_EMPTY;
import static org.ghotibeaun.json.parser.jep.processor.ActionConstants.TOKEN_STATE_NULL;
import static org.ghotibeaun.json.parser.jep.processor.ActionConstants.TOKEN_STATE_NUMBER;
import static org.ghotibeaun.json.parser.jep.processor.ActionConstants.TOKEN_STATE_STRING;
import static org.ghotibeaun.json.util.ByteConstants.BACKSLASH;
import static org.ghotibeaun.json.util.ByteConstants.COLON;
import static org.ghotibeaun.json.util.ByteConstants.COMMA;
import static org.ghotibeaun.json.util.ByteConstants.CR;
import static org.ghotibeaun.json.util.ByteConstants.DECIMAL;
import static org.ghotibeaun.json.util.ByteConstants.E;
import static org.ghotibeaun.json.util.ByteConstants.END_ARRAY;
import static org.ghotibeaun.json.util.ByteConstants.END_MAP;
import static org.ghotibeaun.json.util.ByteConstants.LF;
import static org.ghotibeaun.json.util.ByteConstants.MINUS;
import static org.ghotibeaun.json.util.ByteConstants.NINE;
import static org.ghotibeaun.json.util.ByteConstants.PLUS;
import static org.ghotibeaun.json.util.ByteConstants.QUOTE;
import static org.ghotibeaun.json.util.ByteConstants.SOLIDUS;
import static org.ghotibeaun.json.util.ByteConstants.SPACE;
import static org.ghotibeaun.json.util.ByteConstants.START_ARRAY;
import static org.ghotibeaun.json.util.ByteConstants.START_MAP;
import static org.ghotibeaun.json.util.ByteConstants.TAB;
import static org.ghotibeaun.json.util.ByteConstants.ZERO;
import static org.ghotibeaun.json.util.ByteConstants.a;
import static org.ghotibeaun.json.util.ByteConstants.b;
import static org.ghotibeaun.json.util.ByteConstants.e;
import static org.ghotibeaun.json.util.ByteConstants.f;
import static org.ghotibeaun.json.util.ByteConstants.l;
import static org.ghotibeaun.json.util.ByteConstants.n;
import static org.ghotibeaun.json.util.ByteConstants.r;
import static org.ghotibeaun.json.util.ByteConstants.s;
import static org.ghotibeaun.json.util.ByteConstants.t;
import static org.ghotibeaun.json.util.ByteConstants.u;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventhandler.JSONEventHandler;
import org.ghotibeaun.json.util.ByteRange;
import org.ghotibeaun.json.util.ByteSequence;
import org.ghotibeaun.json.util.ResizableByteBuffer;

/**
 * An <a href="http://www.ecma-international.org/publications/files/ECMA-ST/ECMA-404.pdf">ECMA-404</a>-compliant processor.
 * Data is processed at the byte level and passed to an {@linkplain JSONEventHandler} as {@linkplain ByteBuffer} values
 * so that it can be interpreted with the appropriate character encoding.
 * <p>
 *  Given that some JSON serializers are lax with some escaping, you can set the {@linkplain ParserSettings#setUseStrict(boolean)} to
 *  <code>false</code> if you want to allow for some characters to pass through without being escaped.
 * </p>
 * @author Jim Earley
 *
 */
class JSONBufferedEventProcessor extends BaseEventProcessor {
    /**
     * Number byte range (0-9, e, E, +, -)
     */
    private final ByteRange numberRange = ByteRange.startWith(ZERO, NINE).andAdd(e).andAdd(E).andAdd(PLUS).andAdd(MINUS);
    /**
     * Whitespace byte values: {space},(\t),(\r),(\n)
     */
    private final ByteRange whitespaceRange = ByteRange.startWith(TAB).andAdd(SPACE).andAdd(CR).andAdd(LF);
    /**
     * Hexidecimal character byte values (0-9, Aa-Ff)
     */
    //private final ByteRange hexRange = ByteRange.startWith(ZERO, NINE).andAddFrom(A, F).andAddFrom(a, f);
    /**
     * Boolean character byte values. Both share an 'e' byte.
     */
    private final ByteRange booleanRange = ByteRange.startWith(new byte[] {t, r, u, f, a, l, s, e} );
    private final ByteRange delimiterRange = ByteRange.startWith(COMMA).andAdd(COLON);
    private final ByteRange containerRange = ByteRange.startWith(new byte[]{START_ARRAY, START_MAP, END_ARRAY, END_MAP});
    private final ByteRange escapeRange = ByteRange.startWith(new byte[]{QUOTE, BACKSLASH, SOLIDUS, b, f, n, r, t});
    private final ByteRange nullRange = ByteRange.startWith(n).andAdd(u).andAdd(l);

    private final ByteSequence nullSequence = ByteSequence.startsWith(n).followedBy(u).followedBy(l).followedBy(l);
    private final ByteSequence trueSequence = ByteSequence.startsWith(t).followedBy(r).followedBy(u).followedBy(e);
    private final ByteSequence falseSequence = ByteSequence.startsWith(f).followedBy(a).followedBy(l).followedBy(s).followedBy(e);

    private BufferedInputStream bis;
    private final  ResizableByteBuffer token = new ResizableByteBuffer();

    private int tokenState;
    private int lineNumber;
    private int column;
    private boolean documentStarted = false;
    private boolean escapeFlag = false;



    public JSONBufferedEventProcessor() {

    }

    @Override
    public void start(InputStream stream) throws JSONEventParserException {
        incrementLine();
        incrementColumn();
        try {
            if (stream instanceof BufferedInputStream) {
                bis = (BufferedInputStream) stream;
            } else {
                bis = new BufferedInputStream(stream);
            }

            ByteBuffer block = readBlock();

            while (block != null) {
                processBlock(block);
                block = readBlock();
            }

        } catch (final IOException e) {
            throw new JSONEventParserException(e);
        }
    }


    private void notifyStringTokenStart() {
        setTokenState(TOKEN_STATE_STRING);
        fireStringStartEvent(lineNumber, column);
    }

    private void notifyStringTokenEnd(ByteBuffer tokenValue) {
        resetToken();
        fireStringEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyBooleanTokenStart() {
        setTokenState(TOKEN_STATE_BOOLEAN);
        fireBooleanStartEvent(lineNumber, column);
    }

    private void notifyBooleanTokenEnd(ByteBuffer tokenValue) {
        validateBoolean(tokenValue);
        resetToken();
        fireBooleanEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyNumberTokenStart() {
        setTokenState(TOKEN_STATE_NUMBER);
        fireNumberStartEvent(lineNumber, column);
    }

    private void notifyNumberTokenEnd(ByteBuffer tokenValue) {
        resetToken();
        fireNumberEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyNullTokenStart() {
        setTokenState(TOKEN_STATE_NULL);
        fireNullStartEvent();
    }

    private void notifyNullTokenEnd(ByteBuffer tokenValue) {
        validateNull(tokenValue);
        resetToken();
        fireNullEndEvent(tokenValue, lineNumber, column);
    }

    private void notifyEntityEnd() {
        resetToken();
        fireEntityEndEvent(lineNumber, column);
    }

    private void notifyKeyEnd() {
        resetToken();
        fireKeyEndEvent(lineNumber, column);
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getColumn() {
        return column;
    }

    private void processBlock(ByteBuffer block) throws IOException {
        if (block != null) {
            while (block.hasRemaining()) {
                final byte b = block.get();
                handleByte(b);
            }

            //processBlock(readBlock());
        } else {
            fireDocumentEndEvent(lineNumber, column); //TODO: reached the end;
        }
    }

    private void handleByte(byte b) {
        if (isWhitespace(b)) {
            handleWhitespace(b);

        } else if (isNumeric(b)) {
            handleNumeric(b);

        } else if (isBoolean(b)) {
            handleBoolean(b);

        } else if (isNull(b)) {
            handleNull(b);

        } else if (isDelimiter(b)) {
            handleDelimiter(b);

        } else if (isContainer(b)) {
            handleContainer(b);

        } else {
            handleString(b);
        }


    }

    private boolean isWhitespace(byte b) {
        return hasByte(b, whitespaceRange);
    }

    private void handleWhitespace(byte b) {
        switch (b) {
            case SPACE:
                if (inStringToken()) {
                    appendToken(b);
                } else if (inBooleanToken() || inNumberToken() || inNullToken()) {
                    notifyEndToken();
                }
                incrementColumn();
                break;
            case LF:
                if (inToken() && !inStringToken()) {
                    notifyEndToken();
                }

                incrementLine();
                break;
            case TAB | CR:
                incrementColumn();
                //no-op, extraneous
                //CR will be followed by LF, so line increment will dealt with there.
        }
    }

    private boolean isNumeric(byte byt) {
        return hasByte(byt, numberRange) && (!inToken() || inNumberToken());
    }

    private void handleNumeric(byte byt) {
        if (!inToken()) {
            /*            if (ByteRange.startWith(e).andAdd(E).andAdd(PLUS).contains(byt)
                    || ((byt == ZERO) && getProcessorSettings().getUseStrict())) {
                throwError("Per ECMA-404: Number values cannot start with " + (char)byt);
            }*/

            notifyNumberTokenStart();
            appendToken(byt);
            return;
        }

        if (inNumberToken()) {
            if (ByteRange.startWith(e).andAdd(E).contains(byt) && hasByte(b, token.toByteBuffer())) {
                throwError("Number exception, cannot have two exponent symbols in the same number");
            }

            if (byt == DECIMAL && hasByte(byt, token.toByteBuffer())) {
                throwError("Number exception: cannot have two decimal symbols in the same number");
            }

            appendToken(byt);
        }
    }

    private boolean isBoolean(byte byt) {
        // final boolean isBool = (hasByte(byt, booleanRange) && ((!inToken() && ((byt == t) || (byt == f)) )|| inBooleanToken()));
        return hasByte(byt, booleanRange) && (!inToken() && (byt == t || byt == f)|| inBooleanToken());
    }

    private void handleBoolean(byte byt) {
        if (!inToken()) {
            notifyBooleanTokenStart();
        }

        appendToken(byt);

    }

    private boolean isNull(byte byt) {
        return hasByte(byt, nullRange) && (!inToken() && byt == n|| inNullToken());
    }

    private void handleNull(byte byt) {
        if (!inToken()) {
            notifyNullTokenStart();
        }

        appendToken(byt);
    }

    private boolean isDelimiter(byte byt) {
        return hasByte(byt, delimiterRange) && !inStringToken();
    }

    private void handleDelimiter(byte byt) {
        notifyEndToken();

        if (byt == COMMA) {
            notifyEntityEnd();
        } else if (byt == COLON) {
            notifyKeyEnd();
        }

        incrementColumn();
    }

    private boolean isContainer(byte byt) {
        return hasByte(byt, containerRange);
    }

    private void handleContainer(byte byt) {

        if (inStringToken()) {
            appendToken(byt);
        } else {
            if (inNumberToken() || inBooleanToken() || inNullToken()) {
                notifyEndToken();
            }

            switch (byt) {
                case START_ARRAY:
                    if (!documentStarted) {
                        fireDocumentStartEvent(ByteBuffer.allocate(1).put(byt), lineNumber, column);
                        documentStarted = true;
                        resetToken();
                    } else {
                        fireArrayStartEvent(lineNumber, column);
                    }
                    break;
                case END_ARRAY:
                    fireArrayEndEvent(lineNumber, column);
                    break;
                case START_MAP:
                    if (!documentStarted) {
                        fireDocumentStartEvent(ByteBuffer.allocate(1).put(byt), lineNumber, column);
                        documentStarted = true;
                        resetToken();
                    } else {
                        fireMapStartEvent(lineNumber, column);
                    }
                    break;
                case END_MAP:
                    fireMapEndEvent(lineNumber, column);
            }

            incrementColumn();

        }
    }

    private boolean isSpecialCharacter(byte byt) {
        return inStringToken() && escapeRange.contains(byt);
    }

    private void handleSpecialCharacters(byte byt) {
        if (escapeFlag) {
            appendToken(byt);
            escapeFlag = false;
        } else if (byt == BACKSLASH) {
            appendToken(byt);
            escapeFlag = true;
        } else if (byt == QUOTE) {
            incrementColumn();
            notifyStringTokenEnd(token.toByteBuffer());
        } else if (byt == SOLIDUS) {
            if (getProcessorSettings().getUseStrict() ) {
                throwError("Per ECMA-404 specification, solidus ('/') must be escaped");
            } else {
                appendToken(byt);
            }
        } else {
            appendToken(byt);
        }
    }



    private void handleString(byte byt) {
        if (!inToken()) {
            if (byt == QUOTE) {
                notifyStringTokenStart();
                incrementColumn();
                return;
            } else {
                throwError("Unexpected character found outside of String value " + (char)byt);
            }
        }

        if (isSpecialCharacter(byt) || escapeFlag) {
            handleSpecialCharacters(byt);
        } else {
            appendToken(byt);
        }

    }

    private void notifyEndToken() {
        switch (tokenState) {
            case TOKEN_STATE_BOOLEAN:
                notifyBooleanTokenEnd(token.toByteBuffer());
                break;
            case TOKEN_STATE_NULL:
                notifyNullTokenEnd(token.toByteBuffer());
                break;
            case TOKEN_STATE_NUMBER:
                notifyNumberTokenEnd(token.toByteBuffer());
                break;
            case TOKEN_STATE_STRING:
                notifyStringTokenEnd(token.toByteBuffer());
                break;
        }

        resetToken();
    }

    private void resetToken() {
        token.clear();
        setTokenState(TOKEN_STATE_EMPTY);
    }

    private boolean hasByte(byte byt, ByteRange range) {
        return range.contains(byt);
    }

    private boolean hasByte(byte byt, ByteBuffer buffer) {
        return ByteRange.startWith(buffer.array()).contains(byt);
    }

    private void appendToken(byte byt) {
        token.add(byt);
        incrementColumn();
    }

    private void incrementColumn() {
        column++;
    }

    private void incrementLine() {
        lineNumber++;
        column = 0;
    }

    private ByteBuffer readBlock() throws IOException {
        final byte[] byteBuffer = new byte[getProcessorSettings().getBlockSizeBytes()];
        final int readResult = getStream().read(byteBuffer);

        ByteBuffer buffer = null;

        if (readResult != -1) {
            //read the byte array into a ByteBuffer for better handling of the array
            //and only allocate what was read
            buffer = ByteBuffer.wrap(byteBuffer, 0, readResult);
        }

        return buffer;
    }

    private InputStream getStream() {
        return bis;
    }

    private boolean inStringToken() {
        return tokenState == TOKEN_STATE_STRING;
    }

    private boolean inBooleanToken() {
        return tokenState == TOKEN_STATE_BOOLEAN;
    }

    private boolean inNumberToken() {
        return tokenState == TOKEN_STATE_NUMBER;
    }

    private boolean inNullToken() {
        return tokenState == TOKEN_STATE_NULL;
    }

    private boolean inToken() {
        return tokenState != TOKEN_STATE_EMPTY;
    }

    private void setTokenState(int tokenState) {
        this.tokenState = tokenState;
    }

    private void validateBoolean(ByteBuffer booleanData)  {
        ByteSequence sequenceToUse = null;
        if (booleanData.array()[0] == t) {
            sequenceToUse = trueSequence;
        } else if (booleanData.array()[0] == f) {
            sequenceToUse = falseSequence;
        }

        if (sequenceToUse == null) {
            throwError( "Unexpected boolean value: expecting either " + trueSequence
                    + " or " + falseSequence + ", but found " + ByteSequence.withStartingSequence(booleanData.array()));
        }

        final boolean result = sequenceToUse.matches(booleanData.array());

        if (!result) {
            throwError("Unexpected boolean value: expected " + sequenceToUse +
                    " but found " + ByteSequence.withStartingSequence(booleanData.array()));
        }
    }

    private void validateNull(ByteBuffer nullData) {
        if (!nullSequence.matches(nullData.array())) {
            throwError("Unexpected null value: expected " + nullSequence
                    + " but found " + ByteSequence.withStartingSequence(nullData.array()));
        }
    }

    private void throwError(String message) {
        throw new JSONEventParserException(getLineNumber(), getColumn(), message);
    }
}
