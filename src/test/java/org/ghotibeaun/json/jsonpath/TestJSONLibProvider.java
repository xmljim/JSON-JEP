package org.ghotibeaun.json.jsonpath;

import static org.hamcrest.CoreMatchers.equalTo;

import java.util.EnumSet;
import java.util.Set;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONNode;
import org.ghotibeaun.json.parser.ParserFactory;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

public class TestJSONLibProvider {

    @Test
    public void test() {
        final Configuration.Defaults defaults = new Configuration.Defaults() {

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }

            @Override
            public MappingProvider mappingProvider() {
                return new JSONLibMappingProvider();
            }

            @Override
            public JsonProvider jsonProvider() {
                return new JSONLibProvider();
            }
        };

        Configuration.setDefaults(defaults);
        final Configuration conf = Configuration.defaultConfiguration();
        conf.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);
        conf.addOptions(Option.ALWAYS_RETURN_LIST);

        final JSONArray authors =
                JsonPath.using(conf).parse(getClass().getResourceAsStream("/books.json")).read("$..book[?(@.isbn)]");

        //System.out.println(authors.prettyPrint());

        //final byte[] b = "{".getBytes();
        //System.out.println(b[0]);
    }

    @Test
    public void testJSONNodeJsonPathToArray() {
        final JSONNode node = ParserFactory.getParser().parse(getClass().getResourceAsStream("/books.json"));

        final JSONArray books = node.select("$..book");

        Assert.assertNotNull(books);
        Assert.assertTrue(books instanceof JSONArray);

        //System.out.println(books.prettyPrint());
    }

    @Test
    public void testJSONNodePathToString() {
        final JSONNode node = ParserFactory.getParser().parse(getClass().getResourceAsStream("/books.json"));

        final String title = node.select("$..book[1].title").getValue(0);
        final Number price = node.select("$..book[2].price").getValue(0);
        System.out.println(title);
        System.out.println(price.doubleValue());

        final double doublePrice = ((Number)node.selectValue("$..book[2].price")).doubleValue();
        System.out.println(doublePrice);

        final JSONNode values = node.select("$.store.book[?(@.category=='fiction')]");
        System.out.println(values.asJSONArray().get(0).prettyPrint());
    }

    @Test
    public void testSelectValueNode() {
        final JSONNode node = ParserFactory.getParser().parse(getClass().getResourceAsStream("/us-reps.json"));

        final JSONArray gop = node.select("$.objects[?(@.party=='Republican')]");

        final JSONPath jsonPath = JSONPathFactory.compile("$.*[?(@.state=='CO' && @.district==6)].person.sortname");
        final JSONPath path2 = JSONPathFactory.compile("$.*[?(@.state=='CO' && @.district==6)].current");

        final String person = jsonPath.selectValue(gop);
        final boolean isCurrent = path2.selectValue(gop);

        Assert.assertThat(person, equalTo("Coffman, Mike (Rep.) [R-CO6]"));
        Assert.assertThat(isCurrent, equalTo(true));
    }

    @Test
    public void usRepTest() {
        final JSONNode node = ParserFactory.getParser().parse(getClass().getResourceAsStream("/us-reps.json"));

        final JSONArray dems = node.select("$.objects[?(@.party=='Democrat' && @.state=='MA')]");
        final JSONArray gop = node.select("$.objects[?(@.party=='Republican')]");

        System.out.println(dems.size());
        System.out.println(dems.select("$.*[?(@.leadership_title!=null)]").prettyPrint());

        System.out.println(gop.select("$.*[?(@.state=='CO')].person.sortname").prettyPrint());


    }

}
