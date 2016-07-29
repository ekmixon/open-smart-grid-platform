/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.domain.core.valueobjects.smartmetering;

import java.io.Serializable;

import com.alliander.osgp.shared.exceptionhandling.FunctionalException;

public class SynchronizeTimeRequestData implements Serializable, ActionRequest {

    private static final long serialVersionUID = -4724182593235620894L;

    private final int deviation;

    private final boolean dst;

    public SynchronizeTimeRequestData(final int deviation, final boolean dst) {
        this.deviation = deviation;
        this.dst = dst;
    }

    public int getDeviation() {
        return this.deviation;
    }

    public boolean isDst() {
        return this.dst;
    }

    @Override
    public void validate() throws FunctionalException {
        // TODO: add validation
    }
}
