package com.bluepowermod.part.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.qmunity.lib.helper.RedstoneHelper;

public class WireBluestoneHandler extends DefaultBluestoneHandler {

    private WireBluestone parent;

    public WireBluestoneHandler(WireBluestone parent, BluestoneColor color, int conductionMap) {

        super(parent, color, conductionMap);
        this.parent = parent;
    }

    @Override
    public int getInput(ForgeDirection side) {

        return RedstoneHelper.getInput(parent.getWorld(), parent.getX(), parent.getY(), parent.getZ(), side, parent.getFace());
    }

}
