package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyInstanceConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ResourceBundle;

public class InstanceEditorController implements Initializable {
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField hostTextField;
    @FXML
    public Spinner<Integer> portSpinner;
    @FXML
    public TextField protocolTextField;

    private @Nullable InstanceEditorWindow window;
    private final @Nullable HttpProxyInstanceConfig configInstance;
    private final @NotNull IInstanceEditorValidationCallback validationCallback;
    private final @NotNull IInstanceEditorSubmissionCallback submissionCallback;

    public InstanceEditorController(@Nullable HttpProxyInstanceConfig configInstance,
                                    @NotNull IInstanceEditorValidationCallback validationCallback,
                                    @NotNull IInstanceEditorSubmissionCallback submissionCallback) {
        this.configInstance = configInstance;
        this.validationCallback = validationCallback;
        this.submissionCallback = submissionCallback;
    }

    public void setWindow(@NotNull InstanceEditorWindow window) {
        this.window = window;
    }

    /**
     * Gets called when the user wants to cancel the instance creation.
     * @param actionEvent the event triggering the cancellation.
     */
    @FXML
    public void cancel(ActionEvent actionEvent) {
        assert window != null;

        window.stage().close();
    }

    /**
     * Shows the validation error alert with the given error.
     * @param error the error message.
     */
    private void showValidationErrorAlert(String error) {
        final var alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Validation failure");
        alert.setHeaderText("Validation failure");
        alert.setContentText(error);

        alert.show();
    }

    /**
     * Gets called when the user wants to submit the instance.
     * @param actionEvent the event triggering the submission.
     */
    @FXML
    public void submit(ActionEvent actionEvent) {
        assert window != null;

        // Get all the values.
        final String name = nameTextField.getText();
        final String host = hostTextField.getText();
        final int port = portSpinner.getValue();
        final String protocol = protocolTextField.getText();

        // Calls the validation callback.
        final String error = validationCallback.validate(name, host, port, protocol);
        if (error != null) {
            showValidationErrorAlert(error);
            return;
        }

        // Calls the submit callback.
        submissionCallback.submit(name, host, port, protocol);

        // Closes the stage.
        window.stage().close();
    }

    private void initializeNameTextField() {
        if (configInstance != null) {
            nameTextField.setText(configInstance.name());
        }
    }

    private void initializeHostTextfield() {
        if (configInstance != null) {
            hostTextField.setText(configInstance.host());
        }
    }

    private void initializePortSpinner() {
        if (configInstance != null) {
            portSpinner.getValueFactory().setValue(configInstance.port());
        } else {
            portSpinner.getValueFactory().setValue(80);
        }
    }

    private void initializeProtocolTextField() {
        if (configInstance != null) {
            protocolTextField.setText(configInstance.protocol());
        } else {
            protocolTextField.setText("http");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNameTextField();
        initializeHostTextfield();
        initializePortSpinner();
        initializeProtocolTextField();
    }
}
