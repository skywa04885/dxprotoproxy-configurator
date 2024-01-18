package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointsConfig;
import org.jetbrains.annotations.NotNull;

public class EndpointEditorControllerFactory {
    public static @NotNull EndpointEditorController create(@NotNull HttpProxyEndpointsConfig httpConfigEndpoints) {
        return new EndpointEditorController(
                null,
                new EndpointEditorSaveCallback(httpConfigEndpoints),
                new EndpointEditorValidationCallback(httpConfigEndpoints)
        );
    }

    public static @NotNull EndpointEditorController modify(@NotNull HttpProxyEndpointConfig configEndpoint) {
        return new EndpointEditorController(
                configEndpoint,
                new EndpointEditorSaveCallback(configEndpoint),
                new EndpointEditorValidationCallback(configEndpoint)
        );
    }
}
