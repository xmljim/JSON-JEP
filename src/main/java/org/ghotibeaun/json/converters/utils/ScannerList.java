package org.ghotibeaun.json.converters.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.ghotibeaun.json.JSONObject;
import org.ghotibeaun.json.factory.NodeFactory;

public class ScannerList {
    private final Map<String, ScannerEntry> entries = new HashMap<>();

    public ScannerList() {
        // TODO Auto-generated constructor stub
    }

    public void addEntry(Class<?> clazz, Field field) {
        final ScannerEntry entry = new ScannerEntry(clazz, field);
        if (!entries.containsKey(entry.getJsonKey())) {
            entries.put(entry.getJsonKey(), entry);
        }
    }

    public Optional<ScannerEntry> getEntry(String jsonKey) {
        return Optional.ofNullable(entries.get(jsonKey));
    }

    public boolean hasEntry(String jsonKey) {
        return entries.containsKey(jsonKey);
    }

    public Iterable<String> keys() {
        return entries.keySet();
    }

    public Iterable<ScannerEntry> entries() {
        return entries.values();
    }

    public JSONObject toJSON() {
        final JSONObject scanner = NodeFactory.newJSONObject();
        for (final String key : entries.keySet()) {
            scanner.put(key, entries.get(key).toJSON());
        }

        return scanner;
    }
    @Override
    public String toString() {
        return toJSON().prettyPrint();
    }



}
