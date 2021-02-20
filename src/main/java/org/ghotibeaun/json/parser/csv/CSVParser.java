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
package org.ghotibeaun.json.parser.csv;

import static org.ghotibeaun.json.util.ByteConstants.CR;
import static org.ghotibeaun.json.util.ByteConstants.LF;

import java.io.IOException;
import java.io.InputStream;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.util.ByteConstants;
import org.ghotibeaun.json.util.ResizableByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CSVParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(CSVParser.class);

    private final CSVSettings settings;
    private int line = 0;
    private ResizableByteBuffer buffer;
    private Row row;
    private final JSONArray data = NodeFactory.newJSONArray();
    private Column currentColumn;
    private boolean inEnclosure;
    private boolean inEscape;
    private byte[] block;
    private byte lastByte = 0;
    private final int blockSize = 8192;
    private int position = -1;


    protected CSVParser(CSVSettings settings) {
        this.settings = settings;

    }

    public JSONArray process(InputStream inputStream) {
        buildBlock(inputStream);
        while (block.length > 0) {
            processBlock(inputStream);
            buildBlock(inputStream);
        }

        //process the last row
        appendField();
        processRow();

        return data;
    }

    private void buildBlock(InputStream input) {
        final byte[] buffer = new byte[blockSize];
        try {
            final int blockSize = input.read(buffer);
            if (blockSize != -1) {
                block = trim(buffer, blockSize);
            } else {
                block = new byte[] {};
            }
        } catch (final IOException e) {
            throw new JSONParserException(e.getMessage(), e);
        }

        handleLastByte();
    }

    private byte[] trim (byte[] buffer, int length) {

        final byte[] target = new byte[length];
        System.arraycopy(buffer, 0, target, 0, length);
        return target;
    }

    private void processBlock(InputStream inputStream) {
        for (final byte b : block) {
            position++;
            lastByte = b;
            handleByte(lastByte);
        }
    }

    private void handleLastByte() {
        if (lastByte == 0) {
            return;
        }

        if (isEnclosure(lastByte) && inEnclosure) {
            if (inEscape) {
                appendByte(lastByte);
                inEscape = false;
            } else {
                inEnclosure = false;
            }
        }
    }

    private void handleByte(byte currentByte) {
        if (isDelimiter(currentByte)) {
            handleDelimiter(currentByte);
        } else if (isNewLine(currentByte)) {
            handleNewLine(currentByte);
        } else if (isEscape(currentByte)) {
            handleEscape(currentByte);
        } else if (isEnclosure(currentByte)) {
            handleEnclosure(currentByte);
        } else {
            appendByte(currentByte);
        }
    }

    private void handleDelimiter(byte currentByte) {
        LOGGER.debug("handleDelimiter: {}", currentByte);
        if (!inEnclosure && !inEscape) {
            appendField();
            resetNext(false);
        } else {
            LOGGER.debug("appending delimiter: Enclosure={}; Escape={}", inEnclosure, inEscape); 
            appendByte(currentByte);
            if (inEscape) {
                inEscape = false;
            }
        }
    }

    private void handleNewLine(byte currentByte) {
        LOGGER.debug("handleNewLine");
        if (!inEnclosure) {
            if (currentByte == ByteConstants.LF) {
                appendField();
                processRow();
                resetNext(true);
            }
        } else {
            LOGGER.debug("appending newline: Enclosure={}; Escape={}", inEnclosure, inEscape); 
            appendByte(currentByte);
        }
    }

    private void processRow() {
        LOGGER.debug("processRow");
        if (row.getRowNumber() == 1) {
            LOGGER.debug("In 'HEADER' Row");
            if (settings.getHeaderRow()) {
                if (settings.isDefault()) {
                    LOGGER.debug("Setting Columns");
                    for (final Field f : row) {
                        LOGGER.debug("  - {}", f.getFieldValue());
                        settings.addColumnDefinition(f.getFieldValue());
                    }
                } else {
                    //no-op
                }
            } else { //No header row
                int colNum = 1;
                if (settings.isDefault()) {
                    LOGGER.debug("Set Default Columns");
                    //Set up default column names
                    for (@SuppressWarnings("unused") final Field f : row) {

                        final String fieldName = "col" + colNum;
                        settings.addColumnDefinition(fieldName);
                        colNum++;
                    }
                    //now add the data
                    data.add(row.getJSONObject());
                } else {
                    //assume columns have already been defined
                    data.add(row.getJSONObject());
                }
            }
        } else {
            data.add(row.getJSONObject());
        }
    }

    private void resetNext(boolean lineNumber) {
        //set the current column to null if there are no additional columns in the iterator (e.g., newline)
        if (settings.hasNext()) {
            currentColumn = settings.next();
        } else {
            settings.reset();
            currentColumn = settings.next();
        }

        if (lineNumber) {
            line++;
            row = new Row(line);
        }

        buffer = null;
        inEscape = false;
        inEnclosure = false;
    }

    private void handleEscape(byte currentByte) {
        if (inEscape) {
            appendByte(currentByte);
            inEscape = false;
        } else {
            inEscape = true;
        }
    }

    private void appendField() {
        if (row != null) {

            LOGGER.debug("Append Field [{}:{}]", currentColumn != null ? currentColumn.getColumnName() : "nul", buffer != null? buffer.toString() : "null");
            row.appendField(currentColumn, buffer != null ? buffer.toString() : null, settings);
            buffer = null;
        }

    }

    private void handleEnclosure(byte currentByte) {
        //serialize the enclosure byte if previous character is an escape char
        //then flip the inEscape bit to false
        if (inEscape) {
            appendByte(currentByte);
            inEscape = false;
        } else //If we're at the beginning of the field, flip the inEnclosure bit to true
            //otherwise, turn it off
            if (getBuffer().size() == 0) {
                inEnclosure = true;
            } else {
                //technically, the next character should be the delimiter
                if (isDelimiter(peek())) {
                    inEnclosure = false;
                }
            }
    }

    private byte peek() {
        final int peekIndex = position + 1;
        if (peekIndex < block.length - 1) {
            return block[peekIndex];
        } else {
            return 0;
        }
    }

    private void appendByte(byte currentByte) {
        getBuffer().add(currentByte);
    }

    private ResizableByteBuffer getBuffer() {
        if (row == null) {
            line++;
            row = new Row(line);
            settings.reset();
            currentColumn = settings.next();
        }

        if (buffer == null) {
            //currentColumn = settings.next();
            buffer = new ResizableByteBuffer();
        } else {
            return buffer;
        }

        return buffer;
    }

    private boolean isDelimiter(byte currentByte) {
        return currentByte == settings.getSeparatorByte();
    }

    private boolean isNewLine(byte currentByte) {
        return currentByte == LF || currentByte == CR;
    }

    private boolean isEnclosure(byte currentByte) {
        return currentByte == settings.getEnclosureByte();
    }

    private boolean isEscape(byte currentByte) {
        return currentByte == settings.getEscapeByte();
    }





}
