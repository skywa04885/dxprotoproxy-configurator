package com.github.skywa04885.dxprotoproxy.configurator.http.endpointEditor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IEndpointEditorValidationCallback {
    /**
     * Gets called when the endpoint editor needs to validate its values.
     *
     * @param name the name value.
     * @return null if all the values are valid, otherwise the error message.
     */
    @Nullable String validate(@NotNull String name);
}
