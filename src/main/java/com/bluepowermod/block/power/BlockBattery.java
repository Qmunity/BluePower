/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBattery;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author MoreThanHidden
 */

public class BlockBattery extends BlockContainerBase {

    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 6);

    public BlockBattery() {
        super(Material.IRON, TileBattery.class);
        setTranslationKey(Refs.BATTERYBLOCK_NAME);
        setRegistryName(Refs.MODID, Refs.BATTERYBLOCK_NAME);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if(tile != null && tile.hasCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, null)) {
            IPowerBase storage = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, null);
            double voltage = storage.getVoltage();
            int level = (int)((voltage / storage.getMaxVoltage()) * 6);
            return this.blockState.getBaseState().withProperty(LEVEL, level);
        }
        return super.getActualState(state, worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile != null && tile.hasCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, null)) {
            IPowerBase storage = tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, null);
            if(world.isRemote)
                player.sendMessage(new StringTextComponent("Voltage: " + storage.getVoltage()));
            return true;
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return this.blockState.getBaseState().withProperty(LEVEL, meta);
    }

}
