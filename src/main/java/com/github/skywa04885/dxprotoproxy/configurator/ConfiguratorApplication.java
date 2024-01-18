package com.github.skywa04885.dxprotoproxy.configurator;

import atlantafx.base.theme.CupertinoLight;
import com.github.skywa04885.dxprotoproxy.common.javafx.WindowFacade;
import com.github.skywa04885.dxprotoproxy.configurator.http.primary.PrimaryController;
import com.github.skywa04885.dxprotoproxy.configurator.http.primary.PrimaryWindowFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class ConfiguratorApplication extends Application {
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void start(@NotNull Stage stage) throws Exception {
        Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());

        final @Nullable WindowFacade<PrimaryController> primaryWindow = PrimaryWindowFactory.create(stage);
        if (primaryWindow == null) {
            return;
        }

        primaryWindow.stage().show();
    }

    public static void main(String[] args) {
        launch();
    }
}