package com.bluepowermod.tile.tier1;

import com.bluepowermod.block.gates.BlockGateBase.Side;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static com.bluepowermod.block.gates.BlockGateBase.toDirection;

public class TileRepeater extends TileGate{
    int delay = 1;
    int ticksRemaining = 0;
    boolean currentUpdate = false;
    public TileRepeater(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.REPEATER.get(), pos, state);
    }

    @Override
    protected void addToRedstoneStateMap(Map<String, Object> map) {
        super.addToRedstoneStateMap(map);
        map.put("delay", delay);
    }

    public int getDelay(){
        return delay;
    }

    public void cycleDelay(){
        switch (delay){
            case 1 -> delay = 2;
            case 2 -> delay = 3;
            case 3 -> delay = 4;
            case 4, 8, 16, 32, 64, 128 -> delay *= 2;
            case 256 -> delay = 1024;
            default -> delay = 1;
        }
        markBlockForUpdate();
    }

    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean in = isPowered(Side.BACK);
        if (in != currentUpdate){
            if (in || ticksRemaining == 0){
                ticksRemaining = delay;
                currentUpdate = in;
            }
        }
        if (ticksRemaining > 0) ticksRemaining--;
        boolean frontPowered = isPowered(Side.FRONT);
        if (ticksRemaining == 0) setPowered(Side.FRONT, currentUpdate);
        if (frontPowered != isPowered(Side.FRONT)){
            level.markAndNotifyBlock(pos, level.getChunkAt(pos), state, state, 1, 512);
            Direction dir = toDirection(Side.FRONT, state);
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            level.updateNeighborsAtExceptFromFacing(neighbor, neighborState.getBlock(), dir.getOpposite());
        }
    }

    public void onBlockUpdate(){
        boolean in = MultipartUtils.getRedstonePower(Side.BACK, getBlockState(), level, getBlockPos()) > 0;
        setPowered(Side.BACK, in);
    }

    @Override
    public boolean updateStates(Map<Side, Byte> map, boolean simulate) {
        if (simulate){
            onBlockUpdate();
        }
        return false;
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        tCompound.putInt("delay", delay);
        tCompound.putInt("ticksRemaining", ticksRemaining);
        tCompound.putBoolean("currentUpdate", currentUpdate);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        delay = tCompound.contains("delay") ? tCompound.getInt("delay") : 1;
        ticksRemaining = tCompound.getInt("ticksRemaining");
        currentUpdate = tCompound.getBoolean("currentUpdate");
    }
}
