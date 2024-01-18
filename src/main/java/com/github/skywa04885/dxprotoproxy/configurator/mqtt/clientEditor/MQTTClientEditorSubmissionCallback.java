package com.github.skywa04885.dxprotoproxy.configurator.mqtt.clientEditor;

import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientConfig;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientsConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MQTTClientEditorSubmissionCallback implements IMQTTClientEditorSubmissionCallback {
    /**
     * Member variables.
     */
    private final @NotNull MqttProxyClientsConfig mqttClientsConfig;
    private final @Nullable MqttProxyClientConfig mqttClientConfig;

    /**
     * Constructs a new mqtt client editor submission callback.
     *
     * @param mqttClientsConfig the config containing all the clients.
     * @param mqttClientConfig  the possibly already existing client that's being modified.
     */
    public MQTTClientEditorSubmissionCallback(@NotNull MqttProxyClientsConfig mqttClientsConfig,
                                              @Nullable MqttProxyClientConfig mqttClientConfig) {
        this.mqttClientsConfig = mqttClientsConfig;
        this.mqttClientConfig = mqttClientConfig;
    }

    public MQTTClientEditorSubmissionCallback(@NotNull MqttProxyClientsConfig mqttClientsConfig) {
        this(mqttClientsConfig, null);
    }

    public MQTTClientEditorSubmissionCallback(@NotNull MqttProxyClientConfig mqttClientConfig) {
        this(Objects.requireNonNull(mqttClientConfig.parent()), mqttClientConfig);
    }

    /**
     * Creates a new client config from the given fields.
     *
     * @param clientHostname   the client hostname.
     * @param clientPort       the client port.
     * @param brokerHostname   the broker hostname.
     * @param brokerPort       the broker port.
     * @param username         the username.
     * @param password         the password.
     * @param clientIdentifier the client identifier.
     * @param topics           the topics.
     */
    private void create(@NotNull String clientHostname, int clientPort,
                        @NotNull String brokerHostname, int brokerPort,
                        @Nullable String username, @Nullable String password,
                        @Nullable String clientIdentifier,
                        @NotNull List<String> topics) {
        // Creates the client config.
        final var mqttClientConfig = new MqttProxyClientConfig(clientHostname, clientPort,
                brokerHostname, brokerPort, username, password, clientIdentifier,
                topics, mqttClientsConfig);

        // Inserts the client config into the clients.
        mqttClientsConfig.children().add(mqttClientConfig);
    }

    /**
     * Updates the existing client config using the given fields.
     *
     * @param clientHostname   the client hostname.
     * @param clientPort       the client port.
     * @param brokerHostname   the broker hostname.
     * @param brokerPort       the broker port.
     * @param username         the username.
     * @param password         the password.
     * @param clientIdentifier the client identifier.
     * @param topics           the topics.
     */
    private void update(@NotNull String clientHostname, int clientPort,
                        @NotNull String brokerHostname, int brokerPort,
                        @Nullable String username, @Nullable String password,
                        @Nullable String clientIdentifier,
                        @NotNull List<String> topics) {
        // The client config should never be null.
        assert mqttClientConfig != null;

        // Updates the client hostname.
        if (!clientHostname.equals(mqttClientConfig.clientHostname())) {
            mqttClientConfig.setClientHostname(clientHostname);
        }

        // Updates the client port.
        if (clientPort != mqttClientConfig.clientPort()) {
            mqttClientConfig.setClientPort(clientPort);
        }

        // Updates the broker hostname.
        if (!brokerHostname.equals(mqttClientConfig.brokerHostname())) {
            mqttClientConfig.setBrokerHostname(brokerHostname);
        }

        // Updates the broker port.
        if (brokerPort != mqttClientConfig.brokerPort()) {
            mqttClientConfig.setBrokerPort(brokerPort);
        }

        // Updates the username,
        if ((username == null && mqttClientConfig.username() != null)
                || (username != null && !username.equals(mqttClientConfig.username()))) {
            mqttClientConfig.setUsername(username);
        }

        // Updates the password.
        if ((password == null && mqttClientConfig.password() != null)
                || (password != null && !password.equals(mqttClientConfig.password()))) {
            mqttClientConfig.setPassword(password);
        }

        // Updates the client identifier.
        if ((clientIdentifier == null && mqttClientConfig.clientIdentifier() != null)
                || (clientIdentifier != null && !clientIdentifier.equals(mqttClientConfig.clientIdentifier()))) {
            mqttClientConfig.setClientIdentifier(clientIdentifier);
        }

        // Clears the existing topics.
        mqttClientConfig.topics().clear();

        // Creates all the new topics.
        mqttClientConfig.topics().addAll(topics);
    }

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
     * @param topics           the topics.
     */
    @Override
    public void submit(@NotNull String clientHostname, int clientPort,
                       @NotNull String brokerHostname, int brokerPort,
                       @Nullable String username, @Nullable String password,
                       @Nullable String clientIdentifier,
                       @NotNull List<String> topics) {
        if (mqttClientConfig == null) {
            create(clientHostname, clientPort, brokerHostname, brokerPort, username, password, clientIdentifier,
                    topics);
        } else {
            update(clientHostname, clientPort, brokerHostname, brokerPort, username, password, clientIdentifier,
                    topics);
        }
    }
}
