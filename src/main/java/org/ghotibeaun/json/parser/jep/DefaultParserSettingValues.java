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
package org.ghotibeaun.json.parser.jep;

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

class DefaultParserSettingValues {

    public static final int BLOCK_SIZE_KB = 4;
    public static final String CHARSET = FactorySettings.getSetting(Setting.INPUTSTREAM_CHARSET);
    public static final boolean ENABLE_STATISTICS = true;
    public static final boolean USE_STRICT = false;
    public static final FloatingPointNumber USE_FLOATING_POINT_TYPE = FloatingPointNumber.DOUBLE;
    public static final NonFloatingPointNumber USE_NON_FLOATING_POINT_TYPE = NonFloatingPointNumber.LONG;



    private DefaultParserSettingValues() {
        // TODO Auto-generated constructor stub
    }



}
