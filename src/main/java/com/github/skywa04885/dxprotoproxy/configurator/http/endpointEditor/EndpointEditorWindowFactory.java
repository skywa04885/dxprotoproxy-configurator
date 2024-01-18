package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyEndpointsConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class EndpointEditorWindowFactory {
    @NotNull
    public static EndpointEditorWindow create(@NotNull EndpointEditorController controller, @NotNull String title) {
        try {
            // Creates the loader and sets the controller.
            final var loader = new FXMLLoader(EndpointEditorWindowFactory.class.getResource("view.fxml"));
            loader.setController(controller);

            // Creates the stage and sets its title.
            final var stage = new Stage();
            stage.setTitle(title);

            // Loads the scene, puts it in the stage and shows the stage.
            final var scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            // Creates the request editor window.
            final var window = new EndpointEditorWindow(controller, stage);

            // Gives the controller a reference to the window.
            controller.setWindow(window);

            // Returns the window.
            return window;
        } catch (IOException ioException) {
            throw new Error("Failed to load endpoint editor view");
        }
    }

    /**
     * Creates a new endpoint modification window.
     * @param configEndpoint the endpoint that should be modified.
     * @return the window.
     */
    @NotNull
    public static EndpointEditorWindow modify(@NotNull HttpProxyEndpointConfig configEndpoint) {
        return create(EndpointEditorControllerFactory.modify(configEndpoint), "Modify endpoint");
    }

    /**
     * Creates a new endpoint creation window.
     * @param httpConfigEndpoints the endpoints.
     * @return the window.
     */
    @NotNull
    public static EndpointEditorWindow create(@NotNull HttpProxyEndpointsConfig httpConfigEndpoints) {
        return create(EndpointEditorControllerFactory.create(httpConfigEndpoints), "Create endpoint");
    }
}
