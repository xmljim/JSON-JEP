package org.ghotibeaun.json.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.ghotibeaun.json.JSONFactory;
import org.ghotibeaun.json.JSONObject;
import org.junit.Test;

public class JSONParserTest {

    @Test
    public void testParseObjectInputStream() {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();
        final InputStream i = getClass().getResourceAsStream("/AllSets.json");

        final JSONObject o = (JSONObject)parser.parse(i);
        System.out.println(o.getJSONObject("UST").getJSONArray("booster"));
        //assertTrue(o.getString("id").equals("respiratory-rate"));

    }

    @Test
    public void testParserClassesTest() throws IOException {
        try (InputStream is = getClass().getResourceAsStream("/classes.json")) {
            final JSONObject ob = ParserFactory.getParser().parse(is).asJSONObject();
            assertNotNull(ob);
        }
    }

    @Test
    public void testParseObjectURL() throws URISyntaxException {
        final JSONFactory factory = JSONFactory.newFactory();
        final JSONParser parser = factory.newParser();
        URL u = null;
        try {
            //u = getClass().getResource("/observation-fhir.json").toURI().toURL();
            u = new URL("http://hl7.org/fhir/observation-example-TPMT-haplotype-two.json");
        } catch (final MalformedURLException e) {
            // TODO Auto-generated catch block
            fail("bad URL");
        }
        final JSONObject o = (JSONObject)parser.parse(u);
        System.out.println(o);
        assertNotNull(o);
    }


    public void testParseObjectString() {
        fail("Not yet implemented");
    }


    public void testParseObjectReader() {
        fail("Not yet implemented");
    }


    public void testParseObjectFile() {
        fail("Not yet implemented");
    }


    public void testParseObjectPath() {
        fail("Not yet implemented");
    }


    public void testNewJSONObject() {
        fail("Not yet implemented");
    }


    public void testParseArrayInputStream() {
        fail("Not yet implemented");
    }


    public void testParseArrayURL() {
        fail("Not yet implemented");
    }


    public void testParseArrayString() {
        fail("Not yet implemented");
    }


    public void testParseArrayReader() {
        fail("Not yet implemented");
    }


    public void testParseArrayFile() {
        fail("Not yet implemented");
    }


    public void testParseArrayPath() {
        fail("Not yet implemented");
    }


    public void testNewJSONArray() {
        fail("Not yet implemented");
    }

}
