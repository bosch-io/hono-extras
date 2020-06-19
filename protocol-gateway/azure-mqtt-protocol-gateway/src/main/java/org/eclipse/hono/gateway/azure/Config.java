/*******************************************************************************
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.hono.gateway.azure;

import java.util.List;

import org.eclipse.hono.config.ClientConfigProperties;
import org.eclipse.hono.gateway.sdk.mqtt2amqp.MqttProtocolGatewayConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Boot configuration for the the "Azure IoT Hub" Protocol Gateway.
 */
@Configuration
public class Config {

    /**
     * These are the default secure protocols in Vertx.
     */
    public static final List<String> enabledProtocols = List.of("TLSv1", "TLSv1.1", "TLSv1.2");

    /**
     * Exposes configuration properties for providing an MQTT server as a Spring bean.
     * <p>
     * Sets the TLS protocols from {@link #enabledProtocols} as the enabled secure protocols of the MQTT server if not
     * set explicitly.
     *
     * @return The properties.
     */
    @Bean
    @ConfigurationProperties(prefix = "hono.server.mqtt")
    public MqttProtocolGatewayConfig mqttGatewayConfig() {
        final MqttProtocolGatewayConfig mqttProtocolGatewayConfig = new MqttProtocolGatewayConfig();
        mqttProtocolGatewayConfig.setSecureProtocols(enabledProtocols);
        return mqttProtocolGatewayConfig;
    }

    /**
     * Exposes configuration properties for accessing Hono's AMQP adapter as a Spring bean.
     *
     * @return The properties.
     */
    @Bean
    @ConfigurationProperties(prefix = "hono.client.amqp")
    public ClientConfigProperties amqpClientConfig() {
        return new ClientConfigProperties();
    }

    /**
     * Creates a new Azure IoT Hub protocol gateway instance.
     *
     * @return The new instance.
     */
    @Bean
    public AzureIotHubMqttGateway azureIotHubMqttGateway() {
        return new AzureIotHubMqttGateway(amqpClientConfig(), mqttGatewayConfig(), demoDevice());
    }

    /**
     * Exposes configuration properties for a demo device as a Spring bean.
     *
     * @return The demo device configuration against which the authentication of a connecting device is being performed.
     */
    @Bean
    @ConfigurationProperties(prefix = "hono.demo.device")
    public DemoDeviceConfiguration demoDevice() {
        return new DemoDeviceConfiguration();
    }

}
