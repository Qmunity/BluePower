package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.wire.redstone.*;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.block.machine.BlockInsulatedAlloyWire;
import com.bluepowermod.tile.BPTileEntityType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileWire extends TileEntity {
    private final InsulatedRedstoneDevice device;
    private LazyOptional<IInsulatedRedstoneDevice> redstoneCap;

    public TileWire(MinecraftColor color) {
        super(BPTileEntityType.WIRE);
        this.device = new InsulatedRedstoneDevice(color);
    }

    public TileWire() {
        super(BPTileEntityType.WIRE);
        this.device = new InsulatedRedstoneDevice(MinecraftColor.NONE);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        if(compound.contains("device")) {
            INBT nbtstorage = compound.get("device");
            CapabilityRedstoneDevice.INSULATED_CAPABILITY.getStorage().readNBT(CapabilityRedstoneDevice.INSULATED_CAPABILITY, device, null, nbtstorage);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tCompound) {
        INBT nbtstorage = CapabilityRedstoneDevice.INSULATED_CAPABILITY.getStorage().writeNBT(CapabilityRedstoneDevice.INSULATED_CAPABILITY, device, null);
        tCompound.put("device", nbtstorage);
        return super.write(tCompound);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        List<Direction> directions = new ArrayList<>(BlockBPCableBase.FACING.getAllowedValues());
        if(world != null) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockAlloyWire) {

                //Remove upward connections
                directions.remove(state.get(BlockAlloyWire.FACING));

                //Make sure the cable is on the same side of the block
                directions.removeIf(d -> world.getBlockState(pos.offset(d)).getBlock() instanceof BlockAlloyWire
                        && world.getBlockState(pos.offset(d)).get(BlockAlloyWire.FACING) != state.get(BlockAlloyWire.FACING));


                //Make sure the cable is the same color or none
                if(device.getInsulationColor(null) != MinecraftColor.NONE)
                directions.removeIf(d -> {
                    TileEntity tile = world.getTileEntity(pos.offset(d));
                    return tile instanceof TileWire
                            && !(((TileWire) tile).device.getInsulationColor(d) == device.getInsulationColor(d.getOpposite())
                                || ((TileWire) tile).device.getInsulationColor(d) == MinecraftColor.NONE);
                });
            }
        }

        if(cap == CapabilityRedstoneDevice.INSULATED_CAPABILITY && (side == null || directions.contains(side))){
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
