package net.quetzi.bluepower.blocks.machines;

import java.util.List;

import mcp.mobius.waila.api.IWailaBlock;
import mcp.mobius.waila.api.IWailaBlockDecorator;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.network.IWailaMessage;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileKinectGenerator;
/**
 * 
 * @author TheFjong
 *
 */
public class BlockKinectGenerator extends BlockContainerBase {

	public BlockKinectGenerator() {
		super(Material.iron);
		setCreativeTab(CustomTabs.tabBluePowerMachines);
		setBlockName(Refs.KINECT_NAME);
	}

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		
		return TileKinectGenerator.class;
	}

	@Override
	public GuiIDs getGuiID() {
		
		return GuiIDs.KINECT_ID;
	}

	

	

}
