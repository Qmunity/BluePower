/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.digital;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.util.Color;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.util.Dir;

import java.util.List;

public class GateNor extends GateSimpleDigital {

    private ShiftingBuffer<Boolean> buf = new ShiftingBuffer<Boolean>(1, 3, false);

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

        t = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true);
        t.setState(true);
        addComponent(t);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "nor";
    }

    @Override
    public void doLogic() {

        buf.set(0, (back().isEnabled() && back().getInput()) && (left().isEnabled() && left().getInput())
                && (right().isEnabled() && right().getInput()));
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        buf.shift();

        boolean pow = buf.get(0);

        t.setState(!pow);
        front().setOutput(!pow);
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
