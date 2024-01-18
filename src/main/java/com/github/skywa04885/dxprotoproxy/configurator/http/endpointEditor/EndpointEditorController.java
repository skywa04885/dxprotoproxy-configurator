package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointConfig;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.ResourceBundle;

public class EndpointEditorController implements Initializable {
    // Text fields.
    @FXML
    public TextField nameTextField;

    // Buttons.
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;

    // Instance methods.
    private @Nullable HttpProxyEndpointConfig configEndpoint;
    private @Nullable EndpointEditorWindow window;
    private final @NotNull IEndpointEditorSaveCallback submissionCallback;
    private final @NotNull IEndpointEditorValidationCallback validationCallback;

    /**
     * Creates a new endpoint editor controller that has the given callbacks.
     * @param configEndpoint the endpoint we're possibly editing.
     * @param submissionCallback the submission callback.
     * @param validationCallback the validation callback.
     */
    public EndpointEditorController(@Nullable HttpProxyEndpointConfig configEndpoint,
                                    @NotNull IEndpointEditorSaveCallback submissionCallback,
                                    @NotNull IEndpointEditorValidationCallback validationCallback) {
        this.configEndpoint = configEndpoint;
        this.submissionCallback = submissionCallback;
        this.validationCallback = validationCallback;
    }

    /**
     * Sets the window.
     * @param window the window.
     */
    public void setWindow(@NotNull EndpointEditorWindow window) {
        this.window = window;
    }

    /**
     * Shows the validation error alert with the given message.
     * @param error error.
     */
    private void showValidationErrorAlert(String error) {
        final var alert = new Alert(Alert.AlertType.ERROR);

        final String title = "Invalid endpoint values";

        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(error);

        alert.show();
    }

    /**
     * Saves the endpoint.
     */
    private void save() {
        assert window != null;

        // Gets the values.
        final String name = nameTextField.getText();

        // Validates the values and shows the error if they're not valid.
        String error = validationCallback.validate(name);
        if (error != null) {
            showValidationErrorAlert(error);
            return;
        }

        // Calls the submission callback.
        submissionCallback.submit(name);

        // Closes the endpoint editor.
        window.stage().close();
    }

    /**
     * Gets called on the save button action.
     * @param actionEvent the action event.
     */
    private void onSaveButtonAction(ActionEvent actionEvent) {
        save();
    }

    /**
     * Cancels the editing of the endpoint.
     */
    private void cancel() {
        assert window != null;

        window.stage().close();
    }

    /**
     * Gets called on the cancel button action.
     * @param actionEvent the action event.
     */
    private void onCancelButtonAction(ActionEvent actionEvent) {
        cancel();
    }

    /**
     * Initializes the name text field.
     */
    private void initializeNameTextField() {
        if (configEndpoint != null) {
            nameTextField.setText(configEndpoint.name());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets the button action event handlers.
        saveButton.setOnAction(this::onSaveButtonAction);
        cancelButton.setOnAction(this::onCancelButtonAction);

        // Initializes other stuff.
        initializeNameTextField();
    }
}
