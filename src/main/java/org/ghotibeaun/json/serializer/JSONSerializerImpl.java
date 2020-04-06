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
        } catch (Exception e) { 
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
