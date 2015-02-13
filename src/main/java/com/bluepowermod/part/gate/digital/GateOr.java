/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.digital;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.util.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateOr extends GateSimpleDigital {

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(1, 3, false);

    private GateComponentTorch t1, t2;
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

        t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true);
        t1.setState(true);
        addComponent(t1);
        t2 = new GateComponentTorch(this, 0x6F00B5, 5 / 16D, true);
        t2.setState(false);
        addComponent(t2);

        addComponent(w = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));

        w.setPower((byte) 255);
    }

    @Override
    public String getGateType() {

        return "or";
    }

    @Override
    public void doLogic() {

        buf.set(0, (left().isEnabled() && left().getInput()) || (back().isEnabled() && back().getInput())
                || (right().isEnabled() && right().getInput()));
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        buf.shift();

        boolean mid = !buf.get(0, 1);
        t1.setState(mid);
        w.setPower((byte) (mid ? 255 : 0));

        boolean out = buf.get(0);
        t2.setState(out);
        front().setOutput(out);
    }

    @Override
    public boolean changeMode() {

        if (left().isEnabled() && back().isEnabled() && right().isEnabled()) {
            right().disable();
        } else if (left().isEnabled() && back().isEnabled()) {
            back().disable();
            right().enable();
        } else if (left().isEnabled() && right().isEnabled()) {
            left().disable();
            back().enable();
        } else if (back().isEnabled() && right().isEnabled()) {
            left().enable();
            back().disable();
            right().disable();
        } else if (left().isEnabled()) {
            left().disable();
            back().enable();
        } else if (back().isEnabled()) {
            back().disable();
            right().enable();
        } else {// right enabled
            left().enable();
            back().enable();
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

        info.add(Color.YELLOW + I18n.format("gui.connections") + ":");
        info.add("  " + Dir.LEFT.getLocalizedName() + ": "
                + (left().isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED + I18n.format("random.disabled")));
        info.add("  " + Dir.BACK.getLocalizedName() + ": "
                + (back().isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED + I18n.format("random.disabled")));
        info.add("  " + Dir.RIGHT.getLocalizedName() + ": "
                + (right().isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED + I18n.format("random.disabled")));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        buf.writeToNBT(tag, "buffer");
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        buf.readFromNBT(tag, "buffer");
    }
}
