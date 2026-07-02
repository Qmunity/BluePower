package com.bluepowermod.tile.tier1.gate;

import com.bluepowermod.block.gates.BlockGateBase.Side;
import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

import static com.bluepowermod.block.gates.BlockGateBase.toDirection;

public class TileRepeater extends TileGate {
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

    public void cycleDelay(boolean shift){
        if (!shift) {
            switch (delay){
                case 1,2,3 -> delay ++;
                case 4, 8, 16, 32, 64, 128, 256 -> delay *= 2;
                default -> delay = 1;
            }
        } else {
            switch (delay){
                case 4,3,2 -> delay--;
                case 512, 256, 128, 64, 32, 16, 8 -> delay /= 2;
                default -> delay = 512;
            }
        }
        markBlockForUpdate();
    }

    @Override
    public void tick(Level level, BlockPos pos, BlockState state){
        if (level.isClientSide()) return;
        boolean in = isPowered(Side.BACK);
        if (in != currentUpdate){
            if (in || ticksRemaining == 0){
                ticksRemaining = delay * 2;
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

    @Override
    public boolean canActuallyTick() {
        return true;
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
