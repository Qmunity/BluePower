package com.bluepowermod.blocks.computer;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier3.TileCPU;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCPU extends BlockContainerBase {
	
	@SideOnly(Side.CLIENT)
    protected IIcon topTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon frontTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon sideTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon backTexture;
    @SideOnly(Side.CLIENT)
    protected IIcon bottomTexture;
	
	public BlockCPU() {
		
		super(Material.iron);
		setBlockName(Refs.BLOCKCPU_NAME);
	}
	
	public void updateTick(World world, int x, int y, int z, Random random)
    {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileCPU) {
		}
    }
	
	@Override
    public GuiIDs getGuiID() {
		
        return GuiIDs.CPU;
    }
	
	@Override
	public int tickRate(World world)
    {
        return 1;
    }
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection dir = ForgeDirection.getOrientation(meta);
        if (side == dir.ordinal()) {
            return this.topTexture;
        }
        else if (side == dir.getOpposite().ordinal()) {
        	return this.bottomTexture;
        }
        else if (side == dir.WEST.ordinal()) {
        	return this.frontTexture;
        }
        else if (side == dir.EAST.ordinal()) {
        	return this.backTexture;
        }
        return this.sideTexture;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

		TileCPU tile = (TileCPU) world.getTileEntity(x, y, z);
        ForgeDirection dir = tile.getFacingDirection();
        
        if (dir.ordinal() == side) {
            return this.frontTexture;
        }
        else if (dir.getOpposite().ordinal() == side) {
            return this.backTexture;
        }
        else if (dir.UP.ordinal() == side) {
            return this.topTexture;
        }
        else if (dir.DOWN.ordinal() == side) {
            return this.bottomTexture;
        }
        else {
            return this.sideTexture;
        }
    }
	
	 @SideOnly(Side.CLIENT)
	 public void registerBlockIcons(IIconRegister iconRegister){
		 
	     this.frontTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_front");
	     this.sideTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_side");
	     this.topTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_top");
	     this.backTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_back");
	     this.bottomTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_bottom");
	 }

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		
		return TileCPU.class;
	}
}
