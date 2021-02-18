package org.ghotibeaun.json.converters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.converters.options.Option;
import org.ghotibeaun.json.converters.options.Options;

public class ConverterOptions {
    Map<Options, Option<?>> optionMap = new HashMap<>();

    public ConverterOptions() {
        // TODO Auto-generated constructor stub
    }

    public void set(Options option) {
        optionMap.put(option, option.getOption());
    }

    public Optional<Option<?>> get(Options option) {
        return Optional.of(optionMap.getOrDefault(option, option.getOption()));
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(Options option) {
        return (T) get(option).get().getValue();
    }

    public Options[] getAll() {
        final Options[] opts = null;

        return optionMap.keySet().toArray(opts);
    }

}
