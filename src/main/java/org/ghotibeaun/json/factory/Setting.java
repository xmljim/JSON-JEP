package org.ghotibeaun.json.factory;

import java.util.Arrays;

public enum Setting {
    FACTORY_CLASS("org.ghotibeaun.json.factory", true),
    PARSER_CLASS("org.ghotibeaun.json.parser", true),
    EVENT_PARSER_CLASS("org.ghotibeaun.json.event.parser", true),
    EVENT_PROCESSOR_CLASS("org.ghotibeaun.json.event.processor", true),
    EVENT_PROVIDER_CLASS("org.ghotibeaun.json.event.provider", true),
    JSON_CONVERTER_CLASS("org.ghotibeaun.json.convert.jsonconverter", true),
    CLASS_CONVERTER_CLASS("org.ghotibeaun.json.convert.classconverter", true),
    SERIALIZER_CLASS("org.ghotibeaun.json.serializer", true),
    OBJECT_CLASS("org.ghotibeaun.json.object", true),
    ARRAY_CLASS("org.ghotibeaun.json.array", true),
    VALUE_OBJECT_CLASS("org.ghotibeaun.json.value.object", true),
    VALUE_ARRAY_CLASS("org.ghotibeaun.json.value.array", true),
    VALUE_BOOLEAN_CLASS("org.ghotibeaun.json.value.boolean", true),
    VALUE_NULL_CLASS("org.ghotibeaun.json.value.null", true),
    VALUE_NUMBER_CLASS("org.ghotibeaun.json.value.number", true),
    VALUE_STRING_CLASS("org.ghotibeaun.json.value.string", true),
    INPUTSTREAM_CHARSET("org.ghotibeaun.json.inputstream.charset", false),
    DATE_FORMAT("org.ghotibeaun.json.date.format", false),
    JSONPATH_IMPL_CLASS("org.ghotibeaun.json.jsonpath", true),
    JSONPATH_PROVIDER_CLASS("org.ghotibeaun.json.jsonpath.jsonprovider", true),
    JSONPATH_MAPPING_PROVIDER_CLASS("org.ghotibeaun.json.jsonpath.mappingprovider", true),
    XML_SERIALIZER_CLASS("org.ghotibeaun.json.xmlserializer", true)
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
