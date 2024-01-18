package com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyRequestConfig;
import org.jetbrains.annotations.NotNull;

public class RequestEditorControllerFactory {
    /**
     * Creates a new request editor controller meant for the creation of new requests.
     *
     * @param configEndpoint the endpoint that the new request will belong to.
     * @return the controller.
     */
    public static RequestEditorController create(@NotNull HttpProxyEndpointConfig configEndpoint) {
        return new RequestEditorController(
                null,
                new RequestEditorSubmissionCallback(configEndpoint),
                new RequestEditorValidationCallback(configEndpoint)
        );
    }

    /**
     * Creates a new request editor controller meant for the modification of existing requests.
     *
     * @param configRequest the request that needs to be modified.
     * @return the controller.
     */
    public static RequestEditorController modify(@NotNull HttpProxyRequestConfig configRequest) {
        return new RequestEditorController(
                configRequest,
                new RequestEditorSubmissionCallback(configRequest),
                new RequestEditorValidationCallback(configRequest)
        );
    }
}
