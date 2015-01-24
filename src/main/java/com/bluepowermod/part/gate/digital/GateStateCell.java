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

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.gui.gate.GuiGateSingleTime;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentButton;
import com.bluepowermod.part.gate.component.GateComponentPointer;
import com.bluepowermod.part.gate.component.GateComponentSiliconChip;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import com.bluepowermod.part.wire.redstone.RedwireType;
import com.bluepowermod.util.Layout;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateStateCell extends GateSimpleDigital implements IGuiButtonSensitive {

    private int time = 40;
    private int ticks = 0;
    private boolean triggered = false;
    private boolean locked = false;

    private boolean mirrored = false;

    private GateComponentPointer t1;
    private GateComponentTorch t2;
    private GateComponentWire w1, w2;
    private GateComponentButton chip;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable().setOutputOnly();
        back().enable();
        right().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentPointer(this, 0x0000FF, 7 / 16D, true).setAngle(0.75).setIncrement(0.025));
        addComponent(t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, true).setState(false));

        addComponent(w1 = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(w2 = new GateComponentWire(this, 0x61a11d, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(chip = new GateComponentSiliconChip(this, 0xd6ab17));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "state";
    }

    @Override
    public void doLogic() {

        boolean in = back().getInput();
        GateConnectionDigital out = null;

        if (mirrored) {
            locked = left().getInput();
            out = right();
        } else {
            locked = right().getInput();
            out = left();
        }

        locked |= in;
        triggered |= in;

        if (triggered && !in && !locked) {
            t1.setState(true);
            w2.setPower((byte) 0);
        }

        chip.setState(triggered);

        out.setOutput(triggered);
        if (triggered || locked) {
            if (locked)
                t1.setState(false);
            t1.setAngle(mirrored ? 0.25 : 0.75);
            t1.setIncrement((mirrored ? -1 : 1) * 0.1575 * (1 / (double) time));
        }
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        // Reset everything (pulse)
        front().setOutput(false);
        w1.setPower((byte) 255);
        t2.setState(false);

        GateConnectionDigital out = mirrored ? right() : left();

        if (triggered)
            ticks++;

        if (!triggered || locked)
            ticks = 0;

        if (ticks == time) {
            out.setOutput(false);
            front().setOutput(true);
            t1.setState(false);
            t1.setAngle(mirrored ? 0.25 : 0.75);
            w2.setPower((byte) 255);
            w1.setPower((byte) 0);
            t2.setState(true);
            chip.setState(false);
            triggered = false;
            ticks = 0;
        }
    }

    @Override
    public boolean changeMode() {

        mirrored = !mirrored;

        getComponents().clear();
        initConnections();
        initComponents();
        doLogic();

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
        tag.setBoolean("mirrored", mirrored);
        tag.setBoolean("triggered", triggered);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        ticks = tag.getInteger("ticks");
        time = tag.getInteger("time");
        mirrored = tag.getBoolean("mirrored");
        triggered = tag.getBoolean("triggered");
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(ticks);
        buffer.writeInt(time);
        buffer.writeBoolean(mirrored);
        buffer.writeBoolean(triggered);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        ticks = buffer.readInt();
        time = buffer.readInt();
        mirrored = buffer.readBoolean();
        triggered = buffer.readBoolean();
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        time = value;
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

        info.add(I18n.format("gui.timerInterval") + ": " + SpecialChars.WHITE + t);
    }

    @Override
    public Layout getLayout() {

        Layout layout = super.getLayout();
        if (layout == null)
            return null;
        return layout.getSubLayout(mirrored ? 1 : 0);
    }
}