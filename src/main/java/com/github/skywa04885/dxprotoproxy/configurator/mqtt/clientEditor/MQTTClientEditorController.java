package com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor;

import com.github.skywa04885.dxprotoproxy.common.javafx.ControllerFacade;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientConfig;
import com.github.skywa04885.dxprotoproxy.configurator.GlobalConstants;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MQTTClientEditorController extends ControllerFacade implements Initializable {
    /**
     * Static constants.
     */
    public static final int CLIENT_PORT_INITIAL_VALUE = 40000;
    public static final int BROKER_PORT_INITIAL_VALUE = 1883;
    public static final int PORT_STEP = 1;

    /**
     * Member variables for FXML elements.
     */
    public @FXML TextField clientHostnameTextField;
    public @FXML Spinner<Integer> clientPortSpinner;
    public @FXML TextField brokerHostnameTextField;
    public @FXML Spinner<Integer> brokerPortSpinner;
    public @FXML TextField usernameTextField;
    public @FXML TextField passwordTextField;
    public @FXML TextField clientIdentifierTextField;
    public @FXML ListView<String> topicsListView;
    public @FXML Button cancelButton;
    public @FXML Button submitButton;
    /**
     * Member variables.
     */
    final @NotNull Validator validator = new Validator();
    private @Nullable IMQTTClientEditorSubmissionCallback submissionCallback;

    public void setSubmissionCallback(@NotNull IMQTTClientEditorSubmissionCallback submissionCallback) {
        this.submissionCallback = submissionCallback;
    }

    /**
     * Updates the topics list view by removing blank ones and creating a blank one
     * at the end.
     */
    void updateTopicsListView() {
        // Removes all the blank topics.
        topicsListView.getItems().removeIf(String::isBlank);

        // Adds a new blank topic to the end (for when the user wants to create a new one).
        topicsListView.getItems().add("");
    }

    /**
     * Initializes the client hostname text field.
     */
    private void initializeClientHostnameTextField() {
        validator.createCheck()
                .dependsOn("clientHostname", clientHostnameTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String clientHostname = context.get("clientHostname");

                    if (!GlobalConstants.HOSTNAME_PATTERN.matcher(clientHostname).matches()) {
                        context.error("Client hostname is not valid");
                    }
                })
                .decorates(clientHostnameTextField)
                .immediate();
    }

    /**
     * Initializes the client port spinner.
     */
    private void initializeClientPortSpinner() {
        // Sets the value factory.
        clientPortSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GlobalConstants.PORT_MIN,
                GlobalConstants.PORT_MAX, CLIENT_PORT_INITIAL_VALUE, PORT_STEP));
    }

    /**
     * Initializes the broker hostname text field.
     */
    private void initializeBrokerHostnameTextField() {
        validator.createCheck()
                .dependsOn("brokerHostname", brokerHostnameTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String brokerHostname = context.get("brokerHostname");

                    if (!GlobalConstants.HOSTNAME_PATTERN.matcher(brokerHostname).matches()) {
                        context.error("Broker hostname is not valid");
                    }
                })
                .decorates(brokerHostnameTextField)
                .immediate();
    }

    /**
     * Initializes the broker port spinner.
     */
    private void initializeBrokerPortSpinner() {
        // Sets the value factory.
        brokerPortSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GlobalConstants.PORT_MIN,
                GlobalConstants.PORT_MAX, BROKER_PORT_INITIAL_VALUE, PORT_STEP));
    }

    /**
     * Initializes the topics list view.
     */
    private void initializeTopicsListView() {
        // Sets the cell factory.
        topicsListView.setCellFactory(t -> new MQTTClientEditorTopicListCell(this));

        // Make the list editable.
        topicsListView.setEditable(true);

        // Updates the topics list view.
        updateTopicsListView();
    }

    /**
     * Initializes the username text field.
     */
    private void initializeUsernameTextField() {
        validator.createCheck()
                .dependsOn("username", usernameTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String username = context.get("username");

                    if (username.isBlank()) {
                        return;
                    }

                    if (!GlobalConstants.MQTT_USERNAME.matcher(username).matches()) {
                        context.error("Username format is not valid");
                    }
                })
                .decorates(usernameTextField)
                .immediate();
    }

    /**
     * Initializes the password text field.
     */
    private void initializePasswordTextField() {
        validator.createCheck()
                .dependsOn("password", passwordTextField.textProperty())
                .withMethod(c -> {})
                .decorates(passwordTextField)
                .immediate();
    }


    /**
     * Initializes the client identifier text field.
     */
    private void initializeClientIdentifierTextField() {
        validator.createCheck()
                .dependsOn("clientIdentifier", clientIdentifierTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String clientIdentifier = context.get("clientIdentifier");

                    if (clientIdentifier.isBlank()) {
                        return;
                    }

                    if (!GlobalConstants.MQTT_CLIENT_IDENTIFIER.matcher(clientIdentifier).matches()) {
                        context.error("Client identifier format is not valid");
                    }
                })
                .decorates(clientIdentifierTextField)
                .immediate();
    }

    /**
     * Cancels the editing.
     */
    private void cancel() {
        // Closes the window.
        Objects.requireNonNull(window).close();
    }

    /**
     * The cancel button event handler.
     *
     * @param actionEvent the action event.
     */
    private void cancelButtonActionEventHandler(@NotNull ActionEvent actionEvent) {
        cancel();
    }

    /**
     * Initializes the cancel button.
     */
    private void initializeCancelButton() {
        cancelButton.setOnAction(this::cancelButtonActionEventHandler);
    }

    /**
     * Submits.
     */
    private void submit() {
        // Gets the fields.
        final @NotNull String clientHostname = clientHostnameTextField.getText();
        final int clientPort = Objects.requireNonNull(clientPortSpinner.getValue());
        final @NotNull String brokerHostname = brokerHostnameTextField.getText();
        final int brokerPort = Objects.requireNonNull(brokerPortSpinner.getValue());
        final @Nullable String username = usernameTextField.getText().isBlank()
                ? null : usernameTextField.getText();
        final @Nullable String password = passwordTextField.getText().isBlank()
                ? null : passwordTextField.getText();
        final @Nullable String clientIdentifier = clientIdentifierTextField.getText().isBlank()
                ? null : clientIdentifierTextField.getText();
        final @NotNull List<String> topicsList = topicsListView
                .getItems()
                .stream()
                .filter(t -> !t.isBlank())
                .collect(Collectors.toList());

        // Submits the fields.
        assert submissionCallback != null;
        submissionCallback.submit(clientHostname, clientPort, brokerHostname,
                brokerPort, username, password, clientIdentifier, topicsList);

        // Closes the window.
        Objects.requireNonNull(window).close();
    }

    /**
     * The submit button event handler.
     *
     * @param actionEvent the action event.
     */
    private void submitButtonActionEventHandler(@NotNull ActionEvent actionEvent) {
        submit();
    }

    /**
     * Initializes the submit button.
     */
    private void initializeSubmitButton() {
        submitButton.setOnAction(this::submitButtonActionEventHandler);

        TooltipWrapper<Button> signUpWrapper = new TooltipWrapper<>(
                submitButton,
                validator.containsErrorsProperty(),
                Bindings.concat("Cannot create mqtt client:\n", validator.createStringBinding())
        );
    }

    public void setFieldValues(@NotNull MqttProxyClientConfig mqttClientConfig) {
        // Set the client hostname.
        clientHostnameTextField.setText(mqttClientConfig.clientHostname());

        // Set the client port.
        clientPortSpinner.getValueFactory().setValue(mqttClientConfig.clientPort());

        // Set the broker hostname.
        brokerHostnameTextField.setText(mqttClientConfig.brokerHostname());

        // Set the port.
        brokerPortSpinner.getValueFactory().setValue(mqttClientConfig.brokerPort());

        // Set the topics value.
        topicsListView.getItems().addAll(Objects.requireNonNull(mqttClientConfig).topics());
        updateTopicsListView();

        // Set the username value.
        if (mqttClientConfig.username() != null) {
            usernameTextField.setText(mqttClientConfig.username());
        }

        // Set the password value.
        if (mqttClientConfig.password() != null) {
            passwordTextField.setText(mqttClientConfig.password());
        }

        // Set the client identifier value.
        if (mqttClientConfig.clientIdentifier() != null) {
            clientIdentifierTextField.setText(mqttClientConfig.clientIdentifier());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeClientHostnameTextField();
        initializeClientPortSpinner();
        initializeBrokerHostnameTextField();
        initializeBrokerPortSpinner();
        initializeUsernameTextField();
        initializePasswordTextField();
        initializeClientIdentifierTextField();
        initializeTopicsListView();
        initializeCancelButton();
        initializeSubmitButton();
    }
}
