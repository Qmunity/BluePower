
package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileWire;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlockAlloyWire extends BlockBPCableBase implements IBPColoredBlock{
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    final String type;

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileWire();
    }


    @Override
    protected Capability<?> getCapability() {
        return CapabilityRedstoneDevice.UNINSULATED_CAPABILITY;
    }

    public BlockAlloyWire(String type) {
        super(1,2F);
        this.type = type;
        this.registerDefaultState(super.defaultBlockState().setValue(POWERED, false));
        setRegistryName(Refs.MODID + ":" + type + "_wire");
    }

    public BlockAlloyWire(String type, float width, float height) {
        super(width, height);
        this.type = type;
        this.registerDefaultState(super.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    protected boolean canConnect(World world, BlockPos pos, BlockState state, TileEntity tileEntity, Direction direction) {
        if(state.canConnectRedstone(world, pos, direction))
            return true;
        return super.canConnect(world, pos, state, tileEntity, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, POWERED, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT, WATERLOGGED);
    }

    @Override
    public int getColor(BlockState state, IBlockReader w, BlockPos pos, int tint) {
        return RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex();
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex();
    }

}