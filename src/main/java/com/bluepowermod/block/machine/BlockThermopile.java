package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import com.bluepowermod.tile.tier2.TileThermopile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;


/**
 * @author Koen Beckers (K-4U)
 */
public class BlockThermopile extends BlockContainerBase {

    private IIcon textureTop;
    private IIcon textureBottom;
    private IIcon textureSide_0;
    private IIcon textureSide_1;

    public BlockThermopile() {

        super(Material.rock, TileThermopile.class);
        setBlockName(Refs.THERMOPILE_NAME);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        textureTop = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.THERMOPILE_NAME + "_top");
        textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.THERMOPILE_NAME + "_bottom");
        textureSide_0 = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.THERMOPILE_NAME + "_side_0");
        textureSide_1 = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + Refs.THERMOPILE_NAME + "_side_1");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return getIcon(side, world.getBlockMetadata(x, y, z));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection s = ForgeDirection.getOrientation(side);
        // If is facing

        switch (s) {
            case UP:
                return textureTop;
            case DOWN:
                return textureBottom;
            case EAST:
            case WEST:
                return textureSide_0;
            case NORTH:
            case SOUTH:
            case UNKNOWN:
                return textureSide_1;
            default:
                break;

        }
        return null;
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return 0;
    }
}
