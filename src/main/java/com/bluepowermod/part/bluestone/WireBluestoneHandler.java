package com.bluepowermod.part.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.DefaultBluestoneHandler;
import com.bluepowermod.api.bluestone.DummyBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneHandler;
import com.qmunity.lib.helper.RedstoneHelper;
import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.part.compat.MultipartSystem;
import com.qmunity.lib.vec.Vec3i;

public class WireBluestoneHandler extends DefaultBluestoneHandler {

    private WireBluestone parent;

    public WireBluestoneHandler(WireBluestone parent, BluestoneColor color, int conductionMap) {

        super(parent, color, conductionMap);
        this.parent = parent;
    }

    @Override
    public int getInput(ForgeDirection side) {

        if (side == parent.getFace().getOpposite()) {
            for (MultipartSystem s : MultipartSystem.getAvailableSystems()) {
                IMultipartCompat compat = s.getCompat();
                Vec3i location = new Vec3i(parent);
                if (compat.isMultipart(parent.getWorld(), location))
                    return compat.getWeakRedstoneOuput(parent.getWorld(), location, parent.getFace(), ForgeDirection.UNKNOWN);
            }
            return 0;
        }

        IBluestoneHandler h = getConnectedHandler(side);
        if (side == parent.getFace() || (h != null && h.getDevice() instanceof DummyBluestoneDevice))
            return RedstoneHelper.getInput(parent.getWorld(), parent.getX(), parent.getY(), parent.getZ(), side, parent.getFace());

        return 0;
    }

}
