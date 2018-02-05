package org.ghotibeaun.json.parser;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.exception.JSONParserException;

public interface JSONParser {

    JSONNode parse(InputStream inputStream) throws JSONParserException;

    JSONNode parse(InputStream inputStream, String charSet) throws JSONParserException;

    JSONNode parse(URL url) throws JSONParserException;

    JSONNode parse(String data) throws JSONParserException;

    JSONNode parse(Reader reader) throws JSONParserException;

    JSONNode parse(File file) throws JSONParserException;

    JSONNode parse(Path filePath) throws JSONParserException;

    JSONObject newJSONObject();

    JSONArray newJSONArray();

}