/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;

public class CustomTabs {
    
    public static CreativeTabs tabBluePowerBlocks;
    public static CreativeTabs tabBluePowerMachines;
    public static CreativeTabs tabBluePowerItems;
    public static CreativeTabs tabBluePowerTools;
    public static CreativeTabs tabBluePowerCircuits;
    public static CreativeTabs tabBluePowerLighting;
    
    public static void init() {
    
        tabBluePowerBlocks = new CreativeTabs("tabBluePowerBlocks") {
            
            @Override
            public Item getTabIconItem() {
            
                // Todo: Referer to a static object in the Blocks class.
                Block iconBlock = BPBlocks.basalt;
                if (iconBlock != null) {
                    return Item.getItemFromBlock(iconBlock);
                } else {
                    return Item.getItemFromBlock(net.minecraft.init.Blocks.stone);
                }
            }
        };
        
        tabBluePowerMachines = new CreativeTabs("tabBluePowerMachines") {
            
            @Override
            public Item getTabIconItem() {
            
                // Todo: Referer to a static object in the Blocks class.
                Block iconBlock = BPBlocks.alloy_furnace;
                if (iconBlock != null) {
                    return Item.getItemFromBlock(iconBlock);
                } else {
                    return Item.getItemFromBlock(net.minecraft.init.Blocks.furnace);
                }
            }
        };
        
        tabBluePowerItems = new CreativeTabs("tabBluePowerItems") {
            
            @Override
            public Item getTabIconItem() {
            
                // Todo: Referer to a static object in the Items class.
                Item iconItem = BPItems.ruby;
                if (iconItem != null) {
                    return net.quetzi.bluepower.init.BPItems.ruby;
                } else {
                    return net.minecraft.init.Items.diamond;
                }
            }
        };
        
        tabBluePowerTools = new CreativeTabs("tabBluePowerTools") {
            
            @Override
            public Item getTabIconItem() {
            
                // Todo: Referer to a static object in the Items class.
                Item iconItem = BPItems.ruby_pickaxe;
                if (iconItem != null) {
                    return net.quetzi.bluepower.init.BPItems.ruby_pickaxe;
                } else {
                    return net.minecraft.init.Items.diamond_pickaxe;
                }
            }
        };
        
        tabBluePowerCircuits = new CreativeTabs("tabBluePowerCircuits") {
            
            @Override
            public Item getTabIconItem() {
            
                return net.minecraft.init.Items.redstone;
            }
        };
        
        tabBluePowerLighting = new CreativeTabs("tabBluePowerLighting") {
            
            @Override
            public Item getTabIconItem() {
            
                return Item.getItemFromBlock(Blocks.redstone_lamp);
            }
        };
    }
}
