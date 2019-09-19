package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBlulectricAlloyFurnace;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.block.material.Material;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricAlloyFurnace extends BlockContainerFacingBase {

    public BlockBlulectricAlloyFurnace() {
        super(Material.ROCK, TileBlulectricAlloyFurnace.class);
        setRegistryName(Refs.MODID, Refs.BLULECTRICALLOYFURNACE_NAME);
    }

    @Override
    protected boolean canRotateVertical() {
        return false;
    }

}
