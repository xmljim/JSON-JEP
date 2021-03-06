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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.exception.JSONParserException;

class JSONCSVParserImpl implements JSONCSVParser {

    private final CSVSettings settings;

    protected JSONCSVParserImpl(CSVSettings settings) {
        this.settings = settings;
    }

    @Override
    public CSVSettings getCSVSettings() {
        return settings;
    }

    @Override
    public JSONNode parse(InputStream inputStream) throws JSONParserException {
        final CSVParser parser = new CSVParser(getCSVSettings());
        return parser.process(inputStream);
    }

    @Override
    public JSONNode parse(InputStream inputStream, String charSet) throws JSONParserException {
        return parse(inputStream);
    }

    @Override
    public JSONNode parse(URL url) throws JSONParserException {
        try (InputStream inputStream = url.openStream()) {
            return parse(inputStream);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public JSONNode parse(String data) throws JSONParserException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());) {
            return parse(bais);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public JSONNode parse(Reader reader) throws JSONParserException {
        final JSONNode result = null;

        final char[] charBuffer = new char[8 * 1024];
        final StringBuilder builder = new StringBuilder();
        int numCharsRead;
        try {
            while ((numCharsRead = reader.read(charBuffer, 0, charBuffer.length)) != -1) {
                builder.append(charBuffer, 0, numCharsRead);
            }

            try (InputStream targetStream = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));) {
                return parse(targetStream);
            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public JSONNode parse(File file) throws JSONParserException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parse(fis);
        } catch (final FileNotFoundException e) {
            throw new JSONParserException(e);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        } 
    }

    @Override
    public JSONNode parse(Path filePath) throws JSONParserException {
        try (InputStream inputStream = Files.newInputStream(filePath, StandardOpenOption.READ)) {
            return parse(inputStream);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public JSONObject newJSONObject() {
        throw new JSONParserException("Parser Requires CSV Data to be processed");
    }

    @Override
    public JSONArray newJSONArray() {
        throw new JSONParserException("Parser Requires CSV Data to be processed");
    }

    @Override
    public <T> T parse(InputStream inputStream, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }

    @Override
    public <T> T parse(InputStream inputStream, String charSet, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }

    @Override
    public <T> T parse(URL url, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }

    @Override
    public <T> T parse(String data, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }

    @Override
    public <T> T parse(Reader reader, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }

    @Override
    public <T> T parse(File file, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }

    @Override
    public <T> T parse(Path filePath, Class<T> targetClass) throws JSONParserException {
        throw new JSONParserException("Not supported with CSV");
    }




}
