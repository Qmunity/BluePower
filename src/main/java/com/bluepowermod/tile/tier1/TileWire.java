package com.bluepowermod.tile.tier1;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.multipart.IBPMultipartTile;
import com.bluepowermod.api.multipart.IBPPartTile;
import com.bluepowermod.api.wire.redstone.*;
import com.bluepowermod.block.BlockBPCableBase;
import com.bluepowermod.block.BlockBPCableBase.ConnectionType;
import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileWire extends TileBase implements IRedwire, IBPPartTile {
    private final IRedstoneDevice device;
    IBPMultipartTile multipart = null;
    @Nullable
    private BlockState cachedBlockState;
    private LazyOptional<IRedstoneDevice> redstoneCap;

    public static final ModelProperty<Pair<Integer, Integer>> COLOR_INFO = new ModelProperty<>();
    public static final ModelProperty<Boolean> LIGHT_INFO = new ModelProperty<>();

    public TileWire(BlockPos pos, BlockState state) {
        this(BPBlockEntityType.WIRE.get(), pos, state);
    }

    public TileWire(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        device = new RedstoneStorage(this, state.getValue(BlockBPCableBase.FACING));
    }

    public void onBlockUpdate(){
        this.device.onRedstoneUpdate();
    }


    public @NotNull ModelData getModelData(){
        Boolean lightData = (device.getRedstonePower(null) & 0xFF) > 0;
        return ModelData.builder().with(LIGHT_INFO, lightData).build();
    }

    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ModelData getModelData(BlockState state) {

            //Add Color and Light Data
            Pair<Integer, Integer> colorData = Pair.of(((IBPColoredBlock)state.getBlock()).getColor(state, level, worldPosition, -1), ((IBPColoredBlock)state.getBlock()).getColor(state, level, worldPosition, 2));
            Boolean lightData = (device.getRedstonePower(null) & 0xFF) > 0;

            return ModelData.builder().with(COLOR_INFO, colorData).with(LIGHT_INFO, lightData).build();

    }

    @Override
    protected void readFromPacketNBT(CompoundTag compound) {
        super.readFromPacketNBT(compound);
        if(compound.contains("device")) {
            Tag nbtstorage = compound.get("device");
            IRedstoneDevice.readNBT(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null, nbtstorage);
        }
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        Tag nbtstorage = IRedstoneDevice.writeNBT(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null);
        tCompound.put("device", nbtstorage);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        List<Direction> directions = new ArrayList<>(BlockBPCableBase.FACING.getPossibleValues());
        if(level != null) {
            BlockState state = getBlockState();
            if (state.getBlock() instanceof BlockAlloyWire) {

                //Remove upward connections
                directions.remove(state.getValue(BlockAlloyWire.FACING));

                //Make sure the cable is on the same side of the block
                directions.removeIf(d -> !isConnected(d));


                //Make sure the cable is the same color or none
                //if(device.getInsulationColor(null) != MinecraftColor.NONE)
                    //directions.removeIf(d -> {
                        //BlockEntity tile = world.getBlockEntity(worldPosition.relative(d));
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
    public void invalidateCaps(){
        super.invalidateCaps();
        if( redstoneCap != null )
        {
            redstoneCap.invalidate();
            redstoneCap = null;
        }
    }

    public boolean isConnected(Direction direction){
        Direction[] sides = BlockBPCableBase.directionsFromFacing(getBlockState().getValue(BlockBPCableBase.FACING));
        for (int i = 0; i < 4; i++){
            Direction side = sides[i];
            var property = switch (i){
                case 0 -> BlockBPCableBase.CONNECTION_TYPE_LEFT;
                case 1 -> BlockBPCableBase.CONNECTION_TYPE_RIGHT;
                case 2 -> BlockBPCableBase.CONNECTION_TYPE_FRONT;
                default -> BlockBPCableBase.CONNECTION_TYPE_BACK;
            };
            if (side == direction){
                return getBlockState().getValue(property) != ConnectionType.NONE;
            }
        }
        return false;
    }

    @Override
    public RedwireType getRedwireType(Direction side) {
        return ((BlockAlloyWire)getBlockState().getBlock()).getType();
    }

    @Override
    public boolean canReceivePower(Direction side) {
        return isConnected(side) || getBlockState().getValue(BlockBPCableBase.FACING) == side.getOpposite();
    }

    @Override
    public boolean canOutputPower(Direction side) {
        return canReceivePower(side);
    }

    @Override
    public void setMultipartTile(IBPMultipartTile multipart) {
        this.multipart = multipart;
    }

    @Override
    public IBPMultipartTile getMultipart() {
        return multipart;
    }
}