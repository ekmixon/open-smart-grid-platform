/*
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects.smartmetering;

public enum NotificationTypeDto {
  ADD_METER,
  FIND_EVENTS,
  GET_ACTUAL_POWER_QUALITY,
  REQUEST_PERIODIC_METER_DATA,
  SYNCHRONIZE_TIME,
  SET_SPECIAL_DAYS,
  SET_ALARM_NOTIFICATIONS,
  SET_CONFIGURATION_OBJECT,
  SET_ADMINISTRATIVE_STATUS,
  GET_ADMINISTRATIVE_STATUS,
  SET_ACTIVITY_CALENDAR,
  REQUEST_ACTUAL_METER_DATA,
  READ_ALARM_REGISTER,
  PUSH_NOTIFICATION_ALARM,
  SEND_WAKEUP_SMS,
  GET_SMS_DETAILS,
  REPLACE_KEYS,
  SET_PUSH_SETUP_ALARM,
  SET_PUSH_SETUP_SMS,
  GET_ALL_ATTRIBUTE_VALUES,
  GET_SPECIFIC_ATTRIBUTE_VALUE,
  SET_ENCRYPTION_KEY_EXCHANGE_ON_G_METER,
  HANDLE_BUNDLED_ACTIONS,
  GET_ASSOCIATION_LN_OBJECTS,
  GET_FIRMWARE_VERSION,
  COUPLE_MBUS_DEVICE,
  DECOUPLE_MBUS_DEVICE,
  UPDATE_FIRMWARE,
  ENABLE_DEBUGGING,
  DISABLE_DEBUGGING,
  GET_MESSAGES,
  GET_PROFILE_GENERIC_DATA,
  SET_CLOCK_CONFIGURATION,
  GET_CONFIGURATION_OBJECT,
  GENERATE_AND_REPLACE_KEYS,
  CONFIGURE_DEFINABLE_LOAD_PROFILE,
  SET_MBUS_USER_KEY_BY_CHANNEL,
  COUPLE_MBUS_DEVICE_BY_CHANNEL,
  GET_MBUS_ENCRYPTION_KEY_STATUS,
  SET_DEVICE_COMMUNICATION_SETTINGS,
  CLEAR_ALARM_REGISTER,
  GET_MBUS_ENCRYPTION_KEY_STATUS_BY_CHANNEL,
  SET_DEVICE_LIFECYCLE_STATUS_BY_CHANNEL,
  SCAN_MBUS_CHANNELS,
  DECOUPLE_MBUS_DEVICE_BY_CHANNEL,
  SET_RANDOMISATION_SETTINGS,
  SET_COMMUNICATION_NETWORK_INFORMATION
}
