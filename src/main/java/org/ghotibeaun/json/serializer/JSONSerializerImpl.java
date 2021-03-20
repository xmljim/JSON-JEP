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
package org.ghotibeaun.json.serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.exception.JSONSerializationException;

class JSONSerializerImpl implements JSONSerializer {

    public JSONSerializerImpl() {
        // No-op
    }

    @Override
    public void write(File outputFile, JSONNode json, boolean prettyPrint) throws JSONSerializationException {
        try (final FileWriter fileWriter = new FileWriter(outputFile)) {
            write(fileWriter, json, prettyPrint);
        } catch (final IOException e) {
            throw new JSONSerializationException(e);
        }

    }

    @Override
    public void write(OutputStream stream, JSONNode json, boolean prettyPrint) throws JSONSerializationException {
        try (final OutputStreamWriter writer = new OutputStreamWriter(stream);) {
            write(writer, json, prettyPrint);
        } catch (final Exception e) { 
            throw new JSONSerializationException(e);
        }

    }

    @Override
    public void write(Writer writer, JSONNode json, boolean prettyPrint) throws JSONSerializationException {
        final String jsonString = prettyPrint ? json.prettyPrint() : json.toJSONString();

        try {
            writer.write(jsonString);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            throw new JSONSerializationException(e);
        }

    }

    @Override
    public void write(Path outputPath, JSONNode json, boolean prettyPrint) throws JSONSerializationException {
        try (OutputStream out = Files.newOutputStream(outputPath, StandardOpenOption.CREATE)) {
            write(out, json, prettyPrint);
        } catch (final IOException e) {
            throw new JSONSerializationException(e);
        }
    }

}
