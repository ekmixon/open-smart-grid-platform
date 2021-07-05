/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.security;

import static org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.SecurityKeyType.E_METER_AUTHENTICATION;
import static org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.SecurityKeyType.E_METER_ENCRYPTION;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.opensmartgridplatform.adapter.protocol.dlms.application.services.SecretManagementService;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.AbstractCommandExecutor;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.commands.CorrelatedObject;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.SecurityKeyType;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.factories.DlmsConnectionManager;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ActionRequestDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.ActionResponseDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.GenerateAndReplaceKeysRequestDataDto;
import org.opensmartgridplatform.dto.valueobjects.smartmetering.SetKeysRequestDto;
import org.opensmartgridplatform.shared.exceptionhandling.ComponentType;
import org.opensmartgridplatform.shared.exceptionhandling.EncrypterException;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalExceptionType;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateAndReplaceKeyCommandExecutor
    extends AbstractCommandExecutor<CorrelatedObject<ActionRequestDto>, ActionResponseDto> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(GenerateAndReplaceKeyCommandExecutor.class);

  @Autowired private ReplaceKeyCommandExecutor replaceKeyCommandExecutor;

  @Autowired private SecretManagementService secretManagementService;

  public GenerateAndReplaceKeyCommandExecutor() {
    super(GenerateAndReplaceKeysRequestDataDto.class);
  }

  @Override
  public ActionResponseDto executeBundleAction(
      final DlmsConnectionManager conn,
      final DlmsDevice device,
      final ActionRequestDto actionRequestDto)
      throws OsgpException {
    return this.execute(conn, device, (CorrelatedObject<ActionRequestDto>) actionRequestDto);
  }

  @Override
  public ActionResponseDto execute(
      final DlmsConnectionManager conn,
      final DlmsDevice device,
      final CorrelatedObject<ActionRequestDto> actionRequestDto)
      throws OsgpException {
    LOGGER.info("Generate new keys for device {}", device.getDeviceIdentification());
    final SetKeysRequestDto setKeysRequest =
        this.generateSetKeysRequest(
            actionRequestDto.getCorrelationUid(), device.getDeviceIdentification());
    return this.replaceKeyCommandExecutor.executeBundleAction(conn, device, setKeysRequest);
  }

  private SetKeysRequestDto generateSetKeysRequest(
      final String correlationUid, final String deviceIdentification) throws FunctionalException {
    try {
      final List<SecurityKeyType> keyTypes =
          Arrays.asList(E_METER_AUTHENTICATION, E_METER_ENCRYPTION);
      final Map<SecurityKeyType, byte[]> generatedKeys =
          this.secretManagementService.generate128BitsKeysAndStoreAsNewKeys(
              correlationUid, deviceIdentification, keyTypes);
      final SetKeysRequestDto setKeysRequest =
          new SetKeysRequestDto(
              generatedKeys.get(E_METER_AUTHENTICATION), generatedKeys.get(E_METER_ENCRYPTION));
      setKeysRequest.setGeneratedKeys(true);
      return setKeysRequest;
    } catch (final EncrypterException e) {
      throw new FunctionalException(
          FunctionalExceptionType.ENCRYPTION_EXCEPTION, ComponentType.PROTOCOL_DLMS, e);
    }
  }
}
