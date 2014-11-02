package com.bluepowermod.api.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

public class DefaultBluestoneHandler implements IBluestoneHandler {

    private IBluestoneDevice parent;
    private BluestoneColor color;
    private int conductionMap;

    private int[] power = new int[0xF];
    private IBluestoneHandler[] connectedHandlers = new IBluestoneHandler[6];

    public DefaultBluestoneHandler(IBluestoneDevice parent, BluestoneColor color, int conductionMap) {

        this.parent = parent;
        this.color = color;
        this.conductionMap = conductionMap;
    }

    @Override
    public IBluestoneDevice getDevice() {

        return parent;
    }

    @Override
    public BluestoneColor getInsulationColor() {

        return color;
    }

    @Override
    public int getConductionMap() {

        return conductionMap;
    }

    @Override
    public boolean canConnect(IBluestoneDevice device) {

        // // If both are bundled and share the same color (or are compatible)
        // if (getDevice().getBundleColor() != BluestoneColor.INVALID && device.getBundleColor() != BluestoneColor.INVALID
        // && getDevice().getBundleColor().canConnect(device.getBundleColor()))
        // return true;
        // // If this one is bundled and the other isn't
        // if (getDevice().getBundleColor() != BluestoneColor.INVALID && device.getBundleColor() == BluestoneColor.INVALID) {
        // boolean isColored = false;
        // for (IBluestoneHandler h : device.getHandlers())
        // if (h.getInsulationColor().isColor())
        // isColored = true;
        // if (device.getHandlers().size() > 0) {
        // if (!isColored)
        // return false;
        // } else {
        // return true;
        // }
        // }
        //
        // for (IBluestoneHandler h : device.getHandlers())
        // if (h.getInsulationColor().canConnect(getInsulationColor()))
        // return true;
        //
        // return false;
        return true;
    }

    @Override
    public IBluestoneHandler getConnectedHandler(ForgeDirection side) {

        return connectedHandlers[side.ordinal()];
    }

    private boolean shouldRefresh = true;

    @Override
    public void refreshConnections(boolean spread) {

        if (!shouldRefresh)
            return;

        connectedHandlers = new IBluestoneHandler[6];

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            IBluestoneDevice dev = getDevice().getNeighbor(d);
            if (dev == null)
                continue;

            if (!canConnect(dev))
                continue;

            for (IBluestoneHandler h : dev.getHandlers()) {
                if (h.canConnect(getDevice()))
                    connectedHandlers[d.ordinal()] = h;
            }
        }

        if (spread) {
            shouldRefresh = false;
            for (IBluestoneHandler h : connectedHandlers)
                if (h != null)
                    h.refreshConnections(false);
            shouldRefresh = true;
        }
    }

    @Override
    public void onUpdate(int network, int newValue) {

        power[network] = newValue;

        if (getDevice().getWorld() != null)
            getDevice().getWorld().markBlockRangeForRenderUpdate(getDevice().getX(), getDevice().getY(), getDevice().getZ(),
                    getDevice().getX(), getDevice().getY(), getDevice().getZ());
    }

    @Override
    public int getInput(ForgeDirection side) {

        return 0;
    }

    @Override
    public int getPower(int network) {

        return power[network];
    }

}
