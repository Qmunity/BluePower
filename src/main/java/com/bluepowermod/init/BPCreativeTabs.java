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

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.misc.MinecraftColor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BPCreativeTabs {

    public static CreativeTabs blocks;
    public static CreativeTabs machines;
    public static CreativeTabs items;
    public static CreativeTabs tools;
    public static CreativeTabs circuits;
    public static CreativeTabs wiring;
    public static CreativeTabs lighting;

    static {

        blocks = new BPCreativeTab("bluepower:blocks") {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                Block iconBlock = BPBlocks.amethyst_block;
                if (iconBlock != null) {
                    return new ItemStack(iconBlock);
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        machines = new BPCreativeTab("bluepower:machines") {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                Block iconBlock = BPBlocks.alloyfurnace;
                if (iconBlock != null) {
                    return new ItemStack(iconBlock);
                } else {
                    return new ItemStack(Blocks.FURNACE);
                }
            }
        };

        items = new BPCreativeTab("bluepower:items") {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                Item iconItem = BPItems.ruby_gem;
                if (iconItem != null) {
                    return new ItemStack(BPItems.ruby_gem);
                } else {
                    return new ItemStack(Items.DIAMOND);
                }
            }
        };

        tools = new BPCreativeTab("bluepower:tools") {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                Item iconItem = BPItems.screwdriver;
                if (iconItem != null) {
                    return new ItemStack(BPItems.screwdriver);
                } else {
                    return new ItemStack(Items.DIAMOND_PICKAXE);
                }
            }
        };

        circuits = new BPCreativeTab("bluepower:circuits", true) {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                return ItemStack.EMPTY;
            }

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getIconItemStack() {

                ItemStack iconItem = new ItemStack(BPBlocks.circuit_table);
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        wiring = new BPCreativeTab("bluepower:wiring", true) {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                return ItemStack.EMPTY;
            }

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getIconItemStack() {

                ItemStack iconItem = new ItemStack(BPItems.blue_alloy_ingot);
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        lighting = new BPCreativeTab("bluepower:lighting", true) {

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getTabIconItem() {

                return ItemStack.EMPTY;
            }

            @Override
            @SideOnly(Side.CLIENT)
            public ItemStack getIconItemStack() {

                int t = 1000;

                int i = (int) ((System.currentTimeMillis() / t) % MinecraftColor.VALID_COLORS.length);
                boolean b = ((System.currentTimeMillis() / t) % (MinecraftColor.VALID_COLORS.length * 2)) >= MinecraftColor.VALID_COLORS.length;
                boolean b2 = ((System.currentTimeMillis() / t) % (MinecraftColor.VALID_COLORS.length * 4)) >= MinecraftColor.VALID_COLORS.length;

                ItemStack iconItem = new ItemStack(BPBlocks.blockLampRGB);
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };
    }

    private static abstract class BPCreativeTab extends CreativeTabs {

        private boolean searchbar = false;

        public BPCreativeTab(String label) {

            super(label);
        }

        public BPCreativeTab(String label, boolean searchbar) {

            this(label);
            this.searchbar = searchbar;
        }

        @Override
        public boolean hasSearchBar() {

            return searchbar;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String getBackgroundImageName() {

            return searchbar ? "bp_search.png" : super.getBackgroundImageName();
        }

        @Override
        public int getSearchbarWidth() {

            return 62;
        }

    }
}
