/**
 * Copyright 2017 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package org.osgp.adapter.protocol.dlms.infra.messaging.responses.from.core;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.osgp.adapter.protocol.dlms.application.services.DomainHelperService;
import org.osgp.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.osgp.adapter.protocol.dlms.domain.factories.DlmsConnectionFactory;
import org.osgp.adapter.protocol.dlms.domain.factories.DlmsConnectionHolder;
import org.osgp.adapter.protocol.dlms.exceptions.OsgpExceptionConverter;
import org.osgp.adapter.protocol.dlms.exceptions.ProtocolAdapterException;
import org.osgp.adapter.protocol.dlms.exceptions.RetryableException;
import org.osgp.adapter.protocol.dlms.infra.messaging.DeviceResponseMessageSender;
import org.osgp.adapter.protocol.dlms.infra.messaging.DlmsLogItemRequestMessageSender;
import org.osgp.adapter.protocol.dlms.infra.messaging.LoggingDlmsMessageListener;
import org.osgp.adapter.protocol.dlms.infra.messaging.RetryHeaderFactory;
import org.osgp.adapter.protocol.dlms.infra.messaging.requests.to.core.OsgpRequestMessageType;
import org.osgp.adapter.protocol.jasper.sessionproviders.exceptions.SessionProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alliander.osgp.shared.exceptionhandling.OsgpException;
import com.alliander.osgp.shared.infra.jms.DeviceMessageMetadata;
import com.alliander.osgp.shared.infra.jms.MessageMetadata;
import com.alliander.osgp.shared.infra.jms.MessageProcessor;
import com.alliander.osgp.shared.infra.jms.MessageProcessorMap;
import com.alliander.osgp.shared.infra.jms.ProtocolResponseMessage;
import com.alliander.osgp.shared.infra.jms.ResponseMessageResultType;
import com.alliander.osgp.shared.infra.jms.RetryHeader;

/**
 * Base class for MessageProcessor implementations. Each MessageProcessor implementation should be annotated
 * with @Component. Further the MessageType the MessageProcessor implementation can process should be passed in at
 * construction. The Singleton instance is added to the HashMap of MessageProcessors after dependency injection has
 * completed.
 */
public abstract class OsgpResponseMessageProcessor implements MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsgpResponseMessageProcessor.class);

    @Autowired
    protected DeviceResponseMessageSender responseMessageSender;

    @Autowired
    @Qualifier("protocolDlmsOsgpResponseMessageProcessorMap")
    protected MessageProcessorMap osgpResponseMessageProcessorMap;

    @Autowired
    protected DlmsLogItemRequestMessageSender dlmsLogItemRequestMessageSender;

    @Autowired
    protected OsgpExceptionConverter osgpExceptionConverter;

    @Autowired
    protected DomainHelperService domainHelperService;

    @Autowired
    protected DlmsConnectionFactory dlmsConnectionFactory;

    @Autowired
    private RetryHeaderFactory retryHeaderFactory;

    protected final OsgpRequestMessageType osgpRequestMessageType;

    /**
     * Each MessageProcessor should register it's MessageType at construction.
     *
     * @param deviceRequestMessageType
     *            The MessageType the MessageProcessor implementation can process.
     */
    protected OsgpResponseMessageProcessor(final OsgpRequestMessageType osgpRequestMessageType) {
        this.osgpRequestMessageType = osgpRequestMessageType;
    }

    /**
     * Initialization function executed after dependency injection has finished. The MessageProcessor Singleton is added
     * to the HashMap of MessageProcessors. The key for the HashMap is the integer value of the enumeration member.
     */
    @PostConstruct
    public void init() {
        this.osgpResponseMessageProcessorMap.addMessageProcessor(this.osgpRequestMessageType.ordinal(),
                this.osgpRequestMessageType.name(), this);
    }

    /**
     * @param logger
     *            the logger from the calling subClass
     * @param exception
     *            the exception to be logged
     * @param messageMetadata
     *            a DlmsDeviceMessageMetadata containing debug info to be logged
     */
    protected void logJmsException(final Logger logger, final JMSException exception,
            final MessageMetadata messageMetadata) {
        logger.error("UNRECOVERABLE ERROR, unable to read ObjectMessage instance, giving up.", exception);
        logger.debug("correlationUid: {}", messageMetadata.getCorrelationUid());
        logger.debug("domain: {}", messageMetadata.getDomain());
        logger.debug("domainVersion: {}", messageMetadata.getDomainVersion());
        logger.debug("messageType: {}", messageMetadata.getMessageType());
        logger.debug("organisationIdentification: {}", messageMetadata.getOrganisationIdentification());
        logger.debug("deviceIdentification: {}", messageMetadata.getDeviceIdentification());
    }

    protected void assertRequestObjectType(final Class<?> expected, final Serializable requestObject)
            throws ProtocolAdapterException {
        if (!expected.isInstance(requestObject)) {
            throw new ProtocolAdapterException(
                    String.format("The request object has an incorrect type. %s expected but %s was found.",
                            expected.getCanonicalName(), requestObject.getClass().getCanonicalName()));
        }
    }

    @Override
    public void processMessage(final ObjectMessage message) throws JMSException {
        LOGGER.debug("Processing {} request message", this.osgpRequestMessageType.name());
        MessageMetadata messageMetadata = null;
        DlmsConnectionHolder conn = null;
        DlmsDevice device = null;

        try {
            messageMetadata = MessageMetadata.fromMessage(message);

            device = this.domainHelperService.findDlmsDevice(messageMetadata);

            LOGGER.info("{} called for device: {} for organisation: {}", message.getJMSType(),
                    messageMetadata.getDeviceIdentification(), messageMetadata.getOrganisationIdentification());

            Serializable response = null;
            if (this.usesDeviceConnection()) {
                final LoggingDlmsMessageListener dlmsMessageListener;
                if (device.isInDebugMode()) {
                    dlmsMessageListener = new LoggingDlmsMessageListener(device.getDeviceIdentification(),
                            this.dlmsLogItemRequestMessageSender);
                    dlmsMessageListener.setMessageMetadata(messageMetadata);
                    dlmsMessageListener.setDescription("Create connection");
                } else {
                    dlmsMessageListener = null;
                }
                conn = this.dlmsConnectionFactory.getConnection(device, dlmsMessageListener);
                response = this.handleMessage(conn, device, message.getObject());
            } else {
                response = this.handleMessage(device, message);
            }
        } catch (final JMSException exception) {
            this.logJmsException(LOGGER, exception, messageMetadata);
        } catch (final Exception exception) {
            // Return original request + exception
            LOGGER.error("Unexpected exception during {}", this.osgpRequestMessageType.name(), exception);

            // this.sendResponseMessage(messageMetadata, ResponseMessageResultType.NOT_OK, exception,
            // this.responseMessageSender, message.getObject(), isScheduled);
        } finally {
            if (conn != null) {
                LOGGER.info("Closing connection with {}", device.getDeviceIdentification());
                conn.getDlmsMessageListener().setDescription("Close connection");
                try {
                    conn.close();
                } catch (final Exception e) {
                    LOGGER.error("Error while closing connection", e);
                }
            }
        }
    }

    protected boolean getBooleanPropertyValue(final Message message, final String propertyName) throws JMSException {
        return message.propertyExists(propertyName) ? message.getBooleanProperty(propertyName) : false;
    }

    /**
     * Implementation of this method should call a service that can handle the requestObject and return a response
     * object to be put on the response queue. This response object can also be null for methods that don't provide
     * result data.
     *
     * @param DlmsConnection
     *            the connection to the device.
     * @param device
     *            the device.
     * @param requestObject
     *            Request data object.
     * @return A serializable object to be put on the response queue.
     * @throws OsgpException
     * @throws ProtocolAdapterException
     * @throws SessionProviderException
     */
    protected Serializable handleMessage(final DlmsConnectionHolder conn, final DlmsDevice device,
            final Serializable requestObject) throws OsgpException, ProtocolAdapterException, SessionProviderException {
        throw new UnsupportedOperationException(
                "handleMessage(DlmsConnection, DlmsDevice, Serializable) should be overriden by a subclass, or usesDeviceConnection should return false.");
    }

    protected Serializable handleMessage(final DlmsDevice device, final ObjectMessage message)
            throws OsgpException, ProtocolAdapterException {
        throw new UnsupportedOperationException(
                "handleMessage(Serializable) should be overriden by a subclass, or usesDeviceConnection should return true.");
    }

    protected void sendResponseMessage(final MessageMetadata messageMetadata, final ResponseMessageResultType result,
            final Exception exception, final DeviceResponseMessageSender responseMessageSender,
            final Serializable responseObject) {

        OsgpException osgpException = null;
        if (exception != null) {
            osgpException = this.osgpExceptionConverter.ensureOsgpOrTechnicalException(exception);
        }

        RetryHeader retryHeader;
        if ((result == ResponseMessageResultType.NOT_OK) && (exception instanceof RetryableException)) {
            retryHeader = this.retryHeaderFactory.createRetryHeader(messageMetadata.getRetryCount());
        } else {
            retryHeader = this.retryHeaderFactory.createEmtpyRetryHeader();
        }

        final ProtocolResponseMessage responseMessage = new ProtocolResponseMessage.Builder()
                .deviceMessageMetadata(new DeviceMessageMetadata(messageMetadata)).domain(messageMetadata.getDomain())
                .domainVersion(messageMetadata.getDomainVersion()).result(result).osgpException(osgpException)
                .dataObject(responseObject).retryCount(messageMetadata.getRetryCount()).retryHeader(retryHeader)
                .scheduled(messageMetadata.isScheduled()).build();

        responseMessageSender.send(responseMessage);
    }

    /**
     * Used to determine if the handleMessage needs a device connection or not. Default value is true, override to alter
     * behaviour of subclasses.
     *
     * @return Use device connection in handleMessage.
     */
    protected boolean usesDeviceConnection() {
        return true;
    }
}
