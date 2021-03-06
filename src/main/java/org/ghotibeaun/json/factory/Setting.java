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
package org.ghotibeaun.json.factory;

import java.util.Arrays;

import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.parser.jep.EventParser;


public enum Setting {
    /**
     * Specifies the property for the {@link JSONFactory} implementation class
     */
    FACTORY_CLASS("org.ghotibeaun.json.factory", true),
    /**
     * Specifies the property for the {@link JSONParser} implementation class
     */
    PARSER_CLASS("org.ghotibeaun.json.parser", true),
    /**
     * Specifies the property for the {@link EventParser} implementation class
     */
    EVENT_PARSER_CLASS("org.ghotibeaun.json.event.parser", true),

    EVENT_PROCESSOR_CLASS("org.ghotibeaun.json.event.processor", true),
    EVENT_PROVIDER_CLASS("org.ghotibeaun.json.event.provider", true),
    JSON_CONVERTER_CLASS("org.ghotibeaun.json.convert.jsonconverter", true),
    CLASS_CONVERTER_CLASS("org.ghotibeaun.json.convert.classconverter", true),
    SERIALIZER_CLASS("org.ghotibeaun.json.serializer", true),
    INPUTSTREAM_CHARSET("org.ghotibeaun.json.inputstream.charset", false),
    DATE_FORMAT("org.ghotibeaun.json.date.format", false),
    JSONPATH_IMPL_CLASS("org.ghotibeaun.json.jsonpath", true),
    JSONPATH_PROVIDER_CLASS("org.ghotibeaun.json.jsonpath.jsonprovider", true),
    JSONPATH_MAPPING_PROVIDER_CLASS("org.ghotibeaun.json.jsonpath.mappingprovider", true),
    XML_SERIALIZER_CLASS("org.ghotibeaun.json.xmlserializer", true),
    MERGE_APPEND_KEY("org.ghotibeaun.json.merge.appendkey", false),
    CONVERTER_JSON_KEY_CASE("org.ghotibeaun.json.converter.option.keycase", false),
    CONVERTER_IGNORE_KEYS("org.ghotibeaun.json.converter.option.ignorekeys", false),
    CONVERTER_VALIDATION("org.ghotibeaun.json.converter.option.validation", false)
    ;
    private String property;
    private boolean classValue;

    public String getPropertyName() {
        return property;
    }

    public boolean isClassValue() {
        return classValue;
    }

    public static Setting fromPropertyName(String propertyName) {
        return Arrays.stream(values())
                .filter(setting -> setting.getPropertyName().equals(propertyName))
                .findFirst().orElse(null);
    }

    private Setting(String property, boolean classValue) {
        this.property = property;
        this.classValue = classValue;
    }
}
