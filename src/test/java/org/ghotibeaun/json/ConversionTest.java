/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json;

import java.nio.file.Paths;
import java.util.Map;

import org.ghotibeaun.json.parser.JSONParser;
import org.ghotibeaun.json.parser.ParserFactory;

/**
 * @author Jim Earley
 *
 */
public class ConversionTest {


    public void test() {
        final JSONParser factory = ParserFactory.getParser();
        final JSONNode node = factory.parse(Paths.get("c:\\code\\account-data.json"));
        final JSONObject data = node.asJSONObject();
        final Map<String, Object> map = data.getMap();
        System.out.println(map);
    }

}
