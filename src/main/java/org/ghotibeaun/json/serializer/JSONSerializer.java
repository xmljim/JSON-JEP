package org.ghotibeaun.json.serializer;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.exception.JSONSerializationException;

public interface JSONSerializer {

    void write(File outputFile, JSONNode json) throws JSONSerializationException;

    void write(OutputStream stream, JSONNode json) throws JSONSerializationException;

    void write(Writer writer, JSONNode json) throws JSONSerializationException;
}
