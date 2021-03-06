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
package org.ghotibeaun.json.converters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.converters.options.ConverterOption;
import org.ghotibeaun.json.converters.options.OptionKey;

public class ConverterOptions {
    Map<OptionKey, ConverterOption<?>> optionMap = new HashMap<>();

    public ConverterOptions() {
        // TODO Auto-generated constructor stub
    }

    public void set(ConverterOption<?> option) {
        optionMap.put(option.getKey(), option);
    }

    public Optional<ConverterOption<?>> get(OptionKey option) {
        return Optional.of(optionMap.getOrDefault(option, null));
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(OptionKey option) {
        return (T) get(option).get().getValue();
    }

    public ConverterOption<?>[] getAll() {
        final ConverterOption<?>[] opts = {};

        return optionMap.keySet().toArray(opts);
    }

    public <V> boolean matches(OptionKey key, V value) {
        boolean result = false;

        if (get(key).isPresent()) {
            result = get(key).get().matchesValue(value);
        }

        return result;
    }

}
