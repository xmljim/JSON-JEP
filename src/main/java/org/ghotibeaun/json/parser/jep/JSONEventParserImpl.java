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
package org.ghotibeaun.json.parser.jep;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.exception.JSONEventParserException;
import org.ghotibeaun.json.exception.JSONParserException;

class JSONEventParserImpl extends EventParser {

    public JSONEventParserImpl() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void parse(String data, ParserSettings settings) throws JSONParserException {
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes())) {
            parse(bais, settings);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public void parse(InputStream inputStream, ParserSettings settings) throws JSONParserException {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            settings.getParserConfiguration().init();
            settings.getParserConfiguration().getEventProcessor().start(bis);
            /*            final JSONByteEventProcessor processor = new JSONByteEventProcessor(bis, new DirectEventProviderImpl(eventHandler), new ParserSettings());

            processor.start();*/
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public void parse(Path path, ParserSettings settings) throws JSONParserException {
        try (FileInputStream fis = new FileInputStream(path.toFile())) {
            parse(fis, settings);
        } catch (final FileNotFoundException e) {
            throw new JSONParserException(e);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }

    }

    @Override
    public void parse(File file, ParserSettings settings) throws JSONParserException {
        try (FileInputStream fis = new FileInputStream(file)) {
            parse(fis, settings);
        } catch (final FileNotFoundException e) {
            throw new JSONParserException(e);
        } catch (final IOException e) {
            throw new JSONParserException(e);
        }

    }

    @Override
    public void parse(URL url, ParserSettings settings) throws JSONEventParserException {
        // TODO Auto-generated method stub

    }


}
