/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate.analogue;

import uk.co.qmunity.lib.helper.MathHelper;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentSolarPanel;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateLightCell extends GateSimpleAnalogue {

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
    }

    @Override
    public void initComponents() {

        addComponent(new GateComponentSolarPanel(this, 0xd6ab17));

        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(front()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "lightCell";
    }

    @Override
    public void tick() {

        // if (getWorld().getWorldTime() % 5 == 0)
        front().setOutput((byte) MathHelper.map(getWorld().getBlockLightValue(getPos().getX(), getPos().getY(), getPos().getZ()), 0, 15, 0, 255));
    }

    @Override
    public void doLogic() {

    }

}
