package com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor;

import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMasterConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMastersConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ModbusMasterEditorSubmissionCallback implements IModbusMasterEditorSubmissionCallback {
    private final @NotNull ModbusProxyMastersConfig mastersConfig;
    private final @Nullable ModbusProxyMasterConfig masterConfig;

    public ModbusMasterEditorSubmissionCallback(@NotNull ModbusProxyMastersConfig mastersConfig) {
        this.mastersConfig = mastersConfig;
        masterConfig = null;
    }

    public ModbusMasterEditorSubmissionCallback(@NotNull ModbusProxyMasterConfig masterConfig) {
        mastersConfig = Objects.requireNonNull(masterConfig.parent());
        this.masterConfig = masterConfig;
    }

    private void create(@NotNull String serverAddress, int serverPort, @NotNull String listenAddress, int listenPort) {
        final ModbusProxyMasterConfig modbusMasterConfig = new ModbusProxyMasterConfig(serverAddress, serverPort, listenAddress,
                listenPort, mastersConfig);

        mastersConfig.children().add(modbusMasterConfig);
    }

    private void update(@NotNull String serverAddress, int serverPort, @NotNull String listenAddress, int listenPort) {
        assert masterConfig != null;

        if (!masterConfig.serverAddress().equals(serverAddress)) masterConfig.setServerAddress(serverAddress);
        if (masterConfig.serverPort() != serverPort) masterConfig.setServerPort(serverPort);

        if (!masterConfig.listenAddress().equals(listenAddress)) masterConfig.setListenAddress(listenAddress);
        if (masterConfig.listenPort() != listenPort) masterConfig.setListenPort(listenPort);
    }

    @Override
    public void submit(@NotNull String serverAddress, int serverPort, @NotNull String listenAddress, int listenPort) {
        if (masterConfig == null) {
            create(serverAddress, serverPort, listenAddress, listenPort);
        } else {
            update(serverAddress, serverPort, listenAddress, listenPort);
        }
    }
}
