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

package com.bluepowermod.part.gate;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;
import com.bluepowermod.util.Layout;

/**
 * @author MineMaarten
 */
public class GateTransparentLatch extends GateBase {

    private boolean mirrored;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable().setOutputOnly();
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t1 = new GateComponentTorch(this, 0x215b8d, 4 / 16D, true);
        t1.setState(true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true);
        t2.setState(false);
        addComponent(t2);
        GateComponentTorch t3 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, true);
        t3.setState(true);
        addComponent(t3);
        GateComponentTorch t4 = new GateComponentTorch(this, 0x6F00B5, 5 / 16D, true);
        t4.setState(false);
        addComponent(t4);
        GateComponentTorch t5 = new GateComponentTorch(this, 0x7635c6, 4 / 16D, true);
        t5.setState(false);
        addComponent(t5);

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));
        addComponent(new GateComponentWire(this, 0xd2ae31, RedwireType.BLUESTONE));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "transparent";
    }

    @Override
    public void doLogic() {

        if (back().getInput() > 0) {
            front().setOutput(mirrored ? right().getInput() : left().getInput());
            if (mirrored) {
                left().setOutput(right().getInput());
            } else {
                right().setOutput(left().getInput());
            }
        }
    }

    @Override
    protected boolean changeMode() {

        mirrored = !mirrored;
        if (mirrored) {
            left().setOutputOnly();
            right().setBidirectional();
        } else {
            left().setBidirectional();
            right().setOutputOnly();
        }

        getComponents().clear();
        initializeComponents();

        sendUpdatePacket();
        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setBoolean("mirrored", mirrored);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        mirrored = tag.getBoolean("mirrored");
        if (mirrored) {
            left().setOutputOnly();
            right().setBidirectional();
        } else {
            left().setBidirectional();
            right().setOutputOnly();
        }
        if (getParent() != null && getWorld() != null && getWorld().isRemote)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public Layout getLayout() {

        Layout layout = super.getLayout();
        if (layout == null)
            return null;
        return layout.getSubLayout(mirrored ? 1 : 0);
    }

}
