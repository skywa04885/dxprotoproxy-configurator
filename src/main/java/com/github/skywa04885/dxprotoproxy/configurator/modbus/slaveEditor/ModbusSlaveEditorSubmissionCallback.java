package com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor;

import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlaveConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlavesConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ModbusSlaveEditorSubmissionCallback implements IModbusSlaveEditorSubmissionCallback {
    private final @NotNull ModbusProxySlavesConfig slavesConfig;
    private final @Nullable ModbusProxySlaveConfig slaveConfig;

    public ModbusSlaveEditorSubmissionCallback(@NotNull ModbusProxySlavesConfig SlavesConfig) {
        this.slavesConfig = SlavesConfig;
        slaveConfig = null;
    }

    public ModbusSlaveEditorSubmissionCallback(@NotNull ModbusProxySlaveConfig SlaveConfig) {
        slavesConfig = Objects.requireNonNull(SlaveConfig.parent());
        this.slaveConfig = SlaveConfig;
    }

    private void create(@NotNull String controllerAddress, int controllerPort, @NotNull String masterAddress, int masterPort) {
        final ModbusProxySlaveConfig modbusSlaveConfig = new ModbusProxySlaveConfig(masterAddress, masterPort, controllerAddress,
                controllerPort, slavesConfig);

        slavesConfig.children().add(modbusSlaveConfig);
    }

    private void update(@NotNull String controllerAddress, int controllerPort, @NotNull String masterAddress, int masterPort) {
        assert slaveConfig != null;

        if (!slaveConfig.controllerAddress().equals(controllerAddress)) slaveConfig.setControllerAddress(controllerAddress);
        if (slaveConfig.controllerPort() != controllerPort) slaveConfig.setControllerPort(controllerPort);

        if (!slaveConfig.masterAddress().equals(masterAddress)) slaveConfig.setMasterAddress(masterAddress);
        if (slaveConfig.masterPort() != masterPort) slaveConfig.setMasterPort(masterPort);
    }

    @Override
    public void submit(@NotNull String controllerAddress, int controllerPort, @NotNull String masterAddress, int masterPort) {
        if (slaveConfig == null) {
            create(controllerAddress, controllerPort, masterAddress, masterPort);
        } else {
            update(controllerAddress, controllerPort, masterAddress, masterPort);
        }
    }
}
