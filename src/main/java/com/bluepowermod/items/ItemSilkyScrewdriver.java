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
import uk.co.qmunity.lib.vec.Vec3d;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSilkyScrewdriver extends ItemBase {

    public ItemSilkyScrewdriver() {

        setUnlocalizedName(Refs.SILKYSCREWDRIVER_NAME);
        setCreativeTab(BPCreativeTabs.tools);
        setMaxDamage(250);
        setMaxStackSize(1);
        setTextureName(Refs.MODID + ":" + Refs.SILKYSCREWDRIVER_NAME);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {

        Block block = world.getBlock(x, y, z);
        TileEntity te = world.getTileEntity(x, y, z);

        ITilePartHolder h = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (h != null) {
            QMovingObjectPosition mop = h.rayTrace(new Vec3d(x, y, z, world), new Vec3d(hitX, hitY, hitZ));
            if (mop != null) {
                IPart p = mop.getPart();
                if (p instanceof ISilkyRemovable && !world.isRemote) {
                    if (p instanceof IAdvancedSilkyRemovable && !((IAdvancedSilkyRemovable) p).preSilkyRemoval(world, x, y, z))
                        return false;
                    NBTTagCompound tag = new NBTTagCompound();
                    p.writeToNBT(tag);
                    ItemStack droppedStack = p.getItem();
                    NBTTagCompound stackTag = droppedStack.getTagCompound();
                    stackTag.setTag("tileData", tag);
                    world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, droppedStack));
                    h.removePart(p);
                    if (p instanceof IAdvancedSilkyRemovable)
                        ((IAdvancedSilkyRemovable) p).postSilkyRemoval(world, x, y, z);
                    stack.damageItem(1, player);
                    return true;
                }
            }

            return false;
        }

        if (block instanceof ISilkyRemovable && !world.isRemote) {
            if (block instanceof IAdvancedSilkyRemovable && !((IAdvancedSilkyRemovable) block).preSilkyRemoval(world, x, y, z))
                return false;
            if (te == null)
                throw new IllegalStateException("Block doesn't have a TileEntity?! Implementers of ISilkyRemovable should have one. Offender: "
                        + block.getUnlocalizedName());
            NBTTagCompound tag = new NBTTagCompound();
            te.writeToNBT(tag);
            int metadata = world.getBlockMetadata(x, y, z);
            Item item = block.getItemDropped(metadata, itemRand, 0);
            if (item == null)
                throw new NullPointerException("Block returns null for getItemDropped(meta, rand, fortune)! Offender: " + block.getUnlocalizedName());
            ItemStack droppedStack = new ItemStack(item, 1, block.damageDropped(metadata));
            NBTTagCompound stackTag = droppedStack.getTagCompound();
            if (stackTag == null) {
                stackTag = new NBTTagCompound();
                droppedStack.setTagCompound(stackTag);
            }
            stackTag.setTag("tileData", tag);
            world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, droppedStack));
            world.setBlockToAir(x, y, z);
            if (block instanceof IAdvancedSilkyRemovable)
                ((IAdvancedSilkyRemovable) block).postSilkyRemoval(world, x, y, z);
            stack.damageItem(1, player);
            return true;
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
