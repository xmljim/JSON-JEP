package org.ghotibeaun.json.factory.setting;

public class ClassSetting extends BaseSetting<Class<?>> {

    public ClassSetting(String className) throws ClassNotFoundException {
        this(Class.forName(className));
    }

    public ClassSetting(Class<?> defaultValue) {
        super(defaultValue);
    }

    @Override
    public String toString() {
        return getValue().getName();
    }

    @Override
    public String toDefaultString() {
        return getDefaultValue().getName();
    }

}
