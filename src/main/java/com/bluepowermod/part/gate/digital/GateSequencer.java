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
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.gui.gate.GuiGateSingleCounter;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentTorch;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateSequencer extends GateSimpleDigital implements IGuiButtonSensitive {

    private long start = -1;
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

        addComponent((p = new GateComponentPointer(this, 0xFFFF00, 7 / 16D, true)).setShouldSync(false).setState(true));

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

        long curTime = getWorld().getTotalWorldTime();

        if (start != -1 && curTime != start && (curTime - start) >= time)
            while ((curTime - start) >= time)
                start += time;

        if (start != -1 && (curTime - start) % (time / 4) == 0) {
            playTickSound();
            if (!getWorld().isRemote) {
                front().setOutput(false);
                back().setOutput(false);
                left().setOutput(false);
                right().setOutput(false);

                t1.setState(false);
                t2.setState(false);
                t3.setState(false);
                t4.setState(false);

                int i = (int) ((curTime - start) / (time / 4));
                if (i == 0) {
                    t3.setState(true);
                    front().setOutput(true);
                } else if (i == 1) {
                    t4.setState(true);
                    right().setOutput(true);
                } else if (i == 2) {
                    t1.setState(true);
                    back().setOutput(true);
                } else if (i == 3) {
                    t2.setState(true);
                    left().setOutput(true);
                }
            }
        }

        if (start == -1)
            start = curTime;

        p.setAngle((curTime - start) / (double) time);
        p.setIncrement(1 / (double) time);

        // if (getWorld().isRemote)
        // return;
        //
        // Arrays.fill(power, false);
        //
        // if (start >= 0) {
        // if (ticks >= start + (time / 4D)) {
        // start = ticks;
        // }
        // } else {
        // start = ticks;
        // }
        //
        // double t = (ticks - start) / (time / 4D);
        //
        // if (t >= 2D / 8D && t < 4D / 8D) {
        // power[2] = true;
        // if (!right().getOutput()) {
        // playTickSound();
        //
        // p.setAngle(p.getAngle());
        // p.setIncrement(4 / (double) time);
        // }
        // } else if (t >= 4D / 8D && t < 6D / 8D) {
        // power[3] = true;
        // if (!back().getOutput()) {
        // playTickSound();
        //
        // p.setAngle(p.getAngle());
        // p.setIncrement(4 / (double) time);
        // }
        // } else if (t >= 6D / 8D && t < 8D / 8D) {
        // power[0] = true;
        // if (!left().getOutput()) {
        // playTickSound();
        //
        // p.setAngle(p.getAngle());
        // p.setIncrement(4 / (double) time);
        // }
        // } else if (t >= 0D / 8D && t < 2D / 8D) {
        // power[1] = true;
        // if (!front().getOutput()) {
        // playTickSound();
        //
        // p.setAngle(p.getAngle());
        // p.setIncrement(4 / (double) time);
        // }
        // }
        //
        // left().setOutput(power[0]);
        // front().setOutput(power[1]);
        // right().setOutput(power[2]);
        // back().setOutput(power[3]);
        // t2.setState(power[0]);
        // t3.setState(power[1]);
        // t4.setState(power[2]);
        // t1.setState(power[3]);
        //
        // ticks++;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        time = value * 4;
        sendUpdatePacket();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setLong("start", start);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        start = tag.getLong("start");
        ticks = tag.getInteger("ticks");
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
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player) {

        return new GuiGateSingleCounter(this) {

            @Override
            protected int getCurrentAmount() {

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

        info.add(I18n.format("gui.bluepower:timer.interval") + ": " + SpecialChars.WHITE + t);
    }
}
