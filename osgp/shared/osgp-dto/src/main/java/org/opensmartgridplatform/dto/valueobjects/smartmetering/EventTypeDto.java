/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects.smartmetering;

public enum EventTypeDto {
    EVENTLOG_CLEARED,
    POWER_FAILURE,
    POWER_FAILURE_G,
    POWER_FAILURE_W,
    POWER_RETURNED,
    CLOCK_UPDATE,
    CLOCK_ADJUSTED_OLD_TIME,
    CLOCK_ADJUSTED_NEW_TIME,
    CLOCK_INVALID,
    REPLACE_BATTERY,
    BATTERY_VOLTAGE_LOW,
    TARIFF_ACTIVATED,
    ERROR_REGISTER_CLEARED,
    ALARM_REGISTER_CLEARED,
    HARDWARE_ERROR_PROGRAM_MEMORY,
    HARDWARE_ERROR_RAM,
    HARDWARE_ERROR_NV_MEMORY,
    WATCHDOG_ERROR,
    HARDWARE_ERROR_MEASUREMENT_SYSTEM,
    FIRMWARE_READY_FOR_ACTIVATION,
    FIRMWARE_ACTIVATED,
    PASSIVE_TARIFF_UPDATED,
    SUCCESSFUL_SELFCHECK_AFTER_FIRMWARE_UPDATE,
    COMMUNICATION_MODULE_REMOVED,
    COMMUNICATION_MODULE_INSERTED,
    TERMINAL_COVER_REMOVED,
    TERMINAL_COVER_CLOSED,
    STRONG_DC_FIELD_DETECTED,
    NO_STRONG_DC_FIELD_ANYMORE,
    METER_COVER_REMOVED,
    METER_COVER_CLOSED,
    FAILED_LOGIN_ATTEMPT,
    CONFIGURATION_CHANGE,
    MODULE_COVER_OPENED,
    MODULE_COVER_CLOSED,
    METROLOGICAL_MAINTENANCE,
    TECHNICAL_MAINTENANCE,
    RETRIEVE_METER_READINGS_E,
    RETRIEVE_METER_READINGS_G,
    RETRIEVE_INTERVAL_DATA_E,
    RETRIEVE_INTERVAL_DATA_G,
    UNDER_VOLTAGE_L1,
    UNDER_VOLTAGE_L2,
    UNDER_VOLTAGE_L3,
    PV_VOLTAGE_SAG_L1,
    PV_VOLTAGE_SAG_L2,
    PV_VOLTAGE_SAG_L3,
    PV_VOLTAGE_SWELL_L1,
    PV_VOLTAGE_SWELL_L2,
    PV_VOLTAGE_SWELL_L3,
    OVER_VOLTAGE_L1,
    OVER_VOLTAGE_L2,
    OVER_VOLTAGE_L3,
    VOLTAGE_L1_NORMAL,
    VOLTAGE_L2_NORMAL,
    VOLTAGE_L3_NORMAL,
    PHASE_OUTAGE_L1,
    PHASE_OUTAGE_L2,
    PHASE_OUTAGE_L3,
    PHASE_OUTAGE_TEST,
    PHASE_RETURNED_L1,
    PHASE_RETURNED_L2,
    PHASE_RETURNED_L3,
    COMMUNICATION_ERROR_M_BUS_CHANNEL_1,
    COMMUNICATION_OK_M_BUS_CHANNEL_1,
    REPLACE_BATTERY_M_BUS_CHANNEL_1,
    FRAUD_ATTEMPT_M_BUS_CHANNEL_1,
    CLOCK_ADJUSTED_M_BUS_CHANNEL_1,
    NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_1,
    PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_1,
    COMMUNICATION_ERROR_M_BUS_CHANNEL_2,
    COMMUNICATION_OK_M_BUS_CHANNEL_2,
    REPLACE_BATTERY_M_BUS_CHANNEL_2,
    FRAUD_ATTEMPT_M_BUS_CHANNEL_2,
    CLOCK_ADJUSTED_M_BUS_CHANNEL_2,
    NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_2,
    PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_2,
    COMMUNICATION_ERROR_M_BUS_CHANNEL_3,
    COMMUNICATION_OK_M_BUS_CHANNEL_3,
    REPLACE_BATTERY_M_BUS_CHANNEL_3,
    FRAUD_ATTEMPT_M_BUS_CHANNEL_3,
    CLOCK_ADJUSTED_M_BUS_CHANNEL_3,
    NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_3,
    PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_3,
    COMMUNICATION_ERROR_M_BUS_CHANNEL_4,
    COMMUNICATION_OK_M_BUS_CHANNEL_4,
    REPLACE_BATTERY_M_BUS_CHANNEL_4,
    FRAUD_ATTEMPT_M_BUS_CHANNEL_4,
    CLOCK_ADJUSTED_M_BUS_CHANNEL_4,
    NEW_M_BUS_DEVICE_DISCOVERED_CHANNEL_4,
    PERMANENT_ERROR_FROM_M_BUS_DEVICE_CHANNEL_4,
    MANUFACTURER_SPECIFIC_231,
    MANUFACTURER_SPECIFIC_232,
    MANUFACTURER_SPECIFIC_233,
    MANUFACTURER_SPECIFIC_234,
    MANUFACTURER_SPECIFIC_235,
    MANUFACTURER_SPECIFIC_236,
    MANUFACTURER_SPECIFIC_237,
    MANUFACTURER_SPECIFIC_238,
    MANUFACTURER_SPECIFIC_239,
    MANUFACTURER_SPECIFIC_240,
    MANUFACTURER_SPECIFIC_241,
    MANUFACTURER_SPECIFIC_242,
    MANUFACTURER_SPECIFIC_243,
    MANUFACTURER_SPECIFIC_244,
    MANUFACTURER_SPECIFIC_245,
    MANUFACTURER_SPECIFIC_246,
    MANUFACTURER_SPECIFIC_247,
    MANUFACTURER_SPECIFIC_248,
    MANUFACTURER_SPECIFIC_249,
    FATAL_ERROR_ISKR,
    BILLING_RESET_ISKR,
    POWER_DOWN_PHASE_L1_ISKR,
    POWER_DOWN_PHASE_L2_ISKR,
    POWER_DOWN_PHASE_L3_ISKR,
    POWER_RESTORED_PHASE_L1_ISKR,
    POWER_RESTORED_PHASE_L2_ISKR,
    POWER_RESTORED_PHASE_L3_ISKR,
    MODULE_COVER_OPENED_ISKR,
    MODULE_COVER_CLOSED_ISKR;
}
