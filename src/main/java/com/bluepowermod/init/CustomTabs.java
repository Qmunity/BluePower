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

package com.bluepowermod.init;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import com.bluepowermod.part.PartRegistry;

public class CustomTabs {
    
    public static CreativeTabs tabBluePowerBlocks;
    public static CreativeTabs tabBluePowerMachines;
    public static CreativeTabs tabBluePowerItems;
    public static CreativeTabs tabBluePowerTools;
    public static CreativeTabs tabBluePowerCircuits;
    public static CreativeTabs tabBluePowerLighting;
    
    static {
        
        tabBluePowerBlocks = new BPCreativeTab("tabBluePowerBlocks") {
            
            @Override
            public Item getTabIconItem() {
            
                Block iconBlock = BPBlocks.marble;
                if (iconBlock != null) {
                    return Item.getItemFromBlock(iconBlock);
                } else {
                    return Item.getItemFromBlock(Blocks.stone);
                }
            }
        };
        
        tabBluePowerMachines = new BPCreativeTab("tabBluePowerMachines") {
            
            @Override
            public Item getTabIconItem() {
            
                Block iconBlock = BPBlocks.alloy_furnace;
                if (iconBlock != null) {
                    return Item.getItemFromBlock(iconBlock);
                } else {
                    return Item.getItemFromBlock(Blocks.furnace);
                }
            }
        };
        
        tabBluePowerItems = new BPCreativeTab("tabBluePowerItems") {
            
            @Override
            public Item getTabIconItem() {
            
                Item iconItem = BPItems.ruby;
                if (iconItem != null) {
                    return BPItems.ruby;
                } else {
                    return Items.diamond;
                }
            }
        };
        
        tabBluePowerTools = new BPCreativeTab("tabBluePowerTools") {
            
            @Override
            public Item getTabIconItem() {
            
                Item iconItem = BPItems.screwdriver;
                if (iconItem != null) {
                    return BPItems.screwdriver;
                } else {
                    return Items.diamond_pickaxe;
                }
            }
        };
        
        tabBluePowerCircuits = new BPCreativeTab("tabBluePowerCircuits") {
            
            @Override
            public Item getTabIconItem() {
            
                return BPItems.multipart;
            }
        };
        
        tabBluePowerLighting = new BPCreativeTab("tabBluePowerLighting") {
            
            @Override
            public Item getTabIconItem() {
            
                return Item.getItemFromBlock(Blocks.redstone_lamp);
            }
        };
    }
    
    private static abstract class BPCreativeTab extends CreativeTabs {
        
        public BPCreativeTab(String label) {
        
            super(label);
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void displayAllReleventItems(List l) {
        
            super.displayAllReleventItems(l);
            for (String s : PartRegistry.getInstance().getRegisteredPartsForTab(this))
                l.add(PartRegistry.getInstance().getItemForPart(s));
        }
        
    }
}
