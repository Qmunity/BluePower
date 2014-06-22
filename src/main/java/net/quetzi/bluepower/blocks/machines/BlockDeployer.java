package net.quetzi.bluepower.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.blocks.BlockContainerBase;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileDeployer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
/**
 * 
 * @author TheFjong
 *
 */
public class BlockDeployer extends BlockContainerBase{

	private IIcon textureFront;
	private IIcon textureFront_active;
	private IIcon textureBack;
	
	public BlockDeployer() {
		
		super(Material.rock);
		setCreativeTab(CustomTabs.tabBluePowerMachines);
		setBlockName(Refs.BLOCKDEPLOYER_NAME);
	}

	@Override
	protected Class<? extends TileEntity> getTileEntity() {
		
		return TileDeployer.class;
	}

	@Override
	public GuiIDs getGuiID() {
		
		return GuiIDs.DEPLOYER_ID;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        String unlocName = getUnlocalizedName().substring(5);
        
       	textureFront_active = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + unlocName + "_front_active");
        textureFront = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + unlocName + "_front");
        textureBack = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + unlocName + "_back");
        blockIcon = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + unlocName + "_side");
    }
	
	
	@Override
	public IIcon getIcon(int side, int meta) {
	
		  
		  ForgeDirection direction = ForgeDirection.getOrientation(meta);
	        if (side == direction.ordinal()) {                                                                                                                                                                                                                                                                                                                                                                                                                                                              
	        		return textureFront;
	       
	        } else if (side == direction.getOpposite().ordinal()) { return textureBack; }
	        return blockIcon;
		
	}
	
	@SuppressWarnings("cast")
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack)
    {
		super.onBlockPlacedBy(world, x, y, z, player, stack);
        if (world.getTileEntity(x, y, z) instanceof TileDeployer)
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
            TileDeployer tile = (TileDeployer) world.getTileEntity(x, y, z);
            tile.setOrientation(direction);
         
        }
    }
	
}
