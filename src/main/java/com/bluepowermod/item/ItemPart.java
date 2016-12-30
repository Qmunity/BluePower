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

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.BPPartInfo;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.reference.Refs;
import mcmultipart.api.multipart.IMultipart;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.item.ItemMultipart;

import java.util.ArrayList;
import java.util.List;


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

        return CreativeTabs.CREATIVE_TAB_ARRAY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (CreativeTabs t : info.getExample().getCreativeTabs())
            if (t != null && t.equals(tab) || tab == null)
                subItems.addAll(info.getExample().getSubItems());
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
    public String getCreatedPartType(ItemStack item, EntityPlayer player, World world, RayTraceResult mop) {

        return null;
    }

    @Override
    public BPPart createPart(ItemStack item, EntityPlayer player, World world, RayTraceResult mop) {

        BPPart part = info.create();
        BPApi.getInstance().loadSilkySettings(part, item);
        return part;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!(super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ) == EnumActionResult.SUCCESS))
            return EnumActionResult.PASS;

        if (world.isRemote)
            playPlacementSound(pos, PartManager.getExample(player.getHeldItem(hand)).getPlacementSound());

        return EnumActionResult.SUCCESS;
    }

    @SideOnly(Side.CLIENT)
    private void playPlacementSound(BlockPos pos, SoundType sound) {

        Minecraft
        .getMinecraft()
        .getSoundHandler()
        .playSound(
                new PositionedSoundRecord(new ResourceLocation(sound.func_150496_b()), (sound.getVolume() + 3)
                        * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.BLOCKS), sound.getPitch() * 0.85F,
                        pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F));
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

    public String getPartType() {

        return info.getType();
    }
}
