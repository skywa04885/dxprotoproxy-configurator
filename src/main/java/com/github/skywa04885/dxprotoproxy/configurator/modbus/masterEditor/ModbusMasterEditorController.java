package com.github.skywa04885.dxprotoproxy.configurator.modbus.masterEditor;

import com.github.skywa04885.dxprotoproxy.common.javafx.ControllerFacade;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMasterConfig;
import com.github.skywa04885.dxprotoproxy.configurator.GlobalConstants;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ModbusMasterEditorController extends ControllerFacade implements Initializable {
    public static final int MASTER_PORT_INITIAL_VALUE = 40000;
    public static final String MASTER_ADDRESS_INITIAL_VALUE = "0.0.0.0";
    public static final int LISTEN_PORT_INITIAL_VALUE = 1883;
    public static final int PORT_STEP = 1;

    public @FXML TextField masterAddressTextField;
    public @FXML TextField listenAddressTextField;
    public @FXML Spinner<Integer> masterPortSpinner;
    public @FXML Spinner<Integer> listenPortSpinner;
    public @FXML Button cancelButton;
    public @FXML Button submitButton;

    private final @NotNull Validator validator = new Validator();
    private @Nullable IModbusMasterEditorSubmissionCallback submissionCallback;

    public void setSubmissionCallback(@NotNull IModbusMasterEditorSubmissionCallback submissionCallback) {
        this.submissionCallback = submissionCallback;
    }

    private void initializeMasterAddressTextField() {
        validator.createCheck()
                .dependsOn("masterAddress", masterAddressTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String masterAddress = context.get("masterAddress");

                    if (!GlobalConstants.HOSTNAME_PATTERN.matcher(masterAddress).matches()) {
                        context.error("Master address is not valid");
                    }
                })
                .decorates(masterAddressTextField)
                .immediate();
    }

    private void initializeMasterPortSpinner() {
        masterPortSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GlobalConstants.PORT_MIN,
                GlobalConstants.PORT_MAX, MASTER_PORT_INITIAL_VALUE, PORT_STEP));
    }

    private void initializeListenAddressTextField() {
        validator.createCheck()
                .dependsOn("listenAddress", listenAddressTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String listenAddress = context.get("listenAddress");

                    if (!GlobalConstants.HOSTNAME_PATTERN.matcher(listenAddress).matches()) {
                        context.error("Listen address is not valid");
                    }
                })
                .decorates(listenAddressTextField)
                .immediate();

        listenAddressTextField.setText(MASTER_ADDRESS_INITIAL_VALUE);
    }

    private void initializeListenPortSpinner() {
        listenPortSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GlobalConstants.PORT_MIN,
                GlobalConstants.PORT_MAX, LISTEN_PORT_INITIAL_VALUE, PORT_STEP));
    }

    public void setFieldValues(@NotNull ModbusProxyMasterConfig config) {
        masterAddressTextField.setText(config.serverAddress());
        masterPortSpinner.getValueFactory().setValue(config.serverPort());

        listenAddressTextField.setText(config.listenAddress());
        listenPortSpinner.getValueFactory().setValue(config.listenPort());
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
        final @NotNull String serverAddress = masterAddressTextField.getText();
        final int serverPort = masterPortSpinner.getValue();
        final @NotNull String listenAddress = listenAddressTextField.getText();
        final int listenPort = listenPortSpinner.getValue();

        // Submits the fields.
        assert submissionCallback != null;
        submissionCallback.submit(serverAddress, serverPort, listenAddress, listenPort);

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMasterAddressTextField();
        initializeMasterPortSpinner();
        initializeListenAddressTextField();
        initializeListenPortSpinner();
        initializeCancelButton();
        initializeSubmitButton();
    }
}
