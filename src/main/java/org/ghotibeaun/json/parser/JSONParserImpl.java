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
package org.ghotibeaun.json.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.converters.Converters;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.jep.EventParser;
import org.ghotibeaun.json.parser.jep.ParserConfiguration;
import org.ghotibeaun.json.parser.jep.ParserSettings;
import org.ghotibeaun.json.parser.jep.eventhandler.EventHandler;

class JSONParserImpl implements JSONParser {

    public JSONParserImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public JSONNode parse(InputStream inputStream) throws JSONParserException {
        return parse(inputStream, FactorySettings.getSetting(Setting.INPUTSTREAM_CHARSET));
    }

    @Override
    public JSONNode parse(InputStream inputStream, String charSet) throws JSONParserException {
        final EventHandler handler = FactorySettings.createFactoryClass(Setting.EVENT_HANDLER_CLASS);
        //final NativeEventHandler handler = new NativeEventHandler();

        final ParserSettings settings = new ParserSettings(ParserConfiguration.newConfiguration(handler));
        settings.setCharSet(charSet);

        final EventParser parser = EventParser.newEventParser();
        parser.parse(inputStream, settings);


        return handler.getResult();
    }

    @Override
    public JSONNode parse(URL url) throws JSONParserException {
        try {
            final InputStream stream = url.openStream();
            final JSONNode o = parse(stream);
            stream.close();
            return o;
        } catch (final IOException e) {
            throw new JSONParserException(e.getMessage(), e);
        }
    }

    @Override
    public JSONNode parse(String data) throws JSONParserException {
        final StringReader reader = new StringReader(data);
        if (data.charAt(0) == '{') {
            return parseMap(reader);
        } else {
            return parseList(reader);
        }

    }

    @Override
    public JSONNode parse(Reader reader) throws JSONParserException {
        final Tokenizer t = new Tokenizer(reader);
        final TokenMap map = new TokenMap(t);
        final JSONObject o = NodeFactory.newJSONObject(map);
        return o;
    }

    private JSONNode parseMap(Reader reader) throws JSONParserException {


        final Tokenizer t = new Tokenizer(reader);
        final TokenMap map = new TokenMap(t);
        final JSONObject o = NodeFactory.newJSONObject(map);
        return o;
    }

    private JSONNode parseList(Reader reader) throws JSONParserException {
        final Tokenizer t = new Tokenizer(reader);
        final TokenList map = new TokenList(t);
        final JSONArray o = NodeFactory.newJSONArray(map);
        return o;
    }

    @Override
    public JSONNode parse(File file) throws JSONParserException {
        try {
            final FileInputStream stream = new FileInputStream(file);
            final JSONNode o = parse(stream);
            stream.close();
            return o;
        } catch (final FileNotFoundException e) {
            throw new JSONParserException(e.getMessage(), e);

        } catch (final IOException e) {
            throw new JSONParserException(e.getMessage(), e);
        }
    }

    @Override
    public JSONNode parse(Path filePath) throws JSONParserException {
        return parse(filePath.toFile());
    }

    @Override
    public JSONObject newJSONObject() {
        return NodeFactory.newJSONObject();
    }

    @Override
    public JSONArray newJSONArray() {
        return NodeFactory.newJSONArray();
    }

    @Override
    public <T> T parse(InputStream inputStream, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(inputStream).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public <T> T parse(InputStream inputStream, String charSet, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(inputStream).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public <T> T parse(URL url, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(url).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public <T> T parse(String data, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(data).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public <T> T parse(Reader reader, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(reader).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public <T> T parse(File file, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(file).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public <T> T parse(Path filePath, Class<T> targetClass) throws JSONParserException {
        try {
            return Converters.convertToClass(targetClass, parse(filePath).asJSONObject());
        } catch (JSONParserException | JSONConversionException e) {
            throw new JSONParserException(e);
        }
    }



}
