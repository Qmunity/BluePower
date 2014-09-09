/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.compat.fmp;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.util.Refs;

public class ItemBPMultipart extends JItemMultiPart implements IDatabaseSaveable {

    private final String partName;
    private BPPart p;

    public ItemBPMultipart(String partName) {

        super();
        this.partName = partName;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return "part." + Refs.MODID + ":" + partName;
    }

    @Override
    public void registerIcons(IIconRegister register) {

    }

    @Override
    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_) {
        p_150895_3_.add(PartRegistry.getInstance().getItemForPart(partName));
    }

    @Override
    public TMultiPart newPart(ItemStack is, EntityPlayer player, World w, BlockCoord b, int unused, Vector3 unused1) {

        MultipartBPPart part = (MultipartBPPart) RegisterMultiparts.createPart_(p);

        return part;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float f, float f2, float f3) {

        p = PartRegistry.getInstance().createPart(PartRegistry.getInstance().getPartIdFromItem(stack));

        if (p == null)
            return false;

        p.setWorld(w);
        p.setX(x);
        p.setY(y);
        p.setZ(z);

        if (!p.canPlacePart(stack, player, new com.bluepowermod.api.vec.Vector3(x, y, z, w),
                new MovingObjectPosition(x, y, z, side, Vec3.createVectorHelper(x + f, y + f2, z + f3), true)))
            return false;

        if (super.onItemUse(stack, player, w, x, y, z, side, f, f2, f3)) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeStone.getBreakSound(), Block.soundTypeStone.getVolume(),
                    Block.soundTypeStone.getPitch());

            p.onAdded();

            return true;
        }

        return false;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {

        return PartRegistry.getInstance().hasCustomItemEntity(stack);
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {

        if (PartRegistry.getInstance().hasCustomItemEntity(itemstack)) {
            EntityItem e = PartRegistry.getInstance().createItemEntityForStack(world, location.posX, location.posY, location.posZ, itemstack);
            e.delayBeforeCanPickup = 50;
            return e;
        }

        return super.createEntity(world, location, itemstack);
    }

    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        BPPart part = PartRegistry.getInstance().createPartFromItem(stack);
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.canGoInCopySlot(stack);
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

        BPPart part = PartRegistry.getInstance().createPartFromItem(stack);
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.getItemsOnStack(stack);
    }

}
