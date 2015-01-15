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

package com.bluepowermod.part.gate;

import java.util.Arrays;
import java.util.List;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.gui.gate.GuiGateSingleTime;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentTorch;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Quetzi on 04/11/14.
 */
public class GateSequencer extends GateBase implements IGuiButtonSensitive {

    private final boolean[] power = new boolean[4];
    private int start = -1;
    private int time = 160;
    private int ticks = 0;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable().setOutputOnly();
        back().enable().setOutputOnly();
        right().enable().setOutputOnly();
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t1 = new GateComponentTorch(this, 0x23cecc, 3 / 16D, true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x0000FF, 3 / 16D, true);
        addComponent(t2);
        GateComponentTorch t3 = new GateComponentTorch(this, 0x3e94dc, 3 / 16D, true);
        t3.setState(true);
        addComponent(t3);
        GateComponentTorch t4 = new GateComponentTorch(this, 0x6F00B5, 3 / 16D, true);
        addComponent(t4);

        GateComponentPointer p = new GateComponentPointer(this, 0xFFFF00, 7 / 16D, true);
        p.setState(false);
        addComponent(p);

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "sequencer";
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // public void renderTop(float frame) {
    //
    // RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, 5D / 16D, 9D / 16D, power[1]);
    // RenderHelper.renderDigitalRedstoneTorch(-5D / 16D, 1D / 8D, 0, 9D / 16D, power[2]);
    // RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, -5D / 16D, 9D / 16D, power[3]);
    // RenderHelper.renderDigitalRedstoneTorch(5D / 16D, 1D / 8D, 0, 9D / 16D, power[0]);
    //
    // RenderHelper.renderDigitalRedstoneTorch(0, 1D / 8D, 0, 15D / 16D, true);
    //
    // double t = 0;
    // if ((getParent() == null || (getParent() != null && !getParent().isSimulated())))
    // t = -(double) (ticks - start + frame) / time;
    // RenderHelper.renderPointer(0, 7D / 16D, 0 / 16D, 0.5 + t);
    // }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (!getWorld().isRemote && ticks % 400 == 0)
            sendUpdatePacket();// Prevent slow desyncing of the timer.

        Arrays.fill(power, false);

        if (start >= 0) {
            if (ticks >= start + (time / 4D)) {
                start = ticks;
            }
        } else {
            start = ticks;
        }

        double t = (ticks - start) / (time / 4D);

        if (t >= 2D / 8D && t < 4D / 8D) {
            power[2] = true;
            if (right().getOutput() == 0)
                playTickSound();
        } else if (t >= 4D / 8D && t < 6D / 8D) {
            power[3] = true;
            if (back().getOutput() == 0)
                playTickSound();
        } else if (t >= 6D / 8D && t < 8D / 8D) {
            power[0] = true;
            if (left().getOutput() == 0)
                playTickSound();
        } else if (t >= 0D / 8D && t < 2D / 8D) {
            power[1] = true;
            if (front().getOutput() == 0)
                playTickSound();
        }

        left().setOutput(power[0] ? 15 : 0);
        front().setOutput(power[1] ? 15 : 0);
        right().setOutput(power[2] ? 15 : 0);
        back().setOutput(power[3] ? 15 : 0);

        ticks++;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        time = value * 4;
        sendUpdatePacket();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setInteger("start", start);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        start = tag.getInteger("start");
        ticks = tag.getInteger("ticks");
        time = tag.getInteger("time");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player) {

        return new GuiGateSingleTime(this) {

            @Override
            protected int getCurrentIntervalTicks() {

                return time / 4;
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

        String t = "";

        int time = this.time / 4 * 50;
        if (time >= 1000) {
            t = time / 1000 + "." + time % 1000 + "s";
        } else {
            t = time + "ms";
        }

        info.add(I18n.format("gui.timerInterval") + ": " + SpecialChars.WHITE + t);
    }
}
