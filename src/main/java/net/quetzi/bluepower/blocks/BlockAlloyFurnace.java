package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileEntities.tier1.TileAlloyFurnace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloyFurnace extends BlockContainerBase {
    private IIcon textureTop;
    private IIcon textureBottom;
    private IIcon textureSide;
    private IIcon textureFrontOn;
    private IIcon textureFrontOff;

    public BlockAlloyFurnace() {
        super(Material.rock);
        this.setBlockName(Refs.ALLOYFURNACE_NAME);
        //This might not be needed actually.
        this.setBlockTextureName(Refs.ALLOYFURNACE_NAME + "_front");
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z){
    	int metadata = world.getBlockMetadata(x, y, z);
    	if((metadata & (1 << 3)) != 0){
    		return 13;
    	}
        return 0;
    }

    @Override
    public boolean isOpaqueCube() {
        return true;
    }

    //Not sure if you need this function.
    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
        return Item.getItemFromBlock(BPBlocks.alloy_furnace);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
    	ForgeDirection s = ForgeDirection.getOrientation(side);
    	//If is facing
    	if(meta == side){
    		//Do some bitmasking
    		if((meta & (1 << 3)) != 0){
    			return this.textureFrontOn;
    		}else{
    			return this.textureFrontOff;
    		}
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
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.textureTop = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_top");
        this.textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_bottom");
        this.textureSide = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_side");
        this.textureFrontOn = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_on");
        this.textureFrontOff = iconRegister.registerIcon(Refs.MODID + ":" + this.getUnlocalizedName().substring(5) + "_front_off");
    }

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		return TileAlloyFurnace.class;
	}
}
