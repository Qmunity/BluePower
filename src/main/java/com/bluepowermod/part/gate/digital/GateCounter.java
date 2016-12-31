/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate.digital;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.gui.gate.GuiGateCounter;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentBorderDark;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.util.Color;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GateCounter extends GateSimpleDigital implements IGuiButtonSensitive {

    private int count = 0, max = 10, increment = 1, decrement = 1;

    private boolean wasOnLeft = false, wasOnRight = false;

    private GateComponentTorch t1, t2;
    private GateComponentPointer p;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable().setOutputOnly();
        right().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true).setState(false));
        addComponent(t2 = new GateComponentTorch(this, 0x00c0ff, 4 / 16D, true).setState(true));
        addComponent((p = new GateComponentPointer(this, 0xb220d1, 7 / 16D, true).setAngle(-0.5 - 0.1075)).setState(true));

        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
        addComponent(new GateComponentBorderDark(this, 0x4d4d4d));
    }

    @Override
    public String getGateType() {

        return "counter";
    }

    @Override
    public void doLogic() {

        if (left().getInput() && !wasOnLeft) {
            wasOnLeft = true;
            count -= decrement;
            playTickSound();
        }
        if (!left().getInput())
            wasOnLeft = false;

        if (right().getInput() && !wasOnRight) {
            wasOnRight = true;
            count += increment;
            playTickSound();
        }
        if (!right().getInput())
            wasOnRight = false;

        count = Math.max(Math.min(count, max), 0);
        increment = Math.max(Math.min(increment, max), 0);
        decrement = Math.max(Math.min(decrement, max), 0);

        double min = 0.1075;
        double max = 0.5 - 0.1075;
        double tot = max - min;

        front().setOutput(count == this.max);
        back().setOutput(count == 0);
        t1.setState(count == this.max);
        t2.setState(count == 0);

        p.setAngle(max - (tot * (count / (double) this.max)));
    }

    @Override
    public void tick() {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("count", count);
        tag.setInteger("decrement", decrement);
        tag.setInteger("increment", increment);
        tag.setInteger("max", max);
        tag.setBoolean("wasOnLeft", wasOnLeft);
        tag.setBoolean("wasOnRight", wasOnRight);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        count = tag.getInteger("count");
        decrement = tag.getInteger("decrement");
        increment = tag.getInteger("increment");
        max = tag.getInteger("max");
        wasOnLeft = tag.getBoolean("wasOnLeft");
        wasOnRight = tag.getBoolean("wasOnRight");
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(count);
        buffer.writeInt(decrement);
        buffer.writeInt(increment);
        buffer.writeInt(max);
        buffer.writeBoolean(wasOnLeft);
        buffer.writeBoolean(wasOnRight);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        count = buffer.readInt();
        decrement = buffer.readInt();
        increment = buffer.readInt();
        max = buffer.readInt();
        wasOnLeft = buffer.readBoolean();
        wasOnRight = buffer.readBoolean();
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        System.out.println("Hey!");

        switch (messageId) {
        case 0:
            max = value;
            break;
        case 1:
            increment = value;
            break;
        case 2:
            decrement = value;
            break;
        }
        count = Math.max(Math.min(count, max), 0);
        increment = Math.max(Math.min(increment, max), 0);
        decrement = Math.max(Math.min(decrement, max), 0);
        sendUpdatePacket();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateCounter(this) {

            @Override
            protected int getCurrentMax() {

                return max;
            }

            @Override
            protected int getCurrentIncrement() {

                return increment;
            }

            @Override
            protected int getCurrentDecrement() {

                return decrement;
            }
        };
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

        info.add(I18n.format("gui.bluepower:counter.max") + ": " + Color.YELLOW + max);
        info.add(I18n.format("gui.bluepower:counter.count") + ": " + Color.YELLOW + count);
        info.add(I18n.format("gui.bluepower:counter.increment") + ": " + Color.WHITE + increment);
        info.add(I18n.format("gui.bluepower:counter.decrement") + ": " + Color.WHITE + decrement);
    }
}
