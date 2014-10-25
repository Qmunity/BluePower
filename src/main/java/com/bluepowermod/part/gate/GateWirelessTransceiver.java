package com.bluepowermod.part.gate;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneHandler;
import com.bluepowermod.part.bluestone.BluestoneApi;
import com.bluepowermod.part.bluestone.DefaultBluestoneHandler;
import com.qmunity.lib.misc.ForgeDirectionUtils;
import com.qmunity.lib.vec.Vec3i;

public class GateWirelessTransceiver extends GateBase implements IBluestoneDevice {

    private static List<GateWirelessTransceiver> transceivers = new ArrayList<GateWirelessTransceiver>();

    private List<IBluestoneHandler> handlers = new ArrayList<IBluestoneHandler>();

    public GateWirelessTransceiver() {

        handlers.add(new TransceiverBluestoneHandler(this, BluestoneColor.NONE, 0x000000));
    }

    @Override
    public void initializeConnections() {

    }

    @Override
    public String getId() {

        return "wirelesstransceiver";
    }

    @Override
    protected void renderTop(float frame) {

    }

    @Override
    public void doLogic() {

    }

    @Override
    public void onAdded() {

        super.onAdded();

        if (!transceivers.contains(this))
            transceivers.add(this);
    }

    @Override
    public void onRemoved() {

        super.onRemoved();

        transceivers.remove(this);
    }

    @Override
    public void onLoaded() {

        super.onLoaded();

        if (!transceivers.contains(this))
            transceivers.add(this);
    }

    @Override
    public void onUnloaded() {

        super.onUnloaded();

        transceivers.remove(this);
    }

    private static class TransceiverBluestoneHandler extends DefaultBluestoneHandler {

        private GateWirelessTransceiver parent;

        public TransceiverBluestoneHandler(GateWirelessTransceiver parent, BluestoneColor color, int conductionMap) {

            super(parent, color, conductionMap);
            this.parent = parent;
        }

        @Override
        public IBluestoneHandler getConnectedHandler(ForgeDirection side) {

            IBluestoneHandler t = null;

            for (GateWirelessTransceiver tr : transceivers) {
                if (tr == parent)
                    continue;
                t = tr.getHandlers().get(0);
                break;
            }

            if (side == ForgeDirection.DOWN)
                return t;

            return super.getConnectedHandler(side);
        }
    }

    @Override
    public BluestoneColor getBundleColor() {

        return BluestoneColor.INVALID;
    }

    @Override
    public List<IBluestoneHandler> getHandlers() {

        return handlers;
    }

    @Override
    public IBluestoneDevice getNeighbor(ForgeDirection side) {

        Vec3i loc = new Vec3i(this).add(ForgeDirectionUtils.getOnFace(getFace(), side));
        return BluestoneApi.getInstance().getDevice(getWorld(), loc.getX(), loc.getY(), loc.getZ(), getFace(), true);
    }

}
