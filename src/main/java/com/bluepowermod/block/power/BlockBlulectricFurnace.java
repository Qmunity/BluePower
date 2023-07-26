package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricFurnace  extends BlockContainerHorizontalFacingBase {

    public BlockBlulectricFurnace() {
        super(TileBlulectricFurnace.class);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileBlulectricFurnace::tickFurnace;
    }

    @Override
    protected boolean canRotateVertical() {
        return false;
    }

}
