/**
 * Copyright 2014-2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alliander.osgp.adapter.protocol.iec61850.device.rtu.RtuReadCommand;
import com.alliander.osgp.adapter.protocol.iec61850.device.rtu.RtuReadCommandFactory;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.helper.DataAttribute;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850AlarmCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850AlarmOtherCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850BehaviourCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850HealthCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850LoadActualPowerCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850LoadMaximumActualPowerCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850LoadMinimumActualPowerCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850LoadTotalEnergyCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850ModeCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850WarningCommand;
import com.alliander.osgp.adapter.protocol.iec61850.infra.networking.services.commands.Iec61850WarningOtherCommand;
import com.alliander.osgp.dto.valueobjects.microgrids.MeasurementDto;
import com.alliander.osgp.dto.valueobjects.microgrids.MeasurementFilterDto;

public class Iec61850LoadCommandFactory implements RtuReadCommandFactory<MeasurementDto, MeasurementFilterDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Iec61850LoadCommandFactory.class);

    private static final int ID_START = 1;
    private static final int ID_END = 5;

    private static Iec61850LoadCommandFactory instance;

    private Map<String, RtuReadCommand<MeasurementDto>> rtuCommandMap = new HashMap<>();

    private Iec61850LoadCommandFactory() {
        this.rtuCommandMap.put(DataAttribute.BEHAVIOR.getDescription(), new Iec61850BehaviourCommand());
        this.rtuCommandMap.put(DataAttribute.HEALTH.getDescription(), new Iec61850HealthCommand());
        this.rtuCommandMap.put(DataAttribute.MODE.getDescription(), new Iec61850ModeCommand());

        for (int i = ID_START; i <= ID_END; i++) {
            this.rtuCommandMap.put(DataAttribute.ACTUAL_POWER.getDescription() + i,
                    new Iec61850LoadActualPowerCommand(i));
            this.rtuCommandMap.put(DataAttribute.MAX_ACTUAL_POWER.getDescription() + i,
                    new Iec61850LoadMaximumActualPowerCommand(i));
            this.rtuCommandMap.put(DataAttribute.MIN_ACTUAL_POWER.getDescription() + i,
                    new Iec61850LoadMinimumActualPowerCommand(i));

            this.rtuCommandMap.put(DataAttribute.TOTAL_ENERGY.getDescription() + i,
                    new Iec61850LoadTotalEnergyCommand(i));
        }

        this.rtuCommandMap.put(DataAttribute.ALARM_ONE.getDescription(), new Iec61850AlarmCommand(1));
        this.rtuCommandMap.put(DataAttribute.ALARM_TWO.getDescription(), new Iec61850AlarmCommand(2));
        this.rtuCommandMap.put(DataAttribute.ALARM_THREE.getDescription(), new Iec61850AlarmCommand(3));
        this.rtuCommandMap.put(DataAttribute.ALARM_FOUR.getDescription(), new Iec61850AlarmCommand(4));
        this.rtuCommandMap.put(DataAttribute.ALARM_OTHER.getDescription(), new Iec61850AlarmOtherCommand());
        this.rtuCommandMap.put(DataAttribute.WARNING_ONE.getDescription(), new Iec61850WarningCommand(1));
        this.rtuCommandMap.put(DataAttribute.WARNING_TWO.getDescription(), new Iec61850WarningCommand(2));
        this.rtuCommandMap.put(DataAttribute.WARNING_THREE.getDescription(), new Iec61850WarningCommand(3));
        this.rtuCommandMap.put(DataAttribute.WARNING_FOUR.getDescription(), new Iec61850WarningCommand(4));
        this.rtuCommandMap.put(DataAttribute.WARNING_OTHER.getDescription(), new Iec61850WarningOtherCommand());
    }

    public static Iec61850LoadCommandFactory getInstance() {
        if (instance == null) {
            instance = new Iec61850LoadCommandFactory();
        }
        return instance;
    }

    @Override
    public RtuReadCommand<MeasurementDto> getCommand(final MeasurementFilterDto filter) {
        final DataAttribute da = DataAttribute.fromString(filter.getNode());
        if (this.useFilterId(da)) {
            return this.getCommand(filter.getNode() + filter.getId());
        } else {
            return this.getCommand(filter.getNode());
        }
    }

    @Override
    public RtuReadCommand<MeasurementDto> getCommand(final String node) {
        final RtuReadCommand<MeasurementDto> command = this.rtuCommandMap.get(node);

        if (command == null) {
            LOGGER.warn("No command found for node {}", node);
        }
        return command;
    }

    private boolean useFilterId(final DataAttribute da) {
        return da != DataAttribute.BEHAVIOR && da != DataAttribute.HEALTH && da != DataAttribute.MODE;
    }
}
