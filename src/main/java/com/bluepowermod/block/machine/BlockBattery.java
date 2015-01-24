package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.tile.tier2.TileBattery;
import com.bluepowermod.util.Refs;
import net.minecraft.block.material.Material;

/**
 * @author Koen Beckers (K4Unl)
 */
public class BlockBattery extends BlockContainerBase {

    public BlockBattery() {

        super(Material.rock, TileBattery.class);
        setBlockName(Refs.BATTERY_NAME);
    }

    @Override
    public GuiIDs getGuiID() {

        return GuiIDs.BATTERY_ID;
    }
}
