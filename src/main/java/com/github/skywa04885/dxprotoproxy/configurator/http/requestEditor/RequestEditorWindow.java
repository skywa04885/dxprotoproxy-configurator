package com.github.skywa04885.dxprotoproxy.configurator.http.requestEditor;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class RequestEditorWindow {
    @NotNull
    private RequestEditorController controller;
    @NotNull
    private final Stage stage;

    public RequestEditorWindow(@NotNull RequestEditorController controller, @NotNull Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    @NotNull
    public Stage stage() {
        return stage;
    }
}
