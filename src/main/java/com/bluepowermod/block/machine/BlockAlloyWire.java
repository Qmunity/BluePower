
package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier1.TileWire;
import com.bluepowermod.util.MultipartUtils;
import com.bluepowermod.util.WireHelper;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BlockAlloyWire extends BlockBPCableBase implements IBPColoredBlock, EntityBlock {
    final RedwireType type;

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileWire(pos, state);
    }

    @Override
    protected Capability<?> getCapability() {
        return CapabilityRedstoneDevice.UNINSULATED_CAPABILITY;
    }

    public BlockAlloyWire(RedwireType type) {
        super(1,2F);
        this.type = type;
    }

    public BlockAlloyWire(RedwireType type, float width, float height) {
        super(width, height);
        this.type = type;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return getSignal(state, level, pos, direction);
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockEntity ownTile = pLevel.getBlockEntity(pPos);
        if (ownTile instanceof TileBPMultipart multipart){
            ownTile = multipart.getTileForState(pState);
        }
        return ownTile == null ? 0 : ownTile.getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, pDirection.getOpposite()).map(r -> r.getVanillaRedstonePower(pDirection.getOpposite()) & 0xFF).orElse(0);
    }

    @Override
    protected boolean canConnect(Level world, BlockEntity ownTile, BlockPos neighborPos, BlockState neighborState, BlockEntity neighborTileEntity, Direction direction) {
        if (neighborState.getBlock() instanceof BlockGateBase){
            Direction facing = neighborState.getValue(BlockStateProperties.FACING);
            return facing == ownTile.getBlockState().getValue(BlockStateProperties.FACING) || (neighborPos.equals(ownTile.getBlockPos()) && facing == direction);
        }
        if(neighborState.getBlock().canConnectRedstone(neighborState, world, neighborPos, direction))
            return true;
        return super.canConnect(world, ownTile, neighborPos, neighborState, neighborTileEntity, direction);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @org.jetbrains.annotations.Nullable Direction direction) {
        return true; //TODO proper checks
    }

    @Override
    protected boolean isNeighborStateEquivalent(BlockState state, BlockEntity be, BlockState neighborState, BlockEntity neighborBE) {
        if (neighborState.getBlock() instanceof BlockAlloyWire wire){
            if (wire.type.equals(type)) return true;
        }
        return super.isNeighborStateEquivalent(state, be, neighborState, neighborBE);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (state != oldState && state.is(oldState.getBlock())){
            BlockEntity be = level.getBlockEntity(pos);
            BlockEntity wire = be instanceof TileBPMultipart multipart ? multipart.getTileForState(state) : be;
            if (wire instanceof TileWire wire1) {
                wire1.setBlockState(state);
                wire1.onBlockUpdate();
            }
        }
    }

    @Override
    protected BlockState updateState(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean movedByPiston) {
        BlockState oldState = state;
        state = super.updateState(state, level, pos, blockIn, fromPos, movedByPiston);
/*
        int redstoneLevel = 0;
        for (Direction direction : Direction.values()){
            int j = MultipartUtils.getRedstonePower(direction, state.getValue(FACING), level, pos);
            if (j >= 15){
                redstoneLevel = 15;
                break;
            }
            if (j > redstoneLevel) redstoneLevel = j;
        }
        int redstoneValue = redstoneLevel;
*/
        if (oldState != state) return state; //returning since the redstone update code will be run from onPlace anyways
        BlockEntity be = level.getBlockEntity(pos);
        BlockEntity wire = be instanceof TileBPMultipart multipart ? multipart.getTileForState(state) : be;
        if (wire == null) return state;
        //wire.getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY).ifPresent(r -> r.setRedstonePower(null, (byte) (redstoneValue * 17)));
        if (wire instanceof TileWire wire1) {
            wire1.onBlockUpdate();
        }
        return state;
    }

    @Override
    public int getColor(BlockState state, BlockGetter w, BlockPos pos, int tint) {
        BlockEntity be = w.getBlockEntity(pos);
        if (be instanceof TileBPMultipart multipart){
            be = multipart.getTileForState(state);
        }
        int power = 0;
        if (be != null){
           power = be.getCapability(getCapability()).map(r -> (int)((IRedstoneDevice)r).getRedstonePower(null)).orElse(0);
        }
        return WireHelper.getColorForPowerLevel(type, (byte) power);
    }

    @Override
    public int getColor(ItemStack stack, int tint) {
        return type.getMinColor();
    }

    public RedwireType getType() {
        return type;
    }
}