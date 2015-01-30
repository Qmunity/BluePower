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

package com.bluepowermod.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.item.ItemMultipart;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPart extends ItemMultipart implements IDatabaseSaveable {

    private final PartInfo info;

    public ItemPart(PartInfo info) {

        setUnlocalizedName("part." + Refs.MODID + ":");

        setCreativeTab(BPCreativeTabs.items);

        this.info = info;
    }

    @Override
    public String getUnlocalizedName() {

        return super.getUnlocalizedName() + info.getExample().getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack p_77667_1_) {

        return getUnlocalizedName();
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {

        return CreativeTabs.creativeTabArray;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {

        for (CreativeTabs t : info.getExample().getCreativeTabs())
            if (t != null && t.equals(tab) || tab == null)
                l.addAll(info.getExample().getSubItems());
    }

    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        BPPart part = info.create();
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.canGoInCopySlot(stack);
    }

    @Override
    public boolean canCopy(ItemStack templateStack, ItemStack outputStack) {

        String templateType = PartManager.getPartType(templateStack);
        String outputType = PartManager.getPartType(outputStack);

        if (templateType == null || outputType == null)
            return false;

        if (templateType.equals(outputType)) {
            return canGoInCopySlot(templateStack);
        } else {
            return false;
        }
    }

    @Override
    public List<ItemStack> getItemsOnStack(ItemStack stack) {

        BPPart part = info.create();
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.getItemsOnStack(stack);
    }

    @Override
    public String getCreatedPartType(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop) {

        return null;
    }

    @Override
    public IPart createPart(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop) {

        IPart part = info.create();
        BPApi.getInstance().loadSilkySettings(part, item);
        return part;
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int face, float x_, float y_, float z_) {

        if (!super.onItemUse(item, player, world, x, y, z, face, x_, y_, z_))
            return false;

        Block.SoundType sound = PartManager.getExample(item).getPlacementSound();
        world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, sound.func_150496_b(), sound.getVolume() + 3, sound.getPitch() * 0.85F);

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item, EntityPlayer player, List list, boolean unused) {

        BPPart part = PartManager.getExample(item);
        if (part == null)
            return;
        List<String> l = new ArrayList<String>();
        part.addTooltip(item, l);
        list.addAll(l);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister p_94581_1_) {

    }

    public String getPartType() {

        return info.getType();
    }
}
