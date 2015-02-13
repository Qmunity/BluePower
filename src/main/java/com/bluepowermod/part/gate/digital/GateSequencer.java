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

import java.math.BigDecimal;
import java.math.MathContext;
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

public class GateSequencer extends GateSimpleDigital implements IGuiButtonSensitive {

    private final boolean[] power = new boolean[4];
    private int start = -1;
    private int time = 160;
    private int ticks = 0;

    private GateComponentTorch t1, t2, t3, t4;
    private GateComponentPointer p;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable().setOutputOnly();
        back().enable().setOutputOnly();
        right().enable().setOutputOnly();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x23cecc, 3 / 16D, true));
        addComponent(t2 = new GateComponentTorch(this, 0x0000FF, 3 / 16D, true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 3 / 16D, true).setState(true));
        addComponent(t4 = new GateComponentTorch(this, 0x6F00B5, 3 / 16D, true));

        addComponent((p = new GateComponentPointer(this, 0xFFFF00, 7 / 16D, true)).setState(true));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "sequencer";
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (getWorld().isRemote) {
            // FIXME
            return;
        }

        if (ticks % 500 == 0) {
            p.setIncrement(0);
            p.setIncrement(4 / (double) time);
        }

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
            if (right().getOutput())
                playTickSound();
        } else if (t >= 4D / 8D && t < 6D / 8D) {
            power[3] = true;
            if (back().getOutput())
                playTickSound();
        } else if (t >= 6D / 8D && t < 8D / 8D) {
            power[0] = true;
            if (left().getOutput())
                playTickSound();
        } else if (t >= 0D / 8D && t < 2D / 8D) {
            power[1] = true;
            if (front().getOutput())
                playTickSound();
        }

        left().setOutput(power[0]);
        front().setOutput(power[1]);
        right().setOutput(power[2]);
        back().setOutput(power[3]);
        t2.setState(power[0]);
        t3.setState(power[1]);
        t4.setState(power[2]);
        t1.setState(power[3]);

        ticks++;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        time = value * 4;
        sendUpdatePacket();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("start", start);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
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
            t = time / 1000 + "." + new BigDecimal(time % 1000, new MathContext(3)).toString() + "s";
        } else {
            t = time + "ms";
        }

        info.add(I18n.format("gui.timerInterval") + ": " + SpecialChars.WHITE + t);
    }
}
