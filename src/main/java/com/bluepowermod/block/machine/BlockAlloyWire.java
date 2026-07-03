
package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileWire;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlockAlloyWire extends BlockBPCableBase implements IBPColoredBlock, EntityBlock {
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    final String type;

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileWire(pos, state);
    }

    @Override
    protected Capability<?> getCapability() {
        return CapabilityRedstoneDevice.UNINSULATED_CAPABILITY;
    }

    public BlockAlloyWire(String type) {
        super(1,2F);
        this.type = type;
        this.registerDefaultState(super.defaultBlockState().setValue(POWERED, false));
    }

    public BlockAlloyWire(String type, float width, float height) {
        super(width, height);
        this.type = type;
        this.registerDefaultState(super.defaultBlockState().setValue(POWERED, false));
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {

        return MathHelper.map(((TileWire)pLevel.getBlockEntity(pPos)).getOutputtingRedstone() & 0xFF, 0, 255, 0, 15);
    }

    @Override
    protected boolean canConnect(Level world, BlockPos pos, BlockState state, BlockEntity tileEntity, Direction direction) {
        if(state.getBlock().canConnectRedstone(state, world, pos, direction))
            return true;
        return super.canConnect(world, pos, state, tileEntity, direction);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean bool) {
        super.neighborChanged(state, world, pos, blockIn, fromPos, bool);
        int redstoneValue = world.getBestNeighborSignal(pos);
        world.getBlockEntity(pos).getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY).orElse(null).setRedstonePower(null, (byte)redstoneValue);
        world.setBlock(pos, state.setValue(POWERED, redstoneValue > 0), 2);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING, POWERED, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, JOIN_FRONT, JOIN_BACK, JOIN_LEFT, JOIN_RIGHT, WATERLOGGED);
    }

    @Override
    public int getColor(BlockState state, BlockGetter w, BlockPos pos, int tint) {
        return RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex();
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex();
    }

}