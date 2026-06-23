package com.bluepowermod.tile.tier1;

import com.bluepowermod.block.gates.BlockGateBase.Side;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class TileGate extends TileBase {
    EnumMap<Side, Boolean> poweredSides = new EnumMap<>(Map.of(Side.FRONT, false, Side.BACK, false, Side.LEFT, false, Side.RIGHT, false));
    EnumMap<Side, Boolean> disabledSides = new EnumMap<>(Map.of(Side.FRONT, false, Side.BACK, false, Side.LEFT, false, Side.RIGHT, false));
    @OnlyIn(Dist.CLIENT)
    public static final ModelProperty<Map<String, Object>> REDSTONE_STATES = new ModelProperty<>();
    public TileGate(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.GATE.get(), pos, state);
    }

    public TileGate(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull ModelData getModelData() {
        Map<String, Object> map = new HashMap<>();
        addToRedstoneStateMap(map);
        return ModelData.builder().with(REDSTONE_STATES, map).build();
    }

    @OnlyIn(Dist.CLIENT)
    protected void addToRedstoneStateMap(Map<String, Object> map){
        map.put("powered_back", poweredSides.get(Side.BACK));
        map.put("powered_front", poweredSides.get(Side.FRONT));
        map.put("powered_left", poweredSides.get(Side.LEFT));
        map.put("powered_right", poweredSides.get(Side.RIGHT));
        map.put("disabled_back", disabledSides.get(Side.BACK));
        map.put("disabled_front", disabledSides.get(Side.FRONT));
        map.put("disabled_left", disabledSides.get(Side.LEFT));
        map.put("disabled_right", disabledSides.get(Side.RIGHT));
    }

    public void setPowered(Side side, boolean powered){
        poweredSides.put(side, powered);
        markBlockForUpdate();
    }

    public void setDisabled(Side side, boolean disabled){
        disabledSides.put(side, disabled);
        markBlockForUpdate();
    }

    public boolean updateStates(Map<Side, Byte> map, boolean simulate){
        if (map.isEmpty()) return false;
        EnumMap<Side, Boolean> oldPoweredStates = new EnumMap<>(poweredSides);
        EnumMap<Side, Boolean> newPoweredStates;
        if (!simulate){
            newPoweredStates = poweredSides;
        } else {
            newPoweredStates = new EnumMap<>(Side.class);
        }
        newPoweredStates.putAll(map.entrySet().stream().collect(Collectors.toMap(Entry::getKey, v -> v.getValue() > 0)));
        boolean changed = !oldPoweredStates.equals(newPoweredStates);
        if (changed && !simulate) markBlockForUpdate();
        return changed;
    }

    public boolean isPowered(Side side){
        return poweredSides.getOrDefault(side, false);
    }

    public boolean isDisabled(Side side){
        return disabledSides.getOrDefault(side, false);
    }

    public int redstoneFromSide(Side side){
        return isPowered(side) && !isDisabled(side) ? 16 : 0;
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        CompoundTag poweredSides = new CompoundTag();
        this.poweredSides.forEach((s, b) -> {
            poweredSides.putBoolean(s.name().toLowerCase(Locale.ROOT), b);
        });
        tCompound.put("poweredSides", poweredSides);
        CompoundTag disabledSides = new CompoundTag();
        this.disabledSides.forEach((s, b) -> {
            disabledSides.putBoolean(s.name().toLowerCase(Locale.ROOT), b);
        });
        tCompound.put("disabledSides", disabledSides);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        if (tCompound.contains("poweredSides")){
            CompoundTag poweredSides = tCompound.getCompound("poweredSides");
            for (Side side : Side.values()){
                this.poweredSides.put(side, poweredSides.getBoolean(side.name().toLowerCase(Locale.ROOT)));
            }
        }
        if (tCompound.contains("disabledSides")){
            CompoundTag disabledSides = tCompound.getCompound("disabledSides");
            for (Side side : Side.values()){
                this.disabledSides.put(side, disabledSides.getBoolean(side.name().toLowerCase(Locale.ROOT)));
            }
        }
    }
}
