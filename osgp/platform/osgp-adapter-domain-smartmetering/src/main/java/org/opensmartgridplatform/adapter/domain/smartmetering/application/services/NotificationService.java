/*
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.domain.smartmetering.application.services;

import ma.glasnost.orika.MapperFactory;
import org.opensmartgridplatform.adapter.domain.smartmetering.infra.jms.ws.WebServiceResponseMessageSender;
import org.opensmartgridplatform.domain.core.valueobjects.smartmetering.PushNotificationAlarm;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.PushNotificationAlarmDto;
import org.opensmartgridplatform.shared.infra.jms.DeviceMessageMetadata;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessage;
import org.opensmartgridplatform.shared.infra.jms.ResponseMessageResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "domainSmartMeteringNotificationService")
public class NotificationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

  @Autowired private MapperFactory mapperFactory;

  @Autowired private WebServiceResponseMessageSender webServiceResponseMessageSender;

  public void handlePushNotificationAlarm(
      final DeviceMessageMetadata deviceMessageMetadata,
      final PushNotificationAlarmDto pushNotificationAlarm) {

    LOGGER.info(
        "handlePushNotificationAlarm for MessageType: {}", deviceMessageMetadata.getMessageType());

    final PushNotificationAlarm pushNotificationAlarmDomain =
        this.mapperFactory
            .getMapperFacade()
            .map(pushNotificationAlarm, PushNotificationAlarm.class);

    /*
     * Send the push notification alarm as a response message to the web service, so
     * it can be handled similar to response messages based on earlier web service
     * requests.
     */
    final ResponseMessage responseMessage =
        ResponseMessage.newResponseMessageBuilder()
            .withCorrelationUid(deviceMessageMetadata.getCorrelationUid())
            .withOrganisationIdentification(deviceMessageMetadata.getOrganisationIdentification())
            .withDeviceIdentification(deviceMessageMetadata.getDeviceIdentification())
            .withResult(ResponseMessageResultType.OK)
            .withDataObject(pushNotificationAlarmDomain)
            .withMessagePriority(deviceMessageMetadata.getMessagePriority())
            .build();
    this.webServiceResponseMessageSender.send(
        responseMessage, deviceMessageMetadata.getMessageType());
  }
}
