package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerFacingBase;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.block.material.Material;

public class BlockBlulectricFurnace extends BlockContainerFacingBase {
    public BlockBlulectricFurnace(){
        super(Material.ROCK, TileBlulectricFurnace.class);
        setTranslationKey(Refs.BLULECTRICFURNACE_NAME);
        setRegistryName(Refs.MODID, Refs.BLULECTRICFURNACE_NAME);
    }

    @Override
    public GuiIDs getGuiID() {
        return GuiIDs.BLULECTRIC_FURNACE;
    }

    @Override
    protected boolean canRotateVertical() {
        return false;
    }
}
