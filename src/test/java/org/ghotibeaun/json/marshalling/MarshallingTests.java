package org.ghotibeaun.json.marshalling;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Arrays;

import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.marshalling.classes.AbstractMarshallingTest2;
import org.ghotibeaun.json.marshalling.classes.BasicTestClass;
import org.ghotibeaun.json.marshalling.classes.MarshallingTest2;
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


        final BasicTestClass testClass = MarshallingFactory.getJSONMarshaller().marshall(BasicTestClass.class, basic);

        assertEquals("Hello World", testClass.getMessage());
        assertEquals(4, testClass.getValueSet().size());
        assertTrue(testClass.isRead());
        assertFalse(testClass.getSubclass().getStatus());
    }


    @Test
    public void testParseAndMarshall() {
        try {
            final InputStream stream = getClass().getResourceAsStream("/marshallingTest2.json");
            final InputStream stream2 = getClass().getResourceAsStream("/marshallingTest2.json");
            final JSONFactory factory = JSONFactory.newFactory();
            final JSONParser parser = factory.newParser();

            final AbstractMarshallingTest2 test2 = parser.parse(stream, MarshallingTest2.class);
            final JSONObject json = parser.parse(stream2).asJSONObject();

            assertEquals(json.getString("stringValue"), test2.getStringValue());
            assertEquals(json.getBoolean("booleanValue"), test2.isBooleanValue());
            assertEquals(json.selectValue("$.simpleObject.firstName"), test2.getSimpleObject().getFirstName());

            final MarshallingTest2 cast = (MarshallingTest2)test2;
            final String jsonPath2 = "$.complexArray[0].team";
            final String firstTeam = json.selectValue(jsonPath2);

            assertEquals(firstTeam, cast.getTeams().get(0).getTeam());
        } catch (final Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    private long getValue(int val) {
        long value = 2;
        for (int i = 1; i <= val; i++) {
            value *= i;
        }

        return value;
    }

}
