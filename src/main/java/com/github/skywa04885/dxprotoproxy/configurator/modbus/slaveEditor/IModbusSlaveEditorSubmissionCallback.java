package com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor;

import org.jetbrains.annotations.NotNull;

public interface IModbusSlaveEditorSubmissionCallback {
    void submit(@NotNull String controllerAddress, int controllerPort, @NotNull String masterAddress, int masterPort);
}
