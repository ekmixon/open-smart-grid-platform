package org.osgp.adapter.protocol.dlms.domain.commands;

import com.alliander.osgp.dto.valueobjects.smartmetering.ActionResponseDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.ActualMeterReadsDataDto;

public interface GetActualMeterReadsBundleCommandExecutor extends CommandExecutor<ActualMeterReadsDataDto, ActionResponseDto> {

}
