/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.worldgen;

import com.bluepowermod.client.render.ICustomModelBlock;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockStoneOreConnected extends BlockStoneOre implements ICustomModelBlock {

    private String name;
    public static final List<ConnectedProperty> CONNECTED = new ArrayList<ConnectedProperty>();

    static {
        for(int i = 0; i < 18; i++){
            CONNECTED.add(new ConnectedProperty("connected_" + i));
        }
    }

    public BlockStoneOreConnected(String name) {
        super(name);
        this.name = name;
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) blockState.getBaseState();
        for(int i = 0; i < 18; i++){
            extendedBlockState = extendedBlockState.withProperty(CONNECTED.get(i), false);
        }
        this.setDefaultState(extendedBlockState);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        IProperty[] listedProperties = new IProperty[0]; // no listed properties
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[18];
        for(int i = 0; i < 18; i++){
            unlistedProperties[i] = CONNECTED.get(i);
        }
        return new ExtendedBlockState(this, listedProperties, unlistedProperties);
    }

    private Boolean isSameBlock(IBlockAccess world, BlockPos pos){
        return world.getBlockState(pos).getBlock() == this;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        List<Boolean> connected = new ArrayList<Boolean>();

        //Directions
        connected.add(isSameBlock(world, pos.north()));
        connected.add(isSameBlock(world, pos.south()));
        connected.add(isSameBlock(world, pos.west()));
        connected.add(isSameBlock(world, pos.east()));
        connected.add(isSameBlock(world, pos.up()));
        connected.add(isSameBlock(world, pos.down()));

        //Corners
        connected.add(isSameBlock(world, pos.up().east()));
        connected.add(isSameBlock(world, pos.up().west()));
        connected.add(isSameBlock(world, pos.up().south()));
        connected.add(isSameBlock(world, pos.up().north()));
        connected.add(isSameBlock(world, pos.down().east()));
        connected.add(isSameBlock(world, pos.down().west()));
        connected.add(isSameBlock(world, pos.down().south()));
        connected.add(isSameBlock(world, pos.down().north()));
        connected.add(isSameBlock(world, pos.north().east()));
        connected.add(isSameBlock(world, pos.north().west()));
        connected.add(isSameBlock(world, pos.south().east()));
        connected.add(isSameBlock(world, pos.south().west()));

        for(int i = 0; i < 18; i++){
            extendedBlockState = extendedBlockState.withProperty(CONNECTED.get(i), connected.get(i));
        }

        return extendedBlockState;
    }
    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        world.markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.markBlockRangeForRenderUpdate(pos.add(-1, -1, -1), pos.add(1, 1, 1));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initModel() {
        StateMapperBase stateMapper = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState iBlockState) {
                return new ModelResourceLocation(getRegistryName().toString());
            }
        };
        ModelLoader.setCustomStateMapper(this, stateMapper);

    }

    public static class ConnectedProperty implements IUnlistedProperty<Boolean> {

        private final String name;
        private Class<Boolean> type;

        public ConnectedProperty(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isValid(Boolean value) {
            return true;
        }

        @Override
        public Class<Boolean> getType() {

            return Boolean.class;
        }

        @Override
        public String valueToString(Boolean value) {
            return value.toString();
        }

    }


}
