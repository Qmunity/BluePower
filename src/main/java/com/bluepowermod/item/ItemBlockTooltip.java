package com.bluepowermod.item;

import com.bluepowermod.block.worldgen.BlockStoneOre;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockTooltip extends ItemBlock {

    private Block block;

    public ItemBlockTooltip(Block block) {

        super(block);
        this.block = block;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (block instanceof BlockStoneOre)
            for (String s : ((BlockStoneOre) block).getTooltip())
                tooltip.add(I18n.format(s));
    }
}
