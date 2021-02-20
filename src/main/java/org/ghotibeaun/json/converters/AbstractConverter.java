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

import java.util.Arrays;

import org.ghotibeaun.json.converters.options.KeyNameCasing;
import org.ghotibeaun.json.converters.options.Options;

/**
 * Base class for all Converters
 * @author Jim Earley (xml.jim@gmail.com)
 *
 */
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
