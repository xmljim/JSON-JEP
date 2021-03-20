package org.ghotibeaun.json.factory.setting;

public class StringSetting extends BaseSetting<String> {

    public StringSetting(String defaultValue) {
        super(defaultValue);
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public String toDefaultString() {
        return getDefaultValue();
    }

}
