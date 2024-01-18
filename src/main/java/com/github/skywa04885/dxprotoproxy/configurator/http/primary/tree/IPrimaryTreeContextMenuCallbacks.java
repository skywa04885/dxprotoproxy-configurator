package com.github.skywa04885.dxprotoproxy.configurator.http.primary.tree;

import com.github.skywa04885.dxprotoproxy.common.http.config.*;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMasterConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.master.config.ModbusProxyMastersConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlaveConfig;
import com.github.skywa04885.dxprotoproxy.common.modbus.slave.config.ModbusProxySlavesConfig;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientConfig;
import com.github.skywa04885.dxprotoproxy.common.mqtt.client.config.MqttProxyClientsConfig;
import org.jetbrains.annotations.NotNull;

public interface IPrimaryTreeContextMenuCallbacks {
    void createResponse(HttpProxyRequestConfig request);

    void createApi(@NotNull HttpProxyApisConfig httpConfigApis);

    void modifyApi(@NotNull HttpProxyApiConfig httpConfigApi);

    void deleteApi(@NotNull HttpProxyApiConfig httpConfigApi);

    void createInstance(@NotNull HttpProxyInstancesConfig httpConfigInstances);

    void modifyInstance(@NotNull HttpProxyInstanceConfig httpConfigInstance);

    void deleteInstance(@NotNull HttpProxyInstanceConfig httpConfigInstance);

    void createEndpoint(@NotNull HttpProxyEndpointsConfig httpConfigEndpoints);

    /**
     * Deletes the given endpoint.
     *
     * @param configEndpoint the endpoint to delete.
     */
    void deleteEndpoint(@NotNull HttpProxyEndpointConfig configEndpoint);

    /**
     * Modifies the given endpoint.
     *
     * @param configEndpoint the endpoint to modify.
     */
    void modifyEndpoint(@NotNull HttpProxyEndpointConfig configEndpoint);

    void createRequest(@NotNull HttpProxyEndpointConfig httpConfigEndpoint);

    /**
     * This callback gets called when the given response needs to be edited.
     *
     * @param configResponse the response that needs to be modified.
     */
    void modifyResponse(@NotNull HttpProxyResponseConfig configResponse);

    /**
     * This callback gets called when the given response needs to be deleted.
     *
     * @param configResponse the response that needs to be deleted.
     */
    void deleteResponse(@NotNull HttpProxyResponseConfig configResponse);

    /**
     * Gets called when the given request needs to be deleted.
     *
     * @param configRequest the request that needs to be deleted.
     */
    void deleteRequest(@NotNull HttpProxyRequestConfig configRequest);

    /**
     * Gets called when the given request needs to be modified.
     *
     * @param configRequest the request that needs to be modified.
     */
    void modifyRequest(@NotNull HttpProxyRequestConfig configRequest);

    /**
     * Gets called when a new mqtt client needs to be created in the given clients config.
     *
     * @param mqttClientsConfig the clients config to create the client in.
     */
    void createMqttClient(@NotNull MqttProxyClientsConfig mqttClientsConfig);

    /**
     * Gets called when the given mqtt client needs to be deleted.
     *
     * @param mqttClientConfig the mqtt client to delete.
     */
    void deleteMqttClient(@NotNull MqttProxyClientConfig mqttClientConfig);

    /**
     * Gets called when the given mqtt client config needs to be modified.
     *
     * @param mqttClientConfig the mqtt client config that needs to be modified.
     */
    void modifyMqttClient(@NotNull MqttProxyClientConfig mqttClientConfig);

    /**
     * Gets called when a new modbus slave should be created.
     * @param config the slaves-config.
     */
    void createModbusSlave(@NotNull ModbusProxySlavesConfig config);

    /**
     * Gets called when an ew modbus master should be created.
     * @param config the masters-config.
     */
    void createModbusMaster(@NotNull ModbusProxyMastersConfig config);

    void modifyModbusMaster(@NotNull ModbusProxyMasterConfig modbusMasterConfig);

    void deleteModbusMaster(@NotNull ModbusProxyMasterConfig modbusMasterConfig);

    void modifyModbusSlave(@NotNull ModbusProxySlaveConfig modbusMasterConfig);

    void deleteModbusSlave(@NotNull ModbusProxySlaveConfig modbusMasterConfig);
}
