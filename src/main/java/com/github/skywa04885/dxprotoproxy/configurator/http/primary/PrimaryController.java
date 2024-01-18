package com.github.skywa04885.dxprotoproxy.configurator.http.primary;

import com.github.skywa04885.dxprotoproxy.common.config.ProxyConfig;
import com.github.skywa04885.dxprotoproxy.common.config.ProxyConfigReader;
import com.github.skywa04885.dxprotoproxy.common.config.ProxyConfigWriter;
import com.github.skywa04885.dxprotoproxy.common.http.config.*;
import com.github.skywa04885.dxprotoproxy.common.javafx.ControllerFacade;
import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacade;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMasterConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMastersConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlaveConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlavesConfig;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientConfig;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientsConfig;
import com.github.skywa04885.dxprotoproxy.configurator.IDXTreeItem;
import com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor.EndpointEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.http.primary.tree.*;
import com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor.RequestEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor.ApiEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor.InstanceEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor.ResponseEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor.ModbusMasterEditorController;
import com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor.ModbusMasterEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor.ModbusSlaveEditorController;
import com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor.ModbusSlaveEditorWindowFactory;
import com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor.MQTTClientEditorController;
import com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor.MQTTClientEditorWindowFactory;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PrimaryController extends ControllerFacade implements Initializable {
    private final @NotNull SimpleObjectProperty<File> configFile;

    private final @NotNull SimpleObjectProperty<ProxyConfig> config;

    @FXML
    public TreeView<IDXTreeItem> endpointsTreeView;

    public @FXML MenuItem saveConfigAsMenuItem;
    public @FXML MenuItem saveConfigMenuItem;

    private final @NotNull PrimaryTreeClipboard treeClipboard = new PrimaryTreeClipboard();
    private final @NotNull FileChooser fileChooser = new FileChooser();

    public PrimaryController() {
        configFile = new SimpleObjectProperty<>(null);
        config = new SimpleObjectProperty<>(null, "Configuration", new ProxyConfig());
    }

    public @NotNull ProxyConfig configRoot() {
        return config.getValue();
    }

    @FXML
    public void onImportOpenAPISpecificationMenuItemPressed() {
    }

    @FXML
    public void onOpenConfigMenuItemPressed() {
        assert window != null;

        final File file = fileChooser.showOpenDialog(window.stage());
        if (file == null) {
            return;
        }

        try {
            config.set(ProxyConfigReader.read(file));
        } catch (IOException | ParserConfigurationException | SAXException ioException) {

        }

        configFile.set(file);
    }

    private void asd() {
        final var callbacks = new IPrimaryTreeContextMenuCallbacks() {
            /**
             * Creates a new response.
             * @param configRequest the request to which the response will belong.
             */
            @Override
            public void createResponse(HttpProxyRequestConfig configRequest) {
                ResponseEditorWindowFactory.create(configRequest);
            }

            @Override
            public void createApi(@NotNull HttpProxyApisConfig httpConfigApis) {
                ApiEditorWindowFactory.create(httpConfigApis);
            }

            @Override
            public void modifyApi(@NotNull HttpProxyApiConfig httpConfigApi) {
                ApiEditorWindowFactory.create(httpConfigApi);
            }

            @Override
            public void deleteApi(@NotNull HttpProxyApiConfig httpConfigApi) {
                Objects.requireNonNull(httpConfigApi.parent())
                        .children()
                        .remove(httpConfigApi.name());
            }

            @Override
            public void createInstance(@NotNull HttpProxyInstancesConfig httpConfigInstances) {
                InstanceEditorWindowFactory.create(httpConfigInstances);
            }

            @Override
            public void modifyInstance(@NotNull HttpProxyInstanceConfig httpConfigInstance) {
                InstanceEditorWindowFactory.modify(httpConfigInstance);
            }

            @Override
            public void deleteInstance(@NotNull HttpProxyInstanceConfig configInstance) {
                Objects.requireNonNull(configInstance.parent())
                        .children()
                        .remove(configInstance.name());
            }

            @Override
            public void createEndpoint(@NotNull HttpProxyEndpointsConfig httpConfigEndpoints) {
                EndpointEditorWindowFactory.create(httpConfigEndpoints);
            }

            /**
             * Deletes the given endpoint.
             *
             * @param configEndpoint the endpoint to delete.
             */
            @Override
            public void deleteEndpoint(@NotNull HttpProxyEndpointConfig configEndpoint) {
                Objects.requireNonNull(configEndpoint.parent())
                        .children()
                        .remove(configEndpoint.name());
            }

            /**
             * Modifies the given endpoint.
             *
             * @param configEndpoint the endpoint to modify.
             */
            @Override
            public void modifyEndpoint(@NotNull HttpProxyEndpointConfig configEndpoint) {
                EndpointEditorWindowFactory.modify(configEndpoint);
            }


            @Override
            public void createRequest(@NotNull HttpProxyEndpointConfig httpConfigEndpoint) {
                RequestEditorWindowFactory.create(httpConfigEndpoint);
            }

            /**
             * Opens the window that will modify the given response.
             * @param configResponse the response that needs to be modified.
             */
            @Override
            public void modifyResponse(@NotNull HttpProxyResponseConfig configResponse) {
                ResponseEditorWindowFactory.modify(configResponse);
            }

            /**
             * Deletes the given response.
             * @param configResponse the response that needs to be deleted.
             */
            @Override
            public void deleteResponse(@NotNull HttpProxyResponseConfig configResponse) {
                configResponse
                        .parent()
                        .children()
                        .remove(configResponse.code());
            }

            /**
             * Deletes the given config request.
             * @param configRequest the request that needs to be deleted.
             */
            @Override
            public void deleteRequest(@NotNull HttpProxyRequestConfig configRequest) {
                configRequest
                        .parent()
                        .requests()
                        .remove(configRequest.method());
            }

            /**
             * Opens the modification window for the given config request.
             * @param configRequest the request that needs to be modified.
             */
            @Override
            public void modifyRequest(@NotNull HttpProxyRequestConfig configRequest) {
                RequestEditorWindowFactory.modify(configRequest);
            }

            @Override
            public void createMqttClient(@NotNull MqttProxyClientsConfig mqttClientsConfig) {
                final @Nullable WindowFacade<MQTTClientEditorController> mqttEditorWindow =
                    MQTTClientEditorWindowFactory.create(Objects.requireNonNull(window), mqttClientsConfig);

                if (mqttEditorWindow == null) {
                    return;
                }

                mqttEditorWindow.stage().show();
            }

            @Override
            public void deleteMqttClient(@NotNull MqttProxyClientConfig mqttClientConfig) {
                Objects.requireNonNull(mqttClientConfig.parent()).children().remove(mqttClientConfig);
            }

            @Override
            public void modifyMqttClient(@NotNull MqttProxyClientConfig mqttClientConfig) {
                final @Nullable WindowFacade<MQTTClientEditorController> mqttEditorWindow =
                        MQTTClientEditorWindowFactory.update(Objects.requireNonNull(window), mqttClientConfig);

                if (mqttEditorWindow == null) {
                    return;
                }

                mqttEditorWindow.stage().show();
            }

            @Override
            public void createModbusSlave(@NotNull ModbusProxySlavesConfig modbusSlavesConfig) {
                final @Nullable WindowFacade<ModbusSlaveEditorController> mqttEditorWindow =
                        ModbusSlaveEditorWindowFactory.create(Objects.requireNonNull(window), modbusSlavesConfig);

                if (mqttEditorWindow == null) return;

                mqttEditorWindow.stage().show();
            }

            @Override
            public void createModbusMaster(@NotNull ModbusProxyMastersConfig config) {
                final @Nullable WindowFacade<ModbusMasterEditorController> mqttEditorWindow =
                        ModbusMasterEditorWindowFactory.create(Objects.requireNonNull(window), config);

                if (mqttEditorWindow == null) return;

                mqttEditorWindow.stage().show();
            }

            @Override
            public void modifyModbusMaster(@NotNull ModbusProxyMasterConfig modbusMasterConfig) {
                final @Nullable WindowFacade<ModbusMasterEditorController> mqttEditorWindow =
                        ModbusMasterEditorWindowFactory.update(Objects.requireNonNull(window), modbusMasterConfig);

                if (mqttEditorWindow == null) return;

                mqttEditorWindow.stage().show();
            }

            @Override
            public void deleteModbusMaster(@NotNull ModbusProxyMasterConfig modbusMasterConfig) {
                Objects.requireNonNull(modbusMasterConfig.parent()).children().remove(modbusMasterConfig);
            }

            @Override
            public void modifyModbusSlave(@NotNull ModbusProxySlaveConfig modbusSlaveConfig) {
                final @Nullable WindowFacade<ModbusSlaveEditorController> mqttEditorWindow =
                        ModbusSlaveEditorWindowFactory.update(Objects.requireNonNull(window), modbusSlaveConfig);

                if (mqttEditorWindow == null) return;

                mqttEditorWindow.stage().show();
            }

            @Override
            public void deleteModbusSlave(@NotNull ModbusProxySlaveConfig modbusSlaveConfig) {
                Objects.requireNonNull(modbusSlaveConfig.parent()).children().remove(modbusSlaveConfig);

            }
        };

        final var contextMenuFactory = new PrimaryTreeContextMenuFactory(callbacks, treeClipboard);

        final var treeViewCellFactory = new PrimaryTreeViewCellFactory(contextMenuFactory);

        endpointsTreeView.setCellFactory(treeViewCellFactory);

        final var treeFactory = new PrimaryTreeFactory();

        endpointsTreeView.setRoot(treeFactory.createForConfigRoot(configRoot()));
    }

    /**
     * Gets called when the config has changed.
     *
     * @param observableValue The observable config.
     * @param oldConfig       The old config.
     * @param newConfig       The new config.
     */
    private void onConfigChangedListener(ObservableValue<? extends ProxyConfig> observableValue, ProxyConfig oldConfig, ProxyConfig newConfig) {
        asd();
    }

    private void writeToConfigFile() {
        try {
            ProxyConfigWriter.write(config.get(), configFile.get());
        } catch (ParserConfigurationException | TransformerException | IOException exception) {
            final var alert = new Alert(Alert.AlertType.ERROR);

            alert.setHeaderText("Failed to write config");
            alert.setContentText(exception.getMessage());

            alert.show();
        }
    }

    private void saveConfigAsMenuItemActionHandler(@NotNull ActionEvent actionEvent) {
        assert window != null;

        final File file = fileChooser.showSaveDialog(window.stage());
        if (file == null) {
            return;
        }

        configFile.set(file);

        writeToConfigFile();
    }

    private void initializeFileChooser() {
        final var fileChooserExtensionFilter = new FileChooser.ExtensionFilter("Config file (*.xml)",
                "*.xml");

        fileChooser
                .setSelectedExtensionFilter(fileChooserExtensionFilter);
    }

    private void initializeSaveConfigAsMenuItem() {
        saveConfigAsMenuItem
                .setOnAction(this::saveConfigAsMenuItemActionHandler);
    }

    private void saveConfigActionHandler(@NotNull ActionEvent actionEvent) {
        // If the config file has not been set yet, call the save as action handler.
        if (configFile.get() == null) {
            saveConfigAsMenuItemActionHandler(actionEvent);
            return;
        }

        // Writes to the config file.
        writeToConfigFile();
    }

    private void initializeSaveConfigMenuItem() {
        saveConfigMenuItem
                .setOnAction(this::saveConfigActionHandler);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        config.addListener(this::onConfigChangedListener);
        asd();

        initializeFileChooser();
        initializeSaveConfigAsMenuItem();
        initializeSaveConfigMenuItem();
    }
}