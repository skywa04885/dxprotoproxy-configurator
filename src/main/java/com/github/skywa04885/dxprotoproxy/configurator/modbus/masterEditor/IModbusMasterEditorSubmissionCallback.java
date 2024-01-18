package com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor;

import org.jetbrains.annotations.NotNull;

public interface IModbusMasterEditorSubmissionCallback {
    void submit(@NotNull String serverAddress, int serverPort, @NotNull String listenAddress, int listenPort);
}
