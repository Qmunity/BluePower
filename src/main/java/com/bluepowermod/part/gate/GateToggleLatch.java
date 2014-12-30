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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;

import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.init.BPItems;

public class GateToggleLatch extends GateBase {

    private boolean power = false;
    private boolean state = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        right().enable();
        back().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "toggle";
    }

    @Override
    protected void renderTop(float frame) {

        renderTop("centerleft", power);
        renderTop("left", power);
        renderTop("centerright", power);
        renderTop("right", power);
        RenderHelper.renderDigitalRedstoneTorch(2.5D / 8D, 1D / 8D, -2.5D / 8D, 9D / 16D, !state);
        RenderHelper.renderDigitalRedstoneTorch(2.5D / 8D, 1D / 8D, 2.5D / 8D, 9D / 16D, state);

        // RenderHelper.renderLever(this, 9 / 16D, 1 / 8D, 4 / 16D, !state);
    }

    @Override
    public void doLogic() {

        if ((power != right().getInput() > 0 || left().getInput() > 0) && !power) {
            state = !state;
            playTickSound();
        }
        power = right().getInput() > 0 || left().getInput() > 0;

        front().setOutput(!state ? 0 : 15);
        back().setOutput(state ? 0 : 15);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setBoolean("state", state);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        state = tag.getBoolean("state");
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (item == null || item.getItem() != BPItems.screwdriver) {
            state = !state;
            playTickSound();
            doLogic();
            return true;
        } else {
            return super.onActivated(player, hit, item);
        }
    }

}
