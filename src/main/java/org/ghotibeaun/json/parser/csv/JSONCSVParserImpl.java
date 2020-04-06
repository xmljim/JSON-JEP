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
    
    private CSVSettings settings;
    
    protected JSONCSVParserImpl(CSVSettings settings) {
        this.settings = settings;
    }
    
    public CSVSettings getCSVSettings() {
        return this.settings;
    }

    @Override
    public JSONNode parse(InputStream inputStream) throws JSONParserException {
        CSVParser parser = new CSVParser(getCSVSettings());
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
        } catch (IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public JSONNode parse(String data) throws JSONParserException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes());) {
            return parse(bais);
        } catch (IOException e) {
            throw new JSONParserException(e);
        }
    }

    @Override
    public JSONNode parse(Reader reader) throws JSONParserException {
        JSONNode result = null;
        
        char[] charBuffer = new char[8 * 1024];
        StringBuilder builder = new StringBuilder();
        int numCharsRead;
        try {
            while ((numCharsRead = reader.read(charBuffer, 0, charBuffer.length)) != -1) {
                builder.append(charBuffer, 0, numCharsRead);
            }
            
            try (InputStream targetStream = new ByteArrayInputStream(builder.toString().getBytes(StandardCharsets.UTF_8));) {
                return parse(targetStream);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return result;
    }

    @Override
    public JSONNode parse(File file) throws JSONParserException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return parse(fis);
        } catch (FileNotFoundException e) {
            throw new JSONParserException(e);
        } catch (IOException e) {
            throw new JSONParserException(e);
        } 
    }

    @Override
    public JSONNode parse(Path filePath) throws JSONParserException {
        try (InputStream inputStream = Files.newInputStream(filePath, StandardOpenOption.READ)) {
            return parse(inputStream);
        } catch (IOException e) {
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


}
