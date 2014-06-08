package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.quetzi.bluepower.init.CustomTabs;

public class BlockBase extends Block {
    
    public BlockBase(Material material) {
    
        super(material);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
        this.blockHardness = 3.0F;
    }
}
