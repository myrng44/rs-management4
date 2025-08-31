package ck4.nvb.rsmanagement.base.search.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class SearchUtils {
    /**
     * Extract searchable fields from entity using reflection
     */
    public static String[] extractSearchableFields(Class<?> entityClass, String[] specifiedFields) {
        if (specifiedFields != null && specifiedFields.length > 0) {
            return specifiedFields;
        }

        List<String> fields = new ArrayList<>();
        for (Field field : entityClass.getDeclaredFields()) {
            if (isTextSearchableField(field)) {
                fields.add(field.getName());
            }
        }

        return fields.toArray(new String[0]);
    }

    /**
     * Check if field can be used for text search
     */
    private static boolean isTextSearchableField(Field field) {
        Class<?> type = field.getType();
        return type == String.class &&
                !field.getName().toLowerCase().contains("password") &&
                !field.getName().toLowerCase().contains("token");
    }

    /**
     * Get field value using reflection
     */
    public static Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Check if query is text-based search
     */
    public static boolean isTextBasedQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return false;
        }

        // Text search indicators
        return query.contains(" ") ||           // Multiple words
                query.matches(".*[a-zA-Z].*") || // Contains letters
                query.length() > 2;              // Long enough for meaningful search
    }

    /**
     * Sanitize search input
     */
    public static String sanitizeSearchInput(String input) {
        if (input == null) return null;

        return input.trim()
                .replaceAll("[<>\"'%;()&+]", "") // Remove special chars
                .replaceAll("\\s+", " ");        // Normalize whitespace
    }
}
