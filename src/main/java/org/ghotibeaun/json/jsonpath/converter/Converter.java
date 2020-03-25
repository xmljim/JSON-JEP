/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.jsonpath.converter;

/**
 * @author Jim Earley
 *
 */
public interface Converter<T> {

    T convert(Object data);
}
