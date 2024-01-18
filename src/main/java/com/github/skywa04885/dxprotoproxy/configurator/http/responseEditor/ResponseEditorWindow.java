package com.github.skywa04885.dxprotoproxy.configurator.http.responseEditor;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class ResponseEditorWindow {
    @NotNull
    private final ResponseEditorController controller;
    @NotNull
    private final Stage stage;

    public ResponseEditorWindow(@NotNull ResponseEditorController controller, @NotNull Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    @NotNull
    public Stage stage() {
        return stage;
    }
}
