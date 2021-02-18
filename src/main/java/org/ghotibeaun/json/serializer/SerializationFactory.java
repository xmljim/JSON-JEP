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
