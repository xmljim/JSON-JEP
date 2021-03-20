package org.ghotibeaun.json.factory.setting;

import java.util.Optional;

public interface FactorySetting<T> {

    T getDefaultValue();

    Optional<T> getCustomValue();

    void setValue(T value);

    default T getValue() {
        return getCustomValue().orElse(getDefaultValue());
    }

    String toDefaultString();

    @Override
    String toString();
}
