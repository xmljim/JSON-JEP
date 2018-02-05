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
import static org.ghotibeaun.json.util.ByteConstants.EIGHT;
import static org.ghotibeaun.json.util.ByteConstants.END_ARRAY;
import static org.ghotibeaun.json.util.ByteConstants.END_MAP;
import static org.ghotibeaun.json.util.ByteConstants.FIVE;
import static org.ghotibeaun.json.util.ByteConstants.FOUR;
import static org.ghotibeaun.json.util.ByteConstants.LF;
import static org.ghotibeaun.json.util.ByteConstants.MINUS;
import static org.ghotibeaun.json.util.ByteConstants.NINE;
import static org.ghotibeaun.json.util.ByteConstants.NULL;
import static org.ghotibeaun.json.util.ByteConstants.ONE;
import static org.ghotibeaun.json.util.ByteConstants.PLUS;
import static org.ghotibeaun.json.util.ByteConstants.QUOTE;
import static org.ghotibeaun.json.util.ByteConstants.SEVEN;
import static org.ghotibeaun.json.util.ByteConstants.SIX;
import static org.ghotibeaun.json.util.ByteConstants.SPACE;
import static org.ghotibeaun.json.util.ByteConstants.START_ARRAY;
import static org.ghotibeaun.json.util.ByteConstants.START_MAP;
import static org.ghotibeaun.json.util.ByteConstants.TAB;
import static org.ghotibeaun.json.util.ByteConstants.THREE;
import static org.ghotibeaun.json.util.ByteConstants.TWO;
import static org.ghotibeaun.json.util.ByteConstants.ZERO;
import static org.ghotibeaun.json.util.ByteConstants.a;
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
import java.util.ArrayList;

import org.ghotibeaun.json.exception.JSONEventParserException;

class JSONByteEventProcessor extends BaseEventProcessor{
    private static final byte[] NULLVAL = {n, u, l, l};
    private static final byte[] TRUE = {t, r, u, e};
    private static final byte[] FALSE = {f, a, l, s, e};

    private boolean eof = false;
    private long pos = 0;
    private long line = 0;
    private long linePos = 0;
    private BufferedInputStream bis;
    
    private final int byteBlock = 1024;
    private int blockSize = 512;
    
    private boolean documentStarted;
    
    private final ArrayList<Byte> currentToken = new ArrayList<>();
    
    private int arrayLevel = 0;
    private byte[] workingBlock;
    private int blockPosition = 0;
    private int numberOfBlocks = 0;
    private byte previousByte = 0;


    private int tokenState = TOKEN_STATE_EMPTY;
    
    private final ArrayList<Byte> expectedCharacters = new ArrayList<>();

    //private boolean awaitingLf = false;
    
    public JSONByteEventProcessor() {
        
    }
    
    public long getLinePosition() {
        return linePos;
    }
    
    public void setBlockSizeInKB(int number) {
        blockSize = number;
    }
    
    public int getBlockSizeInKB() {
        return blockSize;
    }
    
    public int getBlockSizeInBytes() {
        return byteBlock * blockSize;
    }
    
    public int getNumberOfBlocks() {
        return numberOfBlocks;
    }
    
    @Override
    public void start(InputStream stream) throws JSONEventParserException {
        bis = stream instanceof BufferedInputStream ? (BufferedInputStream)stream : new BufferedInputStream(stream);
        line++;
        
        try {
            int result = buildBlockBuffer();
            
            while (!isEOF() && (result > 0)) {
                processBlock(workingBlock, result);
                result = buildBlockBuffer();
            }
            
            notifyDocumentEnd();
        } catch (final IOException e) {
            throw new JSONEventParserException(e);
        }

        //System.gc();
    }
    
    private int buildBlockBuffer() throws IOException {
        final int bufferSize = getBlockSizeInBytes();
        workingBlock = new byte[bufferSize];
        return nextBlock(workingBlock);
    }
    
    private void appendToken(byte b) {
        currentToken.add(Byte.valueOf(b));

        incrementPosition(b);
    }
    
    private void processBlock(byte[] buf, int length) {
        for (int i = 0; i < length; i++) {
            blockPosition = i;
            handleByte(buf[i]);
            pos++;
            
        }
    }
    
    private void handleByte(byte b) {
        //this handles the case where we need to peek into the first by of the
        //next subsequent block of bytes to determine
        if (expectedCharacters.size() != 0) {
            if (checkExpected(b)) {
                doNotify(); //FIXME: Won't always be doNotify.

            }
        }
        
        switch (b) {
            case SPACE:
                handleWhitespace(b);
                break;
            case CR:
                handleCarriageReturn(b);
                break;
            case LF :
                handleLineFeed(b);
                break;
            case TAB :
                handleWhitespace(b);
                break;
            case QUOTE:
                handleQuote(b);
                break;
            case COMMA:
                handleComma(b);
                break;
            case COLON:
                handleColon(b);
                break;
            case START_MAP:
                handleMapStart(b);
                break;
            case END_MAP:
                handleMapEnd(b);
                break;
            case START_ARRAY:
                handleArrayStart(b);
                break;
                
            case END_ARRAY:
                handleArrayEnd(b);
                break;
            case n:
                handleNull(b);
                break;
            case t:
            case f:
                handleBoolean(b);
                break;
                //potential number-based values
            case e:
            case ZERO:
            case ONE:
            case TWO:
            case THREE:
            case FOUR:
            case FIVE:
            case SIX:
            case SEVEN:
            case EIGHT:
            case NINE:
            case PLUS:
            case MINUS:
            case DECIMAL:
            case E:
                handleNumber(b);
                break;
            default:
                handleDefault(b);
        }
    }
    
    
    private void handleArrayStart(byte b) {
        if (!inToken()) {
            notifyArrayStart();
            incrementPosition(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else {
            final String message = "Character " + (char)b + " is not valid in the " + getTokenState() + " evaluation";
            throw new JSONEventParserException(getLine(), getLinePosition(), message);
        }
    }
    
    private void handleArrayEnd(byte b) {
        if (!inToken()) {
            notifyArrayEnd();
            incrementPosition(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else if (isNullToken() || isBooleanToken() || isNumberToken()) {
            if (arrayLevel > 0) {
                doNotify();
                incrementPosition(b);
                notifyArrayEnd();
            }
        }
    }
    
    private void handleMapStart(byte b) {
        if (!documentStarted) {
            notifyDocumentStart(ByteBuffer.allocate(1).put(b));
            incrementPosition(b);
        } else if (!inToken()) {
            notifyMapStart();
            incrementPosition(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }
    
    private void handleMapEnd(byte b) {
        if (!inToken()) {
            notifyMapEnd();
            incrementPosition(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else if (isNullToken() || isBooleanToken() || isNumberToken()) {
            
            doNotify();
            incrementPosition(b);
            notifyMapEnd();
            
        }
    }
    
    private void handleDefault(byte b) {
        if (inToken()) {
            if (isNumberToken()) {
                final String message = "Unexpected character value found in a number value: " + (char)b;
                throw new JSONEventParserException(line, linePos, message);
            } else {
                appendToken(b);
            }
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }
    
    private void handleNumber(byte b) {
        if (!inToken()) {
            notifyNumberTokenStart();
            appendToken(b);
        } else if (isStringToken() || isNumberToken()) {
            appendToken(b);
        } else if ((b == e) && isBooleanToken()) {
            appendToken(b);
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }

    private void handleNull(byte b) {
        if (!inToken()) {
            
            notifyNullTokenStart();
            appendToken(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }

    private void handleBoolean(byte b) {
        if (!inToken()) {
            notifyBooleanTokenStart();
            appendToken(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }

    private void handleColon(byte b) {
        if (!inToken()) {
            fireKeyEndEvent();
            incrementPosition(b);
        } else if (isStringToken()) {
            appendToken(b);
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }

    private void handleComma(byte b) {
        //commas can only appear in two places, between entities (e.g., between array items
        //or map items), or in a string.  They should not appear in numbers,
        //since these should be unformatted in the data.

        if (isStringToken()) {
            appendToken(b);
        } else if (isBooleanToken() || isNumberToken() || isNullToken()) {
            doNotify();
            incrementPosition(b);
        } else if (!inToken()) {
            doNotify();
            notifyEntityEnd();
            incrementPosition(b);
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }

    }
    private void handleQuote(byte b) {
        if (!inToken()) {
            notifyStringTokenStart();
            tokenState = TOKEN_STATE_STRING;
            incrementPosition(b);
            
        } else if (isStringToken()) {
            if (previousByte == BACKSLASH) {
                appendToken(b);
                previousByte = b;
            } else {
                //I don't like the idea that we have to inclue TAB, CR or LF bytes, but
                //technically it could be a valid instance since there's no accounting
                //for how the JSON data is formatted between tokens/entities.
                setExpectedBytes(SPACE, COMMA, COLON, END_ARRAY, END_MAP, TAB, CR, LF);
                
                final byte next = nextByte();
                
                if (next != NULL) {
                    if (checkExpected(next)) {

                        incrementPosition(b);
                        doNotify();
                    } else {
                        final String message = "Unexpected character (" + (char)next + ") found following character \" in " + getTokenState() +
                                ". Was expecting '" + SPACE + "', '" + COMMA + "', '" + END_ARRAY + "', '" + END_MAP + "', '\\t, \\r or \\n.";
                        throw new JSONEventParserException(line, linePos, message);
                    }
                } else {//if it is null, then we have to catch it on the next block of bytes and next pass.
                    incrementPosition(b);

                }
            }
        } else {
            final String message = "Unexpected character (" + (char)b + ") found parsing " + getTokenState();
            throw new JSONEventParserException(line, linePos, message);
        }
    }
    
    private void setExpectedBytes(byte...bt) {
        for (final byte expected : bt) {
            expectedCharacters.add(Byte.valueOf(expected));
        }
    }
    
    private boolean checkExpected(byte currentByte) {
        boolean isExpected = false;
        for (final Byte expected : expectedCharacters) {
            if (currentByte == expected.byteValue()) {
                isExpected = true;
                break;
            }
        }
        
        // we can clear the expected cache now that we've check it to avoid
        // other bytes to evaluate against it
        expectedCharacters.clear();

        return isExpected;
    }
    
    private void handleWhitespace(byte b) {
        if (isNonEssentialWhitespace()) {
            //ignore this whitespace
            incrementPosition(b);
        } else if (isStringToken()) {
            appendToken(b);
            incrementPosition(b);
            previousByte = 0;
        } else if (isNumberToken() || isBooleanToken() || isNullToken()) {
            doNotify(); //can't have spaces in primitive values, likely the end of the value. We' confirm with a validate step
            incrementPosition(b);
        }
    }
    
    private void handleCarriageReturn(byte b) {
        incrementPosition(b);
        if (isStringToken()) {
            //include in the string
            currentToken.add(Byte.valueOf(b));
        }
    }
    
    private void handleLineFeed(byte b) {
        line++;
        linePos = 0;
        handleCarriageReturn(b);
    }
    
    private void doNotify() {
        
        if (!documentStarted) {
            final byte startByte = toByteArray()[0];
            documentStarted = true;
            notifyDocumentStart(ByteBuffer.allocate(1).put(startByte));
            resetTokens();
            return;
        }

        if (isStringToken()) {
            notifyStringTokenEnd(ByteBuffer.wrap(toByteArray()));
            resetTokens();
            return;
        }

        if (isNullToken()) {
            if (validateNullValue()) {
                notifyNullTokenEnd(ByteBuffer.wrap(toByteArray()));
                resetTokens();
            } else {
                final String message = "Invalid null value detected. Was expecting value 'null', but found " + new String(toByteArray());
                throw new JSONEventParserException(line, linePos, message);
            }
            return;
        }
        
        if (isBooleanToken()) {
            if (validateBooleanValue()) {
                notifyBooleanTokenEnd(ByteBuffer.wrap(toByteArray()));
                resetTokens();
            } else {
                final String message = "Invalid boolean value detected. Was expecting value 'true' or 'false', but found value '" + new String(toByteArray()) + "'.";
                throw new JSONEventParserException(line, linePos, message);
                
            }
            return;
        }

        if (isNumberToken()) {
            notifyNumberTokenEnd(ByteBuffer.wrap(toByteArray()));
            resetTokens();
        }
    }

    private boolean validateNullValue() {
        boolean isNull = false;
        final byte[] nullTokenTest = toByteArray();
        
        if (nullTokenTest.length == NULLVAL.length) {
            int i = 0;
            
            for (final byte element : NULLVAL) {
                if (element == nullTokenTest[i]) {
                    isNull = true;
                    i++;
                } else {
                    isNull = false;
                    break;
                }
            }

            if (!isNull) {
                final String message = "Invalid null value detected. Was expecting value 'null', but found " + new String(toByteArray());
                throw new JSONEventParserException(line, linePos, message);
            }
        }
        return isNull;
    }
    
    private boolean validateBooleanValue() {
        boolean isBoolean = false;
        final byte[] booleanTokenTest = toByteArray();
        final byte[] booleanTest = booleanTokenTest[0] == t ? TRUE : FALSE;

        if (booleanTokenTest.length == booleanTest.length) {
            int i = 0;
            for (final byte element : booleanTest) {
                if (element == booleanTokenTest[i]) {
                    isBoolean = true;
                    i++;
                } else {
                    isBoolean = false;
                    break;
                }
            }
        }

        return isBoolean;
    }
    
    private byte[] toByteArray() {
        final byte[] output = new byte[currentToken.size()];
        int i = 0;
        for (final Byte by : currentToken) {
            output[i] = by.byteValue();
            i++;
        }
        
        return output;
    }
    
    private void incrementPosition(byte b) {
        linePos++;
        if ((b != NULL) || (b != TAB) || (b != SPACE)) {
            previousByte = new Byte(b).byteValue();
        }
        
    }
    
    private void resetTokens() {
        currentToken.clear();
        expectedCharacters.clear();
        tokenState = TOKEN_STATE_EMPTY;
        
    }
    
    private boolean isNonEssentialWhitespace() {
        return !documentStarted || !inToken();
    }
    
    private boolean inToken() {
        return documentStarted && (isStringToken() | isNullToken() | isNumberToken() | isBooleanToken());
    }
    
    public boolean isEOF() {
        return eof;
    }
    
    public long getPosition() {
        return pos;
    }
    
    public long getLine() {
        return line;
    }
    
    private String getTokenState() {
        String state = null;
        switch (tokenState) {
            case TOKEN_STATE_BOOLEAN:
                state = "Boolean";
                break;
            case TOKEN_STATE_EMPTY:
                state = "In between";
                break;
            case TOKEN_STATE_NULL:
                state = "Null";
                break;
            case TOKEN_STATE_NUMBER:
                state = "Number";
                break;
            case TOKEN_STATE_STRING:
                state = "String";
                break;
        }
        
        return state;
    }
    
    public boolean isStringToken() {
        return tokenState == TOKEN_STATE_STRING;
    }
    
    public boolean isNumberToken() {
        return tokenState == TOKEN_STATE_NUMBER;
    }
    
    public boolean isNullToken() {
        return tokenState == TOKEN_STATE_NULL;
    }
    
    public boolean isBooleanToken() {
        return tokenState == TOKEN_STATE_BOOLEAN;
    }
    
    private int nextBlock(byte[] buf) throws IOException {
        blockPosition = 0;
        numberOfBlocks++;
        
        final int bytes = bis.read(buf);
        if (bytes == -1) {
            eof = true;
        }

        return bytes;
    }
    
    private byte nextByte() {
        if (blockPosition < (getBlockSizeInBytes() - 1)) {
            return workingBlock[blockPosition + 1];
        } else {
            return 0x0;
        }
    }

    private void notifyDocumentStart(ByteBuffer c) {
        documentStarted = true;
        fireDocumentStartEvent(ByteBuffer.allocate(1).put(c));
        
    }

    private void notifyDocumentEnd() {
        documentStarted = false;
        fireDocumentEndEvent();
    }

    private void notifyStringTokenStart() {
        tokenState = TOKEN_STATE_STRING;
        fireStringStartEvent();
        
    }

    private void notifyStringTokenEnd(ByteBuffer tokenValue) {
        tokenState = TOKEN_STATE_EMPTY;
        fireStringEndEvent(tokenValue);
        
    }

    private void notifyBooleanTokenStart() {
        tokenState = TOKEN_STATE_BOOLEAN;
        fireBooleanStartEvent();
        
    }

    private void notifyBooleanTokenEnd(ByteBuffer tokenValue) {
        tokenState = TOKEN_STATE_EMPTY;
        fireBooleanEndEvent(tokenValue);
        
    }

    private void notifyNumberTokenStart() {
        tokenState = TOKEN_STATE_NUMBER;
        fireNumberStartEvent();
        
    }

    private void notifyNumberTokenEnd(ByteBuffer tokenValue) {
        tokenState = TOKEN_STATE_EMPTY;
        fireNumberEndEvent(tokenValue);
    }

    private void notifyNullTokenStart() {
        tokenState = TOKEN_STATE_NULL;
        fireNullStartEvent();
    }

    private void notifyNullTokenEnd(ByteBuffer tokenValue) {
        tokenState = TOKEN_STATE_EMPTY;
        fireNullEndEvent(tokenValue);
    }

    private void notifyMapStart() {
        tokenState = TOKEN_STATE_EMPTY;
        fireMapStartEvent();
        
    }

    private void notifyMapEnd() {
        tokenState = TOKEN_STATE_EMPTY;
        fireMapEndEvent();
    }

    private void notifyArrayStart() {
        tokenState = TOKEN_STATE_EMPTY;
        arrayLevel++;
        fireArrayStartEvent();
    }

    private void notifyArrayEnd() {
        tokenState = TOKEN_STATE_EMPTY;
        arrayLevel--;
        fireArrayEndEvent();
    }

    private void notifyEntityEnd() {
        tokenState = TOKEN_STATE_EMPTY;
        fireEntityEndEvent();
        
    }

    
    
    
    
}
