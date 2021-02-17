package org.ghotibeaun.json.serializer;

import org.ghotibeaun.json.converters.utils.ClassUtils;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.exception.JSONSerializationException;
import org.ghotibeaun.json.factory.FactorySettings;

public final class SerializationFactory {

    private SerializationFactory() {
        //private constructor to avoid instantiation
    }

    @SuppressWarnings("unchecked")
    public static JSONSerializer getSerializer() {
        JSONSerializer serializer = null;

        try {
            final Class<JSONSerializer> clazz = (Class<JSONSerializer>) SerializationFactory.class.getClassLoader().loadClass(FactorySettings.getSetting(FactorySettings.JSON_SERIALIZER));
            serializer = ClassUtils.createInstance(clazz);
        } catch (ClassNotFoundException | JSONConversionException e) {
            throw new JSONSerializationException(e);
        }

        return serializer;
    }

    @SuppressWarnings("unchecked")
    public static XMLSerializer getXmlSerializer() {
        XMLSerializer serializer = null;

        try {
            final Class<XMLSerializer> clazz = (Class<XMLSerializer>) SerializationFactory.class.getClassLoader().loadClass(FactorySettings.getSetting(FactorySettings.XML_SERIALIZER));
            serializer = ClassUtils.createInstance(clazz);
        } catch (ClassNotFoundException | JSONConversionException e) {
            throw new JSONSerializationException(e);
        }

        return serializer;
    }
}
