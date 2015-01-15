/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentSiliconChip;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;

/**
 * @author MineMaarten
 */
public class GateSynchronizer extends GateBase {

    private boolean rightTriggered, leftTriggered, oldLeftState, oldRightState;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable();
    }

    @Override
    public void initializeComponents() {

        addComponent(new GateComponentTorch(this, 0xffb400, 5 / 16D, true));

        addComponent(new GateComponentWire(this, 0x208800, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0x3faa62, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xa50d7f, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentSiliconChip(this, 0x0000FF));
        addComponent(new GateComponentSiliconChip(this, 0x00ccff));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "synchronizer";
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // public void renderTop(float frame) {
    //
    // RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, 5 / 16D, 10D / 16D, front().getInput() > 0);
    // renderTop("front", front());
    // renderTop("right", right());
    // renderTop("back", back());
    // renderTop("left", left());
    // renderTop("frontleft", !leftTriggered);
    // renderTop("frontright", !rightTriggered);
    // RenderHelper.renderRandomizerButton(3 / 16D, 0, -4 / 16D, leftTriggered);
    // RenderHelper.renderRandomizerButton(-3 / 16D, 0, -4 / 16D, rightTriggered);
    // }

    @Override
    public void doLogic() {

        if (!oldLeftState && left().getInput() > 0) {
            leftTriggered = true;
        }
        if (!oldRightState && right().getInput() > 0) {
            rightTriggered = true;
        }
        if (back().getInput() > 0) {
            leftTriggered = false;
            rightTriggered = false;
        }

        if (leftTriggered && rightTriggered) {
            front().setOutput(15);
            leftTriggered = false;
            rightTriggered = false;
        }

        oldLeftState = left().getInput() > 0;
        oldRightState = right().getInput() > 0;

    }

    @Override
    public void tick() {

        front().setOutput(0);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setBoolean("leftTriggered", leftTriggered);
        tag.setBoolean("rightTriggered", rightTriggered);
        tag.setBoolean("oldLeftState", oldLeftState);
        tag.setBoolean("oldRightState", oldRightState);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        leftTriggered = tag.getBoolean("leftTriggered");
        rightTriggered = tag.getBoolean("rightTriggered");
        oldLeftState = tag.getBoolean("oldLeftState");
        oldRightState = tag.getBoolean("oldRightState");
    }

}
