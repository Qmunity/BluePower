/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

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

public class GateTimer extends GateBase implements IGuiButtonSensitive {

    private int time = 40;
    private int curTime = 0;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable().setOutputOnly();
        back().enable();
        left().enable().setOutputOnly();
    }

    @Override
    public void initializeComponents() {

        GateComponentPointer t1 = new GateComponentPointer(this, 0x0000FF, 7 / 16D, true);
        t1.setState(true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x6F00B5, 3 / 16D, true);
        t2.setState(false);
        addComponent(t2);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "timer";
    }

    @Override
    public void doLogic() {

    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected void renderTop(float frame) {
    //
    // // renderTop("front", front());
    // renderTop("right", right());
    // renderTop("back", back());
    // renderTop("left", left());
    //
    // RenderHelper.renderDigitalRedstoneTorch(0, 0, 0, 17 / 16D, back().getInput() == 0);
    //
    // double t = 0;
    // if (back().getInput() == 0 && (getParent() == null || (getParent() != null && !getParent().isSimulated())))
    // t = -(curTime + frame) / (double) time;
    // RenderHelper.renderPointer(0, 7 / 16D, 0, 0.5 + t);
    // }

    @Override
    public void tick() {

        if (front().getOutput() > 0) {
            front().setOutput(0);
            left().setOutput(0);
            right().setOutput(0);
        } else if (back().getInput() == 0) {
            if (++curTime >= time) {
                front().setOutput(15);
                left().setOutput(15);
                right().setOutput(15);
                playTickSound();
                curTime = 0;
            }
        } else {
            curTime = 0;
        }

        // if (front().getOutput() > 0)
        // spawnBlueParticle(8 / 16D, 6 / 16D, 8 / 16D);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setInteger("curTime", curTime);
        tag.setInteger("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        curTime = tag.getInteger("curTime");
        time = tag.getInteger("time");
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
