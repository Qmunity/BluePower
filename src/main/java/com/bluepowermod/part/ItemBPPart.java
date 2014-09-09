/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tileentities.BPTileMultipart;
import com.bluepowermod.util.Refs;

public class ItemBPPart extends Item implements IDatabaseSaveable {

    private final String partName;
    private boolean secondAttempt;

    public ItemBPPart(String name) {

        super();
        partName = name;
    }

    @Override
    public void registerIcons(IIconRegister register) {

    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "part." + Refs.MODID + ":" + partName;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float f, float f2, float f3) {

        boolean flag = true;

        if (flag) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeWood.soundName, Block.soundTypeWood.getVolume(),
                    Block.soundTypeWood.getPitch());

            if (!w.isRemote) {
                if (!player.isSneaking()) {
                    Vector3 v = new Vector3(x, y, z, w);
                    TileEntity te = v.getTileEntity();
                    BPTileMultipart tileMultipart = null;
                    if (te instanceof BPTileMultipart) {
                        tileMultipart = (BPTileMultipart) te;
                    } else {
                        if (v.getBlock(true) == null) {
                            w.setBlock(v.getBlockX(), v.getBlockY(), v.getBlockZ(), BPBlocks.multipart);
                            tileMultipart = (BPTileMultipart) v.getTileEntity();
                        }
                    }

                    if (tileMultipart != null) {
                        BPPart part = PartRegistry.getInstance().createPartFromItem(stack);
                        part.setWorld(w);
                        part.setX(v.getBlockX());
                        part.setY(v.getBlockY());
                        part.setZ(v.getBlockZ());
                        if (!isOccluded(part)
                                && part.canPlacePart(stack, player, v.getRelative(ForgeDirection.getOrientation(side).getOpposite()),
                                        player.rayTrace(player.capabilities.isCreativeMode ? 5 : 4.5, 0))) {
                            tileMultipart.addPart(part);
                            w.markBlockForUpdate(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                            return true;
                        }
                    }
                    if (!secondAttempt) {
                        v.add(ForgeDirection.getOrientation(side));
                        secondAttempt = true;
                        boolean used = onItemUse(stack, player, w, v.getBlockX(), v.getBlockY(), v.getBlockZ(), side, f, f2, f3);
                        secondAttempt = false;
                        return used;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean isOccluded(BPPart part) {

        for (AxisAlignedBB occBox : part.getOcclusionBoxes()) {
            if (part.checkOcclusion(occBox))
                return true;
        }
        return false;
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(PartRegistry.getInstance().getItemForPart(partName));
    }

    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        return PartRegistry.getInstance().createPartFromItem(stack).canGoInCopySlot(stack);
    }

    @Override
    public boolean canCopy(ItemStack templateStack, ItemStack outputStack) {

        if (templateStack.getTagCompound().getString("id").equals(outputStack.getTagCompound().getString("id"))) {
            return canGoInCopySlot(templateStack);
        } else {
            return false;
        }
    }

    @Override
    public List<ItemStack> getItemsOnStack(ItemStack stack) {

        return PartRegistry.getInstance().createPartFromItem(stack).getItemsOnStack(stack);
    }

}
