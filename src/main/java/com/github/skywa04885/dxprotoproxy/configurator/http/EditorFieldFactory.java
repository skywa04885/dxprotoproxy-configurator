package com.github.skywa04885.dxprotoproxy.configurator.http;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyFieldConfig;

public class EditorFieldFactory {
    /**
     * Creates a new editor field from a config field/
     * @param configField the config field.
     * @return the editor field.
     */
    public static EditorField create(HttpProxyFieldConfig configField) {
        return new EditorField(configField, configField.name(), configField.path(), configField.value() == null ? "" : configField.value());
    }
}
