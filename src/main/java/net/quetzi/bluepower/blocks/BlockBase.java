package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.util.RotationHelper;
import net.quetzi.bluepower.init.CustomTabs;

public class BlockBase extends Block {
    private static int rotation;

    public BlockBase(Material material) {
        super(material);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
        this.blockHardness= 3.0F;
    }
}
