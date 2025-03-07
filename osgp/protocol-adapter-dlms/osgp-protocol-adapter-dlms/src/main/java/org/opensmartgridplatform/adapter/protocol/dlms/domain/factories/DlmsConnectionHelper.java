/*
 * Copyright 2019 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.adapter.protocol.dlms.domain.factories;

import java.time.Duration;
import org.opensmartgridplatform.adapter.protocol.dlms.application.config.DevicePingConfig;
import org.opensmartgridplatform.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.opensmartgridplatform.adapter.protocol.dlms.exceptions.ConnectionException;
import org.opensmartgridplatform.adapter.protocol.dlms.infra.messaging.DlmsMessageListener;
import org.opensmartgridplatform.shared.exceptionhandling.OsgpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Helper class for acquiring connections to DLMS devices, that takes care of details like
 * initializing invocation counters when required.
 */
@Component
public class DlmsConnectionHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(DlmsConnectionHelper.class);
  private static final Duration NO_DELAY = Duration.ZERO;

  private final InvocationCounterManager invocationCounterManager;
  private final DlmsConnectionFactory connectionFactory;
  private final DevicePingConfig devicePingConfig;
  private final Duration delayBetweenDlmsConnections;

  @Autowired
  public DlmsConnectionHelper(
      final InvocationCounterManager invocationCounterManager,
      final DlmsConnectionFactory connectionFactory,
      final DevicePingConfig devicePingConfig,
      @Value("${dlms.connections.delay.seconds:30}") final int secondsBetweenDlmsConnections) {

    this.invocationCounterManager = invocationCounterManager;
    this.connectionFactory = connectionFactory;
    this.devicePingConfig = devicePingConfig;
    this.delayBetweenDlmsConnections =
        secondsBetweenDlmsConnections < 1
            ? NO_DELAY
            : Duration.ofSeconds(secondsBetweenDlmsConnections);
  }

  /**
   * Returns an open connection to the device, taking care of details like initializing the
   * invocation counter when required.
   */
  public DlmsConnectionManager createConnectionForDevice(
      final DlmsDevice device, final DlmsMessageListener messageListener) throws OsgpException {

    final boolean pingDevice =
        this.devicePingConfig.pingingEnabled() && StringUtils.hasText(device.getIpAddress());
    final boolean initializeInvocationCounter =
        device.needsInvocationCounter() && !device.isInvocationCounterInitialized();
    final Duration waitBeforeInitializingInvocationCounter = NO_DELAY;
    final Duration waitBeforeCreatingTheConnection =
        initializeInvocationCounter ? this.delayBetweenDlmsConnections : NO_DELAY;

    return this.createConnectionForDevice(
        device,
        messageListener,
        pingDevice,
        initializeInvocationCounter,
        waitBeforeInitializingInvocationCounter,
        waitBeforeCreatingTheConnection);
  }

  private void delay(final Duration duration) {
    if (duration == NO_DELAY) {
      return;
    }
    try {
      Thread.sleep(duration.toMillis());
    } catch (final InterruptedException e) {
      LOGGER.warn("Sleeping to achieve a delay of {} was interrupted", duration, e);
      Thread.currentThread().interrupt();
    }
  }

  private DlmsConnectionManager createConnectionForDevice(
      final DlmsDevice device,
      final DlmsMessageListener messageListener,
      final boolean pingDevice,
      final boolean initializeInvocationCounter,
      final Duration waitBeforeInitializingInvocationCounter,
      final Duration waitBeforeCreatingTheConnection)
      throws OsgpException {

    if (pingDevice) {
      this.devicePingConfig.pinger().ping(device.getIpAddress());
    }

    if (initializeInvocationCounter) {
      this.delay(waitBeforeInitializingInvocationCounter);
      this.invocationCounterManager.initializeInvocationCounter(device);
    }

    try {
      this.delay(waitBeforeCreatingTheConnection);
      return this.connectionFactory.getConnection(device, messageListener);
    } catch (final ConnectionException e) {
      if ((device.needsInvocationCounter() && this.indicatesInvocationCounterOutOfSync(e))
          && !initializeInvocationCounter) {
        LOGGER.warn(
            "Invocation counter (stored value: {}) appears to be out of sync for {}, retry initializing the counter",
            device.getInvocationCounter(),
            device.getDeviceIdentification());
        /*
         * The connection exception is likely caused by an already initialized invocation counter
         * that does not have an appropriate value compared to the counter on the actual device.
         * Retry creating the connection, do not ping anymore, as the device should have already
         * been pinged if that was appropriate. Make sure the invocation counter is initialized,
         * regardless of whether it has already been initialized before or not.
         */
        return this.createConnectionForDevice(
            device,
            messageListener,
            false,
            true,
            this.delayBetweenDlmsConnections,
            this.delayBetweenDlmsConnections);
      }
      /*
       * The connection exception is assumed not to be related to the invocation counter, or has
       * occurred despite the attempt to initialize the invocation counter.
       * Do not go into a loop trying to repeat setting up the connection possibly trying to
       * initialize the invocation counter over and over again.
       * Throw the connection exception and have other parts of the code decide whether or not a
       * retry will be scheduled.
       */
      throw e;
    }
  }

  private boolean indicatesInvocationCounterOutOfSync(final ConnectionException e) {
    return e.getMessage().contains("Socket was closed by remote host.")
        || e.getMessage()
            .contains(
                "Received an association response (AARE) with an error message. Result name REJECTED_PERMANENT. "
                    + "Assumed fault: user.");
  }
}
