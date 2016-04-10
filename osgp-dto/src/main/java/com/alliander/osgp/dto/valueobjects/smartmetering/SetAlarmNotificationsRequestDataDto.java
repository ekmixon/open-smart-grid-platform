/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */

package com.alliander.osgp.dto.valueobjects.smartmetering;

import java.io.Serializable;

public class SetAlarmNotificationsRequestDataDto implements Serializable, ActionDto {

    private static final long serialVersionUID = -1833812559523610412L;

    private final AlarmNotificationsDto alarmNotifications;

    public SetAlarmNotificationsRequestDataDto(final AlarmNotificationsDto alarmNotifications) {
        super();
        this.alarmNotifications = alarmNotifications;
    }

    public AlarmNotificationsDto getAlarmNotifications() {
        return this.alarmNotifications;
    }

}
