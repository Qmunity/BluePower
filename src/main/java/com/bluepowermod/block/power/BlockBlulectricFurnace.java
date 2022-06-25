package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricFurnace  extends BlockContainerHorizontalFacingBase {

    public BlockBlulectricFurnace() {
        super(Material.STONE, TileBlulectricFurnace.class, BPBlockEntityType.BLULECTRIC_FURNACE);
        setRegistryName(Refs.MODID, Refs.BLULECTRICFURNACE_NAME);
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
