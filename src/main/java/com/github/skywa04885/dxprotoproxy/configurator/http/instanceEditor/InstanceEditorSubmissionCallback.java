package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstanceConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstancesConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InstanceEditorSubmissionCallback implements IInstanceEditorSubmissionCallback {
    private final @NotNull HttpProxyInstancesConfig httpConfigInstances;
    private final @Nullable HttpProxyInstanceConfig configInstance;

    public InstanceEditorSubmissionCallback(@NotNull HttpProxyInstancesConfig httpConfigInstances,
                                            @Nullable HttpProxyInstanceConfig configInstance) {
        this.httpConfigInstances = httpConfigInstances;
        this.configInstance = configInstance;
    }

    public InstanceEditorSubmissionCallback(@NotNull HttpProxyInstancesConfig httpConfigInstances) {
        this(httpConfigInstances, null);
    }

    public InstanceEditorSubmissionCallback(@NotNull HttpProxyInstanceConfig configInstance) {
        this(Objects.requireNonNull(configInstance.parent()), configInstance);
    }

    private void update(@NotNull String name, @NotNull String host, int port, @NotNull String protocol) {
        assert configInstance != null;

        // Gets the config api.
        final HttpProxyInstancesConfig httpConfigInstances = Objects.requireNonNull(configInstance.parent());

        // Updates the name.
        if (!configInstance.name().equals(name)) {
            httpConfigInstances.children().remove(configInstance.name());
            configInstance.setName(name);
            httpConfigInstances.children().put(name, configInstance);
        }

        // Updates the host.
        if (!configInstance.host().equals(host)) {
            configInstance.setHost(host);
        }

        // Updates the port.
        if (configInstance.port() != port) {
            configInstance.setPort(port);
        }

        // Updates the protocol.
        if (!configInstance.protocol().equals(protocol)) {
            configInstance.setProtocol(protocol);
        }
    }

    private void create(@NotNull String name, @NotNull String host, int port, @NotNull String protocol) {
        // Creates the config instance and puts it in the API.
        final var configInstance = new HttpProxyInstanceConfig(name, host, port, protocol);
        configInstance.setParent(httpConfigInstances);
        httpConfigInstances.children().put(name, configInstance);
    }

    /**
     * Gets called when the instance editor submits.
     *
     * @param name     the name.
     * @param host     the host.
     * @param port     the port.
     * @param protocol the protocol.
     */
    @Override
    public void submit(@NotNull String name, @NotNull String host, int port, @NotNull String protocol) {
        if (configInstance != null) {
            update(name, host, port, protocol);
        } else {
            create(name, host, port, protocol);
        }
    }
}
