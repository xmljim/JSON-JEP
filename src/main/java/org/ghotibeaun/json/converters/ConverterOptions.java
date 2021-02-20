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
