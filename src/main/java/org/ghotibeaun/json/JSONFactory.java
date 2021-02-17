package org.ghotibeaun.json;

import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.exception.JSONFactoryException;
import org.ghotibeaun.json.exception.JSONParserException;
import org.ghotibeaun.json.exception.JSONSerializationException;
import org.ghotibeaun.json.factory.FactorySettings;
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
            /*
            final Class<JSONFactory> clazz = (Class<JSONFactory>) JSONFactory.class.getClassLoader()
                    .loadClass(FactorySettings.getSetting(FactorySettings.JSON_FACTORY_CLASS));
            impl = ClassUtils.createInstance(clazz);
             */
            return FactorySettings.createFactoryClass(FactorySettings.JSON_FACTORY_CLASS);
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
