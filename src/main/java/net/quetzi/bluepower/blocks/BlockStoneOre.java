package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

import java.util.Random;

public class BlockStoneOre extends Block
{

    public BlockStoneOre(String name)
    {
        super(Material.rock);
        this.setBlockName(name);
        this.setHardness(3.0F);
        if (name == Refs.BASALT_NAME) {
            this.setResistance(25.0F);
            this.setHarvestLevel("pickaxe", 1);
        } else if (name == Refs.MARBLE_NAME) {
            this.setResistance(1.0F);
            this.setHarvestLevel("pickaxe", 1);
            this.setHardness(1.5F);
        } else {
            this.setResistance(5.0F);
            this.setHarvestLevel("pickaxe", 2);
        }
        this.textureName = Refs.MODID + ":" + name;
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
    }

    public Item getItemDropped(int par1, Random par2, int par3)
    {
        if (this.getUnlocalizedName().substring(5).matches(Refs.BASALT_NAME)) {
            return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + Refs.BASALTCOBBLE_NAME));
        }
        return Item.getItemFromBlock(Block.getBlockFromName(Refs.MODID + ":" + this.getUnlocalizedName().substring(5)));
    }

    // Allow storage blocks to be used as a beacon base
    public boolean isBeaconBase(IBlockAccess worldObj, int x, int y, int z, int beaconX, int beaconY, int beaconZ)
    {
        return this == BPBlocks.amethyst_block || this == BPBlocks.ruby_block || this == BPBlocks.sapphire_block || this == BPBlocks.copper_block || this == BPBlocks.tin_block || this == BPBlocks.silver_block;
    }

    public int quantityDropped()
    {
        return 1;
    }

    public void registerBlockIcon(IIconRegister iconRegister)
    {
        iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5));
    }
}
