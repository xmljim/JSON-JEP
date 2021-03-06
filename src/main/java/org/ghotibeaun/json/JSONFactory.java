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
package org.ghotibeaun.json;

import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.exception.JSONFactoryException;
import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.exception.JSONSerializationException;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.parser.csv.CSVSettings;
import org.ghotibeaun.json.parser.csv.JSONCSVParser;
import org.ghotibeaun.json.serializer.JSONSerializer;

/**
 * Factory class used to instantiate a new {@link JSONParser}. For anyone
 * familiar with <a href="http://docs.oracle.com/javaee/1.4/tutorial/doc/JAXPIntro.html">JAXP</a>,
 * this API uses the same type of pattern.
 * <p>
 * <b>Example</b>
 * </p>
 *
 * <pre>
 * JSONFactory factory = JSONFactory.newFactory();
 * <span>//</span>Use the factory to create a new Parser
 * JSONParser parser = factory.newParser();
 * <span>//</span>Now use the parser to create a new JSONObject, using one of its methods - we'll create an empty one here...
 * JSONObject json = parser.newJSONObject();
 * </pre>
 *
 * @author jearley
 *
 */
public abstract class JSONFactory {

    /**
     * Create a new factory implementation. The factory will instantiate a new
     * {@link JSONParser}
     *
     * @return a new factory
     */
    //@SuppressWarnings("unchecked")
    public static JSONFactory newFactory() throws JSONFactoryException {
        //JSONFactory impl = null;

        try {
            return FactorySettings.createFactoryClass(Setting.FACTORY_CLASS);
        } catch (final JSONConversionException e) {
            throw new JSONFactoryException(e);
        }


    }

    public static JSONFactory newFactory(boolean useDefaultSettings) throws JSONFactoryException {
        FactorySettings.setUseDefaultSettings(useDefaultSettings);
        return newFactory();
    }

    /**
     * Instantiate the underlying {@link JSONParser} implementation for this
     * factory
     *
     * @return the {@link JSONParser}
     */
    public abstract JSONParser newParser() throws JSONParserException;

    /**
     * Instantiate the underlying {@link JSONSerializer} implementation for this
     * factory
     *
     * @return the {@link JSONSerializer}
     */
    public abstract JSONSerializer newSerializer() throws JSONSerializationException;

    public abstract JSONCSVParser newCsvParser(CSVSettings settings);

    public abstract JSONCSVParser newCsvParser();


}
