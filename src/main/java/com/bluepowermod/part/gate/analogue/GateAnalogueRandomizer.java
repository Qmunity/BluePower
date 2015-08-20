/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate.analogue;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentRedstoneSiliconChip;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateAnalogueRandomizer extends GateSimpleAnalogue {

    private static final Random random = new Random();

    private int ticks = 0;

    private final byte out[] = new byte[3];

    private GateComponentRedstoneSiliconChip c1, c2, c3;

    @Override
    protected void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable().setOutputOnly();
        right().enable().setOutputOnly();
        back().enable();
    }

    @Override
    public void initComponents() {

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.RED_ALLOY).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.RED_ALLOY).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY).bind(left()));

        addComponent(c2 = new GateComponentRedstoneSiliconChip(this, 0xd6ab17));
        addComponent(c1 = new GateComponentRedstoneSiliconChip(this, 0x0000FF));
        addComponent(c3 = new GateComponentRedstoneSiliconChip(this, 0x00ccff));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "analogue_randomizer";
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        if (back().getInput() != 0) {
            if (ticks % 5 == 0) {
                out[0] = (byte) random.nextInt(255);
                out[1] = (byte) random.nextInt(255);
                out[2] = (byte) random.nextInt(255);
                left().setOutput(out[0]);
                front().setOutput(out[1]);
                right().setOutput(out[2]);
                c1.setState(out[0] != 0);
                c2.setState(out[1] != 0);
                c3.setState(out[2] != 0);
            }
            ticks++;
        } else {
            ticks = 0;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("out_0", out[0]);
        tag.setByte("out_1", out[1]);
        tag.setByte("out_2", out[2]);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        out[0] = tag.getByte("out_0");
        out[1] = tag.getByte("out_1");
        out[2] = tag.getByte("out_2");
    }

    @Override
    public void doLogic() {

    }
}
