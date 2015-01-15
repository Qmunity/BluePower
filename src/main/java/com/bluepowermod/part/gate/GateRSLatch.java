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

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.wire.redstone.RedwireType;
import com.bluepowermod.util.Layout;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateRSLatch extends GateBase {

    private int mode;

    @Override
    public void initializeConnections() {

        front().enable().setOutput(15, false).setOutputOnly();
        front().setInput(0);

        left().enable().setInput(0);
        left().setOutput(0, false);
        if (mode == 0 || mode == 1)
            left().setOutput(15, false);

        right().enable().setInput(0);

        back().enable().setOutputOnly();
        back().setInput(0);
    }

    @Override
    public void initializeComponents() {

        GateComponentTorch t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true);
        t1.setState(true);
        addComponent(t1);
        GateComponentTorch t2 = new GateComponentTorch(this, 0x3E94DC, 4 / 16D, true);
        t2.setState(false);
        addComponent(t2);

        addComponent(new GateComponentWire(this, 0x00FF00, RedwireType.BLUESTONE).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public String getId() {

        return "rs";
    }

    @Override
    public void doLogic() {

        if ((left().getOutput() > 0 || left().getInput() > 0) && (right().getOutput() > 0 || right().getInput() > 0)) {
            // left().setBidirectional();
            left().setOutput(0);
            // right().setBidirectional();
            right().setOutput(0);
            front().setOutput(0);
            back().setOutput(0);
        } else {
            boolean mirrored = mode % 2 == 0;
            if (mirrored ? left().getInput() > 0 || left().getOutput() > 0 : right().getInput() > 0 || right().getOutput() > 0) {
                front().setOutput(15);
                back().setOutput(0);
                if (mode < 2) {
                    if (mirrored) {
                        // left().setOutputOnly();
                        left().setOutput(15);
                        right().setOutput(0);
                    } else {
                        // right().setOutputOnly();
                        right().setOutput(15);
                        left().setOutput(0);
                    }
                }
            }
            if (mirrored ? right().getInput() > 0 || right().getOutput() > 0 : left().getInput() > 0 || left().getOutput() > 0) {
                front().setOutput(0);
                back().setOutput(15);
                if (mode < 2) {
                    if (mirrored) {
                        // right().setOutputOnly();
                        right().setOutput(15);
                        left().setOutput(0);
                    } else {
                        // left().setOutputOnly();
                        left().setOutput(15);
                        right().setOutput(0);
                    }
                }
            }
        }

    }

    @Override
    protected boolean changeMode() {

        if (++mode > 3)
            mode = 0;

        getComponents().clear();
        initializeComponents();

        initializeConnections();
        doLogic();

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setByte("mode", (byte) mode);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        mode = tag.getByte("mode");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

        info.add(I18n.format("gui.mode") + ": " + I18n.format("bluepower.waila.rsLatch." + (mode < 2 ? "feedback" : "noFeedback")));
    }

    @Override
    public Layout getLayout() {

        Layout layout = super.getLayout();
        if (layout == null)
            return null;
        return layout.getSubLayout(mode);
    }
}
