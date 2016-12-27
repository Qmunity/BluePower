/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod;

import mcmultipart.multipart.IMultipart;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.bluepowermod.api.BPApi.IBPApi;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.api.wire.redstone.IRedstoneApi;
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
    public void loadSilkySettings(World world, BlockPos pos, ItemStack stack) {
        TileEntity te = world.getTileEntity(pos);
        Block b = world.getBlockState(pos).getBlock();
        if (te == null)
            throw new IllegalStateException("This block doesn't have a tile entity?!");
        if (stack == null)
            throw new IllegalArgumentException("ItemStack is null!");
        if (stack.hasTagCompound()) {
            NBTTagCompound tag = stack.getTagCompound();
            if (tag.hasKey("tileData")) {
                if (te instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) te).readSilkyData(world, pos, tag.getCompoundTag("tileData"));
                } else if (b instanceof IAdvancedSilkyRemovable) {
                    ((IAdvancedSilkyRemovable) b).readSilkyData(world, pos, tag.getCompoundTag("tileData"));
                } else {
                    NBTTagCompound tileTag = tag.getCompoundTag("tileData");
                    tileTag.setInteger("x", pos.getX());
                    tileTag.setInteger("y", pos.getY());
                    tileTag.setInteger("z", pos.getZ());
                    te.readFromNBT(tileTag);
                }
            }
        }
    }


    @Override
    public void loadSilkySettings(IMultipart part, ItemStack stack) {

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
                        part.getPos();
                    } catch (Exception ex) {
                        err = true;
                    }

                    if (err) {
                        ((IAdvancedSilkyRemovable) part).readSilkyData(null, new BlockPos(0, 0, 0), tileTag);
                    } else {
                        ((IAdvancedSilkyRemovable) part).readSilkyData(part.getWorld(), part.getPos(), tileTag);
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
