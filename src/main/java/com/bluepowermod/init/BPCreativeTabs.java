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
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class BPCreativeTabs {

    public static ItemGroup blocks;
    public static ItemGroup machines;
    public static ItemGroup items;
    public static ItemGroup tools;
    public static ItemGroup circuits;
    public static ItemGroup wiring;
    public static ItemGroup lighting;
    public static ItemGroup microblocks;

    static {

        blocks = new BPCreativeTab("bluepower:blocks") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                Block iconBlock = BPBlocks.amethyst_ore;
                if (iconBlock != null) {
                    return new ItemStack(iconBlock);
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        machines = new BPCreativeTab("bluepower:machines") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

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
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

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
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

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
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                ItemStack iconItem = new ItemStack(BPItems.redstone_pointer_tile);
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        wiring = new BPCreativeTab("bluepower:wiring", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                ItemStack iconItem = new ItemStack(BPBlocks.blulectric_cable);
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }
        };

        lighting = new BPCreativeTab("bluepower:lighting", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
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

        microblocks = new BPCreativeTab("bluepower:microblocks", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                ItemStack iconItem = new ItemStack(BPBlocks.microblocks.get(0));
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> items) {
                for (Block block : ForgeRegistries.BLOCKS) {
                    VoxelShape shape = null;
                    try{
                        shape = block.defaultBlockState().getShape(null, null);
                    }catch (NullPointerException ignored){
                        //Shulker Boxes try to query the Tile Entity
                    }
                    if(block.getRegistryName() != null && shape == VoxelShapes.block()) {
                        for (Block mb : BPBlocks.microblocks){
                            CompoundNBT nbt = new CompoundNBT();
                            nbt.putString("block", block.getRegistryName().toString());
                            ItemStack stack = new ItemStack(mb);
                            stack.setTag(nbt);
                            stack.setHoverName(new TranslationTextComponent(block.getDescriptionId())
                                    .append(new StringTextComponent(" "))
                                    .append(new TranslationTextComponent(mb.getDescriptionId())));
                            items.add(stack);
                        }
                    }
                }
            }
        };

    }

    private static abstract class BPCreativeTab extends ItemGroup {

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
        @OnlyIn(Dist.CLIENT)
        public String getBackgroundSuffix() {

            return searchbar ? "bp_search.png" : super.getBackgroundSuffix();
        }

        @Override
        public int getSearchbarWidth() {

            return 62;
        }

    }
}
