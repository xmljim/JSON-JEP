package org.ghotibeaun.json.converters.valueconverter;

import java.util.Arrays;
import java.util.Optional;

import org.ghotibeaun.json.exception.JSONConversionException;

public abstract class AbstractJSONValueConverter<T> implements ValueConverter<T> {
    private final String[] args;

    public AbstractJSONValueConverter(String...args) {
        this.args = args;
    }

    @Override
    public abstract Class<?>[] accepts();

    public String getAcceptedClassList() {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (final Class<?> c : accepts()) {
            builder.append(c.getName());
            builder.append(", ");
        }

        builder.append("]");
        return builder.toString();
    }

    public <V> String getInvalidMessage(V value) {
        return "INVALID VALUE TYPE CONVERSION: Current value type {" 
                + value.getClass().getName() + "{ is not supported by this converter. This converter accepts: "
                + getAcceptedClassList();
    }

    @Override
    public String[] getArgs() {
        return args;
    }

    public String getArg(int index) {
        return getArgs()[index];
    }

    public <V> boolean accept(V value) {
        if (accepts().length == 0) {
            return true;
        } else {
            final Optional<Class<?>> optional = Arrays.stream(accepts())
                    .filter(clazz -> clazz.equals(value.getClass())).findFirst();

            return optional.isPresent();
        }
    }

    @Override
    public abstract <V> T convertValue(V value) throws JSONConversionException;


}
