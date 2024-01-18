package com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor;

import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacade;
import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacadeFactory;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMasterConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMastersConfig;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class ModbusMasterEditorWindowFactory {
    public static @Nullable WindowFacade<ModbusMasterEditorController> create(
            @NotNull WindowFacade<?> parent,
            @NotNull ModbusProxyMastersConfig mqttClientsConfig) {
        try {
            final WindowFacade<ModbusMasterEditorController> windowFacade = WindowFacadeFactory.createPopup(
                    Objects.requireNonNull(ModbusMasterEditorWindowFactory.class.getResource("view.fxml")),
                    parent, "Create modbus client", ModbusMasterEditorController.class);

            final var submissionCallback = new ModbusMasterEditorSubmissionCallback(mqttClientsConfig);

            final var controller = windowFacade.controller();
            controller.setSubmissionCallback(submissionCallback);

            return windowFacade;
        } catch (IOException ioException) {
            ioException.printStackTrace();

            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not create modbus master");
            alert.setContentText("Failed to load the view for master editor creation window");
            alert.show();

            return null;
        }
    }

    public static @Nullable WindowFacade<ModbusMasterEditorController> update(
            @NotNull WindowFacade<?> parent,
            @NotNull ModbusProxyMasterConfig config) {
        try {
            final WindowFacade<ModbusMasterEditorController> windowFacade = WindowFacadeFactory.createPopup(
                    Objects.requireNonNull(ModbusMasterEditorWindowFactory.class.getResource("view.fxml")),
                    parent, "Modify modbus client", ModbusMasterEditorController.class);

            final var submissionCallback = new ModbusMasterEditorSubmissionCallback(config);

            final var controller = windowFacade.controller();
            controller.setSubmissionCallback(submissionCallback);

            controller.setFieldValues(config);

            return windowFacade;
        } catch (IOException ioException) {
            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not update modbus master");
            alert.setContentText("Failed to load the view for master editor modifying window");
            alert.show();

            return null;
        }
    }

}
