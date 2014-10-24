package com.bluepowermod.part.bluestone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneConnectionType;
import com.bluepowermod.api.bluestone.ConductionMapHelper;
import com.bluepowermod.api.bluestone.IBluestoneConductor;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.part.BPPartFace;

public class PartBluestone extends BPPartFace implements IBluestoneConductor {

    @Override
    public String getType() {

        return "bluestone";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestone";
    }

    @Override
    public IBluestoneDevice getConnectedDevice(ForgeDirection side, BluestoneConnectionType type) {

        return null;
    }

    @Override
    public void onPowerUpdate(BluestoneConnectionType changed, int oldValue, int newValue) {

    }

    @Override
    public void propagate(List<IBluestoneDevice> visited, BluestoneConnectionType type, ForgeDirection from, int oldValue, int newValue) {

        visited.add(this);

        int map = getConductionMap(type);
        int net = ConductionMapHelper.getNetwork(map, from);

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == getFace() || side == getFace().getOpposite() || side == from)
                continue;
            if (net != ConductionMapHelper.getNetwork(map, side))
                continue;

            IBluestoneDevice dev = getConnectedDevice(side, type);
            if (dev != null)
                dev.propagate(visited, type, side.getOpposite(), oldValue, newValue);
        }
    }

    @Override
    public int getConductionMap(BluestoneConnectionType connection) {

        return 0x012222;
    }

}
