package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.wire.redstone.*;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

class RedstoneDevice implements IRedstoneDevice {

    private byte power = 0;

    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {
        return true;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {
        return null;
    }

    @Override
    public byte getRedstonePower(Direction side) {
        return power;
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        this.power = power;
    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public boolean isNormalFace(Direction side) {
        return false;
    }

}

public class TileWire extends TileMachineBase {
    private final IRedstoneDevice device = new RedstoneDevice();
    private LazyOptional<IRedstoneDevice> redstoneCap;


    //public TileWire(MinecraftColor color) {
        //super(BPTileEntityType.WIRE);
        //this.device = UNINSULATED_CAPABILITY.getDefaultInstance();
    //}


    public TileWire() {
        super(BPTileEntityType.WIRE);
    }

    @Override
    protected void readFromPacketNBT(CompoundNBT compound) {
        super.readFromPacketNBT(compound);
        if(compound.contains("device")) {
            INBT nbtstorage = compound.get("device");
            CapabilityRedstoneDevice.UNINSULATED_CAPABILITY.getStorage().readNBT(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null, nbtstorage);
        }
    }

    @Override
    protected void writeToPacketNBT(CompoundNBT tCompound) {
        super.writeToPacketNBT(tCompound);
        INBT nbtstorage = CapabilityRedstoneDevice.UNINSULATED_CAPABILITY.getStorage().writeNBT(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null);
        tCompound.put("device", nbtstorage);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        List<Direction> directions = new ArrayList<>(BlockBPCableBase.FACING.getAllowedValues());
        if(world != null) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockAlloyWire) {

                //Remove upward connections
                directions.remove(state.get(BlockAlloyWire.FACING));

                //Make sure the cable is on the same side of the block
                directions.removeIf(d -> world.getBlockState(pos.offset(d)).getBlock() instanceof BlockAlloyWire
                        && world.getBlockState(pos.offset(d)).get(BlockAlloyWire.FACING) != state.get(BlockAlloyWire.FACING));


                //Make sure the cable is the same color or none
                //if(device.getInsulationColor(null) != MinecraftColor.NONE)
                    //directions.removeIf(d -> {
                        //TileEntity tile = world.getTileEntity(pos.offset(d));
                        //return tile instanceof TileWire
                                //&& !(((TileWire) tile).device.getInsulationColor(d) == device.getInsulationColor(d.getOpposite())
                                //|| ((TileWire) tile).device.getInsulationColor(d) == MinecraftColor.NONE);
                    //});
            }
        }

        if(cap == CapabilityRedstoneDevice.UNINSULATED_CAPABILITY && (side == null || directions.contains(side))){
            if( redstoneCap == null ) redstoneCap = LazyOptional.of( () -> device );
            return redstoneCap.cast();
        }

        return LazyOptional.empty();
    }

    @Override
    protected void invalidateCaps(){
        super.invalidateCaps();
        if( redstoneCap != null )
        {
            redstoneCap.invalidate();
            redstoneCap = null;
        }
    }


}