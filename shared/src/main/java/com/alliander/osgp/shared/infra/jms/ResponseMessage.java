/**
 * Copyright 2015 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.shared.infra.jms;

import java.io.Serializable;

import com.alliander.osgp.shared.exceptionhandling.OsgpException;

public class ResponseMessage implements Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -214808702310700742L;

    private final String correlationUid;
    private final String organisationIdentification;
    private final String deviceIdentification;
    private final ResponseMessageResultType result;
    private final OsgpException osgpException;
    private final Serializable dataObject;

    public ResponseMessage(final String correlationUid, final String organisationIdentification,
            final String deviceIdentification, final ResponseMessageResultType result,
            final OsgpException osgpException, final Serializable dataObject) {
        this.correlationUid = correlationUid;
        this.organisationIdentification = organisationIdentification;
        this.deviceIdentification = deviceIdentification;
        this.result = result;
        this.osgpException = osgpException;
        this.dataObject = dataObject;
    }

    public ResponseMessage(final String correlationUid, final String organisationIdentification,
            final String deviceIdentification, final ResponseMessageResultType result, final OsgpException osgpException) {
        this(correlationUid, organisationIdentification, deviceIdentification, result, osgpException, null);
    }

    public String getCorrelationUid() {
        return this.correlationUid;
    }

    public String getOrganisationIdentification() {
        return this.organisationIdentification;
    }

    public String getDeviceIdentification() {
        return this.deviceIdentification;
    }

    public ResponseMessageResultType getResult() {
        return this.result;
    }

    public OsgpException getOsgpException() {
        return this.osgpException;
    }

    public Serializable getDataObject() {
        return this.dataObject;
    }

}
