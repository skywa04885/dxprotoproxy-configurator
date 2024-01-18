package com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyRequestMethod;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorQueryParameter;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorField;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorHeader;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IRequestEditorSubmissionCallback {
    /**
     * Submits the given values.
     * @param path the path.
     * @param queryParameters the query parameters.
     * @param method the method.
     * @param headers the headers.
     * @param fields the fields.
     * @param format the body format.
     */
    void submit(@NotNull String path, @NotNull List<EditorQueryParameter> queryParameters,
                @NotNull HttpProxyRequestMethod method, @NotNull List<EditorHeader> headers,
                @NotNull List<EditorField> fields, @NotNull HttpProxyFieldsFormat format);
}
