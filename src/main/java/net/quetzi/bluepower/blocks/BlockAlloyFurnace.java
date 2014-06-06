package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileEntities.tier1.TileAlloyFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloyFurnace extends BlockContainerBase {
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
    public IIcon getIcon(int side, int meta){
    	ForgeDirection s = ForgeDirection.getOrientation(side);
    	if(side == meta){
    		//Todo: Some meta bitmasking here to check whether or not it's active.
    		return this.textureFrontOff;
    	}
    	switch (s) {
    	case UP:
    		return textureTop;
    	case DOWN:
			return textureBottom;
		case EAST:
		case NORTH:
		case SOUTH:
		case WEST:
		case UNKNOWN:
			return textureSide;
		default:
			break;

		}
    	return null;
    }
    
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textureTop = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_top");
        this.textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_bottom");
        this.textureSide = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_side");
        this.textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_off");
        this.textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_on");
    }

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		return TileAlloyFurnace.class;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack){
		int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;
		
		
		int metaDataToSet = 0;
		switch(sideToPlace){
		case 0:
			metaDataToSet = 2;
			break;
		case 1:
			metaDataToSet = 5;
			break;
		case 2:
			metaDataToSet = 3;
			break;
		case 3:
			metaDataToSet = 4;
			break;
		}
		
		world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
	}
}
