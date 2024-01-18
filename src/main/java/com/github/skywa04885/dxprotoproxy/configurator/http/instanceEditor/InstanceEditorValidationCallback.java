package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstanceConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstancesConfig;
import com.github.skywa04885.dxprotoproxy.configurator.GlobalConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class InstanceEditorValidationCallback implements IInstanceEditorValidationCallback {
    private final @NotNull HttpProxyInstancesConfig httpConfigInstances;
    private final @Nullable HttpProxyInstanceConfig configInstance;

    public InstanceEditorValidationCallback(@NotNull HttpProxyInstancesConfig httpConfigInstances,
                                            @Nullable HttpProxyInstanceConfig configInstance) {
        this.httpConfigInstances = httpConfigInstances;
        this.configInstance = configInstance;
    }

    public InstanceEditorValidationCallback(@NotNull HttpProxyInstancesConfig httpConfigInstances) {
        this(httpConfigInstances, null);
    }

    public InstanceEditorValidationCallback(@NotNull HttpProxyInstanceConfig configInstance) {
        this(Objects.requireNonNull(configInstance.parent()), configInstance);
    }

    /**
     * Gets called when the instance editor wants to validate.
     *
     * @param name     the name.
     * @param host     the host.
     * @param port     the port.
     * @param protocol the protocol.
     */
    @Override
    public @Nullable String validate(@NotNull String name, @NotNull String host, int port, @NotNull String protocol) {
        if (!GlobalConstants.isNameValid(name)) {
            return "Invalid name";
        }

        if (!GlobalConstants.isValidHost(host)) {
            return "Invalid host";
        }

        if (!GlobalConstants.isProtocolValid(protocol)) {
            return "Invalid protocol";
        }

        if (configInstance != null && !configInstance.name().equals(name)
                && httpConfigInstances.children().containsKey(name)) {
            return "Name already in use";
        } else if (httpConfigInstances.children().containsKey(name)) {
            return "Name already in use";
        }


        return null;
    }
}
