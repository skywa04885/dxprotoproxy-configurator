package com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor;

import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacade;
import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacadeFactory;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlaveConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlavesConfig;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class ModbusSlaveEditorWindowFactory {
    public static @Nullable WindowFacade<ModbusSlaveEditorController> create(
            @NotNull WindowFacade<?> parent,
            @NotNull ModbusProxySlavesConfig mqttClientsConfig) {
        try {
            final WindowFacade<ModbusSlaveEditorController> windowFacade = WindowFacadeFactory.createPopup(
                    Objects.requireNonNull(ModbusSlaveEditorWindowFactory.class.getResource("view.fxml")),
                    parent, "Create modbus client", ModbusSlaveEditorController.class);

            final var submissionCallback = new ModbusSlaveEditorSubmissionCallback(mqttClientsConfig);

            final var controller = windowFacade.controller();
            controller.setSubmissionCallback(submissionCallback);

            return windowFacade;
        } catch (IOException ioException) {
            ioException.printStackTrace();

            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not create modbus Slave");
            alert.setContentText("Failed to load the view for Slave editor creation window");
            alert.show();

            return null;
        }
    }

    public static @Nullable WindowFacade<ModbusSlaveEditorController> update(
            @NotNull WindowFacade<?> parent,
            @NotNull ModbusProxySlaveConfig config) {
        try {
            final WindowFacade<ModbusSlaveEditorController> windowFacade = WindowFacadeFactory.createPopup(
                    Objects.requireNonNull(ModbusSlaveEditorWindowFactory.class.getResource("view.fxml")),
                    parent, "Modify modbus client", ModbusSlaveEditorController.class);

            final var submissionCallback = new ModbusSlaveEditorSubmissionCallback(config);

            final var controller = windowFacade.controller();
            controller.setSubmissionCallback(submissionCallback);

            controller.setFieldValues(config);

            return windowFacade;
        } catch (IOException ioException) {
            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not update modbus Slave");
            alert.setContentText("Failed to load the view for Slave editor modifying window");
            alert.show();

            return null;
        }
    }

}
