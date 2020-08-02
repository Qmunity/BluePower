package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.*;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.TileBase;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileWire extends TileBase {
    private IRedstoneDevice device;
    @Nullable
    private BlockState cachedBlockState;
    private LazyOptional<IRedstoneDevice> redstoneCap;

    public static final ModelProperty<Pair<Integer, Integer>> COLOR_INFO = new ModelProperty<>();
    public static final ModelProperty<Boolean> LIGHT_INFO = new ModelProperty<>();

    public TileWire() {
        super(BPTileEntityType.WIRE);
    }

    public TileWire(TileEntityType type) {
        super(type);
    }


    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public IModelData getModelData(BlockState state) {

            //Add Color and Light Data
            Pair<Integer, Integer> colorData = Pair.of(((IBPColoredBlock)state.getBlock()).getColor(state, world, pos, -1), ((IBPColoredBlock)state.getBlock()).getColor(state, world, pos, 2));
            Boolean lightData = state.get(BlockAlloyWire.POWERED);

            return new ModelDataMap.Builder().withInitial(COLOR_INFO, colorData).withInitial(LIGHT_INFO, lightData).build();

    }

    @Override
    protected void readFromPacketNBT(CompoundNBT compound) {
        super.readFromPacketNBT(compound);
        //if(compound.contains("device")) {
        //    INBT nbtstorage = compound.get("device");
        //    CapabilityRedstoneDevice.UNINSULATED_CAPABILITY.getStorage().readNBT(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null, nbtstorage);
        //}
    }

    @Override
    protected void writeToPacketNBT(CompoundNBT tCompound) {
        super.writeToPacketNBT(tCompound);
        //INBT nbtstorage = CapabilityRedstoneDevice.UNINSULATED_CAPABILITY.getStorage().writeNBT(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null);
        //tCompound.put("device", nbtstorage);
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