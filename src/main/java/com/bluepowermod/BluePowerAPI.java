/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.api.BPApi.IBPApi;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.power.IBluePowered;
import com.bluepowermod.api.power.IPowerHandler;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.api.wire.redstone.IRedstoneApi;
import com.bluepowermod.power.PowerHandler;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.redstone.RedstoneApi;

public class BluePowerAPI implements IBPApi {

    // @Override
    // public IPneumaticTube getPneumaticTube(TileEntity te) {
    //
    // PneumaticTube tube = getMultipartCompat().getBPPart(te, PneumaticTube.class);
    // return tube != null ? tube.getLogic() : null;
    // }

    @Override
    public IAlloyFurnaceRegistry getAlloyFurnaceRegistry() {

        return AlloyFurnaceRegistry.getInstance();
    }

    @Override
    public IPowerHandler getNewPowerHandler(IBluePowered device, float maxAmps) {

        return new PowerHandler(device, maxAmps);
    }

    @Override
    public void loadSilkySettings(World world, int x, int y, int z, ItemStack stack) {

        TileEntity te = world.getTileEntity(x, y, z);
        Block b = world.getBlock(x, y, z);
        if (te == null)
            throw new IllegalStateException("This block doesn't have a tile entity?!");
        if (stack == null)
            throw new IllegalArgumentException("ItemStack is null!");
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("tileData")) {
                if (te instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) te).readSilkyData(world, x, y, z, tag.getCompoundTag("tileData"));
                } else if (b instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) b).readSilkyData(world, x, y, z, tag.getCompoundTag("tileData"));
                } else {
                    NBTTagCompound tileTag = tag.getCompoundTag("tileData");
                    tileTag.setInteger("x", x);
                    tileTag.setInteger("y", y);
                    tileTag.setInteger("z", z);
                    te.readFromNBT(tileTag);
                }
            }
        }
    }

    @Override
    public void loadSilkySettings(IPart part, ItemStack stack) {

        if (stack == null)
            throw new IllegalArgumentException("ItemStack is null!");
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("tileData")) {
                NBTTagCompound tileTag = tag.getCompoundTag("tileData");
                boolean err = false;
                if (part instanceof IAdvancedSilkyRemovable) {
                    try {
                        part.getWorld();
                        part.getX();
                        part.getY();
                        part.getZ();
                    } catch (Exception ex) {
                        err = true;
                    }

                    if (err) {
                        ((IAdvancedSilkyRemovable) part).readSilkyData(null, 0, 0, 0, tileTag);
                    } else {
                        ((IAdvancedSilkyRemovable) part).readSilkyData(part.getWorld(), part.getX(), part.getY(), part.getZ(), tileTag);
                    }
                } else {
                    part.readFromNBT(tileTag);
                }
            }
        }
    }

    @Override
    public IRedstoneApi getRedstoneApi() {

        return RedstoneApi.getInstance();
    }

}
