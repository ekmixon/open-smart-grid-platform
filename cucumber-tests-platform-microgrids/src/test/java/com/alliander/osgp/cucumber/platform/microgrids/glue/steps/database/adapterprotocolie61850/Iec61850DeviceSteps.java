/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.cucumber.platform.microgrids.glue.steps.database.adapterprotocolie61850;

import static com.alliander.osgp.cucumber.core.ReadSettingsHelper.getBoolean;
import static com.alliander.osgp.cucumber.core.ReadSettingsHelper.getInteger;
import static com.alliander.osgp.cucumber.core.ReadSettingsHelper.getString;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alliander.osgp.adapter.protocol.iec61850.domain.entities.Iec61850Device;
import com.alliander.osgp.adapter.protocol.iec61850.domain.repositories.Iec61850DeviceRepository;
import com.alliander.osgp.cucumber.core.GlueBase;
import com.alliander.osgp.cucumber.core.ScenarioContext;
import com.alliander.osgp.cucumber.platform.helpers.SettingsHelper;
import com.alliander.osgp.cucumber.platform.microgrids.PlatformMicrogridsDefaults;
import com.alliander.osgp.cucumber.platform.microgrids.PlatformMicrogridsKeys;
import com.alliander.osgp.cucumber.platform.microgrids.config.Iec61850MockServerConfig;
import com.alliander.osgp.cucumber.platform.microgrids.glue.steps.database.core.RtuDeviceSteps;

import cucumber.api.java.en.Given;

/**
 * IEC61850 specific device steps.
 */
public class Iec61850DeviceSteps extends GlueBase {

    private static final String DEFAULT_DEVICE_TYPE = "RTU";
    private static final String DEFAULT_PROTOCOL = "IEC61850";
    private static final String DEFAULT_PROTOCOL_VERSION = "1.0";

    private static final Map<String, String> RTU_DEFAULT_SETTINGS = Collections
            .unmodifiableMap(new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;
                {
                    this.put(PlatformMicrogridsKeys.KEY_DEVICE_TYPE, DEFAULT_DEVICE_TYPE);
                    this.put(PlatformMicrogridsKeys.KEY_PROTOCOL, DEFAULT_PROTOCOL);
                    this.put(PlatformMicrogridsKeys.KEY_PROTOCOL_VERSION, DEFAULT_PROTOCOL_VERSION);
                }
            });

    @Autowired
    private Iec61850DeviceRepository iec61850DeviceRespository;

    @Autowired
    private Iec61850MockServerConfig iec61850MockServerConfig;

    @Autowired
    private RtuDeviceSteps rtuDeviceSteps;

    /**
     * Creates an IEC61850 device.
     *
     * @param settings
     * @throws Throwable
     */
    @Given("^an rtu iec61850 device$")
    public void anRtuIec61850Device(final Map<String, String> settings) throws Throwable {

        ScenarioContext.current().put(PlatformMicrogridsKeys.KEY_DEVICE_IDENTIFICATION,
                PlatformMicrogridsDefaults.DEFAULT_DEVICE_IDENTIFICATION);
        final Map<String, String> rtuSettings = SettingsHelper.addAsDefaults(settings, RTU_DEFAULT_SETTINGS);
        rtuSettings.put(PlatformMicrogridsKeys.KEY_NETWORKADDRESS,
                this.iec61850MockServerConfig.iec61850MockNetworkAddress());

        this.rtuDeviceSteps.anRtuDevice(rtuSettings);

        this.rtuDeviceSteps.updateRtuDevice(rtuSettings);

        this.createIec61850Device(rtuSettings);
    }

    @Transactional("txMgrIec61850")
    private void createIec61850Device(final Map<String, String> settings) {

        /*
         * Make sure an ICD filename, port and servername corresponding to the
         * mock server settings will be used from the application to connect to
         * the device. ICD filename, port and servername may be null
         */
        final Iec61850Device iec61850Device = new Iec61850Device(
                getString(settings, PlatformMicrogridsKeys.KEY_DEVICE_IDENTIFICATION,
                        PlatformMicrogridsDefaults.DEFAULT_DEVICE_IDENTIFICATION));

        iec61850Device.setIcdFilename(getString(settings, PlatformMicrogridsKeys.KEY_IEC61850_ICD_FILENAME));
        iec61850Device.setPort(getInteger(settings, PlatformMicrogridsKeys.KEY_IEC61850_PORT));
        iec61850Device.setServerName(getString(settings, PlatformMicrogridsKeys.KEY_IEC61850_SERVERNAME));
        iec61850Device.setEnableAllReportsOnConnect(getBoolean(settings, PlatformMicrogridsKeys.ENABLE_ALL_REPORTS,
                PlatformMicrogridsDefaults.ENABLE_ALL_REPORTS));
        iec61850Device.setUseCombinedLoad(getBoolean(settings, PlatformMicrogridsKeys.USE_COMBINED_LOAD,
                PlatformMicrogridsDefaults.USE_COMBINED_LOAD));

        this.iec61850DeviceRespository.save(iec61850Device);
    }
}
