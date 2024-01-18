package com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IMQTTClientEditorSubmissionCallback {
    /**
     * Submits the given fields.
     *
     * @param clientHostname   the client hostname.
     * @param clientPort       the client port.
     * @param brokerHostname   the broker hostname.
     * @param brokerPort       the broker port.
     * @param username         the username.
     * @param password         the password.
     * @param clientIdentifier the client identifier.
     * @param subscriptions    the subscriptions.
     */
    void submit(@NotNull String clientHostname, int clientPort,
                @NotNull String brokerHostname, int brokerPort,
                @Nullable String username, @Nullable String password,
                @Nullable String clientIdentifier,
                @NotNull List<String> subscriptions);
}
