package com.github.skywa04885.dxprotoproxy.configurator.modbus.slaveEditor;

import com.github.skywa04885.dxprotoproxy.common.javafx.ControllerFacade;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlaveConfig;
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

public class ModbusSlaveEditorController extends ControllerFacade implements Initializable {
    public static final int CONTROLLER_PORT_INITIAL_VALUE = 40000;
    public static final String MASTER_ADDRESS_INITIAL_VALUE = "127.0.0.1";
    public static final int MASTER_PORT_INITIAL_VALUE = 1883;
    public static final int PORT_STEP = 1;

    public @FXML TextField controllerAddressTextField;
    public @FXML TextField masterAddressTextField;
    public @FXML Spinner<Integer> controllerPortSpinner;
    public @FXML Spinner<Integer> masterPortSpinner;
    public @FXML Button cancelButton;
    public @FXML Button submitButton;

    private final @NotNull Validator validator = new Validator();
    private @Nullable IModbusSlaveEditorSubmissionCallback submissionCallback;

    public void setSubmissionCallback(@NotNull IModbusSlaveEditorSubmissionCallback submissionCallback) {
        this.submissionCallback = submissionCallback;
    }

    private void initializeControllerAddressTextField() {
        validator.createCheck()
                .dependsOn("controllerAddress", controllerAddressTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String controllerAddress = context.get("controllerAddress");

                    if (!GlobalConstants.HOSTNAME_PATTERN.matcher(controllerAddress).matches()) {
                        context.error("Controller address is not valid");
                    }
                })
                .decorates(controllerAddressTextField)
                .immediate();
    }

    private void initializeControllerPortSpinner() {
        controllerPortSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GlobalConstants.PORT_MIN,
                GlobalConstants.PORT_MAX, CONTROLLER_PORT_INITIAL_VALUE, PORT_STEP));
    }

    private void initializeSlaveAddressTextField() {
        validator.createCheck()
                .dependsOn("slaveAddress", masterAddressTextField.textProperty())
                .withMethod(context -> {
                    final @NotNull String slaveAddress = context.get("slaveAddress");

                    if (!GlobalConstants.HOSTNAME_PATTERN.matcher(slaveAddress).matches()) {
                        context.error("Slave address is not valid");
                    }
                })
                .decorates(masterAddressTextField)
                .immediate();

        masterAddressTextField.setText(MASTER_ADDRESS_INITIAL_VALUE);
    }

    private void initializeSlavePortSpinner() {
        masterPortSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(GlobalConstants.PORT_MIN,
                GlobalConstants.PORT_MAX, MASTER_PORT_INITIAL_VALUE, PORT_STEP));
    }

    public void setFieldValues(@NotNull ModbusProxySlaveConfig config) {
        controllerAddressTextField.setText(config.controllerAddress());
        controllerPortSpinner.getValueFactory().setValue(config.controllerPort());

        masterAddressTextField.setText(config.masterAddress());
        masterPortSpinner.getValueFactory().setValue(config.masterPort());
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
        final @NotNull String controllerAddress = controllerAddressTextField.getText();
        final int controllerPort = controllerPortSpinner.getValue();
        final @NotNull String masterAddress = masterAddressTextField.getText();
        final int masterPort = masterPortSpinner.getValue();

        assert submissionCallback != null;
        submissionCallback.submit(controllerAddress, controllerPort, masterAddress, masterPort);

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
        initializeControllerAddressTextField();
        initializeControllerPortSpinner();
        initializeSlaveAddressTextField();
        initializeSlavePortSpinner();
        initializeCancelButton();
        initializeSubmitButton();
    }
}
