package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class EndpointEditorWindow {
    @NotNull
    private EndpointEditorController controller;
    @NotNull
    private final Stage stage;

    public EndpointEditorWindow(@NotNull EndpointEditorController controller, @NotNull Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    @NotNull
    public Stage stage() {
        return stage;
    }
}
