package net.quetzi.bluepower.blocks;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;

public abstract class BlockContainerBase extends BlockContainer {
    private static int rotation;

    public BlockContainerBase(Material material) {
        super(material);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
        this.blockHardness= 3.0F;
    }

    
    @Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		try{
			return getTileEntity().newInstance();
		}catch(Exception e){
			return null;
		}
	}
	
    /**
     * Method to be overwritten to fetch the TileEntity Class that goes with the block
     * @return a .class
     */
	protected abstract Class<? extends TileEntity> getTileEntity(); 
}
