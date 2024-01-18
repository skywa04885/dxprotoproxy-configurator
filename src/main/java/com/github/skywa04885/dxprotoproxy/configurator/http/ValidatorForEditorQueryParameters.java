package com.github.skywa04885.dxprotoproxy.configurator.http;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidatorForEditorQueryParameters {
    /**
     * Validates the given query parameters by checking for duplicate keys.
     * @param queryParameters the query parameters to validate.
     * @return null if they're valid, an error message otherwise.
     */
    private static String checkForDuplicateKeys(List<EditorQueryParameter> queryParameters) {
        String error = null;

        // Gets all the query parameter keys.
        final List<String> queryParameterKeys = queryParameters.stream().map(EditorQueryParameter::key).toList();

        // Finds all the duplicate keys.
        final Set<String> duplicateQueryParameterKeys = queryParameterKeys.stream()
                .filter(item -> Collections.frequency(queryParameterKeys, item) > 1).collect(Collectors.toSet());

        // Set the error message if there are any duplicate keys.
        if (!duplicateQueryParameterKeys.isEmpty()) {
            error = "Found duplicate query parameter keys: " + String.join(", ", duplicateQueryParameterKeys);
        }

        // Return the error message.
        return error;
    }

    /**
     * Validates the given query parameters.
     * @param queryParameters the query parameters to validate.
     * @return null if they're valid, an error message otherwise.
     */
    public static String validate(List<EditorQueryParameter> queryParameters) {
        String error;

        error = checkForDuplicateKeys(queryParameters);

        return error;
    }
}
