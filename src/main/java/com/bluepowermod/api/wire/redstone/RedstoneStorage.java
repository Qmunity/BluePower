package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.helper.RedstoneHelper;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class RedstoneStorage implements IRedstoneDevice {
    private final RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    byte power = 0;
    private final Level level;
    private final BlockPos blockPos;
    private boolean hasUpdated = false;
    private RedwireType type;

    public RedstoneStorage(Level level, BlockPos blockPos, RedwireType type) {
        this.level = level;
        this.blockPos = blockPos;
        this.type = type;
    }



    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {
        if ((type == ConnectionType.STRAIGHT && side == getFace().getOpposite() && dev instanceof IFace)
                || side == null)
            return false;
        if (type == ConnectionType.CLOSED_CORNER) {
            if (side == getFace())
                return false;
            if (side == getFace().getOpposite())
                return false;
        }

        if (dev instanceof IRedwire) {
            RedwireType rwt = this.type;
            if (type == null)
                return false;
            RedwireType rwt_ = ((IRedwire) dev).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
            if (rwt_ == null)
                return false;
            if (!rwt.canConnectTo(rwt_))
                return false;
        }

        return true;
    }

    public Direction getFace() {
        return level.getBlockState(blockPos).getValue(BlockAlloyWire.FACING);
    }

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {
        return redstoneConnections;
    }

    @Override
    public byte getRedstonePower(Direction side) {
        if (!RedstoneApi.getInstance().shouldWiresOutputPower(type.hasLoss()))
            return 0;

        if (!type.isAnalogue())
            return (byte) ((power & 0xFF) > 0 ? 255 : 0);

        return power;
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        byte pow = type.hasLoss() ? power : (((power & 0xFF) > 0) ? (byte) 255 : (byte) 0);
        hasUpdated = hasUpdated | (pow != this.power);
        this.power = pow;
    }

    @Override
    public void onRedstoneUpdate() {
        if (hasUpdated) {
            level.getBlockEntity(blockPos).setChanged();

            for (Direction dir : Direction.values()) {
                IConnection<IRedstoneDevice> c = redstoneConnections.getConnectionOnSide(dir);
                IRedstoneDevice dev = null;
                if (c != null)
                    dev = c.getB();
                if (dir == getFace()) {
                    RedstoneHelper.notifyRedstoneUpdate(getLevel(), getBlockPos(), dir, true);
                } else if ((dev == null || dev instanceof DummyRedstoneDevice) && dir != getFace().getOpposite()) {
                    RedstoneHelper.notifyRedstoneUpdate(getLevel(), getBlockPos(), dir, false);
                }
            }

            hasUpdated = false;
        }
    }

    @Override
    public boolean isNormalFace(Direction side) {
        return false;
    }

    @Override
    public BlockPos getBlockPos() {
        return blockPos;
    }

    @Override
    public Level getLevel() {
        return level;
    }
}
