package com.example.ourknowledgebackend.infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFormatter {
    public static Map<String, Object> formatMap(Map<String, Object> map, Map<String, String> replacements) {
        Map<String, Object> formattedMap = new HashMap<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value instanceof String) {
                String formattedValue = formatString((String) value, replacements);
                formattedMap.put(key, formattedValue);
            } else if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) value;
                formattedMap.put(key, formatMap(subMap, replacements));
            } else if (value instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> subList = (List<Object>) value;
                formattedMap.put(key, formatList(subList, replacements));
            } else {
                formattedMap.put(key, value);
            }
        }
        return formattedMap;
    }

    private static List<Object> formatList(List<Object> list, Map<String, String> replacements) {
        List<Object> formattedList = new ArrayList<>();

        for (Object item : list) {
            if (item instanceof String) {
                formattedList.add(formatString((String) item, replacements));
            } else if (item instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) item;
                formattedList.add(formatMap(subMap, replacements));
            } else if (item instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> subList = (List<Object>) item;
                formattedList.add(formatList(subList, replacements));
            } else {
                formattedList.add(item);
            }
        }
        return formattedList;
    }

    private static String formatString(String value, Map<String, String> replacements) {
        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            String placeholder = "${" + replacement.getKey() + "}";
            value = value.replace(placeholder, replacement.getValue());
        }
        return value;
    }
}