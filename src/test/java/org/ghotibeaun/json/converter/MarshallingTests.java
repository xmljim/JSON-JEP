package org.ghotibeaun.json.converter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.converter.classes.BasicTestClass;
import org.ghotibeaun.json.converter.classes.ConvertLocalDateTimeToLong;
import org.ghotibeaun.json.converter.classes.ConvertLongToLocalDateTime;
import org.ghotibeaun.json.converter.classes.MarshallingTest2;
import org.ghotibeaun.json.converters.Converters;
import org.ghotibeaun.json.converters.utils.ClassScanner;
import org.ghotibeaun.json.converters.valueconverter.ValueConverter;
import org.ghotibeaun.json.exception.JSONConversionException;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.parser.JSONParser;
import org.junit.Test;

public class MarshallingTests {

    @Test
    public void testBasic() {
        final JSONObject basic = NodeFactory.newJSONObject();

        final String[] valueList = new String[] {"foo", "bar", "baz", "blah"};
        final JSONObject subclass = NodeFactory.newJSONObject();
        subclass.put("name", "Test 1");
        subclass.put("status", false);

        basic.put("message", "Hello World");
        basic.put("read", true);
        basic.put("length", getValue(60));
        basic.put("valueSet", Arrays.asList(valueList));
        basic.put("subclass", subclass);


        final BasicTestClass testClass = //ConverterFactory.getJSONMarshaller().marshall(BasicTestClass.class, basic);
                Converters.convertToClass(BasicTestClass.class, basic);
        assertEquals("Hello World", testClass.getMessage());
        assertEquals(4, testClass.getValueSet().size());
        assertTrue(testClass.isRead());
        assertFalse(testClass.getSubclass().getStatus());
    }

    @Test()
    public void testParseAndConvertToConcreteClass() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();

        JSONObject json = null;
        MarshallingTest2 testClass = null;

        try (final InputStream stream = getClass().getResourceAsStream("/marshallingTest2.json")) {
            json = parser.parse(stream).asJSONObject();
        } catch (final Exception e) {
            fail();
        }

        try (final InputStream stream = getClass().getResourceAsStream("/marshallingTest2.json")) {
            testClass = parser.parse(stream, MarshallingTest2.class);
        } catch (final Exception e) {
            fail();
        }

        assertEquals(json.getString("stringValue"), testClass.getStringValue());
        assertEquals(json.getBoolean("booleanValue"), testClass.isBooleanValue());
        assertEquals(json.selectValue("$.simpleObject.firstName"), testClass.getSimpleObject().getFirstName());
        final String jsonPath2 = "$.complexArray[0].team";
        final String firstTeam = json.selectValue(jsonPath2);

        assertEquals(firstTeam, testClass.getTeams().get(0).getTeam());
    }

    @Test
    public void testConvertToClass() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();

        JSONObject json = null;
        MarshallingTest2 testClass = null;

        try (final InputStream stream = getClass().getResourceAsStream("/marshallingTest2.json")) {
            json = parser.parse(stream).asJSONObject();
        } catch (final Exception e) {
            fail();
        }

        try {
            testClass = Converters.convertToClass(MarshallingTest2.class, json);
        } catch (final JSONConversionException e) {
            fail();
        }

        assertNotNull(testClass);
        assertEquals(json.getString("stringValue"), testClass.getStringValue());
        assertEquals(json.getBoolean("booleanValue"), testClass.isBooleanValue());
        assertEquals(json.selectValue("$.simpleObject.firstName"), testClass.getSimpleObject().getFirstName());
        final String jsonPath2 = "$.complexArray[0].team";
        final String firstTeam = json.selectValue(jsonPath2);

        assertEquals(firstTeam, testClass.getTeams().get(0).getTeam());
    }

    @Test
    public void testConvertClassToJSONObject() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();

        JSONObject json = null;
        MarshallingTest2 testClass = null;

        try (final InputStream stream = getClass().getResourceAsStream("/marshallingTest2.json")) {
            json = parser.parse(stream).asJSONObject();
        } catch (final Exception e) {
            fail();
        }

        try (final InputStream stream = getClass().getResourceAsStream("/marshallingTest2.json")) {
            testClass = parser.parse(stream, MarshallingTest2.class);
        } catch (final Exception e) {
            fail();
        }

        final JSONObject converted = Converters.convertToJSONObject(testClass);
        assertTrue(json.isEquivalent(converted));
    }

    @Test
    public void testValueConverterJSONToClass() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();

        JSONArray json = null;

        try (final InputStream stream = getClass().getResourceAsStream("/valueConverterTest1.json")) {
            json = parser.parse(stream).asJSONArray();
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        final ValueConverter<?> converter = new ConvertLongToLocalDateTime();
        final List<LocalDateTime> listOfDates = Converters.convertToList(json, Optional.of(converter), Optional.empty());
        //final List<LocalDateTime> listOfDates = Converters.convertToL

        assertTrue(listOfDates.get(0) instanceof LocalDateTime);
        assertTrue(listOfDates.get(1) instanceof LocalDateTime);
        //we test that the values 
        assertEquals(listOfDates.get(0), LocalDateTime.ofInstant(Instant.ofEpochMilli(1613575337508L), ZoneId.systemDefault()));
        assertEquals(listOfDates.get(1), LocalDateTime.ofInstant(Instant.ofEpochMilli(1611212400000L), ZoneId.systemDefault()));
    }

    @Test
    public void testValueConverterClassToJSON() {
        final List<LocalDateTime> listOfDates = new ArrayList<>();
        listOfDates.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(1613575337508L), ZoneId.systemDefault()));
        listOfDates.add(LocalDateTime.ofInstant(Instant.ofEpochMilli(1611212400000L), ZoneId.systemDefault()));

        final JSONArray converted = Converters.convertToJSONArray(listOfDates, Optional.of(new ConvertLocalDateTimeToLong()), Optional.empty());

        assertEquals(1613575337508L, converted.getLong(0));
        assertEquals(1611212400000L, converted.getLong(1));
    }

    @Test
    public void testRoundTripConverters() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();

        JSONArray json = null;

        try (final InputStream stream = getClass().getResourceAsStream("/valueConverterTest1.json")) {
            json = parser.parse(stream).asJSONArray();
        } catch (final Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        final ValueConverter<?> toLocalDateTimeConverter = new ConvertLongToLocalDateTime();
        final ValueConverter<?> toLongConverter = new ConvertLocalDateTimeToLong();

        final List<LocalDateTime> dateList = Converters.convertToList(json, Optional.of(toLocalDateTimeConverter), Optional.empty());

        final JSONArray converted = Converters.convertToJSONArray(dateList, Optional.of(toLongConverter), Optional.empty());

        assertTrue(converted.isEquivalent(converted));
    }


    @Test
    public void testScanner() {
        final ClassScanner scanner = new ClassScanner();
        scanner.scanClass(MarshallingTest2.class);
        //System.out.println(scanner);
    }


    private long getValue(int val) {
        long value = 2;
        for (int i = 1; i <= val; i++) {
            value *= i;
        }

        return value;
    }

}
