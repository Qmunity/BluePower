package net.quetzi.bluepower.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.TileBase;

public abstract class BlockContainerBase extends BlockContainer
{
    private static int rotation;

    public BlockContainerBase(Material material)
    {
        super(material);
        this.setStepSound(soundTypeStone);
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
        this.blockHardness = 3.0F;
    }


    @Override
    public TileEntity createNewTileEntity(World world, int metadata)
    {
        try {
            return getTileEntity().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method to be overwritten to fetch the TileEntity Class that goes with the block
     *
     * @return a .class
     */
    protected abstract Class<? extends TileEntity> getTileEntity();

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        super.onNeighborBlockChange(world, x, y, z, block);
        //Only do this on the server side.
        if (!world.isRemote) {
            TileBase tileEntity = (TileBase) world.getTileEntity(x, y, z);
            if (tileEntity != null) {
                tileEntity.checkRedstonePower();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking()) {
            return false;
        }

        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity == null || !(entity instanceof TileBase)) {
            return false;
        }

        if (getGuiID() != GuiIDs.INVALID) {
            player.openGui(BluePower.instance, getGuiID().ordinal(), world, x, y, z);
            return true;
        }
        return false;
    }

    /**
     * Method to detect how the block was placed, and what way it's facing.
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack iStack)
    {
        int sideToPlace = MathHelper.floor_double(player.rotationYaw / 90F + 0.5D) & 3;

        int metaDataToSet = 0;
        switch (sideToPlace) {
            case 0:
                metaDataToSet = 2;
                break;
            case 1:
                metaDataToSet = 5;
                break;
            case 2:
                metaDataToSet = 3;
                break;
            case 3:
                metaDataToSet = 4;
                break;
        }

        world.setBlockMetadataWithNotify(x, y, z, metaDataToSet, 2);
    }

    /**
     * Method to be overwritten that returns a GUI ID
     *
     * @return
     */
    public abstract GuiIDs getGuiID();
}
