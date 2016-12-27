/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.analogue;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IAnalogueComparatorReadout;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentQuartzResonator;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;

public class GateComparator extends GateSimpleAnalogue {

    private GateComponentTorch t1, t2, t3;
    private GateComponentWire w;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void onNeighborTileChange() {

        if (!getWorld().isRemote)
            onUpdate();
    }

    @Override
    public void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, false).setState(true));
        addComponent(t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, false).setState(true));
        addComponent(t3 = new GateComponentTorch(this, 0x3e94dc, 5 / 16D, false).setState(false));

        addComponent(w = new GateComponentWire(this, 0x18FF00, RedwireType.RED_ALLOY).setPower((byte) 255));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.RED_ALLOY).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY).bind(left()));

        addComponent(new GateComponentQuartzResonator(this, 0xd6ab17));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getGateType() {

        return "comparator";
    }

    @Override
    public void doLogic() {

        byte power = back().getInput();

        EnumFacing d = back().getDirection().toEnumFacing(getFace(), getRotation());
        Vec3i a = new Vec3i(getX(), getY(), getZ(), getWorld()).add(d);
        Block ba = a.getBlock(false);
        if (ba.hasComparatorInputOverride())
            power = (byte) MathHelper.map(
                    ba.getComparatorInputOverride(getWorld(), a.getX(), a.getY(), a.getZ(), d.getOpposite().ordinal()), 0, 15, 0, 255);
        if (ba instanceof IAnalogueComparatorReadout && ((IAnalogueComparatorReadout) ba).hasAnalogueComparatorInputOverride())
            power = ((IAnalogueComparatorReadout) ba).getAnalogueComparatorInputOverride(getWorld(), a.getX(), a.getY(), a.getZ(), d
                    .getOpposite().ordinal());
        if (ba.isOpaqueCube()) {
            Vec3i b = a.getRelative(d);
            Block bb = b.getBlock(false);
            if (bb.hasComparatorInputOverride())
                power = (byte) MathHelper.map(
                        bb.getComparatorInputOverride(getWorld(), b.getX(), b.getY(), b.getZ(), d.getOpposite().ordinal()), 0, 15, 0, 255);
            if (bb instanceof IAnalogueComparatorReadout && ((IAnalogueComparatorReadout) bb).hasAnalogueComparatorInputOverride())
                power = ((IAnalogueComparatorReadout) bb).getAnalogueComparatorInputOverride(getWorld(), b.getX(), b.getY(), b.getZ(), d
                        .getOpposite().ordinal());
        }

        t1.setState(left().getInput() == 0);
        t2.setState(right().getInput() == 0);

        w.setPower((byte) (255 - (power & 0xFF)));
        t3.setState(power != 0);
        front().setOutput((byte) Math.max((power & 0xFF) - Math.max(left().getInput() & 0xFF, right().getInput() & 0xFF), 0));
    }

    @Override
    public void tick() {

    }
}
