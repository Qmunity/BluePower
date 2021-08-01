package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.tile.BPBlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class TileInsulatedWire extends TileWire {
    private MinecraftColor color = MinecraftColor.ANY;

    public TileInsulatedWire() {
        super(BPBlockEntityType.INSULATEDWIRE);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag compound) {
        super.readFromPacketNBT(compound);
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
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