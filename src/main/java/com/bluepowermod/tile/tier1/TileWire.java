package com.bluepowermod.tile.tier1;

import com.bluepowermod.tile.TileBase;
import net.minecraft.util.Direction;

public class TileWire extends TileBase {

    @Override
    public boolean canConnectRedstone() {
        return true;
    }

    @Override
    public void onBlockNeighbourChanged() {
        for(Direction face : Direction.HORIZONTALS) {
            if (world.getBlockState(pos.offset(face)).getWeakPower(world, pos.offset(face), face.getOpposite()) != 0) {
                this.setOutputtingRedstone(true);
            }
        }
    }
}
