package com.github.skywa04885.dxprotoproxy.configurator.http;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorForEditorFields {
    /**
     * Validates the paths of the fields.
     *
     * @param fields the fields.
     * @return null if the field paths are valid, an error message otherwise.
     */
    private static String validatePaths(List<EditorField> fields) {
        String error = null;

        // Gets all the field paths,
        final List<String> fieldPaths = fields.stream().map(EditorField::path).toList();

        // Finds all the duplicate field paths.
        final Set<String> duplicateFieldPaths = fieldPaths.stream()
                .filter(item -> Collections.frequency(fieldPaths, item) > 1).collect(Collectors.toSet());

        // Set the error message if there are duplicate field paths.
        if (!duplicateFieldPaths.isEmpty()) {
            error = "Found duplicate field paths: " + String.join(", ", duplicateFieldPaths);
        }

        // Return the possible error.
        return error;
    }

    /**
     * Validates the names of the fields.
     *
     * @param fields the fields.
     * @return null if the field names are valid, an error message otherwise.
     */
    private static String validateNames(List<EditorField> fields) {
        String error = null;

        // Gets all the field paths,
        final List<String> fieldNames = fields.stream().map(EditorField::name).toList();

        // Finds all the duplicate field paths.
        final Set<String> duplicateFieldNames = fieldNames.stream()
                .filter(item -> Collections.frequency(fieldNames, item) > 1).collect(Collectors.toSet());

        // Set the error message if there are duplicate field paths.
        if (!duplicateFieldNames.isEmpty()) {
            error = "Found duplicate field names: " + String.join(", ", duplicateFieldNames);
        }

        // Return the possible error.
        return error;
    }

    /**
     * Checks if there aren't any blank field names.
     *
     * @param editorFields the fields.
     * @return null if there aren't any blank field names, otherwise an error message.
     */
    private static String checkForBlankFieldNames(List<EditorField> editorFields) {
        String error = null;

        if (editorFields.stream().map(EditorField::name).anyMatch(String::isBlank)) {
            error = "One or more field names is blank";
        }

        return error;
    }

    /**
     * Checks if there aren't any blank field paths.
     *
     * @param editorFields the fields.
     * @return null if there aren't any field paths keys, otherwise an error message.
     */
    private static String checkForBlankFieldPaths(List<EditorField> editorFields) {
        String error = null;

        if (editorFields.stream().map(EditorField::path).anyMatch(String::isBlank)) {
            error = "One or more field paths is blank";
        }

        return error;
    }

    /**
     * Validates given fields.
     *
     * @param fields the fields to validate.
     * @return null if the fields are valid, an error message otherwise.
     */
    public static String validate(List<EditorField> fields) {
        String error;

        error = validatePaths(fields);
        if (error == null) error = validateNames(fields);
        if (error == null) error = checkForBlankFieldNames(fields);
        if (error == null) error = checkForBlankFieldPaths(fields);

        return error;
    }
}
