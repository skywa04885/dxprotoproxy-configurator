package com.github.skywa04885.dxprotoproxy.configurator.http;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class Window<TController> {
    protected TController controller;
    protected final Stage stage;

    protected Window(TController controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
    }

    protected Window(TController controller) {
        this(controller, new Stage());
    }

    protected Window() {
        this((TController) null);
    }

    protected Window(Stage stage) {
        this(null, stage);
    }

    protected void loadView(URL url) throws IOException {
        // Creates the fxml loader and configures it.
        final var fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(controller);

        // Loads the scene and puts it in the stage.
        final var scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    public TController getController() {
        return controller;
    }

    public void show() {
        stage.show();
    }

    public void close() {
        stage.close();
    }
}
