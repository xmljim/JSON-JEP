package org.ghotibeaun.json.converters;

import java.util.Arrays;

import org.ghotibeaun.json.converters.options.KeyNameCasing;
import org.ghotibeaun.json.converters.options.Options;

public abstract class AbstractConverter implements Converter {

    private final ConverterOptions options = new ConverterOptions();

    public AbstractConverter(Options... option) {
        Arrays.stream(option).forEach(opt -> getConverterOptions().set(opt));
    }

    @Override
    public ConverterOptions getConverterOptions() {
        return options;
    }

    public Options[] copyOptions() {
        return getConverterOptions().getAll();
    }

    public KeyNameCasing getJsonKeyCase() {
        return getConverterOptions().getValue(Options.JSON_KEY_CASE);
    }






}
