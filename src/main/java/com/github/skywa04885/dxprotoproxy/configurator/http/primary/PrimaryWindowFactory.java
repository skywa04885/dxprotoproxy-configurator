package com.github.skywa04885.dxprotoproxy.configurator.http.primary;

import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacade;
import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacadeFactory;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class PrimaryWindowFactory {
    public static @Nullable WindowFacade<PrimaryController> create(@NotNull Stage stage) {
        // Tries to create the window.
        try {
            // Gets the URL for the view resource.
            final @Nullable URL viewURL = PrimaryWindowFactory.class.getResource("view.fxml");

            // Creates the window and returns it.
            return WindowFacadeFactory.create(Objects.requireNonNull(viewURL),
                    stage, "Configurator", PrimaryController.class);
        } catch (IOException ioException) {
            // Shows the alert indicating the failure of the loading.
            final var alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not open application");
            alert.setContentText("Failed to load resource of the primary view");
            alert.show();

            // Returns null.
            return null;
        }
    }
}
