/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.automatictests.platform.config.ws.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.server.endpoint.adapter.method.MarshallingPayloadMethodProcessor;

import com.alliander.osgp.automatictests.platform.config.ws.BaseWebServiceConfig;
import com.alliander.osgp.automatictests.platform.support.ws.WebServiceTemplateFactory;

@Configuration
public class CoreConfigurationManagementWebServiceConfig extends BaseWebServiceConfig {

    @Value("${web.service.template.default.uri.core.configurationmanagement}")
    private String webserviceTemplateDefaultUriCoreConfigurationManagement;

    @Value("${jaxb2.marshaller.context.path.core.configurationmanagement}")
    private String contextPathCoreConfigurationManagement;

    @Bean
    public WebServiceTemplateFactory coreConfigurationManagementWstf() {
        return new WebServiceTemplateFactory.Builder().setMarshaller(this.coreConfigurationManagementMarshaller())
                .setMessageFactory(this.messageFactory())
                .setDefaultUri(this.baseUri.concat(this.webserviceTemplateDefaultUriCoreConfigurationManagement))
                .setKeyStoreType(this.webserviceKeystoreType).setKeyStoreLocation(this.webserviceKeystoreLocation)
                .setKeyStorePassword(this.webserviceKeystorePassword)
                .setTrustStoreFactory(this.webServiceTrustStoreFactory()).setApplicationName(this.applicationName)
                .build();
    }

    /**
     * Method for creating the Marshaller for Core ConfigurationManagement.
     *
     * @return Jaxb2Marshaller
     */
    @Bean
    public Jaxb2Marshaller coreConfigurationManagementMarshaller() {
        final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();

        marshaller.setContextPath(this.contextPathCoreConfigurationManagement);

        return marshaller;
    }

    /**
     * Method for creating the Marshalling Payload Method Processor for Core
     * ConfigurationManagement.
     *
     * @return MarshallingPayloadMethodProcessor
     */
    @Bean
    public MarshallingPayloadMethodProcessor coreConfigurationManagementMarshallingPayloadMethodProcessor() {
        return new MarshallingPayloadMethodProcessor(this.coreConfigurationManagementMarshaller(),
                this.coreConfigurationManagementMarshaller());
    }
}
