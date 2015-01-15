/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.analog;

import net.minecraft.block.Block;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentQuartzResonator;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;

public class GateComparator extends GateBase {

    private int power = 0;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        right().enable();
        back().enable();
        left().enable();
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, false);
        t1.setState(true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x6F00B5, 4 / 16D, false);
        t2.setState(false);
        addComponent(t2);
        GateComponentTorch t3 = new GateComponentTorch(this, 0x3e94dc, 5 / 16D, false);
        t3.setState(true);
        addComponent(t3);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.RED_ALLOY));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.RED_ALLOY).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.RED_ALLOY).bind(left()));

        addComponent(new GateComponentQuartzResonator(this, 0xd6ab17));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "comparator";
    }

    @Override
    public void doLogic() {

        power = back().getInput();

        ForgeDirection d = back().getDirection().toForgeDirection(getFace(), getRotation());
        Vec3i a = new Vec3i(getX(), getY(), getZ(), getWorld()).add(d);
        Block ba = a.getBlock(false);
        if (ba.hasComparatorInputOverride())
            power = ba.getComparatorInputOverride(getWorld(), a.getX(), a.getY(), a.getZ(), d.getOpposite().ordinal());
        if (ba.isOpaqueCube()) {
            Vec3i b = a.getRelative(d);
            Block bb = b.getBlock(false);
            if (bb.hasComparatorInputOverride())
                power = bb.getComparatorInputOverride(getWorld(), b.getX(), b.getY(), b.getZ(), d.getOpposite().ordinal());
        }

        front().setOutput(Math.max(power - Math.max(left().getInput(), right().getInput()), 0));
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected void renderTop(float frame) {
    //
    // renderTop("front", front().getOutput() == 0);
    // renderTop("right", right());
    // renderTop("back", back());
    // renderTop("left", left());
    //
    // RenderHelper.renderAnalogRedstoneTorch(0, 0, 5 / 16D, 11 / 16D, front().getOutput() > 0);
    //
    // RenderHelper.renderAnalogRedstoneTorch(-4 / 16D, 0, -3 / 16D, 13 / 16D, right().getInput() == 0);
    // RenderHelper.renderAnalogRedstoneTorch(4 / 16D, 0, -3 / 16D, 13 / 16D, left().getInput() == 0);
    //
    // RenderHelper.renderQuartzResonator(0, 0, -4 / 16D);
    // }

    @Override
    public void tick() {

    }
}
