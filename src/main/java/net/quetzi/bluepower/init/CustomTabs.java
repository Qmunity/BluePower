package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.quetzi.bluepower.references.Refs;

public class CustomTabs {
    public static CreativeTabs tabBluePowerBlocks;
    public static CreativeTabs tabBluePowerMachines;

    public static void init() {
        tabBluePowerBlocks = new CreativeTabs("tabBluePowerBlocks") {
            @Override
            public Item getTabIconItem() {
                return net.minecraft.init.Items.apple;
            }
        };

        tabBluePowerMachines = new CreativeTabs("tabBluePowerMachines") {

            @Override
            public Item getTabIconItem() {
                // Todo: Referer to a static object in the Blocks class.
                Block iconBlock = BPBlocks.basalt;
                if (iconBlock != null) {
                    return Item.getItemFromBlock(iconBlock);
                } else {
                    return Item.getItemFromBlock(net.minecraft.init.Blocks.furnace);
                }
            }
        };
    }
}
