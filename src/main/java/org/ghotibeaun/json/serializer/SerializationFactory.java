package org.ghotibeaun.json.serializer;

import org.ghotibeaun.json.exception.JSONSerializationException;
import org.ghotibeaun.json.factory.FactorySettings;

public class SerializationFactory {

    private SerializationFactory() {
        //private constructor to avoid instantiation
    }

    @SuppressWarnings("unchecked")
    public static JSONSerializer getSerializer() {
        JSONSerializer serializer = null;

        try {
            final Class<JSONSerializer> clazz = (Class<JSONSerializer>) SerializationFactory.class.getClassLoader().loadClass(FactorySettings.getSetting(FactorySettings.JSON_SERIALIZER));
            serializer = clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            // TODO Auto-generated catch block
            throw new JSONSerializationException(e);
        }

        return serializer;
    }
}
