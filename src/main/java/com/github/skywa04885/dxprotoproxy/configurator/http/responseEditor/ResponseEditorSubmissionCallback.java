package com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.config.*;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorField;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

/**
 * An implementation of the response editor submission callback that either updates or creates responses.
 */
public class ResponseEditorSubmissionCallback implements IResponseEditorSubmissionCallback {
    @Nullable
    private final HttpProxyRequestConfig request;
    @Nullable
    private final HttpProxyResponseConfig response;

    /**
     * Creates a new response editor submission callback.
     *
     * @param request  the request that the response belongs to.
     * @param response the original response that's being modified, if it's there.
     */
    public ResponseEditorSubmissionCallback(@Nullable HttpProxyRequestConfig request, @Nullable HttpProxyResponseConfig response) {
        this.request = request;
        this.response = response;
    }

    public ResponseEditorSubmissionCallback(@NotNull HttpProxyRequestConfig configRequest) {
        this(configRequest, null);
    }

    public ResponseEditorSubmissionCallback(@Nullable HttpProxyResponseConfig configResponse) {
        this(null, configResponse);
    }

    /**
     * Creates a new response.
     *
     * @param code          the code.
     * @param editorHeaders the headers.
     * @param editorFields  the fields.
     * @param fieldsFormat  the format of the fields.
     */
    private void create(int code, List<EditorHeader> editorHeaders, List<EditorField> editorFields, HttpProxyFieldsFormat fieldsFormat) {
        assert response == null;
        assert request != null;

        // Creates the editor headers and the editor fields objects.
        final var configHeaders = new HttpProxyHeadersConfig(new HashMap<>());
        final var configFields = new HttpProxyFieldsConfig(new HashMap<>(), fieldsFormat);

        // Inserts all the editor headers.
        editorHeaders.forEach(editorHeader -> {
            final var header = new HttpProxyHeaderConfig(editorHeader.key(), editorHeader.value(), editorHeader.name());
            configHeaders.children().put(editorHeader.name(), header);
        });

        // Inserts all the editor fields.
        editorFields.forEach(editorField -> {
            final var configField = new HttpProxyFieldConfig(editorField.path(), editorField.name(), editorField.value());
            configFields.children().put(editorField.name(), configField);
        });

        // Creates the response.
        final var response = new HttpProxyResponseConfig(request.responses(), code, configFields, configHeaders);

        // Puts the response in the request.
        request.responses().children().put(code, response);
    }

    /**
     * Updates the headers.
     *
     * @param editorHeaders the editor headers.
     */
    private void updateHeaders(List<EditorHeader> editorHeaders) {
        assert response != null;

        // Gets the headers from the config.
        final HttpProxyHeadersConfig configHeaders = response.headers();

        // Creates all the new headers.
        editorHeaders.stream().filter(EditorHeader::hasNoConfigHeader).forEach(editorHeader -> {
            final var configHeader = new HttpProxyHeaderConfig(editorHeader.key(), editorHeader.value(), editorHeader.name());
            configHeaders.children().put(editorHeader.name(), configHeader);
        });

        // Updates all the existing headers.
        editorHeaders.stream().filter(EditorHeader::hasConfigHeader).forEach(editorHeader -> {
            // Updates the header key.
            final HttpProxyHeaderConfig configHeader = editorHeader.configHeader();
            if (!configHeader.key().equals(editorHeader.key())) {
                configHeaders.children().remove(configHeader.key());
                configHeader.setKey(editorHeader.key());
                configHeaders.children().put(editorHeader.key(), configHeader);
            }

            // Updates the header value.
            if (editorHeader.value().isBlank() && configHeader.value() != null) {
                configHeader.setValue(null);
            } else if (configHeader.value() == null || !configHeader.value().equals(editorHeader.value())) {
                configHeader.setValue(editorHeader.value());
            }

            // Updates the header name.
            if (!configHeader.name().equals(editorHeader.name())) {
                configHeader.setName(editorHeader.name());
            }
        });

        // Removes all the removed headers.
        configHeaders.children().keySet().removeIf(configHeaderName -> editorHeaders.stream()
                .noneMatch(editorHeader -> editorHeader.name().equals(configHeaderName)));
    }

    /**
     * Updates the fields.
     *
     * @param editorFields the editor fields.
     */
    private void updateFields(List<EditorField> editorFields) {
        assert response != null;

        // Gets the fields from the config.
        final HttpProxyFieldsConfig configFields = response.fields();

        // Creates all the new fields.
        editorFields.stream().filter(EditorField::hasNoConfigField).forEach(editorField -> {
            final var configField = new HttpProxyFieldConfig(editorField.path(), editorField.name(), editorField.value());
            configFields.children().put(editorField.name(), configField);
        });

        // Updates all the existing fields.
        editorFields.stream().filter(EditorField::hasConfigField).forEach(editorField -> {
            // Updates the config field path.
            final HttpProxyFieldConfig configField = editorField.configField();
            if (!configField.path().equals(editorField.path())) {
                configFields.children().remove(configField.path());
                configField.setPath(editorField.path());
                configFields.children().put(editorField.path(), configField);
            }

            // Updates the config field value.
            if (editorField.value().isBlank() && configField.value() != null) {
                configField.setValue(null);
            } else if (configField.value() == null || !editorField.value().equals(configField.value())) {
                configField.setValue(editorField.value());
            }

            // Updates the config field name.
            if (!configField.name().equals(editorField.name())) {
                configField.setName(editorField.name());
            }
        });

        // Removes all the removed fields.
        configFields.children().keySet().removeIf(configFieldName -> editorFields.stream()
                .noneMatch(editorField -> editorField.name().equals(configFieldName)));
    }

    /**
     * Updates an existing response.
     *
     * @param code          the code.
     * @param editorHeaders the headers.
     * @param editorFields  the fields.
     * @param fieldsFormat  the format of the fields.
     */
    private void update(int code, List<EditorHeader> editorHeaders, List<EditorField> editorFields, HttpProxyFieldsFormat fieldsFormat) {
        assert response != null;
        assert request == null;

        // Gets the config responses.
        final HttpProxyResponsesConfig configResponses = response.parent();

        // Updates the field format.
        if (response.fields().format() != fieldsFormat) {
            response.fields().setFormat(fieldsFormat);
        }

        // Updates the headers and the fields.
        updateHeaders(editorHeaders);
        updateFields(editorFields);

        // Updates the status code in both the response and the responses map.
        if (response.code() != code) {
            configResponses.children().remove(response.code());
            response.setCode(code);
            configResponses.children().put(response.code(), response);
        }
    }

    /**
     * Saves the response editor by either creating a new response or updating an existing one.
     *
     * @param code          the code.
     * @param editorHeaders the headers.
     * @param editorFields  the fields.
     * @param fieldsFormat  the format of the fields.
     */
    @Override
    public void submit(int code, List<EditorHeader> editorHeaders, List<EditorField> editorFields, HttpProxyFieldsFormat fieldsFormat) {
        if (response == null && request != null) {
            create(code, editorHeaders, editorFields, fieldsFormat);
        } else if (response != null && request == null) {
            update(code, editorHeaders, editorFields, fieldsFormat);
        } else {
            throw new Error("Either the response or request must be set, both are null now.");
        }
    }
}
