/*
 * Copyright (c) 2019, Flatirons Digital Innovations. All Rights Reserved.
 * This code may not be used without the express written permission
 * of the copyright holder, Flatirons Digital Innovations.
 */
package org.ghotibeaun.json.factory;

import org.ghotibeaun.json.JSONObject;
import org.junit.Test;

/**
 * @author Jim Earley
 *
 */
public class EscapedStringTest {

    @Test
    public void test() {
        final String unescaped = "’Twas brillig, and the slithy toves Did gyre and gimble in the wabe: All mimsy were the borogoves, And the mome raths outgrabe. \"Beware the Jabberwock, my son! The jaws that bite, the claws that catch! Beware the Jubjub bird, and shun The frumious Bandersnatch!\"";
        System.out.println(unescaped);
        System.out.println(unescaped.replaceAll("\"", "\\\\\""));

        final JSONObject obj = NodeFactory.newJSONObject();
        obj.put("Jabberwocky", unescaped);
        System.out.println(obj.prettyPrint());
    }

}
