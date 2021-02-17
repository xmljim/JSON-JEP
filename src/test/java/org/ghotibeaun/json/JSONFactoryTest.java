package org.ghotibeaun.json;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.serializer.JSONSerializer;
import org.junit.Test;

public class JSONFactoryTest {

    @Test()
    public void testFactoryInstantiation() {
        final JSONFactory factory = JSONFactory.newFactory();
        assertNotNull(factory);
        assertTrue(FactorySettings.getSetting(FactorySettings.JSON_FACTORY_CLASS).equals(factory.getClass().getName()));
    }

    @Test
    public void testFactoryParserInstantiation() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();
        assertNotNull(parser);
        assertTrue(FactorySettings.getSetting(FactorySettings.JSON_PARSER_CLASS).equals(parser.getClass().getName()));
    }

    @Test
    public void testFactorySerializationInstantiation() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONSerializer serializer = factory.newSerializer();
        assertNotNull(serializer);
        assertTrue(FactorySettings.getSetting(FactorySettings.JSON_SERIALIZER_CLASS).equals(serializer.getClass().getName()));
    }

}
