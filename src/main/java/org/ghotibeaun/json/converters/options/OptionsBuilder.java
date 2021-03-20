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
package org.ghotibeaun.json.converters.options;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

public class OptionsBuilder {
    private final Map<OptionKey, ConverterOption<?>> optionMap = new HashMap<>();

    public OptionsBuilder() {
        setJSONKeyCase(null);
        setIgnoreKeys();
        setConverterValidation(null);
    }

    public static ConverterOption<?>[] defaultOptions() {
        return new OptionsBuilder().build();
    }

    public OptionsBuilder setJSONKeyCase(KeyNameCasing casing) {
        final KeyNameCasing defaultVal = KeyNameCasing.valueOf(FactorySettings.getSetting(Setting.CONVERTER_JSON_KEY_CASE));
        final DefaultingValue<KeyNameCasing> value = DefaultingValue.of(defaultVal, Optional.ofNullable(casing));
        final SingleConverterOption<KeyNameCasing> option = new SingleConverterOption<>(OptionKey.JSON_KEY_CASE, value);

        optionMap.put(option.getKey(), option);
        return this;
    }

    public OptionsBuilder setIgnoreKeys(String...keys) {
        final String defaultVal = FactorySettings.getSetting(Setting.CONVERTER_IGNORE_KEYS);
        final String[] keyArray = defaultVal == null ? new String[] {} : defaultVal.split(",");
        final DefaultingValue<String> value = DefaultingValue.ofList(Arrays.asList(keyArray), Arrays.asList(keys));
        final SingleConverterOption<String> option = new SingleConverterOption<>(OptionKey.IGNORE_FIELDS, value);

        optionMap.put(option.getKey(), option);
        return this;
    }

    public OptionsBuilder setConverterValidation(ScannerValidationOption validation) {
        final ScannerValidationOption defaultVal = 
                ScannerValidationOption.valueOf(FactorySettings.getSetting(Setting.CONVERTER_VALIDATION));
        final DefaultingValue<ScannerValidationOption> value = 
                DefaultingValue.of(defaultVal, Optional.ofNullable(validation));
        final SingleConverterOption<ScannerValidationOption> option = 
                new SingleConverterOption<>(OptionKey.VALIDATION, value);

        optionMap.put(option.getKey(), option);
        return this;
    }

    public ConverterOption<?>[] build() {
        final ConverterOption<?>[] options = new ConverterOption<?>[] {};
        return optionMap.values().toArray(options);
    }
}
