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
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IEnviromentBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * @author Koen Beckers (K4Unl)
 *
 */
public class BlockLamp extends BlockContainerBase implements ICustomModelBlock, IBPColoredBlock{

    public static final IntegerProperty POWER = IntegerProperty.create("power", 0, 15);

    private final boolean isInverted;
    private final MinecraftColor color;
    private final String name;

    public BlockLamp(String name, boolean isInverted, MinecraftColor color) {
        super(Material.REDSTONE_LIGHT, TileLamp.class);
        this.isInverted = isInverted;
        this.color = color;
        this.name = name;
        setDefaultState(getStateContainer().getBaseState().with(POWER, isInverted ? 15 : 0));
        setRegistryName(name + "." + (color == MinecraftColor.NONE ? "" : color.name().toLowerCase()) + (isInverted ? ".inverted" : ""));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initModel() {

    }


    protected TileLamp get(IBlockReader w, BlockPos pos) {

        TileEntity te = w.getTileEntity(pos);

        if (te == null || !(te instanceof TileLamp))
            return null;

        return (TileLamp) te;
    }

    @Override
    public int getLightValue(BlockState state, IEnviromentBlockReader w, BlockPos pos) {
        return !isInverted ? state.get(POWER) : 15 - state.get(POWER);
    }

    @Override
    public int getColor(IBlockReader w, BlockPos pos, int tint) {

        return color.getHex();
    }

    @Override
    public int getColor(int tint) {

        return color.getHex();

    }

    public boolean isInverted() {

        return isInverted;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return !(world.getBlockState(pos).getBlock() instanceof BlockLampRGB) && super.canConnectRedstone(state, world, pos, side);
    }

    @Override
    public boolean canRenderInLayer(BlockState state, BlockRenderLayer layer) {
        RenderLamp.pass = layer.ordinal();
        return super.canRenderInLayer(state, layer);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(POWER);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
        return super.getStateForPlacement(state, facing, state2, world, pos1, pos2, hand);
        //.with(POWER, world.getRedstonePowerFromNeighbors(pos));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        world.setBlockState(pos, state.with(POWER, world.getRedstonePowerFromNeighbors(pos)));
    }

}
