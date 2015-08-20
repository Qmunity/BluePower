package com.bluepowermod.api.gate.ic;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacementFlat;

public interface IIntegratedCircuitPart extends IPart, IPartCustomPlacementFlat {

    public boolean canPlaceOnIntegratedCircuit();

}
