package com.bluepowermod.redstone;

import com.bluepowermod.api.wire.redstone.CapabilityRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneProvider;
import com.bluepowermod.tile.TileBPMultipart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class RedstoneProviderMultipart implements IRedstoneProvider {
    @Override
    public IRedstoneDevice getRedstoneDeviceAt(Level world, BlockPos pos, Direction face, Direction side) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof TileBPMultipart multipart){
            if (face != null){
                BlockState partState = multipart.getStateByFacing(face);
                if (partState != null){
                    BlockEntity partTE = multipart.getTileForState(partState);
                    if (partTE != null){
                        if (partTE.getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, side).isPresent()){
                            return partTE.getCapability(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, side).map(r -> r).get();
                        }
                        if (partTE.getCapability(CapabilityRedstoneDevice.INSULATED_CAPABILITY, side).isPresent()){
                            return partTE.getCapability(CapabilityRedstoneDevice.INSULATED_CAPABILITY, side).map(r -> r).get();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceAt(Level world, BlockPos pos, Direction face, Direction side) {
        return null;
    }
}
