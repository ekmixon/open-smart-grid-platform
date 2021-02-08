/**
 * Copyright 2021 Alliander N.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package org.opensmartgridplatform.dto.valueobjects.smartmetering;

import java.io.Serializable;
import java.util.List;

public class ActualPowerQualityDataDto implements Serializable {

    private static final long serialVersionUID = 5222890224967684849L;

    private final List<CaptureObjectDto> captureObjects;
    private final List<ActualValueDto> actualValues;

    public ActualPowerQualityDataDto(final List<CaptureObjectDto> captureObjects,
            final List<ActualValueDto> actualValues) {
        super();
        this.captureObjects = captureObjects;
        this.actualValues = actualValues;
    }

    public List<CaptureObjectDto> getCaptureObjects() {
        return this.captureObjects;
    }

    public List<ActualValueDto> getActualValues() {
        return this.actualValues;
    }
}
