package net.quetzi.bluepower.blocks;

import net.minecraft.block.material.Material;
import net.quetzi.bluepower.init.CustomTabs;

public class BlockAlloyFurnace extends BlockBase {

    public BlockAlloyFurnace() {
        super(Material.rock);
        this.blockHardness = 2.0F;
        this.setCreativeTab(CustomTabs.tabBluePowerMachines);
    }
    public int getLightValue() {
        // TODO: add light logic when furnace is active
        return 0;
    }
    public boolean isOpaqueCube() {
        return true;
    }
}
