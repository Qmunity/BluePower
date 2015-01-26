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

package com.bluepowermod.part.gate.digital;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentLever;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateToggleLatch extends GateSimpleDigital {

    private boolean power = false;
    private boolean state = true;

    private GateComponentTorch t1, t2;
    private GateComponentLever l;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        right().enable();
        back().enable().setOutputOnly();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true));
        addComponent(t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true).setState(true));

        addComponent(l = new GateComponentLever(this, 0x00FF00).setState(true));

        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "toggle";
    }

    @Override
    public void doLogic() {

        if ((power != right().getInput() || left().getInput()) && !power) {
            state = !state;
            playTickSound();
        }
        power = right().getInput() || left().getInput();

        front().setOutput(state);
        back().setOutput(!state);
        t1.setState(!state);
        t2.setState(state);
        l.setState(state);
    }

    @Override
    public void tick() {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("state", state);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        state = tag.getBoolean("state");
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (super.onActivated(player, hit, item))
            return true;

        if (!getWorld().isRemote) {
            state = !state;
            doLogic();
        }
        playTickSound();
        return true;
    }

}
