package com.bluepowermod.part.cable.bluepower;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.part.cable.CableWall;
import com.bluepowermod.api.bluepower.IBluePowered;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Koen Beckers (K4Unl);
 */

public class WireBluePower extends CableWall{

    @Override
    public boolean canConnectToCable(CableWall cable) {

        return cable instanceof WireBluePower;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        return false;
    }

    @Override
    public boolean canConnectToTileEntity(TileEntity tile) {

        return tile instanceof IBluePowered;
    }

    @Override
    public String getType() {

        return "bluepowerWire";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluepowerWire";
    }
}
