package com.bluepowermod.tile.tier1;

import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class TileRepeater extends TileGate{
    int delay = 1;
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

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        tCompound.putInt("delay", delay);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        delay = tCompound.contains("delay") ? tCompound.getInt("delay") : 1;
    }
}
