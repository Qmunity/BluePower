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

public class GateAnd extends GateSimpleDigital {

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(5, 2, false);

    private GateComponentTorch t1, t2, t3, t4;
    private GateComponentWire wire;

    @Override
    protected String getGateType() {

        return "and";
    }

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x215b8d, 4 / 16D, true).setState(true));
        addComponent(t2 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true).setState(true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, true).setState(true));
        addComponent(t4 = new GateComponentTorch(this, 0x6F00B5, 5 / 16D, true).setState(false));

        addComponent(wire = new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).setPower((byte) 255));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public void doLogic() {

        buf.set(0, !left().isEnabled() || left().getInput());
        buf.set(1, !back().isEnabled() || back().getInput());
        buf.set(2, !right().isEnabled() || right().getInput());
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        buf.shift();

        boolean pow = buf.get(0) && buf.get(1) && buf.get(2);

        t1.setState(!buf.get(0));
        t2.setState(!buf.get(1));
        t3.setState(!buf.get(2));

        wire.setPower((byte) (!pow ? 255 : 0));

        buf.set(3, pow);
        t4.setState(buf.get(3));

        buf.set(4, buf.get(3));
        front().setOutput(buf.get(4));
    }

    @Override
    public boolean changeMode() {

        if (getWorld().isRemote)
            return true;

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

        info.add(Color.YELLOW + I18n.format("waila.bluepower:gate.connections") + ":");
        info.add("  "
                + Dir.LEFT.getLocalizedName()
                + ": "
                + (left().isEnabled() ? Color.GREEN + I18n.format("bluepower:misc.enabled") : Color.RED
                        + I18n.format("bluepower:misc.disabled")));
        info.add("  "
                + Dir.BACK.getLocalizedName()
                + ": "
                + (back().isEnabled() ? Color.GREEN + I18n.format("bluepower:misc.enabled") : Color.RED
                        + I18n.format("bluepower:misc.disabled")));
        info.add("  "
                + Dir.RIGHT.getLocalizedName()
                + ": "
                + (right().isEnabled() ? Color.GREEN + I18n.format("bluepower:misc.enabled") : Color.RED
                        + I18n.format("bluepower:misc.disabled")));
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
