/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.items;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScrewdriver extends ItemBase {

    public ItemScrewdriver() {

        setUnlocalizedName(Refs.SCREWDRIVER_NAME);
        setCreativeTab(CustomTabs.tabBluePowerTools);
        setMaxDamage(250);
        setMaxStackSize(1);
        setTextureName(Refs.MODID + ":" + Refs.SCREWDRIVER_NAME);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        Block block = world.getBlock(x, y, z);

        if (player.isSneaking()) {
            ITilePartHolder itph = MultipartCompatibility.getPartHolder(world, new Vec3i(x, y, z));

            if (itph != null) {
                QMovingObjectPosition mop = itph.rayTrace(RayTracer.instance().getStartVector(player), RayTracer.instance().getEndVector(player));
                if (mop == null)
                    return false;
                IPart p = mop.getPart();
                if (p instanceof BPPartFaceRotate) {
                    ((BPPartFaceRotate) p).setRotation((((BPPartFaceRotate) p).getRotation() + 1) % 4);
                }
            }
        }

        if (block instanceof BlockContainerBase) {
            if (((BlockContainerBase) block).getGuiID() != GuiIDs.INVALID) {
                if (player.isSneaking()) {
                    block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
                    if (!player.capabilities.isCreativeMode) {
                        stack.setItemDamage(stack.getItemDamage() + 1);
                    }
                }
            } else {
                block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
                if (!player.capabilities.isCreativeMode) {
                    stack.setItemDamage(stack.getItemDamage() + 1);
                }
            }
        } else {
            block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
            if (!player.capabilities.isCreativeMode) {
                stack.setItemDamage(stack.getItemDamage() + 1);
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isFull3D() {

        return true;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {

        return EnumAction.block;
    }
}
