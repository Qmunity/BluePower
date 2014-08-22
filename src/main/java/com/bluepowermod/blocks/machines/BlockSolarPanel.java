package com.bluepowermod.blocks.machines;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererSolarPanel;
import com.bluepowermod.tileentities.tier2.TileSolarPanel;
import com.bluepowermod.util.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

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

        return RendererSolarPanel.RENDER_ID;
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


}
