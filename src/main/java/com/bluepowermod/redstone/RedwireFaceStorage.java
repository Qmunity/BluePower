package com.bluepowermod.redstone;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedstoneStorage;
import com.bluepowermod.helper.MathHelper;
import net.minecraft.core.Direction;

public class RedwireFaceStorage extends RedstoneStorage implements IRedstoneConductor, IFace {
    IRedwire wire;
    public RedwireFaceStorage(IRedwire wire) {
        super(wire);
        this.wire = wire;
    }

    @Override
    public byte getVanillaRedstonePower(Direction side) {
        if (side != null && !wire.canOutputPower(side)) return 0;
        return (byte) MathHelper.map(getRedstonePower(side) & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public boolean hasLoss(Direction side) {
        return wire.getRedwireType(side).hasLoss();
    }

    @Override
    public boolean isAnalogue(Direction side) {
        return wire.getRedwireType(side).isAnalogue();
    }

    @Override
    public Direction getFace() {
        return wire instanceof IFace face ? face.getFace() : null;
    }

    @Override
    public boolean canPropagateFrom(Direction fromSide) {
        return true;
    }
}
