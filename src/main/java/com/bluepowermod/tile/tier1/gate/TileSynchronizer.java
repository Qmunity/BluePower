package com.bluepowermod.tile.tier1.gate;

import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class TileSynchronizer extends TileGate {
    boolean leftChipPowered = false;
    boolean rightChipPowered = false;
    public TileSynchronizer(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.SYNCHRONIZER.get(), pos, state);
    }

    public boolean isLeftChipPowered() {
        return leftChipPowered;
    }

    public boolean isRightChipPowered() {
        return rightChipPowered;
    }

    public void setLeftChipPowered(boolean leftChipPowered) {
        this.leftChipPowered = leftChipPowered;
        markBlockForUpdate();
    }

    public void setRightChipPowered(boolean rightChipPowered) {
        this.rightChipPowered = rightChipPowered;
        markBlockForUpdate();
    }

    @Override
    protected void addToRedstoneStateMap(Map<String, Object> map) {
        super.addToRedstoneStateMap(map);
        map.put("left_chip_powered", leftChipPowered);
        map.put("right_chip_powered", rightChipPowered);
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        tCompound.putBoolean("leftChipPowered", leftChipPowered);
        tCompound.putBoolean("rightChipPowered", rightChipPowered);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        leftChipPowered = tCompound.getBoolean("leftChipPowered");
        rightChipPowered = tCompound.getBoolean("rightChipPowered");
    }
}
