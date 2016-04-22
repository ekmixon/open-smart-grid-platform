/**
 * Copyright 2016 Smart Society Services B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 */
package com.alliander.osgp.dto.valueobjects.smartmetering;

import java.io.Serializable;

public class SpecificConfigurationObjectDto implements Serializable {
    private static final long serialVersionUID = 2506458162101143461L;

    private int classId;
    private int attribute;
    private ObisCodeValuesDto obisCode;
    
    public SpecificConfigurationObjectDto(int classId, int attribute, ObisCodeValuesDto obisCode) {
        super();
        this.classId = classId;
        this.attribute = attribute;
        this.obisCode = obisCode;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public int getClassId() {
        return classId;
    }

    public int getAttribute() {
        return attribute;
    }

    public ObisCodeValuesDto getObisCode() {
        return obisCode;
    }

    
}
