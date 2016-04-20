package org.osgp.adapter.protocol.dlms.domain.commands.stub;

import org.openmuc.jdlms.ClientConnection;
import org.osgp.adapter.protocol.dlms.domain.commands.SetSpecialDaysBundleCommandExecutor;
import org.osgp.adapter.protocol.dlms.domain.entities.DlmsDevice;
import org.osgp.adapter.protocol.dlms.exceptions.ProtocolAdapterException;

import com.alliander.osgp.dto.valueobjects.smartmetering.ActionResponseDto;
import com.alliander.osgp.dto.valueobjects.smartmetering.SpecialDaysRequestDataDto;

public class SetSpecialDaysBundleCommandExecutorStub extends AbstractCommandExecutorStub implements SetSpecialDaysBundleCommandExecutor{

    @Override
    public ActionResponseDto execute(ClientConnection conn, DlmsDevice device, SpecialDaysRequestDataDto object)
            throws ProtocolAdapterException {
        return doExecute(conn, device, object);
    }

}
