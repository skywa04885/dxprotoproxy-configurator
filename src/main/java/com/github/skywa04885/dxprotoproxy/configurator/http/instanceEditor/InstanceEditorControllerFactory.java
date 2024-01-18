package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstanceConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstancesConfig;
import org.jetbrains.annotations.NotNull;

public class InstanceEditorControllerFactory {
    public static @NotNull InstanceEditorController create(@NotNull HttpProxyInstancesConfig httpConfigInstances) {
        return new InstanceEditorController(null,
                new InstanceEditorValidationCallback(httpConfigInstances),
                new InstanceEditorSubmissionCallback(httpConfigInstances));
    }

    public static @NotNull InstanceEditorController modify(@NotNull HttpProxyInstanceConfig configInstance) {
        return new InstanceEditorController(configInstance,
                new InstanceEditorValidationCallback(configInstance),
                new InstanceEditorSubmissionCallback(configInstance));
    }
}
