package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointsConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EndpointEditorSaveCallback implements IEndpointEditorSaveCallback {
    private final @Nullable HttpProxyEndpointsConfig configEndpoints;
    private final @Nullable HttpProxyEndpointConfig configEndpoint;

    /**
     * Creates a new endpoint editor submission callback.
     *
     * @param configEndpoints      the config endpoints.
     * @param configEndpoint the config endpoint.
     */
    public EndpointEditorSaveCallback(@Nullable HttpProxyEndpointsConfig configEndpoints,
                                      @Nullable HttpProxyEndpointConfig configEndpoint) {
        this.configEndpoints = configEndpoints;
        this.configEndpoint = configEndpoint;
    }

    /**
     * Creates a new endpoint editor submission callback meant for creating endpoints.
     *
     * @param configEndpoints the config endpoints.
     */
    public EndpointEditorSaveCallback(@Nullable HttpProxyEndpointsConfig configEndpoints) {
        this(configEndpoints, null);
    }

    /**
     * Creates a new endpoint editor submission callback meant for modifying endpoints.
     *
     * @param configEndpoint the endpoint that should be modified.
     */
    public EndpointEditorSaveCallback(@Nullable HttpProxyEndpointConfig configEndpoint) {
        this(null, configEndpoint);
    }

    /**
     * Updates an existing endpoint.
     *
     * @param name the name of the endpoint to update.
     */
    private void update(@NotNull String name) {
        assert configEndpoint != null;

        // gets the config api.
        final @NotNull HttpProxyEndpointsConfig configApi =
                Objects.requireNonNull(configEndpoint.parent());

        // Updates the name of the config endpoint.
        configApi.children().remove(configEndpoint.name());
        configEndpoint.setName(name);
        configApi.children().put(name, configEndpoint);
    }

    /**
     * Creates a new endpoint.
     *
     * @param name the name of the endpoint to craete.
     */
    private void create(@NotNull String name) {
        assert configEndpoints != null;

        // Creates the new config endpoint and puts it in the map.
        final var configEndpoint = new HttpProxyEndpointConfig(name);
        configEndpoint.setParent(configEndpoints);
        configEndpoints.children().put(name, configEndpoint);
    }

    /**
     * Gets called when the endpoint editor has been submitted.
     *
     * @param name the name of the endpoint.
     */
    @Override
    public void submit(@NotNull String name) {
        // Checks whether we should create or update.
        if (configEndpoint == null) {
            create(name);
        } else {
            update(name);
        }
    }
}
