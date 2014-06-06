package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CustomTabs {
    
    public static CreativeTabs tabBluePowerBlocks;
    public static CreativeTabs tabBluePowerMachines;
    public static CreativeTabs tabBluePowerTools;
    public static CreativeTabs tabBluePowerCircuits;
    
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
                    return Item
                            .getItemFromBlock(net.minecraft.init.Blocks.furnace);
                }
            }
        };
        tabBluePowerTools = new CreativeTabs("tabBluePowerTools") {
            
            @Override
            public Item getTabIconItem() {
            
                return net.minecraft.init.Items.iron_pickaxe;
            }
        };
        tabBluePowerCircuits = new CreativeTabs("tabBluePowerCircuits") {
            
            @Override
            public Item getTabIconItem() {
                
                return net.minecraft.init.Items.redstone;
            }
        };
        
    }
}
