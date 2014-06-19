package net.quetzi.bluepower.blocks;

import java.util.Random;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.TileMonitor;

public class BlockMonitor extends BlockContainerBase {
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
	
	public BlockMonitor() {
		super(Material.iron);
		setBlockName(Refs.BLOCKMONITOR_NAME);
	}
	
	public void updateTick(World world, int x, int y, int z, Random random)
    {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if (tileEntity instanceof TileMonitor) {
			//((TileCPU)tileEntity).updateEntity();
			//Logs.log(Level.INFO, "[BluePowerControl] CPU TE ticked");
		}
    }
	
	@Override
    public GuiIDs getGuiID() {
        return GuiIDs.MONITOR;
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

		TileMonitor tile = (TileMonitor) world.getTileEntity(x, y, z);
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
	 public void registerBlockIcons(IIconRegister iconRegister)
	 {
		 int i = 0;
	     this.frontTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "monitor_front");
	     this.sideTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_side");
	     this.topTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_top");
	     this.backTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_back");
	     this.bottomTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_bottom");
	     //this.frontTexture = this.blockIcon;
	 }

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		return TileMonitor.class;
	}
}
