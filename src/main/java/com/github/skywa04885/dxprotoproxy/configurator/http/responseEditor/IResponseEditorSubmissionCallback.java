package com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorField;
import com.github.skywa04885.dxprotoproxy.configurator.http.EditorHeader;

import java.util.List;

/**
 * This interface represents an implementation of the submission callback for a response editor.
 */
public interface IResponseEditorSubmissionCallback {
    /**
     * Submits a response that has the given code, headers and fields.
     * @param code the code.
     * @param headers the headers.
     * @param fields the fields.
     * @param fieldsFormat the format of the fields.
     */
    void submit(int code, List<EditorHeader> headers, List<EditorField> fields, HttpProxyFieldsFormat fieldsFormat);
}
