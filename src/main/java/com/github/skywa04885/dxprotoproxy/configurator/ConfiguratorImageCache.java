package com.github.skywa04885.dxprotoproxy.configurator;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class ConfiguratorImageCache {
    private static final ConfiguratorImageCache instance = new ConfiguratorImageCache();

    public static ConfiguratorImageCache instance() {
        return instance;
    }

    private final HashMap<String, Image> images = new HashMap<>();

    public Image read(final String resource) {
        Image image = images.get(resource);
        if (image != null) return image;

        image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resource)));
        images.put(resource, image);

        return image;
    }
}
