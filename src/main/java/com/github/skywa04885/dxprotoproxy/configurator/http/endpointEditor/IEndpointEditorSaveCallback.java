package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import org.jetbrains.annotations.NotNull;

public interface IEndpointEditorSaveCallback {
    /**
     * Gets called when the endpoint editor has been submitted.
     *
     * @param name the name of the endpoint.
     */
    void submit(@NotNull String name);
}
