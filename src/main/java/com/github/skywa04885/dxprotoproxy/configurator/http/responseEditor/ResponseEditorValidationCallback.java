package com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyRequestConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyResponseConfig;
import com.github.skywa04885.dxprotoproxy.configurator.http.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * An implementation of the validation callback that is meant for the creation of a new response.
 */
public class ResponseEditorValidationCallback implements IResponseEditorValidationCallback {
    @Nullable
    private final HttpProxyRequestConfig request;
    @Nullable
    private final HttpProxyResponseConfig response;

    /**
     * Creates a new response editor validation callback for the given request.
     *
     * @param request  the request to which the response is going to belong.
     * @param response the existing response that's being modified.
     */
    public ResponseEditorValidationCallback(@Nullable HttpProxyRequestConfig request, @Nullable HttpProxyResponseConfig response) {
        this.request = request;
        this.response = response;
    }

    public ResponseEditorValidationCallback(@NotNull HttpProxyRequestConfig configRequest) {
        this(configRequest, null);
    }

    public ResponseEditorValidationCallback(@Nullable HttpProxyResponseConfig configResponse) {
        this(null, configResponse);
    }

    /**
     * Checks if the given code is already in use (only when creating).
     *
     * @param code the code to validate.
     * @return null if it's not yet in use, an error message otherwise.
     */
    private String checkIfCodeAlreadyUsed(int code) {
        String error = null;

        if ((this.response == null || this.response.code() != code) && request != null && request.responses().children().containsKey(code)) {
            error = "Response with code " + code + " is already specified";
        }

        return error;
    }

    /**
     * Validates a response that has the given code, headers and fields.
     *
     * @param code         the code.
     * @param headers      the headers.
     * @param fields       the fields.
     * @param fieldsFormat the format of the fields.
     * @return null if the contents are valid, a string containing the error message otherwise.
     */
    @Override
    public String validate(int code, List<EditorHeader> headers, List<EditorField> fields, HttpProxyFieldsFormat fieldsFormat) {
        String error;

        error = ValidatorForResponseCode.validate(code);
        if (error == null) error = ValidatorForEditorHeaders.validate(headers);
        if (error == null) error = ValidatorForEditorFields.validate(fields);
        if (error == null) error = checkIfCodeAlreadyUsed(code);

        return error;
    }
}
