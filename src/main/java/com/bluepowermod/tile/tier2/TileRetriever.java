/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import mcmultipart.api.multipart.MultipartHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.tile.IFuzzyRetrieving;
import com.bluepowermod.tile.tier1.TileFilter;

/**
 * @author MineMaarten
 */
public class TileRetriever extends TileFilter implements IFuzzyRetrieving {

    public int slotIndex;
    public int mode;

    @Override
    protected void pullItem() {

        if (isBufferEmpty()) {
            PneumaticTube tube = MultipartHelper.getPart(world, pos.offset(getFacingDirection()), PneumaticTube.class);
            if (tube != null) {
                boolean everythingNull = true;
                for (int i = 0; i < inventory.length; i++) {
                    if (mode == 1 || slotIndex == i) {
                        ItemStack stack = inventory[i];
                        if (stack != null) {
                            if (tube.getLogic().retrieveStack(this, getFacingDirection(), stack)) {
                                if (mode == 0) {
                                    if (++slotIndex >= inventory.length)
                                        slotIndex = 0;
                                    while (slotIndex != i) {
                                        if (inventory[slotIndex] != null)
                                            break;
                                        if (++slotIndex >= inventory.length)
                                            slotIndex = 0;
                                    }
                                }
                                return;
                            }
                            everythingNull = false;
                        }
                    }
                }
                if (everythingNull) {
                    tube.getLogic().retrieveStack(this, getFacingDirection(), null);
                    slotIndex = 0;
                }
            } else {
                super.pullItem();
            }
        }
    }

    @Override
    public String getName() {

        return BPBlocks.retriever.getUnlocalizedName();
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 2) {
            mode = value;
        } else {
            super.onButtonPress(player, messageId, value);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("slotIndex", (byte) slotIndex);
        tag.setByte("mode", (byte) mode);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        slotIndex = tag.getByte("slotIndex");
        mode = tag.getByte("mode");
    }

    @Override
    public int getFuzzySetting() {
        return fuzzySetting;
    }
}
