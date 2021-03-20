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

import java.nio.charset.Charset;

import org.ghotibeaun.json.parser.jep.eventhandler.EventHandler;

public class ParserSettings {

    private int blockSize = DefaultParserSettingValues.BLOCK_SIZE_KB;
    private boolean enableStatistics = DefaultParserSettingValues.ENABLE_STATISTICS;
    private Charset characterSet = null;
    private boolean useStrict = DefaultParserSettingValues.USE_STRICT;
    private ParserConfiguration configuration = null;
    private FloatingPointNumber floatingPoint = DefaultParserSettingValues.USE_FLOATING_POINT_TYPE;
    private NonFloatingPointNumber nonFloatingPoint = DefaultParserSettingValues.USE_NON_FLOATING_POINT_TYPE;

    protected ParserSettings() {
        characterSet = Charset.forName(DefaultParserSettingValues.CHARSET);
    }

    public ParserSettings(ParserConfiguration parserConfiguration) {
        this();
        setParserConfiguration(parserConfiguration);
    }

    public static ParserSettings newSettings(EventHandler handler) {
        return new ParserSettings(ParserConfiguration.newConfiguration(handler));
    }


    public ParserSettings setBlockSize(int sizeInKb) {
        blockSize = sizeInKb;
        return this;
    }


    public int getBlockSize() {
        return blockSize;
    }

    public int getBlockSizeBytes() {
        return blockSize * 1024;
    }

    public ParserSettings setCharSet(String charSet) {
        characterSet = Charset.forName(charSet);
        return this;
    }

    public String getCharSetName() {
        return characterSet.name();
    }

    public Charset getCharset() {
        return characterSet;
    }

    public ParserSettings setEnableStatistics(boolean enable) {
        enableStatistics = enable;
        return this;
    }

    public boolean getEnableStatistics() {
        return enableStatistics;
    }

    public boolean getUseStrict() {
        return useStrict;
    }

    public ParserSettings setUseStrict(boolean useStrict) {
        this.useStrict = useStrict;
        return this;
    }

    public ParserSettings setUseFloatingPointType(FloatingPointNumber value) {
        floatingPoint = value;
        return this;
    }

    public FloatingPointNumber getUseFloatingPointType() {
        return floatingPoint;
    }

    public ParserSettings setUseNonFloatingPointType(NonFloatingPointNumber value) {
        nonFloatingPoint = value;
        return this;
    }

    public NonFloatingPointNumber getUseNonFloatingPointType() {
        return nonFloatingPoint;
    }

    public ParserSettings setParserConfiguration(ParserConfiguration configuration) {
        this.configuration = configuration;
        configuration.getEventHandler().setParserSettings(this);
        configuration.getEventProcessor().setParserSettings(this);
        configuration.getEventProvider().setParserSettings(this);

        return this;
    }

    public ParserConfiguration getParserConfiguration() {
        return configuration;
    }


}
