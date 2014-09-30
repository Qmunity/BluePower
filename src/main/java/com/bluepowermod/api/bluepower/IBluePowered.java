package com.bluepowermod.api.bluepower;

/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Interface implemented by Blulectric machines.
 * @author MineMaarten & Koen Beckers (K4Unl)
 *
 */
public interface IBluePowered {
    
    /**
     * @author MineMaarten
     * Called from the client only, when true it'll render the 'powered' icon, if false, it'll render the 'not powered' icon.
     * @return
     */
    public boolean isPowered();


    /**
     * @author Koen Beckers (K4Unl)
     * Gets the tier of the machine.
     * @return
     */
    public BluePowerTier getTier();


    /**
     * @author Koen Beckers (K4Unl)
     * Gets the powerHandler
     * @return
     */
    public IPowerBase getHandler();


    /**
     * @author Koen Beckers (K4Unl)
     * Whether or not this machine can connect power to this direction
     * @param dir
     * @return
     */
    boolean canConnectTo(ForgeDirection dir);
}
