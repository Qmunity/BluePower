/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author Koen Beckers (K4Unl)
 *
 */
public class BlockLamp extends BlockContainerBase {

    private final boolean isInverted;
    private final MinecraftColor color;

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite off, on;

    public BlockLamp(boolean isInverted, MinecraftColor color) {

        super(Material.iron, TileLamp.class);
        this.isInverted = isInverted;
        this.color = color;
        setBlockName(Refs.LAMP_NAME + "." + color.name().toLowerCase() + (isInverted ? ".inverted" : ""));
        setCreativeTab(BPCreativeTabs.lighting);

    }

    protected TileLamp get(IBlockAccess w, int x, int y, int z) {

        TileEntity te = w.getTileEntity(x, y, z);

        if (te == null || !(te instanceof TileLamp))
            return null;

        return (TileLamp) te;
    }

    public int getPower(IBlockAccess w, int x, int y, int z) {

        TileLamp te = get(w, x, y, z);
        if (te == null)
            return 0;

        int power = te.getPower();
        if (isInverted())
            power = 15 - power;
        return power;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(TextureMap iconRegister) {

        on = iconRegister.registerIcon(Refs.MODID + ":lamps/lamp_on");
        off = iconRegister.registerIcon(Refs.MODID + ":lamps/lamp_off");
    }

    @Override
    public int getLightValue(IBlockAccess w, int x, int y, int z) {

        int pow = getPower(w, x, y, z);

        if (Loader.isModLoaded("coloredlightscore")) {
            int color = getColor(w, x, y, z);

            int ri = (color >> 16) & 0xFF;
            int gi = (color >> 8) & 0xFF;
            int bi = (color >> 0) & 0xFF;

            float r = ri / 256F;
            float g = gi / 256F;
            float b = bi / 256F;

            // Clamp color channels
            if (r < 0.0f)
                r = 0.0f;
            else if (r > 1.0f)
                r = 1.0f;

            if (g < 0.0f)
                g = 0.0f;
            else if (g > 1.0f)
                g = 1.0f;

            if (b < 0.0f)
                b = 0.0f;
            else if (b > 1.0f)
                b = 1.0f;

            return pow | ((((int) (15.0F * b)) << 15) + (((int) (15.0F * g)) << 10) + (((int) (15.0F * r)) << 5));
        }

        return pow;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {

        return 0;
    }

    public int getColor(IBlockAccess w, int x, int y, int z) {

        return color.getHex();
    }

    public int getColor() {

        return color.getHex();
    }

    public boolean isInverted() {

        return isInverted;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return !(world.getBlockState(pos) instanceof BlockLampRGB);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        RenderLamp.pass = layer;
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderBlockPass() {

        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {

        return getColor(world, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(IBlockAccess world, int x, int y, int z, int side) {

        int power = getPower(world, x, y, z);

        return power > 0 ? on : off;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getIcon(int side, int meta) {

        return isInverted ? on : off;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {

        super.onNeighborBlockChange(world, x, y, z, block);

        if (this instanceof BlockLampRGB && block instanceof BlockLampRGB)
            return;

        TileLamp te = get(world, x, y, z);
        if (te == null)
            return;
        te.onUpdate();
    }

    @Override
    public void onBlockAdded(World world, int x, int y, int z) {

        super.onBlockAdded(world, x, y, z);

        TileLamp te = get(world, x, y, z);
        if (te == null)
            return;
        te.onUpdate();
    }
}
