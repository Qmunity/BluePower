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

package com.bluepowermod.item;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartInteractable;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemScrewdriver extends ItemBase implements IScrewdriver {

    public ItemScrewdriver() {

        setUnlocalizedName(Refs.SCREWDRIVER_NAME);
        setCreativeTab(BPCreativeTabs.tools);
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
                if (p instanceof IPartInteractable) {
                    if (((IPartInteractable) p).onActivated(player, mop, stack)) {
                        damage(stack, 1, player, false);
                        return true;
                    }
                }
            }
        }

        if (block instanceof BlockContainerBase) {
            if (((BlockContainerBase) block).getGuiID() != GuiIDs.INVALID) {
                if (player.isSneaking()) {
                    if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                        damage(stack, 1, player, false);
                        return true;
                    }
                }
            } else {
                if (!player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                    damage(stack, 1, player, false);
                    return true;
                }
            }
        } else {
            if (!player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                damage(stack, 1, player, false);
                return true;
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
    public boolean damage(ItemStack stack, int damage, EntityPlayer player, boolean simulated) {

        if (player != null && player.capabilities.isCreativeMode)
            return true;
        if ((stack.getItemDamage() % stack.getMaxDamage()) + damage > stack.getMaxDamage())
            return false;

        if (!simulated) {
            if (stack.attemptDamageItem(damage, new Random())) {
                if (player != null)
                    player.renderBrokenItemStack(stack);
                --stack.stackSize;

                if (player != null && player instanceof EntityPlayer)
                    player.addStat(StatList.objectBreakStats[Item.getIdFromItem(stack.getItem())], 1);

                if (stack.stackSize < 0)
                    stack.stackSize = 0;

                stack.setItemDamage(0);
            }
        }

        return true;
    }
}
