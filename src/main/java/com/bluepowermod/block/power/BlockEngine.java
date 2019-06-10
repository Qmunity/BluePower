/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileEngine;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

;import javax.annotation.Nullable;

/**
 *
 * @author TheFjong, MoreThanHidden
 *
 */
public class BlockEngine extends BlockContainerBase {

    public static final PropertyBool ACTIVE = PropertyBool.create("active");
    public static final PropertyBool GEAR = PropertyBool.create("gear");
    public static final PropertyBool GLIDER = PropertyBool.create("glider");
    public static final PropertyEnum<Direction> FACING = PropertyEnum.create("facing", Direction.class);

    public BlockEngine() {

        super(Material.IRON, TileEngine.class);
        setCreativeTab(BPCreativeTabs.machines);
        setTranslationKey(Refs.ENGINE_NAME);
        setDefaultState(blockState.getBaseState().withProperty(ACTIVE, false).withProperty(GEAR, false).withProperty(GLIDER, false).withProperty(FACING, Direction.DOWN));
        setRegistryName(Refs.MODID, Refs.ENGINE_NAME);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE, GEAR, GLIDER, FACING);
    }
    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(ACTIVE) ? 0 : 1;
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(ACTIVE, meta == 1);
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEngine tile = (TileEngine)worldIn.getTileEntity(pos);
        return ((tile != null) && (tile.getOrientation() != null)) ? getDefaultState().withProperty(FACING, tile.getOrientation()).withProperty(ACTIVE, tile.isActive) : super.getActualState(state, worldIn, pos);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, Direction dir) {
        if(!world.isRemote) {
            TileEngine engine = (TileEngine) world.getTileEntity(pos);
            if (engine != null) {
                engine.setOrientation(Direction.byIndex(dir.ordinal()));
            }
            world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
        }
        return super.rotateBlock(world, pos, dir);
    }

    @SuppressWarnings("cast")
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity player, ItemStack iStack) {
        if (world.getTileEntity(pos) instanceof TileEngine) {
            Direction facing;

            if (player.rotationPitch > 45) {

                facing = Direction.DOWN;
            } else if (player.rotationPitch < -45) {

                facing = Direction.UP;
            } else {

                facing = player.getHorizontalFacing().getOpposite();
            }

            TileEngine tile = (TileEngine) world.getTileEntity(pos);
            if (tile != null) {
                tile.setOrientation(facing);
            }
        }
    }

    @SuppressWarnings("cast")
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction efacing, float hitX, float hitY, float hitZ) {
        if (!player.inventory.getCurrentItem().isEmpty()) {
            Item item = player.inventory.getCurrentItem().getItem();
            if (item == BPItems.screwdriver) {
                return true;
            }
        }

        return super.onBlockActivated(world, pos, state, player, hand, efacing, hitX, hitY, hitZ);
    }


    /**
     * Method to be overwritten that returns a GUI ID
     *
     * @return
     */
    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.INVALID;
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockAccess world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

}
