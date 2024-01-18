package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IInstanceEditorValidationCallback {
    /**
     * Gets called when the create instance window is about to submit and needs to validate the values.
     * @param name the name.
     * @param host the host.
     * @param port the port.
     * @param protocol the protocol.
     * @return null if the values are valid, otherwise an error message.
     */
    @Nullable String validate(@NotNull String name, @NotNull String host, int port, @NotNull String protocol);
}
