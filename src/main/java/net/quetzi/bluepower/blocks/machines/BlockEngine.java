package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileEngine;
/**
 * 
 * @author TheFjong
 *
 */
public class BlockEngine extends BlockContainer {

	public BlockEngine() {
		super(Material.iron);
		setCreativeTab(CustomTabs.tabBluePowerMachines);
		setBlockName(Refs.ENGINE_NAME);
		setBlockTextureName("models/engineoff");
		
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
	
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
        if (world.getTileEntity(x, y, z) instanceof TileEngine)
        {
            int direction = 0;
            int facing;
            
            if (player.rotationPitch > 45) {
                facing = 5;
            } else if (player.rotationPitch < -45) {
                facing = 4;
            } else {
                facing = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            }

            if (facing == 0){
            	
            	direction = ForgeDirection.SOUTH.ordinal();
            }else
            if(facing == 1){
            	
            	direction = ForgeDirection.WEST.ordinal();
            }else
            if(facing == 2){
            	
            	direction = ForgeDirection.NORTH.ordinal();
            }else
            if(facing == 3){
            	
            	direction = ForgeDirection.EAST.ordinal();
            }else
            if(facing == 4){
            	
            	direction = ForgeDirection.UP.ordinal();
            }else
            if(facing == 5){
            	
            	direction = ForgeDirection.DOWN.ordinal();
            }         
            TileEngine tile = (TileEngine) world.getTileEntity(x, y, z);
            tile.setOrientation(direction);
         
        }
    }



	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new TileEngine();
	}
	
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return this.blockIcon;
	}
	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		this.blockIcon = p_149651_1_.registerIcon(Refs.MODID +":"+ "models/engineoff");
	}
	
	@Override
	public boolean onBlockActivated(World world, int x,int y, int z, EntityPlayer player,int p_149727_6_, float p_149727_7_, float p_149727_8_,float p_149727_9_) {
		
		Item item = player.inventory.getCurrentItem().getItem();
		
		if(item !=null){
			if(item == BPItems.screwdriver){
				if(!world.isRemote){
					int direction = 0;
					int facing    = 0;
					
					if (player.rotationPitch > 45) {
		                facing = 5;
		            } else if (player.rotationPitch < -45) {
		                facing = 4;
		            } else {
		                facing = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
		            }
					
					TileEngine engine = (TileEngine) world.getTileEntity(x, y, z);
					
					 if (facing == 0){
						 if(player.isSneaking()) direction = ForgeDirection.NORTH.ordinal();
			            	direction = ForgeDirection.SOUTH.ordinal();
			            }else
			            if(facing == 1){
			            	if(player.isSneaking()) direction = ForgeDirection.EAST.ordinal();
			            	direction = ForgeDirection.WEST.ordinal();
			            }else
			            if(facing == 2){
			            	if(player.isSneaking()) direction = ForgeDirection.SOUTH.ordinal();
			            	direction = ForgeDirection.NORTH.ordinal();
			            }else
			            if(facing == 3){
			            	if(player.isSneaking()) direction = ForgeDirection.WEST.ordinal();
			            	direction = ForgeDirection.EAST.ordinal();
			            }else
			            if(facing == 4){
			            	if(player.isSneaking()) direction = ForgeDirection.DOWN.ordinal();
			            	direction = ForgeDirection.UP.ordinal();
			            }else
			            if(facing == 5){
			            	if(player.isSneaking()) direction = ForgeDirection.UP.ordinal();
			            	direction = ForgeDirection.DOWN.ordinal();
			            }     
					 
					engine.setOrientation(direction);
					world.markBlockForUpdate(x, y, z);
					System.out.println(direction + " Direction:Facing " + facing);
					
				}
			}	
		}
		
		return false;
	}	
}
