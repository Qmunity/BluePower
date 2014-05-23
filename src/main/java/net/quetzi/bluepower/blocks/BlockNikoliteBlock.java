package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class BlockNikoliteBlock extends Block {
    public BlockNikoliteBlock(String name) {
        super(Material.rock);
        this.textureName = Refs.MODID + ":" + name;
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
        this.setHardness(1.9F);
        this.setResistance(10.0F);
        this.setBlockName(name);
    }

    public void registerBlockIcon(IIconRegister iconRegister) {
        iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
    }
}
