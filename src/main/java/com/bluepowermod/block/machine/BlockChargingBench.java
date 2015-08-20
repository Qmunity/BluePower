package com.bluepowermod.block.machine;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.client.render.RenderChargingBench;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileChargingBench;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K-4U)
 */
public class BlockChargingBench extends BlockContainerBase {

    private IIcon textureTop_On;
    private IIcon textureTop_Off;
    private IIcon textureBottom;
    private IIcon[] textureFront_On;
    private IIcon[] textureFront_Off;
    private IIcon textureSide_On;
    private IIcon textureSide_Off;

    public BlockChargingBench() {

        super(Material.iron, TileChargingBench.class);
        setBlockName(Refs.CHARGINGBENCH_NAME);
        setGuiId(GuiIDs.CHARGINGBENCH_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {

        TileChargingBench te = (TileChargingBench) world.getTileEntity(x, y, z);
        ForgeDirection forgeSide = ForgeDirection.getOrientation(side);
        if (forgeSide == ForgeDirection.UP) {
            return te.isPowered() ? textureTop_On : textureTop_Off;
        }
        if (forgeSide == ForgeDirection.DOWN)
            return textureBottom;
        if (forgeSide.equals(te.getFacingDirection())) {
            return te.isPowered() ? textureFront_On[te.getTextureIndex()] : textureFront_Off[te.getTextureIndex()];
        }

        return te.isPowered() ? textureSide_On : textureSide_Off;

    }

    // This handler is only for items.
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {

        ForgeDirection s = ForgeDirection.getOrientation(side);
        if (side == 3) {
            return textureFront_Off[0];
        }

        switch (s) {
        case UP:
            return textureTop_Off;
        case DOWN:
            return textureBottom;
        case EAST:
        case NORTH:
        case SOUTH:
        case WEST:
        case UNKNOWN:
            return textureSide_Off;
        default:
            break;

        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {

        textureTop_On = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "chargingbench/chargingbench_top_on");
        textureTop_Off = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "chargingbench/chargingbench_top_off");
        textureBottom = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "chargingbench/chargingbench_bottom");
        textureSide_On = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "chargingbench/chargingbench_side_on");
        textureSide_Off = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "chargingbench/chargingbench_side_off");
        textureFront_Off = new IIcon[5];
        textureFront_On = new IIcon[5];
        for (int i = 0; i <= 4; i++) {
            textureFront_Off[i] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION
                    + "chargingbench/chargingbench_front_off_" + i);
            textureFront_On[i] = iconRegister.registerIcon(Refs.MODID + ":" + Refs.MACHINE_TEXTURE_LOCATION + "chargingbench/chargingbench_front_on_"
                    + i);
        }
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return RenderChargingBench.RENDER_ID;
    }

    @Override
    public boolean isNormalCube() {

        return false;
    }

    @Override
    public boolean isOpaqueCube() {

        return false;
    }

    @Override
    public boolean isBlockNormalCube() {

        return false;
    }
}
