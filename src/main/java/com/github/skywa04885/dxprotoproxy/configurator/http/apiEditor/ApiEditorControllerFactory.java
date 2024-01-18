package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApiConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApisConfig;
import org.jetbrains.annotations.NotNull;

public class ApiEditorControllerFactory {
    public static ApiEditorController create(@NotNull HttpProxyApisConfig httpConfigApis) {
        return new ApiEditorController(null,
                new ApiEditorValidationCallback(httpConfigApis),
                new ApiEditorSubmissionCallback(httpConfigApis));
    }

    public static ApiEditorController create(@NotNull HttpProxyApiConfig httpConfigApi) {
        return new ApiEditorController(httpConfigApi,
                new ApiEditorValidationCallback(httpConfigApi),
                new ApiEditorSubmissionCallback(httpConfigApi));
    }
}
