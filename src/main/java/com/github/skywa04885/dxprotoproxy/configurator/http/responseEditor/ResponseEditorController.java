package com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor;

import com.github.skywa04885.dxprotoproxy.common.http.HttpProxyFieldsFormat;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyResponseConfig;
import com.github.skywa04885.dxprotoproxy.configurator.http.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ResponseEditorController implements Initializable {
    // Headers table view.
    @FXML
    public TableView<EditorHeader> headersTableView;
    @FXML
    public TableColumn<EditorHeader, String> headersTableViewKeyColumn;
    @FXML
    public TableColumn<EditorHeader, String> headersTableViewNameColumn;
    @FXML
    public TableColumn<EditorHeader, String> headersTableViewValueColumn;

    // The combo boxes.
    @FXML
    public ComboBox<HttpProxyFieldsFormat> formatComboBox;

    // The spinners.
    @FXML
    public Spinner<Integer> codeSpinner;

    // Fields table view.
    @FXML
    public TableView<EditorField> bodyTableView;
    @FXML
    public TableColumn<EditorField, String> bodyTableViewPathColumn;
    @FXML
    public TableColumn<EditorField, String> bodyTableViewNameColumn;
    @FXML
    public TableColumn<EditorField, String> bodyTableViewValueColumn;

    // Buttons.
    @FXML
    public Button saveButton;
    @FXML
    public Button cancelButton;

    @Nullable
    private final HttpProxyResponseConfig configResponse;
    private final IResponseEditorValidationCallback validationCallback;
    private final IResponseEditorSubmissionCallback submissionCallback;
    private ResponseEditorWindow window = null;

    public ResponseEditorController(HttpProxyResponseConfig configResponse, IResponseEditorValidationCallback validationCallback, IResponseEditorSubmissionCallback submissionCallback) {
        this.configResponse = configResponse;
        this.validationCallback = validationCallback;
        this.submissionCallback = submissionCallback;
    }

    public void setWindow(ResponseEditorWindow window) {
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
     * Saves the edited request.
     */
    private void save() {
        // Gets all the values.
        final int code = this.codeSpinner.getValue();
        final List<EditorHeader> editorHeaders = headersTableView.getItems().stream().filter(EditorHeader::isNotBlank).toList();
        final List<EditorField> editorFields = bodyTableView.getItems().stream().filter(EditorField::isNotBlank).toList();
        final HttpProxyFieldsFormat fieldsFormat = formatComboBox.getValue();

        // Validates the values.
        final String error = validationCallback.validate(code, editorHeaders, editorFields, fieldsFormat);

        // Shows the error if it's there.
        if (error != null) {
            showValidationErrorAlert(error);
            return;
        }

        // Submits the request.
        submissionCallback.submit(code, editorHeaders, editorFields, fieldsFormat);

        // Closes the stage.
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
        // Closes the stage.
        window.stage().close();
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
     * Updates the headers table (removing empty ones and making sure the last one is meant for creation).
     */
    private void updateHeadersTableView() {
        headersTableView.getItems().removeIf(EditorHeader::isBlank);
        headersTableView.getItems().add(new EditorHeader());
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
        if (configResponse != null) {
            // Creates the editor headers from the config headers.
            final List<EditorHeader> editorHeaders = configResponse
                    .headers()
                    .children()
                    .values()
                    .stream()
                    .map(EditorHeaderFactory::create)
                    .toList();

            // Adds all the newly created editor headers.
            headersTableView
                    .getItems()
                    .addAll(editorHeaders);
        }

        // Updates the headers table view.
        updateHeadersTableView();
    }

    /**
     * Updates the body table view (removing empty ones and making sure the last one is meant for creation).
     */
    private void updateBodyTableView() {
        bodyTableView.getItems().removeIf(EditorField::isBlank);
        bodyTableView.getItems().add(new EditorField());
    }

    /**
     * Gets called when a value in the path column was changed.
     *
     * @param event the event.
     */
    private void onBodyTableViewPathColumnEditCommit(TableColumn.CellEditEvent<EditorField, String> event) {
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
     * Initializes the body table view.
     */
    private void initializeBodyTableView() {
        // Makes the table editable.
        bodyTableView.setEditable(true);

        // Sets the key press handler for the body table.
        bodyTableView.setOnKeyPressed(this::onBodyTableViewKeyPressed);

        // Sets the cell factories for the columns.
        bodyTableViewPathColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyTableViewValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        bodyTableViewNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Makes the columns editable.
        bodyTableViewPathColumn.setEditable(true);
        bodyTableViewValueColumn.setEditable(true);
        bodyTableViewNameColumn.setEditable(true);

        // Sets the cell value factories for the columns.
        bodyTableViewPathColumn.setCellValueFactory(header -> header.getValue().pathProperty());
        bodyTableViewValueColumn.setCellValueFactory(header -> header.getValue().valueProperty());
        bodyTableViewNameColumn.setCellValueFactory(header -> header.getValue().nameProperty());

        // Sets the on commit listeners.
        bodyTableViewPathColumn.setOnEditCommit(this::onBodyTableViewPathColumnEditCommit);
        bodyTableViewValueColumn.setOnEditCommit(this::onBodyTableViewValueColumnEditCommit);
        bodyTableViewNameColumn.setOnEditCommit(this::onBodyTableViewNameColumnEditCommit);

        // Checks if we're modifying an existing response, if so, insert its fields into the table.
        if (configResponse != null) {
            // Creates the editor fields from the fields headers.
            final List<EditorField> editorFields = configResponse
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
     * Initializes the format combo box.
     */
    private void initializeFormatComboBox() {
        // Sets the items of the format combo box and sets json as the default format.
        formatComboBox.setItems(FXCollections.observableList(Arrays.stream(HttpProxyFieldsFormat.values()).toList()));

        // Sets the default format.
        if (configResponse != null) {
            formatComboBox.getSelectionModel().select(configResponse.fields().format());
        } else {
            formatComboBox.getSelectionModel().select(HttpProxyFieldsFormat.JSON);
        }
    }

    /**
     * Initializes the code spinner.
     */
    private void initializeCodeSpinner() {
        // Sets the value factory.
        codeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                ValidatorForResponseCode.from,
                ValidatorForResponseCode.to,
                1
        ));

        // Sets the initial value (either 200 by default or the value of the current response we're modifying).
        if (configResponse != null) {
            codeSpinner.getValueFactory().setValue(configResponse.code());
        } else {
            codeSpinner.getValueFactory().setValue(200);
        }
    }

    /**
     * Initializes the response editor controller.
     * @param url the url.
     * @param resourceBundle the resource bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets all the button action listeners.
        saveButton.setOnAction(this::onSaveButtonAction);
        cancelButton.setOnAction(this::onCancelButtonAction);

        // Calls the other initialization methods.
        initializeFormatComboBox();
        initializeHeadersTableView();
        initializeBodyTableView();
        initializeCodeSpinner();
    }
}
