package org.ghotibeaun.json.converter.classes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface JSONConvert {

    Class<?> className();


}
