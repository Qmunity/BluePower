package com.bluepowermod.part.wire.redstone.propagation;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class BundledConductorWrapper extends BundledDeviceWrapper implements IRedstoneConductor {

    private IBundledConductor cond;

    protected BundledConductorWrapper(IBundledConductor dev, MinecraftColor color) {

        super(dev, color);
        cond = dev;
    }

    @Override
    public boolean hasLoss() {

        return cond.hasLoss();
    }

    @Override
    public boolean isAnalog() {

        return cond.isAnalog();
    }

    @Override
    public List<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        List<Pair<IRedstoneDevice, ForgeDirection>> list = super.propagate(fromSide);

        if (cond instanceof IRedstoneDevice) {
            MinecraftColor color = ((IRedstoneDevice) cond).getInsulationColor(fromSide);
            if (color != null && !color.matches(getInsulationColor(fromSide)))
                return list;
        }

        for (Pair<IBundledDevice, ForgeDirection> pair : cond.propagateBundled(fromSide))
            list.add(new Pair<IRedstoneDevice, ForgeDirection>(BundledDeviceWrapper.getWrapper(pair.getKey(),
                    getInsulationColor(pair.getValue())), pair.getValue()));

        return list;
    }
}
