package com.bluepowermod.block.machine;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.client.render.RenderSolarPanel;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileSolarPanel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl);
 */
public class BlockSolarPanel extends BlockContainerBase {
    public BlockSolarPanel() {

        super(Material.rock, TileSolarPanel.class);
        setBlockName(Refs.SOLAR_PANEL_NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderSolarPanel.RENDER_ID;
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z){

        float minX = 0.0F;
        float minY = 0.0F;
        float minZ = 0.0F;
        float maxX = 1.0F;
        float maxY = 4.0F/16.0F;
        float maxZ = 1.0F;

        setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity){

        float minX = 0.0F;
        float minY = 0.0F;
        float minZ = 0.0F;
        float maxX = 1.0F;
        float maxY = 4.0F/16.0F;
        float maxZ = 1.0F;

        super.addCollisionBoxesToList(world, x, y, z, axisalignedbb, arraylist, par7Entity);
        setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }


}
