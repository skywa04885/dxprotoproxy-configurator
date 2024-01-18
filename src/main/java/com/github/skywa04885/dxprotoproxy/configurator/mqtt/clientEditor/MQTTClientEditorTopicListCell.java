package com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor;

import com.github.skywa04885.dxprotoproxy.configurator.GlobalConstants;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import net.synedra.validatorfx.Check;
import org.jetbrains.annotations.NotNull;

public class MQTTClientEditorTopicListCell extends ListCell<String> {
    private final @NotNull MQTTClientEditorController controller;
    private TextField textField;
    private Check check;

    public MQTTClientEditorTopicListCell(@NotNull MQTTClientEditorController controller) {
        super();

        this.controller = controller;
    }

    @Override
    public void startEdit() {
        super.startEdit();

        final @NotNull String item = getItem();

        textField = new TextField();
        textField.setText(item);
        check = controller.validator.createCheck()
                .dependsOn("topic", textField.textProperty())
                .withMethod(context -> {
                    final @NotNull String topic = context.get("topic");

                    if (!GlobalConstants.MQTT_TOPIC_PATTERN.matcher(topic).matches()) {
                        context.error("Invalid MQTT topic");
                    }
                })
                .decorates(textField)
                .immediate();

        // Sets the event handler for when a key has been released on the
        //  text field.
        textField.setOnKeyReleased(keyEvent -> {
            // If enter is pressed, and the contents are valid, then commit, otherwise
            //  if escape is pressed, then cancel the edit.
            if (keyEvent.getCode() == KeyCode.ENTER && check.getValidationResult().getMessages().isEmpty()) {
                commitEdit(textField.getText());
            } else if (keyEvent.getCode() == KeyCode.ESCAPE) {
                cancelEdit();
            }
        });

        // Make it so that a commit can be made by not focussing anymore.
        textField.focusedProperty().addListener((observableValue, wasFocussed, isFocussed) -> {
            if (isEditing() && wasFocussed && !isFocussed && check.getValidationResult().getMessages().isEmpty()) {
                commitEdit(textField.getText());
            }
        });

        setText(null);
        setGraphic(textField);

        textField.requestFocus();
    }

    @Override
    protected void updateItem(String s, boolean empty) {
        super.updateItem(s, empty);

        if (empty) {
            setGraphic(null);
            setText(null);
        } else {
            final @NotNull String item = getItem();

            if (isEditing()) {
                setGraphic(textField);
                setText(null);
            } else {
                setGraphic(null);
                setText(item);
            }
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();

        textField = null;
        controller.validator.remove(check);

        setText(getItem());
        setGraphic(null);
    }

    @Override
    public void commitEdit(@NotNull String s) {
        super.commitEdit(s);

        textField = null;
        controller.validator.remove(check);

        setText(getItem());
        setGraphic(null);

        controller.updateTopicsListView();
    }
}
