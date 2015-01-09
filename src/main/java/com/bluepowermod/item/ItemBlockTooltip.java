package com.bluepowermod.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.bluepowermod.block.worldgen.BlockStoneOre;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBlockTooltip extends ItemBlock {

    private Block block;

    public ItemBlockTooltip(Block block) {

        super(block);
        this.block = block;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) {

        super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);

        if (block instanceof BlockStoneOre)
            for (String s : ((BlockStoneOre) block).getTooltip())
                p_77624_3_.add(I18n.format(s));
    }

}
