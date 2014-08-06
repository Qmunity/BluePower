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

import com.bluepowermod.BluePower;
import com.bluepowermod.api.Dependencies;
import com.bluepowermod.api.Refs;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier3.IRedBusWindow;
import com.bluepowermod.tileentities.tier3.TileCPU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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

        if (block == BPBlocks.multipart) {
            IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
            BPPart p = compat.getClickedPart(new Vector3(x, y, z, world), new Vector3(hitX, hitY, hitZ), player, null);

            if (p != null && player.isSneaking()) {
                p.onActivated(player, new MovingObjectPosition(x, y, z, side, Vec3.createVectorHelper(x + hitX, y + hitY, z + hitZ)), stack);
            }

            return false;
        }

        //TODO: Check this bit of code.. I don't understand why it's in here..
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IRedBusWindow && player.isSneaking() && !(te instanceof TileCPU)) {
            player.openGui(BluePower.instance, GuiIDs.REDBUS_ID.ordinal(), world, x, y, z);
        }
        //Check untill here.

        if(block instanceof BlockContainerBase){
            if(((BlockContainerBase)block).getGuiID() != GuiIDs.INVALID){
                if(player.isSneaking()){
                    block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
                    if (!player.capabilities.isCreativeMode) {
                        stack.setItemDamage(stack.getItemDamage() + 1);
                    }
                }
            }else{
                block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
                if (!player.capabilities.isCreativeMode) {
                    stack.setItemDamage(stack.getItemDamage() + 1);
                }
            }
        }else{
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
