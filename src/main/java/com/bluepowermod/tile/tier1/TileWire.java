package com.bluepowermod.tile.tier1;

import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public class TileWire extends TileBase {

    public TileWire() {
        super(BPTileEntityType.WIRE);
    }

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Override
    public void onBlockNeighbourChanged() {
        for(Direction face : Direction.values()) {
            if (world.getBlockState(pos.offset(face)).getWeakPower(world, pos.offset(face), face.getOpposite()) != 0) {
                this.setOutputtingRedstone(true);
            }
        }
    }
}
