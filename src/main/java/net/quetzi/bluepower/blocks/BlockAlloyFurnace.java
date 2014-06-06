package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloyFurnace extends BlockBase {
    public boolean isActive = false;
    private IIcon textureTop;
    private IIcon textureBottom;
    private IIcon textureSide;
    private IIcon textureFrontOn;
    private IIcon textureFrontOff;

    public BlockAlloyFurnace(boolean isActive) {
        super(Material.rock);
        this.isActive = isActive;
        this.setBlockName(Refs.ALLOYFURNACE_NAME);
        this.setBlockTextureName(Refs.ALLOYFURNACE_NAME + "_front");
    }

    public int getLightValue() {
        if (this.isActive) {
            return 13;
        }
        return 0;
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
        return Item.getItemFromBlock(BPBlocks.alloy_furnace);
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        // Deal with rotation here
    }
    
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.textureTop : (side == 0 ? this.textureBottom : (side != meta ? this.blockIcon : this.textureSide));
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textureTop = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_top");
        this.textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_bottom");
        this.textureSide = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_side");
        this.textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_off");
        this.textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_on");
    }
}
