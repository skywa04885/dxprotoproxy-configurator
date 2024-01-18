package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApiConfig;
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

public class ApiEditorController implements Initializable {
    @FXML
    public TextField nameTextField;
    @FXML
    public TextField httpVersionTextField;
    @FXML
    public Button addButton;
    @FXML
    public Button cancelButton;

    private final @Nullable HttpProxyApiConfig configApi;
    private final @NotNull IApiEditorValidationCallback validationCallback;
    private final @NotNull IApiEditorSubmissionCallback submissionCallback;
    private @Nullable ApiEditorWindow window;

    public ApiEditorController(@Nullable HttpProxyApiConfig configApi,
                               @NotNull IApiEditorValidationCallback validationCallback,
                               @NotNull IApiEditorSubmissionCallback submissionCallback) {
        this.configApi = configApi;
        this.validationCallback = validationCallback;
        this.submissionCallback = submissionCallback;
    }

    public void setWindow(@NotNull ApiEditorWindow window) {
        this.window = window;
    }

    private void showValidationErrorAlert(String error) {
        final var alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("Validation failure");
        alert.setHeaderText("Validation failure");
        alert.setContentText(error);

        alert.show();
    }

    /**
     * Gets called when the user wants to submit.
     * @param actionEvent the event triggering the submission.
     */
    private void submit(ActionEvent actionEvent) {
        assert window != null;

        // Gets all the values.
        final String name = nameTextField.getText();
        final String httpVersion = httpVersionTextField.getText();

        // Calls the validation callback.
        final String error = validationCallback.validate(name, httpVersion);
        if (error != null) {
            showValidationErrorAlert(error);
            return;
        }

        // Calls the submit callback.
        submissionCallback.submit(name, httpVersion);

        // Closes the stage.
        window.stage().close();
    }

    /**
     * Gets called when the user wants to cancel the creation of the api.
     * @param actionEvent the event that triggered the cancellation.
     */
    private void cancel(ActionEvent actionEvent) {
        assert window != null;

        window.stage().close();
    }

    private void initializeNameField() {
        if (configApi != null) {
            nameTextField.setText(configApi.name());
        }
    }

    private void initializeHttpVersion() {
        if (configApi != null) {
            httpVersionTextField.setText(configApi.httpVersion());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(this::submit);
        cancelButton.setOnAction(this::cancel);

        initializeNameField();
        initializeHttpVersion();
    }
}
