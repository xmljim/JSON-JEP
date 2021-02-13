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

/**
 * JSON Parser interface.  
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public interface JSONParser {

    /**
     * Parse from an InputStream. Character Set defaults to UTF-8
     * @param inputStream the InputStream
     * @return a JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(InputStream inputStream) throws JSONParserException;

    /**
     * Parse from an InputStream using a defined character
     * @param inputStream the InputStream
     * @param charSet The Character Set
     * @return A JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(InputStream inputStream, String charSet) throws JSONParserException;

    /**
     * Parse from a URL. Converts it to an InputStream
     * @param url the fully qualified URL
     * @return a JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(URL url) throws JSONParserException;

    /**
     * Parse from a String
     * @param data the String data
     * @return a JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(String data) throws JSONParserException;

    /**
     * Parse from a Reader
     * @param url the Reader implementation
     * @return a JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(Reader reader) throws JSONParserException;

    /**
     * Parse from a File
     * @param file the File
     * @return a JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(File file) throws JSONParserException;

    /**
     * Parse from a Path reference
     * @param filePath the Path reference
     * @return a JSONNode instance
     * @throws JSONParserException thrown if parsing error occurs
     */
    JSONNode parse(Path filePath) throws JSONParserException;

    /**
     * Create an empty JSONObject
     * @return the JSONObject
     */
    JSONObject newJSONObject();

    /**
     * Create an empty JSONArray
     * @return the JSONArray
     */
    JSONArray newJSONArray();

    /**
     * Parse and marshal JSON data to a Class instance
     * @param <T> the Class type. Could be the target class type, a superclass or interface
     * @param inputStream The InputStream. Character Set defaults to UTF-8
     * @param targetClass the target class reference
     * @return an instance of the specified class
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(InputStream inputStream, Class<T> targetClass) throws JSONParserException;

    /**
     * Parse and marshal JSON data to a Class instance
     * @param <T> the Class type. Could be the target class type, a superclass or interface
     * @param inputStream The InputStream
     * @param charSet The Character Set
     * @param targetClass the target class 
     * @return an instance of the specified class
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(InputStream inputStream, String charSet, Class<T> targetClass) throws JSONParserException;

    /**
     * Parse and marshal JSON data to Class instance
     * @param <T> The Class type. Could be the target class type, a superclass or interface
     * @param url the fully qualified URL
     * @param targetClass the target class
     * @return an instance of the specified class
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(URL url, Class<T> targetClass) throws JSONParserException;

    /**
     * Parse and marshal JSON data to Class instance
     * @param <T> The Class type. Could be the target class type, a superclass or interface
     * @param data the String data
     * @param targetClass the target concrete class to instantiate
     * @return an instance of the targetClass
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(String data, Class<T> targetClass) throws JSONParserException;

    /**
     * Parse and marshal JSON data to Class instance
     * @param <T> The Class type. Could be the target class type, a superclass or interface
     * @param reader The Reader implementation
     * @param targetClass the target concrete class to instantiate
     * @return an instance of the targetClass
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(Reader reader, Class<T> targetClass) throws JSONParserException;

    /**
     * Parse and marshal JSON data to Class instance
     * @param <T> The Class type. Could be the target class type, a superclass or interface
     * @param file The File reference
     * @param targetClass the target concrete class to instantiate
     * @return an instance of the targetClass
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(File file, Class<T> targetClass) throws JSONParserException;

    /**
     * Parse and marshal JSON data to Class instance
     * @param <T> The Class type. Could be the target class type, a superclass or interface
     * @param filePath the Path reference
     * @param targetClass the target concrete class to instantiate
     * @return an instance of the targetClass
     * @throws JSONParserException thrown if parsing or marshalling error occcurs
     */
    <T> T parse(Path filePath, Class<T> targetClass) throws JSONParserException;

}