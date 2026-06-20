package com.bluepowermod.tile.tier1;

import com.bluepowermod.block.gates.BlockGateBase.Side;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

    public boolean updateStates(Map<Side, Byte> map){
        SideStates oldPoweredStates = new SideStates(poweredFront, poweredBack, poweredLeft, poweredRight);
        poweredFront = map.get(Side.FRONT) > 0;
        poweredBack = map.get(Side.BACK) > 0;
        poweredLeft = map.get(Side.LEFT) > 0;
        poweredRight = map.get(Side.RIGHT) > 0;
        return poweredFront != oldPoweredStates.front || poweredBack != oldPoweredStates.back || poweredLeft != oldPoweredStates.left || poweredRight != oldPoweredStates.right;
    }

    public boolean isPoweredFront() {
        return poweredFront;
    }

    public boolean isPoweredBack() {
        return poweredBack;
    }

    public boolean isPoweredLeft() {
        return poweredLeft;
    }

    public boolean isPoweredRight() {
        return poweredRight;
    }

    public boolean isDisabledFront() {
        return disabledFront;
    }

    public boolean isDisabledBack() {
        return disabledBack;
    }

    public boolean isDisabledLeft() {
        return disabledLeft;
    }

    public boolean isDisabledRight() {
        return disabledRight;
    }

    public int redstoneFromSide(Side side){
        return switch (side){
            case FRONT -> poweredFront && !disabledFront ? 16 : 0;
            case BACK -> poweredBack && !disabledBack ? 16 : 0;
            case LEFT -> poweredLeft && !disabledLeft ? 16 : 0;
            case RIGHT -> poweredRight && !disabledRight ? 16 : 0;
        };
    }

    public record SideStates(boolean front, boolean back, boolean left, boolean right){}
}
