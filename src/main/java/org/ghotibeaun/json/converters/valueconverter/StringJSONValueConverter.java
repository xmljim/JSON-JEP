package org.ghotibeaun.json.converters.valueconverter;

import org.ghotibeaun.json.exception.JSONConversionException;

/**
 * Converts a JSONValue to a String. Optionally accepts a single argument to format the value
 * <p><strong>NOTE:</strong> This class uses the native {@link String#format(String, Object...)} method. 
 * As a result, please consult the appropriate formatting tokens.
 * </p>
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
public class StringJSONValueConverter extends AbstractJSONValueConverter<String> {

    public StringJSONValueConverter(String... args) {
        super(args);
    }

    /**
     * Accepts any type
     */
    @Override
    public Class<?>[] accepts() {
        return new Class<?>[] {};
    }

    @Override
    public <V> String convertValue(V value) throws JSONConversionException {
        if (getArgs().length == 0) {
            return value.toString();
        } else {
            System.out.println(value);
            //MessageFormat format = new MessageFormat(getArg(0));
            final String fmt = String.format(getArg(0), value.toString());
            System.out.println("StringJSONValueConverter: " + fmt);
            return fmt;
        }
    }

}
