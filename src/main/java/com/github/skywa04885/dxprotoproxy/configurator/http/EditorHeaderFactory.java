package com.github.skywa04885.dxprotoproxy.configurator.http;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyHeaderConfig;

public class EditorHeaderFactory {
    public static EditorHeader create(HttpProxyHeaderConfig configHeader) {
        return new EditorHeader(configHeader, configHeader.name(), configHeader.key(),
                configHeader.value() == null ? "" : configHeader.value());
    }
}
