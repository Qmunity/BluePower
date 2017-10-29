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
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author Koen Beckers (K4Unl)
 *
 */
public class BlockLamp extends BlockContainerBase implements IBlockColor, IItemColor, ICustomModelBlock{

    public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);

    private final boolean isInverted;
    private final MinecraftColor color;
    private final AxisAlignedBB size;
    private final String name;

    public BlockLamp(String name, boolean isInverted, MinecraftColor color, AxisAlignedBB size) {
        super(Material.REDSTONE_LIGHT, TileLamp.class);
        this.isInverted = isInverted;
        this.color = color;
        this.size = size;
        this.name = name;
        setUnlocalizedName(name + "." + color.name().toLowerCase() + (isInverted ? ".inverted" : ""));
        setCreativeTab(BPCreativeTabs.lighting);
        setDefaultState(blockState.getBaseState().withProperty(POWER, isInverted ? 15 : 0));
        setRegistryName(name + (isInverted ? "inverted" : "") + color.name());
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        //Bounding box for the Caged Lamp
        return size.equals(Refs.CAGELAMP_AABB) ? size.grow( 0.0625 ) : size;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public AxisAlignedBB getSize(){
        return size;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        //All lamps need to use the same blockstate
        StateMapperBase stateMapper = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation(Refs.MODID + ":" + name, (!isInverted == iBlockState.getValue(POWER) > 0) ? "powered=true" : "powered=false");
            }
        };
        ModelLoader.setCustomStateMapper(this, stateMapper);
       if(!isInverted()) {
           ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Refs.MODID + ":" + name, "inventory"));
       }else {
           ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Refs.MODID + ":" + name, "inventory_glow"));
       }
    }


    protected TileLamp get(IBlockAccess w, BlockPos pos) {

        TileEntity te = w.getTileEntity(pos);

        if (te == null || !(te instanceof TileLamp))
            return null;

        return (TileLamp) te;
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess w, BlockPos pos) {
        int pow = !isInverted ? state.getValue(POWER) : 15 - state.getValue(POWER);

        if (Loader.isModLoaded("coloredlightscore")) {
            int color = getColor(w, pos);

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

    public int getColor(IBlockAccess w, BlockPos pos) {

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
        return !(world.getBlockState(pos) instanceof BlockLampRGB) && super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
        RenderLamp.pass = layer.ordinal();
        return super.canRenderInLayer(state, layer);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWER);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWER);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(POWER, meta);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        super.onBlockAdded(world, pos, state);
        world.setBlockState(pos, this.getDefaultState().withProperty(POWER, world.isBlockIndirectlyGettingPowered(pos)));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        world.setBlockState(pos, this.getDefaultState().withProperty(POWER, world.isBlockIndirectlyGettingPowered(pos)));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
        //Color for Block
        return getColor(world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        //Color for Itemstack
        return getColor();
    }
}
