package com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyRequestConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyResponseConfig;

public class ResponseEditorControllerFactory {
    /**
     * Creates a new response editor controller meant for the creation of a new response.
     *
     * @param configRequest the request that the response will belong to.
     * @return the controller.
     */
    public static ResponseEditorController create(HttpProxyRequestConfig configRequest) {
        return new ResponseEditorController(
                null,
                new ResponseEditorValidationCallback(configRequest),
                new ResponseEditorSubmissionCallback(configRequest)
        );
    }

    /**
     * Creates a new response editor controller meant for updating an existing response.
     *
     * @param configResponse the response that must be updated.
     * @return the controller.
     */
    public static ResponseEditorController modify(HttpProxyResponseConfig configResponse) {
        return new ResponseEditorController(
                configResponse,
                new ResponseEditorValidationCallback(configResponse),
                new ResponseEditorSubmissionCallback(configResponse)
        );
    }
}
