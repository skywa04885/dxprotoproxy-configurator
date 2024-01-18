package com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyRequestMethod;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorQueryParameter;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorField;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorHeader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IRequestEditorValidationCallback {
    /**
     * Validates the given values from the query editor.
     * @param path the path to validate.
     * @param queryParameters the query parameters.
     * @param method the method to validate.
     * @param headers the headers to validate.
     * @param fields the fields to validate.
     * @param format the body format.
     * @return null if everything was correct, otherwise the error message.
     */
    String validate(@NotNull String path, @NotNull List<EditorQueryParameter> queryParameters,
                    @NotNull HttpProxyRequestMethod method, @NotNull List<EditorHeader> headers,
                    @NotNull List<EditorField> fields, @NotNull HttpProxyFieldsFormat format);
}
