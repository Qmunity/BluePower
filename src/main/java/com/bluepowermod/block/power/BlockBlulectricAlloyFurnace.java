package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.tier3.TileBlulectricAlloyFurnace;
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
public class BlockBlulectricAlloyFurnace extends BlockContainerHorizontalFacingBase {

    public BlockBlulectricAlloyFurnace() {
        super(Material.STONE, TileBlulectricAlloyFurnace.class, BPBlockEntityType.BLULECTRIC_ALLOY_FURNACE);
        setRegistryName(Refs.MODID, Refs.BLULECTRICALLOYFURNACE_NAME);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileBlulectricAlloyFurnace::tickAlloyFurnace;
    }

    @Override
    protected boolean canRotateVertical() {
        return false;
    }

}
