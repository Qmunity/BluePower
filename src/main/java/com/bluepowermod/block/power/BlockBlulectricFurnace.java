package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.world.level.material.Material;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricFurnace  extends BlockContainerHorizontalFacingBase {

    public BlockBlulectricFurnace() {
        super(Material.STONE, TileBlulectricFurnace.class, BPBlockEntityType.BLULECTRIC_FURNACE);
        setRegistryName(Refs.MODID, Refs.BLULECTRICFURNACE_NAME);
    }

    @Override
    protected boolean canRotateVertical() {
        return false;
    }

}
