package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApiConfig;
import com.github.skywa04885.dxprotoproxy.common.http.config.HttpProxyApisConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ApiEditorSubmissionCallback implements IApiEditorSubmissionCallback {
    private final @NotNull HttpProxyApisConfig httpConfigApis;
    private final @Nullable HttpProxyApiConfig httpConfigApi;

    public ApiEditorSubmissionCallback(@NotNull HttpProxyApisConfig httpConfigApis, @Nullable HttpProxyApiConfig httpConfigApi) {
        this.httpConfigApis = httpConfigApis;
        this.httpConfigApi = httpConfigApi;
    }

    public ApiEditorSubmissionCallback(@NotNull HttpProxyApisConfig httpConfigApis) {
        this(httpConfigApis, null);
    }

    public ApiEditorSubmissionCallback(@NotNull HttpProxyApiConfig httpConfigApi) {
        this(Objects.requireNonNull(httpConfigApi.parent()), httpConfigApi);
    }

    private void modify(@NotNull String name, @NotNull String httpVersion) {
        assert httpConfigApi != null;

        if (!httpConfigApi.name().equals(name)) {
            httpConfigApis.children().remove(httpConfigApi.name());
            httpConfigApi.setName(name);
            httpConfigApis.children().put(name, httpConfigApi);
        }

        if (!httpConfigApi.httpVersion().equals(httpVersion)) {
            httpConfigApi.setHttpVersion(httpVersion);
        }
    }

    private void create(@NotNull String name, @NotNull String httpVersion) {
        final var configApi = new HttpProxyApiConfig(name, httpVersion);
        configApi.setParent(httpConfigApis);
        httpConfigApis.children().put(configApi.name(), configApi);
    }

    @Override
    public void submit(@NotNull String name, @NotNull String httpVersion) {
        if (this.httpConfigApi != null) {
            modify(name, httpVersion);
        } else {
            create(name, httpVersion);
        }
    }
}
