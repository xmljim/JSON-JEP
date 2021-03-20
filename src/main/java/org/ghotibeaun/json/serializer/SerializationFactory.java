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
package org.ghotibeaun.json.serializer;

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.Setting;

public final class SerializationFactory {

    private SerializationFactory() {
        //private constructor to avoid instantiation
    }

    //@SuppressWarnings("unchecked")
    public static JSONSerializer getSerializer() {
        /*JSONSerializer serializer = null;

        try {
            final Class<JSONSerializer> clazz = (Class<JSONSerializer>) SerializationFactory.class.getClassLoader().loadClass(FactorySettings.getSetting(FactorySettings.JSON_SERIALIZER_CLASS));
            serializer = ClassUtils.createInstance(clazz);
        } catch (ClassNotFoundException | JSONConversionException e) {
            throw new JSONSerializationException(e);
        }

        return serializer;
         */
        return FactorySettings.createFactoryClass(Setting.SERIALIZER_CLASS);
    }

    //@SuppressWarnings("unchecked")
    public static XMLSerializer getXmlSerializer() {
        /*
        XMLSerializer serializer = null;

        try {
            final Class<XMLSerializer> clazz = (Class<XMLSerializer>) SerializationFactory.class.getClassLoader().loadClass(FactorySettings.getSetting(FactorySettings.XML_SERIALIZER_CLASS));
            serializer = ClassUtils.createInstance(clazz);
        } catch (ClassNotFoundException | JSONConversionException e) {
            throw new JSONSerializationException(e);
        }

        return serializer;
         */
        return FactorySettings.createFactoryClass(Setting.XML_SERIALIZER_CLASS);
    }
}
