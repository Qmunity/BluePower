/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.digital;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.gui.gate.GuiGateSingleTime;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTimer extends GateSimpleDigital implements IGuiButtonSensitive {

    private int time = 40;
    private int curTime = 0;

    private GateComponentPointer p;
    private GateComponentTorch t;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initComponents() {

        addComponent((p = new GateComponentPointer(this, 0x0000FF, 7 / 16D, true)).setState(true));
        addComponent(t = new GateComponentTorch(this, 0x6F00B5, 3 / 16D, true));

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "timer";
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (!getWorld().isRemote) {
            front().setOutput(false);
            left().setOutput(false);
            right().setOutput(false);
            t.setState(false);

            left().notifyUpdateIfNeeded();
            right().notifyUpdateIfNeeded();
        }

        if (!back().getInput() && !(!front().getOutput() && (left().getInput() || right().getInput()))) {
            p.setState(true);
            p.setIncrement(1 / (double) time);
            if (++curTime >= time) {
                if (!getWorld().isRemote) {
                    front().setOutput(true);
                    left().setOutput(true);
                    right().setOutput(true);
                    t.setState(true);
                }
                playTickSound();
                curTime = 0;
            }
        } else {
            curTime = 0;
            p.setState(false);
            p.setAngle(0);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("curTime", curTime);
        tag.setInteger("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        curTime = tag.getInteger("curTime");
        time = tag.getInteger("time");
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(time);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        time = buffer.readInt();
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        time = value;
        curTime = 0;
        sendUpdatePacket();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateSingleTime(this) {

            @Override
            protected int getCurrentIntervalTicks() {

                return time;
            }

        };
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }
}
