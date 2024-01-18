package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApiConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApisConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ApiEditorWindowFactory {
    public static ApiEditorWindow create(@NotNull ApiEditorController controller, @NotNull String title) {
        try {
            // Creates the loader and sets the controller.
            final var loader = new FXMLLoader(ApiEditorWindowFactory.class.getResource("view.fxml"));
            loader.setController(controller);

            // Creates the stage and sets its title.
            final var stage = new Stage();
            stage.setTitle(title);

            // Loads the scene, puts it in the stage and shows the stage.
            final var scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

            // Creates the response editor window.
            final var window = new ApiEditorWindow(controller, stage);

            // Gives the controller a reference to the window.
            controller.setWindow(window);

            // Returns the window.
            return window;
        } catch (IOException ioException) {
            throw new Error("Failed to load instance editor view");
        }
    }

    @NotNull
    public static ApiEditorWindow create(@NotNull HttpProxyApiConfig httpConfigApi) {
        return create(ApiEditorControllerFactory.create(httpConfigApi), "Modify API");
    }

    @NotNull
    public static ApiEditorWindow create(@NotNull HttpProxyApisConfig httpConfigApis) {
        return create(ApiEditorControllerFactory.create(httpConfigApis), "Create API");
    }
}
