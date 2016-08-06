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
import java.util.List;

import com.bluepowermod.init.Config;
import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.gui.gate.GuiGateSingleCounter;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTimer extends GateSimpleDigital implements IGuiButtonSensitive {

    private int time = 40;
    private long start = -1;

    private GateComponentPointer p;
    private GateComponentTorch t;
    private GateComponentWire w;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initComponents() {

        addComponent((p = new GateComponentPointer(this, 0x0000FF, 7 / 16D, true)).setShouldSync(false).setState(true));
        addComponent(t = new GateComponentTorch(this, 0x6F00B5, 3 / 16D, true));

        addComponent(w = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
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

        sendUpdatePacket();
    }

    @Override
    public void tick() {

        if ((back().getInput() && !front().getOutput()) || (left().getInput() && !left().getOutput())
                || (right().getInput() && !right().getOutput())) {
            start = -1;

            p.setAngle(0);
            p.setIncrement(0);

            return;
        }

        if (!getWorld().isRemote) {
            front().setOutput(false);
            left().setOutput(false);
            right().setOutput(false);
            t.setState(false);
            w.setPower((byte) 255);

            left().notifyUpdateIfNeeded();
            right().notifyUpdateIfNeeded();
        }

        long curTime = getWorld().getTotalWorldTime();

        if (start != -1 && curTime != start && (curTime - start) >= time) {
            while ((curTime - start) >= time)
                start += time;

            playTickSound();
            if (!getWorld().isRemote) {
                front().setOutput(true);
                left().setOutput(true);
                right().setOutput(true);
                t.setState(true);
                w.setPower((byte) 0);

                left().notifyUpdateIfNeeded();
                right().notifyUpdateIfNeeded();
            }
        }

        if (start == -1)
            start = curTime;

        p.setAngle((curTime - start) / (double) time);
        p.setIncrement(1 / (double) time);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setLong("start", start);
        tag.setInteger("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        start = tag.getLong("start");
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

        if (value < Config.minTimerTicks) {
            value = Config.minTimerTicks;
        }

        time = value;
        start = 0;
        sendUpdatePacket();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateSingleCounter(this) {

            @Override
            protected int getCurrentAmount() {

                return time;
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

        int time = this.time * 50;
        if (time >= 1000) {
            String ms = "" + time % 1000;
            if (ms.length() > 1)
                while (ms.length() < 3)
                    ms = "0" + ms;
            t = time / 1000 + "." + ms + "s";
        } else {
            t = time + "ms";
        }

        info.add(I18n.format("gui.bluepower:timer.interval") + ": " + SpecialChars.WHITE + t);
    }
}
