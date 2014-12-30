/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.tube;

import net.minecraft.util.IIcon;

import com.bluepowermod.client.render.IconSupplier;

/**
 * @author MineMaarten
 */
public class RestrictionTube extends PneumaticTube {
    
    @Override
    public String getType() {
    
        return "restrictionTube";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "restrictionTube";
    }
    
    @Override
    public int getWeigth() {
    
        return 5000;
    }
    
    @Override
    protected IIcon getSideIcon() {
    
        return IconSupplier.restrictionTubeSide;
    }
    
    @Override
    protected IIcon getNodeIcon() {
    
        return IconSupplier.restrictionTubeNode;
    }
}
