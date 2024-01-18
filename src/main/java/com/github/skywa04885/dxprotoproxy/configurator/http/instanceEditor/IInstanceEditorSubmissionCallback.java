package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import org.jetbrains.annotations.NotNull;

public interface IInstanceEditorSubmissionCallback {
    /**
     * Gets called when the create instance windows submits.
     *
     * @param name     the name.
     * @param host     the host.
     * @param port     the port.
     * @param protocol the protocol.
     */
    void submit(@NotNull String name, @NotNull String host, int port, @NotNull String protocol);
}
