package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileWindmill;
/**
 * 
 * @author TheFjong
 *
 */
public class BlockWindmill extends Block implements ITileEntityProvider {

	public BlockWindmill() {
		super(Material.iron);
		setCreativeTab(CustomTabs.tabBluePowerItems);
		setBlockName(Refs.WINDMILL_NAME);
		setBlockBounds(0, 0, 0, 1, 1, 1);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileWindmill();
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	@Override
	public int getRenderType() {
		return -1;
	}
	

}
