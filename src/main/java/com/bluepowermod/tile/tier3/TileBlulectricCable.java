package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.power.BlutricityStorage;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.block.power.BlockBlulectricCable;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.tile.TileMachineBase;
import mcmultipart.api.multipart.IMultipartTile;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */

public class TileBlulectricCable extends TileMachineBase implements ITickable, IMultipartTile {
    private final BlutricityStorage storage = new BlutricityStorage(100, 100);
    private LazyOptional<IPowerBase> blutricityCap;

    @Override
    public void update() {
        if (world != null && !world.isRemote) {
            storage.resetCurrent();
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockBlulectricCable) {
                List<EnumFacing> directions = new ArrayList<>(BlockBlulectricCable.FACING.getAllowedValues());

                //Check the side has capability
                directions.removeIf(d -> !getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d).isPresent());

                //Balance power of attached blulectric blocks.
                for (EnumFacing facing : directions) {
                    Block fBlock = world.getBlockState(pos.offset(facing)).getBlock();
                    if (fBlock != Blocks.AIR && fBlock != Blocks.WATER) {
                        TileEntity tile = world.getTileEntity(pos.offset(facing));
                        if (tile != null)
                            tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                                    exStorage -> EnergyHelper.balancePower(exStorage, storage)
                            );
                    } else {
                        TileEntity tile = world.getTileEntity(pos.offset(facing).offset(state.getValue(BlockBlulectricCable.FACING).getOpposite()));
                        if (tile instanceof TileBlulectricCable)
                            tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, state.getValue(BlockBlulectricCable.FACING)).ifPresent(
                                    exStorage -> EnergyHelper.balancePower(exStorage, storage)
                            );
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        List<EnumFacing> directions = new ArrayList<>(BlockBlulectricCable.FACING.getAllowedValues());
        if(world != null) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockBlulectricCable) {

                //Remove upward connections
                directions.remove(state.getValue(BlockBlulectricCable.FACING));

                //Make sure the cable is on the same side of the block
                directions.removeIf(d -> world.getBlockState(pos.offset(d)).getBlock() instanceof BlockBlulectricCable
                        && world.getBlockState(pos.offset(d)).getValue(BlockBlulectricCable.FACING) != state.getValue(BlockBlulectricCable.FACING));
            }
        }
        if(capability == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
            return CapabilityBlutricity.BLUTRICITY_CAPABILITY.cast(storage);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    protected void invalidateCaps(){
        super.invalidateCaps();
        if( blutricityCap != null )
        {
            blutricityCap.invalidate();
            blutricityCap = null;
        }
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {
        super.readFromPacketNBT(tCompound);
        if(tCompound.hasKey("energy")) {
            NBTBase nbtstorage = tCompound.getTag("energy");
            CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
    }

    @Override
    protected void writeToPacketNBT(NBTTagCompound tCompound) {
        super.writeToPacketNBT(tCompound);
        NBTBase nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
        tCompound.setTag("energy", nbtstorage);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }
}
