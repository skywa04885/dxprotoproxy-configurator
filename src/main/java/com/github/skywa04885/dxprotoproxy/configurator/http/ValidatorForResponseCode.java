package com.github.skywa04885.dxprotoproxy.configurator.http;

public class ValidatorForResponseCode {
    public static int from = 100;
    public static int to = 599;

    /**
     * Validates the given response code.
     * @param code the response code to validate.
     * @return null if the response code is valid, an error message otherwise.
     */
    public static String validate(int code) {
        String error = null;

        if (code < from || code >= to) {
            error = "Invalid HTTP response code";
        }

        return error;
    }
}
