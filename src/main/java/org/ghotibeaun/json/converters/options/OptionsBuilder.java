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
