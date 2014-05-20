package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.quetzi.bluepower.references.Refs;

public class BlockBasalt extends Block {

    public BlockBasalt() {
        super(Material.rock);
        this.textureName = Refs.BASALT_TEXTURE_NAME;
        this.setCreativeTab(CreativeTabs.tabBlock);
        
    }

}
