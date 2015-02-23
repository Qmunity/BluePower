/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.digital;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentButton;
import com.bluepowermod.part.gate.component.GateComponentSiliconChip;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

/**
 * @author MineMaarten
 */
public class GateSynchronizer extends GateSimpleDigital {

    private boolean rightTriggered, leftTriggered, oldLeftState, oldRightState;
    private boolean turnOff = false;

    private GateComponentTorch t;
    private GateComponentWire w1, w2, w3;
    private GateComponentButton b1, b2;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t = new GateComponentTorch(this, 0xffb400, 5 / 16D, true));

        addComponent(w1 = new GateComponentWire(this, 0x208800, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(w2 = new GateComponentWire(this, 0x3faa62, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(w3 = new GateComponentWire(this, 0xa50d7f, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(b1 = new GateComponentSiliconChip(this, 0x0000FF));
        addComponent(b2 = new GateComponentSiliconChip(this, 0x00ccff));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "synchronizer";
    }

    @Override
    public void doLogic() {

        if (!oldLeftState && left().getInput()) {
            b1.setState(leftTriggered = true);
            w1.setPower((byte) 0);
        }
        if (!oldRightState && right().getInput()) {
            b2.setState(rightTriggered = true);
            w2.setPower((byte) 0);
        }

        if (back().getInput()) {
            b1.setState(leftTriggered = false);
            b2.setState(rightTriggered = false);
            w1.setPower((byte) 255);
            w2.setPower((byte) 255);
        }

        if (leftTriggered && rightTriggered) {
            front().setOutput(true);
            w3.setPower((byte) 255);
            b1.setState(leftTriggered = false);
            b2.setState(rightTriggered = false);
            t.setState(true);
        }

        oldLeftState = left().getInput();
        oldRightState = right().getInput();

    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        if (front().getOutput()) {
            if (turnOff) {
                front().setOutput(false);
                w3.setPower((byte) 0);
                w1.setPower((byte) 255);
                w2.setPower((byte) 255);
                t.setState(false);
            }
            turnOff = !turnOff;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("leftTriggered", leftTriggered);
        tag.setBoolean("rightTriggered", rightTriggered);
        tag.setBoolean("oldLeftState", oldLeftState);
        tag.setBoolean("oldRightState", oldRightState);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        leftTriggered = tag.getBoolean("leftTriggered");
        rightTriggered = tag.getBoolean("rightTriggered");
        oldLeftState = tag.getBoolean("oldLeftState");
        oldRightState = tag.getBoolean("oldRightState");
    }

}
