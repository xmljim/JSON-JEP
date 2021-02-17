package org.ghotibeaun.json.converters.options;

import java.util.ArrayList;
import java.util.List;

public class OptionsBuilder {
    private final List<Options> optionList;

    public OptionsBuilder() {
        optionList = new ArrayList<>();
    }

    public OptionsBuilder setJSONKeyCase(KeyNameCasing casing) {
        Options.JSON_KEY_CASE.setOption(new KeyCaseOption(casing));
        optionList.add(Options.JSON_KEY_CASE);
        return this;
    }

    public OptionsBuilder setIgnoreKeys(String...keys) {
        Options.IGNORE_FIELDS.setOption(new StringListOption(keys));
        optionList.add(Options.IGNORE_FIELDS);
        return this;
    }

    public Options[] build() {
        final Options[] options = new Options[] {};
        return optionList.toArray(options);
    }
}
