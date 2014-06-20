package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileEngine;
/**
 * 
 * @author TheFjong
 *
 */
public class BlockEngine extends BlockContainerBase {

	public BlockEngine() {
		super(Material.iron);
		setCreativeTab(CustomTabs.tabBluePowerMachines);
		setBlockName(Refs.ENGINE_NAME);
	}

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		
		return TileEngine.class;
	}

	@Override
	public GuiIDs getGuiID() {
		
		return null;
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
