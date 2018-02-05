package org.ghotibeaun.json.jsonpath;

import java.util.EnumSet;
import java.util.Set;

import org.ghotibeaun.json.JSONArray;
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

        System.out.println(authors.prettyPrint());

        final byte[] b = "{".getBytes();
        System.out.println(b[0]);
    }

}
