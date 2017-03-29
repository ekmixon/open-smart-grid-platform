/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.dlms.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.alliander.osgp.shared.application.config.AbstractConfig;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Base class for the application configuration.
 */
@Configuration
@PropertySources({ @PropertySource("classpath:cucumber-platform-dlms.properties"),
        @PropertySource(value = "file:/etc/osp/test/global-cucumber.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "file:/etc/osp/test/cucumber-platform-dlms.properties", ignoreResourceNotFound = true), })
public class ApplicationConfiguration extends AbstractConfig {

    @Value("${web.service.template.default.uri.smartmetering.adhoc}")
    public String webserviceTemplateDefaultUriSmartMeteringAdHoc;

    @Value("${jaxb2.marshaller.context.path.smartmetering.adhoc}")
    public String contextPathSmartMeteringAdHoc;

    @Value("${web.service.template.default.uri.smartmetering.bundle}")
    public String webserviceTemplateDefaultUriSmartMeteringBundle;

    @Value("${jaxb2.marshaller.context.path.smartmetering.bundle}")
    public String contextPathSmartMeteringBundle;

    @Value("${web.service.template.default.uri.smartmetering.configuration}")
    public String webserviceTemplateDefaultUriSmartMeteringConfiguration;

    @Value("${jaxb2.marshaller.context.path.smartmetering.configuration}")
    public String contextPathSmartMeteringConfiguration;

    @Value("${web.service.template.default.uri.smartmetering.installation}")
    public String webserviceTemplateDefaultUriSmartMeteringInstallation;

    @Value("${jaxb2.marshaller.context.path.smartmetering.installation}")
    public String contextPathSmartMeteringInstallation;

    @Value("${web.service.template.default.uri.smartmetering.management}")
    public String webserviceTemplateDefaultUriSmartMeteringManagement;

    @Value("${jaxb2.marshaller.context.path.smartmetering.management}")
    public String contextPathSmartMeteringManagement;

    @Value("${web.service.template.default.uri.smartmetering.monitoring}")
    public String webserviceTemplateDefaultUriSmartMeteringMonitoring;

    @Value("${jaxb2.marshaller.context.path.smartmetering.monitoring}")
    public String contextPathSmartMeteringMonitoring;

    @Bean
    public MapperFactory defaultMapperFactory() {
        return new DefaultMapperFactory.Builder().build();
    }

    @Bean
    public MapperFacade defaultMapper(final MapperFactory defaultMapperFactory) {
        return defaultMapperFactory.getMapperFacade();
    }
}
