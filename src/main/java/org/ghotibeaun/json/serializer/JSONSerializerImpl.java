package org.ghotibeaun.json.serializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.exception.JSONSerializationException;

class JSONSerializerImpl implements JSONSerializer {

    public JSONSerializerImpl() {
        // No-op
    }

    @Override
    public void write(File outputFile, JSONNode json) throws JSONSerializationException {
        try {
            final FileWriter fileWriter = new FileWriter(outputFile);
            write(fileWriter, json);
        } catch (final IOException e) {
            throw new JSONSerializationException(e);
        }

    }

    @Override
    public void write(OutputStream stream, JSONNode json) throws JSONSerializationException {
        final OutputStreamWriter writer = new OutputStreamWriter(stream);
        write(writer, json);

    }

    @Override
    public void write(Writer writer, JSONNode json) throws JSONSerializationException {
        final String jsonString = json.toJSONString();

        try {
            writer.write(jsonString);
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            throw new JSONSerializationException(e);
        }

    }

}
