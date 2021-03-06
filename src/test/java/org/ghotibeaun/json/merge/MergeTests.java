package org.ghotibeaun.json.merge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.ghotibeaun.json.JSONArray;
import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.factory.FactorySettings;
import org.ghotibeaun.json.factory.NodeFactory;
import org.ghotibeaun.json.factory.Setting;
import org.ghotibeaun.json.merge.strategies.ArrayConflict;
import org.ghotibeaun.json.merge.strategies.ObjectConflict;
import org.junit.Test;

public class MergeTests {


    @Test
    public void testMergeSimpleObjectNoConflicts() {
        final JSONObject primary = NodeFactory.newJSONObject();
        primary.put("N", "North");
        primary.put("S", "South");
        primary.put("E", "East");
        primary.put("W", "West");

        final JSONObject secondary = NodeFactory.newJSONObject();
        secondary.put("NW", "Northwest");
        secondary.put("NE", "Northeast");
        secondary.put("SW", "Southwest");
        secondary.put("SE", "Southeast");

        final JSONObject merged = MergeProcessor.merge(primary, secondary);

        assertEquals(merged.size(), primary.size() + secondary.size());

        for (final String key: primary.keys()) {
            assertTrue(merged.containsKey(key));
            assertTrue(merged.get(key).isEquivalent(primary.get(key)));
        }

        for (final String key: secondary.keys()) {
            assertTrue(merged.containsKey(key));
            assertTrue(merged.get(key).isEquivalent(secondary.get(key)));
        }
    }

    @Test
    public void testMergeSimpleArrayNoConflicts() {
        final JSONArray primary = NodeFactory.newJSONArray();
        primary.add("North");
        primary.add("South");
        primary.add("East");
        primary.add("West");

        final JSONArray secondary = NodeFactory.newJSONArray();
        secondary.add("Northwest");
        secondary.add("Northeast");
        secondary.add("Southeast");
        secondary.add("Southwest");

        final JSONArray merged = MergeProcessor.merge(primary, secondary);

        assertEquals(merged.size(), primary.size() + secondary.size());

        for (int i = 0; i < primary.size(); i++) {
            assertTrue(merged.get(i).isEquivalent(primary.get(i)));
        }

        for (int i = primary.size(); i < primary.size() + secondary.size(); i++) {
            assertEquals(merged.getString(i), secondary.getString(i - primary.size()));
        }
    }

    @Test
    public void testMergeObjectAcceptPrimary() {


        final JSONObject primary = NodeFactory.newJSONObject();
        primary.put("N", "North");
        primary.put("S", "South");
        primary.put("E", "East");
        primary.put("W", "West");

        final JSONObject secondary = NodeFactory.newJSONObject();
        secondary.put("N", "Not North");
        secondary.put("NW", "Northwest");
        secondary.put("NE", "Northeast");
        secondary.put("SW", "Southwest");
        secondary.put("SE", "Southeast");

        final JSONObject merged = MergeProcessor.merge(primary, secondary, ArrayConflict.APPEND, ObjectConflict.ACCEPT_PRIMARY);

        assertEquals(merged.size(), primary.size() + secondary.size() - 1);
        assertNotEquals(merged.getString("N"), secondary.getString("N"));
        assertEquals(merged.getString("N"), primary.getString("N"));

        //System.out.println(merged.prettyPrint());
    }

    @Test
    public void testMergeObjectAcceptSecondary() {
        final JSONObject primary = NodeFactory.newJSONObject();
        primary.put("N", "North");
        primary.put("S", "South");
        primary.put("E", "East");
        primary.put("W", "West");

        final JSONObject secondary = NodeFactory.newJSONObject();
        secondary.put("N", "Not North");
        secondary.put("NW", "Northwest");
        secondary.put("NE", "Northeast");
        secondary.put("SW", "Southwest");
        secondary.put("SE", "Southeast");

        final JSONObject merged = MergeProcessor.merge(primary, secondary, ArrayConflict.APPEND, ObjectConflict.ACCEPT_SECONDARY);

        assertEquals(merged.size(), primary.size() + secondary.size() - 1);
        assertNotEquals(merged.getString("N"), primary.getString("N"));
        assertEquals(merged.getString("N"), secondary.getString("N"));
    }

    @Test
    public void testMergeObjectAppend() {
        final JSONObject primary = NodeFactory.newJSONObject();
        primary.put("N", "North");
        primary.put("S", "South");
        primary.put("E", "East");
        primary.put("W", "West");

        final JSONObject secondary = NodeFactory.newJSONObject();
        secondary.put("N", "Not North");
        secondary.put("NW", "Northwest");
        secondary.put("NE", "Northeast");
        secondary.put("SW", "Southwest");
        secondary.put("SE", "Southeast");

        final JSONObject merged = MergeProcessor.merge(primary, secondary, ObjectConflict.APPEND);

        assertEquals(merged.size(), primary.size() + secondary.size());
        assertEquals(merged.getString("N"), primary.getString("N"));
        assertEquals(merged.getString("N" + FactorySettings.getSetting(Setting.MERGE_APPEND_KEY)), secondary.getString("N"));
    }

    @Test
    public void testMergeArrayProperty() {
        final JSONObject primary = NodeFactory.newJSONObject();
        final JSONArray primaryArray = NodeFactory.newJSONArray();
        primaryArray.add("Boston");
        primaryArray.add("New York");
        primary.put("NE", primaryArray);

        final JSONObject secondary = NodeFactory.newJSONObject();
        final JSONArray secondaryArray = NodeFactory.newJSONArray();
        secondaryArray.add("Providence");
        secondaryArray.add("Hartford");
        secondary.put("NE", secondaryArray);

        final JSONObject merged = MergeProcessor.merge(primary, secondary);

        assertTrue("Should Equal 'NE", merged.containsKey("NE"));
        assertEquals("Key Size Should be the same", 1, merged.size());
        assertTrue(merged.get("NE").isArray());
        assertEquals(merged.getJSONArray("NE").size(), primaryArray.size() + secondaryArray.size());

        for (int i = 0; i < primary.size(); i++) {
            assertTrue(merged.getJSONArray("NE").get(i).isEquivalent(primary.getJSONArray("NE").get(i)));
        }

        for (int i = primaryArray.size(); i < primaryArray.size() + secondaryArray.size(); i++) {
            assertEquals(merged.getJSONArray("NE").getString(i), secondary.getJSONArray("NE").getString(i - primaryArray.size()));
        }
    }

    @Test
    public void testMergeArrayPropertyWithDuplicateValues() {
        final JSONObject primary = NodeFactory.newJSONObject();
        final JSONArray primaryArray = NodeFactory.newJSONArray();
        primaryArray.add("Boston");
        primaryArray.add("New York");
        primary.put("NE", primaryArray);

        final JSONObject secondary = NodeFactory.newJSONObject();
        final JSONArray secondaryArray = NodeFactory.newJSONArray();
        secondaryArray.add("Boston");
        secondaryArray.add("Providence");
        secondaryArray.add("Hartford");
        secondary.put("NE", secondaryArray);

        final JSONObject merged = MergeProcessor.merge(primary, secondary);

        System.out.println(merged.prettyPrint());
    }

}
