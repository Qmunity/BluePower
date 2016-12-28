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

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.raytrace.QRayTraceResult;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.util.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateRepeater extends GateSimpleDigital {

    private boolean power = false;
    private boolean currentUpdate = false;

    private int ticksRemaining = 0;
    private int location = 0;
    private int[] ticks = { 1, 2, 3, 4, 8, 16, 32, 64, 128, 256, 1024 };

    private GateComponentTorch t1, t2;
    private GateComponentWire w1, w2;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        back().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 5 / 16D, true));
        addComponent(t2 = new GateComponentTorch(this, 11 / 16D, 12 / 16D, 4 / 16D, true).setState(true));

        addComponent(w1 = new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(w2 = new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "repeater";
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        boolean in = back().getInput();

        if (in != currentUpdate) {
            if (in || (!in && ticksRemaining == 0)) {
                ticksRemaining = ticks[location];
                currentUpdate = in;
            }
        }

        if (ticksRemaining > 0)
            ticksRemaining--;

        if (ticksRemaining == 0)
            power = currentUpdate;

        front().setOutput(power);
        t1.setState(power);
        w1.setPower(power ? 0 : (byte) 255);
        w2.setPower((back().getInput() || (ticksRemaining > 0 && currentUpdate)) ? (byte) 255 : 0);
        t2.setState(!(back().getInput() || (ticksRemaining > 0 && currentUpdate)));
    }

    @Override
    public boolean changeMode() {

        location = (location + 1) % ticks.length;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

        info.add(I18n.format("waila.bluepower:repeater.delay") + ": " + Color.WHITE + ticks[location] + " "
                + I18n.format("waila.bluepower:repeater.ticks"));
    }

    @Override
    public boolean onActivated(EntityPlayer player, QRayTraceResult hit, ItemStack item) {

        if (super.onActivated(player, hit, item))
            return true;

        location = (location + 1) % ticks.length;
        sendUpdatePacket();
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("power", power);
        tag.setBoolean("currentUpdate", currentUpdate);
        tag.setInteger("ticksRemaining", ticksRemaining);
        tag.setInteger("location", location);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        power = tag.getBoolean("power");
        currentUpdate = tag.getBoolean("currentUpdate");
        ticksRemaining = tag.getInteger("ticksRemaining");
        location = tag.getInteger("location");
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(location);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        location = buffer.readInt();

        t2.setZ((12 - location) / 16D);
    }
}
