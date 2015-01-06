/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile;

/**
 * Interface implemented by Blulectric machines.
 * @author MineMaarten
 *
 */
public interface IBluePowered {
    
    /**
     * Called from the client only, when true it'll render the 'powered' icon, if false, it'll render the 'not powered' icon.
     * @return
     */
    public boolean isPowered();
}
