package com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyRequestMethod;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyRequestConfig;
import com.github.skywa04885.dxprotoproxy.configurator.http.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class RequestEditorController implements Initializable {
    // Headers table view.
    @FXML
    public TableView<EditorHeader> headersTableView;
    @FXML
    public TableColumn<EditorHeader, String> headersTableViewKeyColumn;
    @FXML
    public TableColumn<EditorHeader, String> headersTableViewNameColumn;
    @FXML
    public TableColumn<EditorHeader, String> headersTableViewValueColumn;

    // Fields table view.
    @FXML
    public TableView<EditorField> bodyTableView;
    @FXML
    public TableColumn<EditorField, String> bodyTableViewFieldColumn;
    @FXML
    public TableColumn<EditorField, String> bodyTableViewNameColumn;
    @FXML
    public TableColumn<EditorField, String> bodyTableViewValueColumn;

    // Query parameters table view.
    @FXML
    public TableView<EditorQueryParameter> uriQueryParametersTableView;
    @FXML
    public TableColumn<EditorQueryParameter, String> uriQueryParametersTableViewKeyColumn;
    @FXML
    public TableColumn<EditorQueryParameter, String> uriQueryParametersTableViewValueColumn;

    // Text fields.
    @FXML
    public TextField pathTextField;

    // Combo boxes.
    @FXML
    public ComboBox<HttpProxyRequestMethod> methodComboBox;
    @FXML
    public ComboBox<HttpProxyFieldsFormat> formatComboBox;

    // Buttons.
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;

    // Instance variables.
    @Nullable
    private final HttpProxyRequestConfig configRequest;
    @NotNull
    private final IRequestEditorSubmissionCallback submissionCallback;
    @NotNull
    private final IRequestEditorValidationCallback validationCallback;
    @Nullable
    private RequestEditorWindow window;

    /**
     * Creates a new request editor controller with the given callbacks.
     *
     * @param configRequest      the existing request.
     * @param submissionCallback the submission callback.
     * @param validationCallback the validation callback.
     */
    public RequestEditorController(@Nullable HttpProxyRequestConfig configRequest,
                                   @NotNull IRequestEditorSubmissionCallback submissionCallback,
                                   @NotNull IRequestEditorValidationCallback validationCallback) {
        this.configRequest = configRequest;
        this.submissionCallback = submissionCallback;
        this.validationCallback = validationCallback;
    }

    /**
     * Shows the validation error alert with the given message.
     *
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
     * Saves the edited request.
     */
    private void save() {
        assert window != null;

        // Gets all the values.
        final String pathTemplate = pathTextField.getText();
        final HttpProxyRequestMethod requestMethod = methodComboBox.getValue();
        final HttpProxyFieldsFormat bodyFormat = formatComboBox.getValue();
        final List<EditorField> fields = bodyTableView.getItems().stream()
                .filter(field -> !field.isBlank()).toList();
        final List<EditorHeader> headers = headersTableView.getItems().stream()
                .filter(field -> !field.isBlank()).toList();
        final List<EditorQueryParameter> queryParameters = uriQueryParametersTableView.getItems().stream()
                .filter(field -> !field.isEmpty()).toList();

        // Validates the values.
        final String error = validationCallback.validate(pathTemplate, queryParameters, requestMethod,
                headers, fields, bodyFormat);

        // Shows the error if it's there.
        if (error != null) {
            showValidationErrorAlert(error);
            return;
        }

        // Submits the request.
        submissionCallback.submit(pathTemplate, queryParameters, requestMethod, headers, fields, bodyFormat);

        // Closes the editor.
        window.stage().close();
    }

    /**
     * Gets called on the save button action.
     *
     * @param actionEvent the action event.
     */
    private void onSaveButtonAction(ActionEvent actionEvent) {
        save();
    }

    /**
     * Cancels the editing of the request.
     */
    private void cancel() {
        assert window != null;

        window.stage().close();
    }

    /**
     * Sets the request editor window.
     *
     * @param window the request editor window.
     */
    public void setWindow(@NotNull RequestEditorWindow window) {
        this.window = window;
    }

    /**
     * Gets called on cancel button action.
     *
     * @param actionEvent the action event.
     */
    private void onCancelButtonAction(ActionEvent actionEvent) {
        cancel();
    }

    /**
     * Initializes the method combo box.
     */
    private void initializeMethodComboBox() {
        // Sets the items of the method combo box and sets get as the default method.
        methodComboBox.setItems(FXCollections.observableList(Arrays.stream(HttpProxyRequestMethod.values()).toList()));

        // Sets the default method.
        if (configRequest != null) {
            methodComboBox.getSelectionModel().select(configRequest.method());
        } else {
            methodComboBox.getSelectionModel().select(HttpProxyRequestMethod.Get);
        }
    }

    /**
     * Initializes the format combo box.
     */
    private void initializeFormatComboBox() {
        // Sets the items of the format combo box and sets json as the default format.
        formatComboBox.setItems(FXCollections.observableList(Arrays.stream(HttpProxyFieldsFormat.values()).toList()));

        // Sets the default format.
        if (configRequest != null) {
            formatComboBox.getSelectionModel().select(configRequest.fields().format());
        } else {
            formatComboBox.getSelectionModel().select(HttpProxyFieldsFormat.JSON);
        }
    }

    /**
     * Updates the headers table (removing empty ones and making sure the last one is meant for creation).
     */
    private void updateHeadersTableView() {
        headersTableView.getItems().removeIf(EditorHeader::isBlank);
        headersTableView.getItems().add(new EditorHeader());
    }

    /**
     * Updates the body table view (removing empty ones and making sure the last one is meant for creation).
     */
    private void updateBodyTableView() {
        bodyTableView.getItems().removeIf(EditorField::isBlank);
        bodyTableView.getItems().add(new EditorField());
    }

    /**
     * Updates the uri query parameters table view (removing empty ones and making sure the last one is meant for
     * creation).
     */
    private void updateUriQueryParametersTableView() {
        uriQueryParametersTableView.getItems().removeIf(EditorQueryParameter::isEmpty);
        uriQueryParametersTableView.getItems().add(new EditorQueryParameter());
    }

    /**
     * Gets called when a value in the key column was changed.
     *
     * @param event the event.
     */
    private void onHeadersTableViewKeyColumnEditCommit(TableColumn.CellEditEvent<EditorHeader, String> event) {
        event.getRowValue().setKey(event.getNewValue());

        updateHeadersTableView();
    }

    /**
     * Gets called when a value in the value column was changed.
     *
     * @param event the event.
     */
    private void onHeadersTableViewValueColumnEditCommit(TableColumn.CellEditEvent<EditorHeader, String> event) {
        event.getRowValue().setValue(event.getNewValue());

        updateHeadersTableView();
    }

    /**
     * Gets called when a value in the name column was changed.
     *
     * @param event the event.
     */
    private void onHeadersTableViewNameColumnEditCommit(TableColumn.CellEditEvent<EditorHeader, String> event) {
        event.getRowValue().setName(event.getNewValue());

        updateHeadersTableView();
    }

    /**
     * Gets called when a value in the field column was changed.
     *
     * @param event the event.
     */
    private void onBodyTableViewFieldColumnEditCommit(TableColumn.CellEditEvent<EditorField, String> event) {
        event.getRowValue().setPath(event.getNewValue());

        updateBodyTableView();
    }

    /**
     * Gets called when a value in the value column was changed.
     *
     * @param event the event.
     */
    private void onBodyTableViewValueColumnEditCommit(TableColumn.CellEditEvent<EditorField, String> event) {
        event.getRowValue().setValue(event.getNewValue());

        updateBodyTableView();
    }

    /**
     * Gets called when a value in the name column was changed.
     *
     * @param event the event.
     */
    private void onBodyTableViewNameColumnEditCommit(TableColumn.CellEditEvent<EditorField, String> event) {
        event.getRowValue().setName(event.getNewValue());

        updateBodyTableView();
    }

    /**
     * Gets called when a value in the value key was changed.
     *
     * @param event the event.
     */
    private void onUriQueryParametersTableViewKeyColumnEditCommit(TableColumn.CellEditEvent<EditorQueryParameter, String> event) {
        event.getRowValue().setKey(event.getNewValue());

        updateUriQueryParametersTableView();
    }

    /**
     * Gets called when a value in the value column was changed.
     *
     * @param event the event.
     */
    private void onUriQueryParametersTableViewValueColumnEditCommit(TableColumn.CellEditEvent<EditorQueryParameter, String> event) {
        event.getRowValue().setValue(event.getNewValue());

        updateUriQueryParametersTableView();
    }

    /**
     * Gets called when a key has been pressed on the headers table.
     *
     * @param keyEvent the key event.
     */
    private void onHeadersTableViewKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            headersTableView.getItems().removeAll(headersTableView.getSelectionModel().getSelectedItems().stream()
                    .toList());

            updateHeadersTableView();
        }
    }

    /**
     * Gets called when a key has been pressed on the body table.
     *
     * @param keyEvent the key event.
     */
    private void onBodyTableViewKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            bodyTableView.getItems().removeAll(bodyTableView.getSelectionModel().getSelectedItems().stream()
                    .toList());

            updateBodyTableView();
        }
    }

    /**
     * Gets called when a key has been pressed on the uri query parameters table.
     *
     * @param keyEvent the key event.
     */
    private void onUriQueryParametersTableViewKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.DELETE) {
            uriQueryParametersTableView.getItems().removeAll(uriQueryParametersTableView.getSelectionModel()
                    .getSelectedItems().stream().toList());

            updateUriQueryParametersTableView();
        }
    }

    /**
     * Initializes the headers table view.
     */
    private void initializeHeadersTableView() {
        // Makes the table editable.
        headersTableView.setEditable(true);

        // Sets the key press handler for the headers table.
        headersTableView.setOnKeyPressed(this::onHeadersTableViewKeyPressed);

        // Sets the cell factories for the columns.
        headersTableViewKeyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        headersTableViewValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        headersTableViewNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Makes the columns editable.
        headersTableViewKeyColumn.setEditable(true);
        headersTableViewValueColumn.setEditable(true);
        headersTableViewNameColumn.setEditable(true);

        // Sets the cell value factories for the columns.
        headersTableViewKeyColumn.setCellValueFactory(header -> header.getValue().keyProperty());
        headersTableViewValueColumn.setCellValueFactory(header -> header.getValue().valueProperty());
        headersTableViewNameColumn.setCellValueFactory(header -> header.getValue().nameProperty());

        // Sets the on commit listeners.
        headersTableViewKeyColumn.setOnEditCommit(this::onHeadersTableViewKeyColumnEditCommit);
        headersTableViewValueColumn.setOnEditCommit(this::onHeadersTableViewValueColumnEditCommit);
        headersTableViewNameColumn.setOnEditCommit(this::onHeadersTableViewNameColumnEditCommit);

        // Checks if we're modifying an existing response, if so, insert its headers into the table.
        if (configRequest != null) {
            // Creates the editor headers from the config headers.
            final List<EditorHeader> editorHeaders = configRequest
                    .headers()
                    .children()
                    .values()
                    .stream()
                    .map(EditorHeaderFactory::create)
                    .toList();

            // Adds all the newly created editor headers.
            headersTableView.getItems().addAll(editorHeaders);
        }

        // Updates the headers table view.
        updateHeadersTableView();
    }

    /**
     * Initializes the body table view.
     */
    private void initializeBodyTableView() {
        // Makes the table editable.
        bodyTableView.setEditable(true);

        // Sets the key press handler for the body table.
        bodyTableView.setOnKeyPressed(this::onBodyTableViewKeyPressed);

        // Sets the cell factories for the columns.
        bodyTableViewFieldColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyTableViewValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyTableViewNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Makes the columns editable.
        bodyTableViewFieldColumn.setEditable(true);
        bodyTableViewValueColumn.setEditable(true);
        bodyTableViewNameColumn.setEditable(true);

        // Sets the cell value factories for the columns.
        bodyTableViewFieldColumn.setCellValueFactory(header -> header.getValue().pathProperty());
        bodyTableViewValueColumn.setCellValueFactory(header -> header.getValue().valueProperty());
        bodyTableViewNameColumn.setCellValueFactory(header -> header.getValue().nameProperty());

        // Sets the on commit listeners.
        bodyTableViewFieldColumn.setOnEditCommit(this::onBodyTableViewFieldColumnEditCommit);
        bodyTableViewValueColumn.setOnEditCommit(this::onBodyTableViewValueColumnEditCommit);
        bodyTableViewNameColumn.setOnEditCommit(this::onBodyTableViewNameColumnEditCommit);

        // Checks if we're modifying an existing response, if so, insert its fields into the table.
        if (configRequest != null) {
            // Creates the editor fields from the fields headers.
            final List<EditorField> editorFields = configRequest
                    .fields()
                    .children()
                    .values()
                    .stream()
                    .map(EditorFieldFactory::create)
                    .toList();

            // Adds all the newly created editor fields.
            bodyTableView.getItems().addAll(editorFields);
        }

        // Updates the columns.
        updateBodyTableView();
    }

    /**
     * Initializes the uri query parameters table view.
     */
    private void initializeUriQueryParametersTableView() {
        // Makes the table editable.
        uriQueryParametersTableView.setEditable(true);

        // Sets the key press handler for the body table.
        uriQueryParametersTableView.setOnKeyPressed(this::onUriQueryParametersTableViewKeyPressed);

        // Sets the cell factories for the columns.
        uriQueryParametersTableViewKeyColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        uriQueryParametersTableViewValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Makes the columns editable.
        uriQueryParametersTableViewKeyColumn.setEditable(true);
        uriQueryParametersTableViewValueColumn.setEditable(true);

        // Sets the cell value factories for the columns.
        uriQueryParametersTableViewKeyColumn.setCellValueFactory(header -> header.getValue().keyProperty());
        uriQueryParametersTableViewValueColumn.setCellValueFactory(header -> header.getValue().valueProperty());

        // Sets the on commit listeners.
        uriQueryParametersTableViewKeyColumn.setOnEditCommit(this::onUriQueryParametersTableViewKeyColumnEditCommit);
        uriQueryParametersTableViewValueColumn.setOnEditCommit(this::onUriQueryParametersTableViewValueColumnEditCommit);

        // If we're modifying an existing request, put its parameters inside the table.
        if (configRequest != null) {
            // Creates the editor query parameters from the config query parameters.
            final List<EditorQueryParameter> queryParameters = configRequest
                    .uri()
                    .queryParameters()
                    .children()
                    .values()
                    .stream()
                    .map(EditorQueryParameterFactory::create)
                    .toList();

            // Adds all the editor query parameters to the table.
            uriQueryParametersTableView
                    .getItems()
                    .addAll(queryParameters);
        }

        // Updates the columns.
        updateUriQueryParametersTableView();
    }

    /**
     * Initializes the path.
     */
    private void initializePath() {
        if (configRequest != null) {
            pathTextField.setText(configRequest.uri().path().stringOfSegments());
        }
    }

    /**
     * Initializes the request editor controller.
     *
     * @param url            the url.
     * @param resourceBundle the resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets all the button action listeners.
        saveButton.setOnAction(this::onSaveButtonAction);
        cancelButton.setOnAction(this::onCancelButtonAction);

        // Calls the other initialization methods.
        initializeMethodComboBox();
        initializeFormatComboBox();
        initializeHeadersTableView();
        initializeBodyTableView();
        initializePath();
        initializeUriQueryParametersTableView();
    }
}
