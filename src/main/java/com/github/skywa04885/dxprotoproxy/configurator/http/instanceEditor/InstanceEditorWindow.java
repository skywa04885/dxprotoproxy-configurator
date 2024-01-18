package com.github.skywa04885.dxprotoproxy.configurator.http.instanceEditor;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class InstanceEditorWindow {
    private @NotNull InstanceEditorController controller;
    private @NotNull final Stage stage;

    public InstanceEditorWindow(@NotNull InstanceEditorController controller, @NotNull Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public @NotNull Stage stage() {
        return stage;
    }
}
