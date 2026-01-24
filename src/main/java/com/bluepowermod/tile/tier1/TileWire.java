package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.wire.redstone.*;
import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        IRedstoneDevice.loadValue(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null, input);
    }

    @Override
    protected void saveAdditional(ValueOutput valueOutput) {
        super.saveAdditional(valueOutput);
        IRedstoneDevice.storeValue(CapabilityRedstoneDevice.UNINSULATED_CAPABILITY, device, null, valueOutput);
    }

}