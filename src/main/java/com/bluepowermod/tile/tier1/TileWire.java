package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.wire.redstone.*;
import com.bluepowermod.block.BlockBPCableBase;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileWire extends TileBase {
    private final IRedstoneDevice device = new RedstoneStorage(level, worldPosition, RedwireType.RED_ALLOY);
    @Nullable

    public static final ModelProperty<Pair<Integer, Integer>> COLOR_INFO = new ModelProperty<>();
    public static final ModelProperty<Boolean> LIGHT_INFO = new ModelProperty<>();

    public TileWire(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.WIRE.get(), pos, state);
    }

    public TileWire(BlockEntityType type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public ModelData getModelData(BlockState state) {

            //Add Color and Light Data
            Pair<Integer, Integer> colorData = Pair.of(((IBPColoredBlock)state.getBlock()).getColor(state, level, worldPosition, -1), ((IBPColoredBlock)state.getBlock()).getColor(state, level, worldPosition, 2));
            Boolean lightData = state.getValue(BlockAlloyWire.POWERED);

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

}