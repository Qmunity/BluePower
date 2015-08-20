package com.bluepowermod.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileBattery;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 */
public class BlockBattery extends BlockContainerBase {

    private IIcon textureTop;
    private IIcon textureBottom;
    private IIcon textureSide[];

    public BlockBattery() {

        super(Material.iron, TileBattery.class);
        setBlockName(Refs.BATTERY_NAME);
        setGuiId(GuiIDs.BATTERY_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        TileBattery te = (TileBattery) world.getTileEntity(x, y, z);
        ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
        if (forgeSide == ForgeDirection.UP)
            return textureTop;
        if (forgeSide == ForgeDirection.DOWN)
            return textureBottom;

        return textureSide[te.getTextureIndex()];
    }

    //This handler is only for items.
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection s = ForgeDirection.getOrientation(side);

        switch (s) {
        case UP:
            return textureTop;
        case DOWN:
            return textureBottom;
        case EAST:
        case NORTH:
        case SOUTH:
        case WEST:
        case UNKNOWN:
            return textureSide[0];
        default:
            break;

        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        textureTop = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "battery/battery_top");
        textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "battery/battery_bottom");
        textureSide = new IIcon[7];
        for (int i = 0; i <= 6; i++) {
            textureSide[i] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "battery/battery_side_" + i);
        }
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
