package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBlulectricAlloyFurnace;
import net.minecraft.block.material.Material;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricAlloyFurnace extends BlockContainerHorizontalFacingBase {

    public BlockBlulectricAlloyFurnace() {
        super(Material.STONE, TileBlulectricAlloyFurnace.class);
        setRegistryName(Refs.MODID, Refs.BLULECTRICALLOYFURNACE_NAME);
    }

    @Override
    protected boolean canRotateVertical() {
        return false;
    }

}
