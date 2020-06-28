
package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileWire;
import net.minecraft.block.*;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlockAlloyWire extends BlockBPCableBase implements IBPColoredBlock{

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    static final BooleanProperty POWERED = BooleanProperty.create("powered");
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
        super(1,1);
        this.type = type;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(POWERED, false)
                .with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false)
                .with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false)
                .with(JOIN_FRONT, false).with(JOIN_BACK, false)
                .with(JOIN_LEFT, false).with(JOIN_RIGHT, false));
        setRegistryName(Refs.MODID + ":" + type + "_wire");
    }

    protected BlockAlloyWire(String type, float width, float height){
        super(width,height);
        this.type = type;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(POWERED, false)
                .with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false)
                .with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false)
                .with(JOIN_FRONT, false).with(JOIN_BACK, false)
                .with(JOIN_LEFT, false).with(JOIN_RIGHT, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, POWERED, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT);
    }

    @Override
    public int getColor(IBlockReader w, BlockPos pos, int tint) {
        return tint == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

    @Override
    public int getColor(int tint) {
        return tint == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

}