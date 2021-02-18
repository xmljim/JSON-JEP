package org.ghotibeaun.json.converters.options;

public enum Options implements IOptions {
    IGNORE_FIELDS(new StringListOption()),
    JSON_KEY_CASE(new KeyCaseOption(KeyNameCasing.CAMEL))
    ;

    private Options(Option<?> option) {
        this.option = option;
    }

    private Option<?> option;

    @Override
    public void setOption(Option<?> option) {
        this.option = option;

    }

    @Override
    public Option<?> getOption() {
        return option;
    }

}
