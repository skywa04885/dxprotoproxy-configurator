package com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor;

import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacade;
import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacadeFactory;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientConfig;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientsConfig;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class MQTTClientEditorWindowFactory {
    /**
     * Creates a new mqtt client editor window that's meant for creation of configurations.
     *
     * @param parent            the parent window.
     * @param mqttClientsConfig the clients-configuration.
     * @return the created window.
     * @throws IOException gets thrown if the resource could not be loaded.
     */
    public static @Nullable WindowFacade<MQTTClientEditorController> create(
            @NotNull WindowFacade<?> parent,
            @NotNull MqttProxyClientsConfig mqttClientsConfig) {
        // Attempts to create the window, shows an error otherwise.
        try {
            // Creates the window.
            final WindowFacade<MQTTClientEditorController> windowFacade = WindowFacadeFactory.createPopup(
                    Objects.requireNonNull(MQTTClientEditorWindowFactory.class.getResource("view.fxml")),
                    parent, "Create MQTT client", MQTTClientEditorController.class);

            // Creates the callbacks.
            final var submissionCallback = new MQTTClientEditorSubmissionCallback(mqttClientsConfig);

            // Sets the callbacks.
            final var controller = windowFacade.controller();
            controller.setSubmissionCallback(submissionCallback);

            // Returns the constructed window.
            return windowFacade;
        } catch (IOException ioException) {
            // Shows the alert indicating the failure of the loading.
            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not create mqtt client");
            alert.setContentText("Failed to load the view for client editor creation window");
            alert.show();

            // Returns null.
            return null;
        }
    }

    /**
     * Creates a new mqtt-client-editor window that's meant for the modification of an existing mqtt client.
     *
     * @param parent           the parent window.
     * @param mqttClientConfig the client-configuration to modify.
     * @return the created window.
     */

    public static @Nullable WindowFacade<MQTTClientEditorController> update(
            @NotNull WindowFacade<?> parent,
            @NotNull MqttProxyClientConfig mqttClientConfig) {
        // Attempts to create the window, shows an error otherwise.
        try {
            // Creates the window.
            final WindowFacade<MQTTClientEditorController> windowFacade = WindowFacadeFactory.createPopup(
                    Objects.requireNonNull(MQTTClientEditorWindowFactory.class.getResource("view.fxml")),
                    parent, "Modify MQTT client", MQTTClientEditorController.class);

            // Creates the callbacks.
            final var submissionCallback = new MQTTClientEditorSubmissionCallback(mqttClientConfig);

            // Sets the callbacks.
            final var controller = windowFacade.controller();
            controller.setSubmissionCallback(submissionCallback);

            // Set the field values.
            controller.setFieldValues(mqttClientConfig);

            // Returns the constructed window.
            return windowFacade;
        } catch (IOException ioException) {
            // Shows the alert indicating the failure of the loading.
            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not update mqtt client");
            alert.setContentText("Failed to load the view for client editor modifying window");
            alert.show();

            // Returns null.
            return null;
        }
    }

}
