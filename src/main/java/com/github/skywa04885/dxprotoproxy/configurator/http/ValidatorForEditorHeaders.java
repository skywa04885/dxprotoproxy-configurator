package com.github.skywa04885.dxprotoproxy.configurator.http;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorForEditorHeaders {
    /**
     * Validates the given headers for duplicate keys.
     * @param headers the headers.
     * @return null if they don't contain duplicate keys, an error message otherwise.
     */
    private static String validateDuplicateKeys(List<EditorHeader> headers) {
        String error = null;

        // Gets the list of all header keys.
        final List<String> headerKeys = headers.stream().map(EditorHeader::key).toList();

        // Gets the header keys which occur more than once.
        final Set<String> duplicateHeaderKeys = headerKeys.stream()
                .filter(item -> Collections.frequency(headerKeys, item) > 1).collect(Collectors.toSet());

        // Set the error message if there are header keys which occur more than once.
        if (!duplicateHeaderKeys.isEmpty()) {
            error = "Found duplicate header keys: " + String.join(", ", duplicateHeaderKeys);
        }

        return error;
    }

    /**
     * Validates the given headers for duplicate names.
     * @param headers the headers.
     * @return null if they don't contain duplicate names, an error message otherwise.
     */
    private static String validateDuplicateNames(List<EditorHeader> headers) {
        String error = null;

        // Gets the list of all header names.
        final List<String> headerNames = headers.stream().map(EditorHeader::name).toList();

        // Gets the header names which occur more than once.
        final Set<String> duplicateHeaderNames = headerNames.stream()
                .filter(item -> Collections.frequency(headerNames, item) > 1).collect(Collectors.toSet());

        // Set the error message if there are header names which occur more than once.
        if (!duplicateHeaderNames.isEmpty()) {
            error = "Found duplicate header names: " + String.join(", ", duplicateHeaderNames);
        }

        return error;
    }

    /**
     * Checks if there aren't any blank header names.
     * @param headers the headers.
     * @return null if there aren't any blank header names, otherwise an error message.
     */
    private static String checkForBlankHeaderNames(List<EditorHeader> headers) {
        String error = null;

        if (headers.stream().map(EditorHeader::name).anyMatch(String::isBlank)) {
            error = "One or more header names is blank";
        }

        return error;
    }

    /**
     * Checks if there aren't any blank header keys.
     * @param headers the headers.
     * @return null if there aren't any blank header keys, otherwise an error message.
     */
    private static String checkForBlankHeaderKeys(List<EditorHeader> headers) {
        String error = null;

        if (headers.stream().map(EditorHeader::key).anyMatch(String::isBlank)) {
            error = "One or more header keys is blank";
        }

        return error;
    }

    /**
     * Validates the given headers.
     * @param headers the headers to validate.
     * @return null if it's valid, the error message otherwise.
     */
    public static String validate(List<EditorHeader> headers) {
        String error;

        error = validateDuplicateKeys(headers);
        if (error == null) error = validateDuplicateNames(headers);
        if (error == null) error = checkForBlankHeaderNames(headers);
        if (error == null) error = checkForBlankHeaderKeys(headers);

        return error;
    }
}
