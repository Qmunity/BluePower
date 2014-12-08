package com.bluepowermod.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.item.ItemMultipart;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.item.IDatabaseSaveable;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.util.Refs;

public class ItemPart extends ItemMultipart implements IDatabaseSaveable {

    public ItemPart() {

        setUnlocalizedName("part." + Refs.MODID + ":");

        setCreativeTab(BPCreativeTabs.items);
    }

    @Override
    public CreativeTabs[] getCreativeTabs() {

        return CreativeTabs.creativeTabArray;
    }

    @Override
    public String getUnlocalizedName(ItemStack item) {

        return super.getUnlocalizedName() + PartManager.getPartType(item);
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {

        for (PartInfo i : PartManager.getRegisteredParts()) {
            for (CreativeTabs t : i.getExample().getCreativeTabs())
                if ((t != null && t.equals(tab)) || tab == null)
                    l.add(i.getItem());
        }
    }

    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        BPPart part = PartManager.getExample(stack);
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

        BPPart part = PartManager.getExample(stack);
        BPApi.getInstance().loadSilkySettings(part, stack);
        return part.getItemsOnStack(stack);
    }

    @Override
    public String getCreatedPartType(ItemStack item, EntityPlayer player, World world, MovingObjectPosition mop) {

        return PartManager.getPartType(item);
    }

    @Override
    public boolean onItemUse(ItemStack item, EntityPlayer player, World world, int x, int y, int z, int face, float x_, float y_, float z_) {

        if (!super.onItemUse(item, player, world, x, y, z, face, x_, y_, z_))
            return false;

        Block.SoundType sound = PartManager.getExample(item).getPlacementSound();
        world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, sound.func_150496_b(), sound.getVolume() + 3, sound.getPitch() * 0.85F);

        return true;
    }
}
