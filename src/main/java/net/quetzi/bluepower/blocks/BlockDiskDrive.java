package net.quetzi.bluepower.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier3.TileDiskDrive;

public class BlockDiskDrive extends BlockContainerBase {
	
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

	public BlockDiskDrive() {
		super(Material.iron);
		setBlockName(Refs.BLOCKDISKDRIVE_NAME);
	}
	
	@Override
    public GuiIDs getGuiID() {
        return GuiIDs.DISK_DRIVE;
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

		TileDiskDrive tile = (TileDiskDrive) world.getTileEntity(x, y, z);
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
	     this.frontTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "disk_drive_front");
	     this.sideTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_side");
	     this.topTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_top");
	     this.backTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_back");
	     this.bottomTexture = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "cpu_bottom");
	 }
	
	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		return TileDiskDrive.class;
	}

}
