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
package org.ghotibeaun.json.converters.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONObject;


public class ClassScanner {
    private Class<?> targetClass = null;
    private final ScannerList scannerList = new ScannerList();

    public ClassScanner() {
        // TODO Auto-generated constructor stub
    }

    public ClassScanner(Class<?> clazz) {
        setTargetClass(clazz);
        scanClass(clazz);
    }

    public ScannerList getScannerList() {
        return scannerList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    private void setTargetClass(Class<?> clazz) {
        if (getTargetClass() == null) {
            targetClass = clazz;
        }
    }

    public Validation validate(JSONObject referenceJSON) {
        final List<String> errors = new ArrayList<>();

        for (final String key : referenceJSON.keys()) {
            if (!getScannerList().hasEntry(key)) {
                final String missingScan = "Missing key in ClassScanner: " + key;
                errors.add(missingScan);
            }
        }

        for (final String sKey : getScannerList().keys()) {
            if (!referenceJSON.containsKey(sKey)) {
                if (!getScannerList().getEntry(sKey).get().isIgnore()) {
                    final String missingKey = "missingKey in reference JSON object: " + sKey;
                    errors.add(missingKey);
                }
            }
        }

        final Validation validation = new Validation(getTargetClass(), referenceJSON, errors.size() == 0, errors, getScannerList().toJSON());
        return validation;
    }

    public void scanClass(Class<?> clazz) {
        setTargetClass(clazz);
        Arrays.stream(clazz.getDeclaredFields()).forEach(field -> addEntry(clazz,field));
        final Optional<Class<?>> optionalSuper = getSuperclass(clazz);
        if (optionalSuper.isPresent()) {
            scanClass(optionalSuper.get());
        }
    }

    private Optional<Class<?>> getSuperclass(Class<?> clazz) {
        final Class<?> superclass = clazz.getSuperclass();

        final Class<?> willSend = superclass != Object.class ? superclass : null;

        return Optional.ofNullable(willSend);
    }

    private void addEntry(Class<?> clazz, Field field) {
        //final String key = AnnotationUtils.findJSONElementKey(field).orElse(field.getName());
        //System.out.println("   * [" + key + "]: "+ field.getName() + ": " + field.getType().toString() + ": " 
        //        + field.trySetAccessible());

        getScannerList().addEntry(clazz, field);
    }

    public Optional<ScannerEntry> get(String key) {
        return getScannerList().getEntry(key);
    }

    public Iterable<String> keys() {
        return getScannerList().keys();
    }

    public Iterable<ScannerEntry> entries() {
        return getScannerList().entries();
    }

    @Override
    public String toString() {
        return getScannerList().toString();
    }

    public class Validation {
        private final boolean isValid;
        private final String[] missingElements;
        private final Class<?> targetClass;
        private final JSONObject context;
        private final JSONObject scanRef;


        public Validation(Class<?> targetClass, JSONObject context, boolean isValid, List<String> missing, JSONObject scanRef) {
            final String[] notFound = new String[] {};
            missingElements = missing.toArray(notFound);

            this.targetClass = targetClass;
            this.context = context;
            this.scanRef = scanRef;
            this.isValid = isValid;
        }


        public boolean isValid() {
            return isValid;
        }


        public String[] getMissingElements() {
            return missingElements;
        }


        public Class<?> getTargetClass() {
            return targetClass;
        }


        public JSONObject getContext() {
            return context;
        }


        public JSONObject getScanRef() {
            return scanRef;
        }




    }
}
