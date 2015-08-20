package com.bluepowermod.api.power;

/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.connect.ConnectionType;

/**
 * Interface implemented by Blulectric machines.
 *
 * @author MineMaarten & Koen Beckers (K4Unl)
 *
 */
public interface IPowered extends IWorldLocation {

    /**
     * @author Koen Beckers (K4Unl) Gets the powerHandler
     * @return
     */
    public IPowerBase getPowerHandler(ForgeDirection side);

    /**
     * Returns whether or not the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnectPower(ForgeDirection side, IPowered dev, ConnectionType type);

    /**
     * Returns whether or not the face on the specified side occupies the entire side of the block. This is used to determine how the connection logic
     * should work.
     */
    public boolean isNormalFace(ForgeDirection side);

}
