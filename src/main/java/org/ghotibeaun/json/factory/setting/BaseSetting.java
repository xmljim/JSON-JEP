package org.ghotibeaun.json.factory.setting;

import java.util.Optional;

public abstract class BaseSetting<T> implements FactorySetting<T> {

    private final T defaultValue;
    private T customValue = null;

    public BaseSetting(T defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public T getDefaultValue() {
        // TODO Auto-generated method stub
        return defaultValue;
    }

    @Override 
    public void setValue(T value) {
        customValue = value;
    }

    @Override
    public Optional<T> getCustomValue() {
        return Optional.ofNullable(customValue);
    }

    @Override
    public abstract String toString();

}
