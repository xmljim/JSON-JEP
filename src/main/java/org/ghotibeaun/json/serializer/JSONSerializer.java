package org.ghotibeaun.json.serializer;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.file.Path;

import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.exception.JSONSerializationException;

public interface JSONSerializer {

    void write(File outputFile, JSONNode json, boolean prettyPrint) throws JSONSerializationException;

    void write(OutputStream stream, JSONNode json, boolean prettyPrint) throws JSONSerializationException;

    void write(Writer writer, JSONNode json, boolean prettyPrint) throws JSONSerializationException;
    
    void write(Path outputPath, JSONNode json, boolean prettyPrint) throws JSONSerializationException;
}
