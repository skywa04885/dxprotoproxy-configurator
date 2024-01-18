package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import org.jetbrains.annotations.NotNull;

public interface IApiEditorSubmissionCallback {
    void submit(@NotNull String name, @NotNull String httpVersion);
}
