package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class ApiEditorWindow {
    private @NotNull ApiEditorController controller;
    private @NotNull final Stage stage;

    public ApiEditorWindow(@NotNull ApiEditorController controller, @NotNull Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    public @NotNull Stage stage() {
        return stage;
    }
}
