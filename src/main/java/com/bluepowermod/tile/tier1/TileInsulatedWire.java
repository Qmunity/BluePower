package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TileInsulatedWire extends TileWire {
    private MinecraftColor color = MinecraftColor.ANY;

    public TileInsulatedWire(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.INSULATEDWIRE.get(), pos, state);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
    }

    public MinecraftColor getColor() {
        return color;
    }

    public boolean setColor(MinecraftColor color) {
        if(this.color != color){
            this.color = color;
            return true;
        }
        return false;
    }
}