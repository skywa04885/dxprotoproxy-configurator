package com.github.skywa04885.dxprotoproxy.configurator.http.apiEditor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IApiEditorValidationCallback {
    @Nullable String validate(@NotNull String name, @NotNull String httpVersion);
}
