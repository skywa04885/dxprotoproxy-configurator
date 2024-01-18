package com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyRequestMethod;
import com.github.skywa04885.dxprotoproxy.common.http.config.*;
import com.github.skywa04885.dxprotoproxy.common.http.path.HttpProxyPathTemplate;
import com.github.skywa04885.dxprotoproxy.common.http.path.HttpProxyPathTemplateParser;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorQueryParameter;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorField;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorHeader;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestEditorSubmissionCallback implements IRequestEditorSubmissionCallback {
    @Nullable
    private final HttpProxyEndpointConfig configEndpoint;
    @Nullable
    private final HttpProxyRequestConfig configRequest;

    /**
     * Creates a new submission callback meant for either creating or updating.
     *
     * @param configEndpoint the endpoint, not null for creating.
     * @param configRequest  the request, not null for updating.
     */
    public RequestEditorSubmissionCallback(@Nullable HttpProxyEndpointConfig configEndpoint,
                                           @Nullable HttpProxyRequestConfig configRequest) {
        this.configEndpoint = configEndpoint;
        this.configRequest = configRequest;
    }

    /**
     * Creates a new submission callback meant for creating.
     *
     * @param configEndpoint the endpoint that the request should be created in.
     */
    public RequestEditorSubmissionCallback(@NotNull HttpProxyEndpointConfig configEndpoint) {
        this(configEndpoint, null);
    }

    /**
     * Creates a new submission callback meant for updating.
     *
     * @param configRequest the config request to update.
     */
    public RequestEditorSubmissionCallback(@NotNull HttpProxyRequestConfig configRequest) {
        this(null, configRequest);
    }

    /**
     * Updates all the query parameters based on the editor query parameters.
     *
     * @param editorQueryParameters the editor query parameters.
     */
    private void updateQueryParameters(@NotNull List<EditorQueryParameter> editorQueryParameters) {
        assert configRequest != null;

        // Gets the query parameters.
        final Map<String, HttpProxyUrlQueryParameterConfig> configQueryParameters = configRequest
                .uri()
                .queryParameters()
                .children();

        // Creates all the new query parameters.
        editorQueryParameters
                .stream()
                .filter(EditorQueryParameter::hasNoConfigQueryParameter)
                .forEach(editorQueryParameter -> {
                    // Creates the new query parameter.
                    final var configQueryParameter = new HttpProxyUrlQueryParameterConfig(editorQueryParameter.key(),
                            editorQueryParameter.value().isBlank() ? null : editorQueryParameter.value());

                    // Puts the new query parameter in the map.
                    configQueryParameters.put(editorQueryParameter.key(), configQueryParameter);
                });

        // Updates all the existing parameters.
        editorQueryParameters
                .stream()
                .filter(EditorQueryParameter::hasConfigQueryParameter)
                .forEach(editorQueryParameter -> {
                    // Gets the config query parameter from the editor query parameter.
                    final HttpProxyUrlQueryParameterConfig configQueryParameter =
                            editorQueryParameter.configQueryParameter();
                    assert configQueryParameter != null;

                    // Updates the key of the query parameter.
                    if (!editorQueryParameter.key().equals(configQueryParameter.key())) {
                        configQueryParameters.remove(configQueryParameter.key());
                        configQueryParameter.setKey(editorQueryParameter.key());
                        configQueryParameters.put(editorQueryParameter.key(), configQueryParameter);
                    }

                    // Updates the value of the query parameter.
                    if (editorQueryParameter.value().isBlank() && configQueryParameter.value() != null) {
                        configQueryParameter.setValue(null);
                    } else if (configQueryParameter.value() == null ||
                            !editorQueryParameter.value().equals(configQueryParameter.value())) {
                        configQueryParameter.setValue(editorQueryParameter.value());
                    }
                });

        // Removes all the query parameters that are no longer in the editor.
        configQueryParameters.keySet().removeIf(configQueryParameter -> editorQueryParameters.stream()
                .noneMatch(editorQueryParameter -> editorQueryParameter.key().equals(configQueryParameter)));
    }

    /**
     * Updates the headers.
     *
     * @param editorHeaders the editor headers.
     */
    private void updateHeaders(@NotNull List<EditorHeader> editorHeaders) {
        assert configRequest != null;

        // Gets the headers from the config.
        final HttpProxyHeadersConfig configHeaders = configRequest.headers();

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
    private void updateFields(@NotNull List<EditorField> editorFields) {
        assert configRequest != null;

        // Gets the fields from the config.
        final HttpProxyFieldsConfig configFields = configRequest.fields();

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
     * Updates the format.
     *
     * @param format the format.
     */
    private void updateFormat(@NotNull HttpProxyFieldsFormat format) {
        assert configRequest != null;

        // Gets the config fields.
        final HttpProxyFieldsConfig configFields = configRequest.fields();

        // Checks if the format is different, if so, update it.
        if (!configFields.format().equals(format)) {
            configFields.setFormat(format);
        }
    }

    /**
     * Updates the method.
     *
     * @param method the method.
     */
    private void updateMethod(@NotNull HttpProxyRequestMethod method) {
        assert configRequest != null;

        // Checks if the method is different, if so, update it.
        if (!configRequest.method().equals(method)) {
            configRequest.setMethod(method);
        }
    }

    /**
     * Updates the path.
     * @param path the new path.
     */
    private void updatePath(@NotNull String path) {
        assert configRequest != null;

        // Gets the config uri.
        final HttpProxyUrlConfig configUri = configRequest.uri();

        // Parses the path template.
        final var httpPathTemplateParser = new HttpProxyPathTemplateParser();
        final HttpProxyPathTemplate pathTemplate = httpPathTemplateParser.parse(path);

        // Checks if we need to update it, and if so, update it.
        if (!pathTemplate.equals(configUri.path())) {
            configUri.setPath(pathTemplate);
        }
    }

    /**
     * Updates an existing request.
     *
     * @param path            the path.
     * @param queryParameters the query parameters.
     * @param method          the method.
     * @param headers         the headers.
     * @param fields          the fields.
     * @param format          the format.
     */
    private void update(@NotNull String path, @NotNull List<EditorQueryParameter> queryParameters,
                        @NotNull HttpProxyRequestMethod method, @NotNull List<EditorHeader> headers,
                        @NotNull List<EditorField> fields, @NotNull HttpProxyFieldsFormat format) {
        assert configRequest != null;

        // Calls the individual update methods.
        updateQueryParameters(queryParameters);
        updateFormat(format);
        updateMethod(method);
        updateFields(fields);
        updateHeaders(headers);
        updatePath(path);
    }

    /**
     * Create a new request.
     *
     * @param path            the path.
     * @param queryParameters the query parameters.
     * @param method          the method.
     * @param headers         the headers.
     * @param fields          the fields.
     * @param format          the format.
     */
    private void create(@NotNull String path, @NotNull List<EditorQueryParameter> queryParameters,
                        @NotNull HttpProxyRequestMethod method, @NotNull List<EditorHeader> headers,
                        @NotNull List<EditorField> fields, @NotNull HttpProxyFieldsFormat format) {
        assert configEndpoint != null;
        assert configRequest == null;

        // Parses the path template.
        final var httpPathTemplateParser = new HttpProxyPathTemplateParser();
        final HttpProxyPathTemplate httpPathTemplate = httpPathTemplateParser.parse(path);

        // Builds the maps for the headers, query parameters and fields.
        final Map<String, HttpProxyHeaderConfig> headerMap = headers.stream().collect(Collectors
                .toMap(EditorHeader::name, EditorHeader::httpConfigHeader));
        final Map<String, HttpProxyFieldConfig> fieldmap = fields.stream().collect(Collectors
                .toMap(EditorField::name, EditorField::httpConfigField));
        final Map<String, HttpProxyUrlQueryParameterConfig> queryParameterMap = queryParameters.stream()
                .collect(Collectors.toMap(EditorQueryParameter::key,
                        EditorQueryParameter::httpConfigUriQueryParameter));

        // Constructs the request.
        final var httpConfigQueryParameters = new HttpProxyQueryParametersConfig(queryParameterMap);
        final var httpConfigUri = new HttpProxyUrlConfig(httpPathTemplate, httpConfigQueryParameters);
        final var httpConfigHeaders = new HttpProxyHeadersConfig(headerMap);
        final var httpConfigFields = new HttpProxyFieldsConfig(fieldmap, format);
        final var httpConfigResponses = new HttpProxyResponsesConfig(new HashMap<>());
        final var httpConfigRequest = new HttpProxyRequestConfig(httpConfigUri, method, httpConfigHeaders, httpConfigFields, httpConfigResponses);
        httpConfigRequest.setParent(configEndpoint);

        // Inserts the request into the endpoint.
        configEndpoint.getRequests().put(method, httpConfigRequest);
    }

    /**
     * Gets called when we need to submit the request editor.
     *
     * @param path            the path.
     * @param queryParameters the query parameters.
     * @param method          the method.
     * @param headers         the headers.
     * @param fields          the fields.
     * @param format          the body format.
     */
    @Override
    public void submit(@NotNull String path, @NotNull List<EditorQueryParameter> queryParameters,
                       @NotNull HttpProxyRequestMethod method, @NotNull List<EditorHeader> headers,
                       @NotNull List<EditorField> fields, @NotNull HttpProxyFieldsFormat format) {
        if (configRequest == null && configEndpoint != null) {
            create(path, queryParameters, method, headers, fields, format);
        } else if (configRequest != null && configEndpoint == null) {
            update(path, queryParameters, method, headers, fields, format);
        } else {
            throw new Error("Either the config request or the config endpoint must be set, both are null now");
        }
    }
}
