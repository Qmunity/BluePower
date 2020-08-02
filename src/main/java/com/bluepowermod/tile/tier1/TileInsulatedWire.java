package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.tile.BPTileEntityType;
import net.minecraft.nbt.CompoundNBT;

public class TileInsulatedWire extends TileWire {
    private MinecraftColor color = MinecraftColor.ANY;

    public TileInsulatedWire() {
        super(BPTileEntityType.INSULATEDWIRE);
    }

    @Override
    protected void readFromPacketNBT(CompoundNBT compound) {
        super.readFromPacketNBT(compound);
    }

    @Override
    protected void writeToPacketNBT(CompoundNBT tCompound) {
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