package com.bluepowermod.tile.tier1;

import com.bluepowermod.block.gates.BlockGateBase.Side;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TileGate extends TileBase {
    boolean poweredFront, poweredBack, poweredLeft, poweredRight;
    boolean disabledFront, disabledBack, disabledLeft, disabledRight;
    @OnlyIn(Dist.CLIENT)
    public static final ModelProperty<SideStates> POWERED_PROPERTY = new ModelProperty<>();
    @OnlyIn(Dist.CLIENT)
    public static final ModelProperty<SideStates> DISABLED_PROPERTY = new ModelProperty<>();
    public TileGate(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.GATE.get(), pos, state);
    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(POWERED_PROPERTY, new SideStates(poweredFront, poweredBack, poweredLeft, poweredRight))
                .with(DISABLED_PROPERTY, new SideStates(disabledFront, disabledBack, disabledLeft, disabledRight)).build();
    }

    public void setPowered(Side side, boolean powered){
        switch (side){
            case LEFT -> poweredLeft = powered;
            case FRONT -> poweredFront = powered;
            case BACK -> poweredBack = powered;
            case RIGHT -> poweredRight = powered;
        }
        markBlockForUpdate();
    }

    public void setDisabled(Side side, boolean disabled){
        switch (side){
            case LEFT -> disabledLeft = disabled;
            case FRONT -> disabledFront = disabled;
            case BACK -> disabledBack = disabled;
            case RIGHT -> disabledRight = disabled;
        }
        markBlockForUpdate();
    }

    public boolean updateStates(Map<Side, Byte> map, boolean simulate){
        SideStates oldPoweredStates = new SideStates(poweredFront, poweredBack, poweredLeft, poweredRight);
        SideStates newPoweredStates;
        if (!simulate){
            poweredFront = map.get(Side.FRONT) > 0;
            poweredBack = map.get(Side.BACK) > 0;
            poweredLeft = map.get(Side.LEFT) > 0;
            poweredRight = map.get(Side.RIGHT) > 0;
            newPoweredStates = new SideStates(poweredFront, poweredBack, poweredLeft, poweredRight);
        } else {
            newPoweredStates = new SideStates(map.get(Side.FRONT) > 0, map.get(Side.BACK) > 0, map.get(Side.LEFT) > 0, map.get(Side.RIGHT) > 0);
        }
        boolean changed = !oldPoweredStates.equals(newPoweredStates);
        if (changed && !simulate) markBlockForUpdate();
        return changed;
    }

    public boolean isPowered(Side side){
        return switch (side){
            case FRONT -> poweredFront;
            case BACK -> poweredBack;
            case LEFT -> poweredLeft;
            case RIGHT -> poweredRight;
        };
    }

    public boolean isDisabled(Side side){
        return switch (side){
            case FRONT -> disabledFront;
            case BACK -> disabledBack;
            case LEFT -> disabledLeft;
            case RIGHT -> disabledRight;
        };
    }

    public int redstoneFromSide(Side side){
        return isPowered(side) && !isDisabled(side) ? 16 : 0;
    }

    @Override
    protected void writeToPacketNBT(CompoundTag tCompound) {
        super.writeToPacketNBT(tCompound);
        tCompound.putBoolean("poweredFront", poweredFront);
        tCompound.putBoolean("poweredBack", poweredBack);
        tCompound.putBoolean("poweredLeft", poweredLeft);
        tCompound.putBoolean("poweredRight", poweredRight);
        tCompound.putBoolean("disabledFront", disabledFront);
        tCompound.putBoolean("disabledBack", disabledBack);
        tCompound.putBoolean("disabledLeft", disabledLeft);
        tCompound.putBoolean("disabledRight", disabledRight);
    }

    @Override
    protected void readFromPacketNBT(CompoundTag tCompound) {
        super.readFromPacketNBT(tCompound);
        poweredFront = tCompound.getBoolean("poweredFront");
        poweredBack = tCompound.getBoolean("poweredBack");
        poweredLeft = tCompound.getBoolean("poweredLeft");
        poweredRight = tCompound.getBoolean("poweredRight");
        disabledFront = tCompound.getBoolean("disabledFront");
        disabledBack = tCompound.getBoolean("disabledBack");
        disabledLeft = tCompound.getBoolean("disabledLeft");
        disabledRight = tCompound.getBoolean("disabledRight");
        markForRenderUpdate();
    }

    public record SideStates(boolean front, boolean back, boolean left, boolean right){}
}
