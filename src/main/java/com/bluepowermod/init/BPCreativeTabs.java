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

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPCreativeTabs {

    public static CreativeModeTab blocks;
    public static CreativeModeTab machines;
    public static CreativeModeTab items;
    public static CreativeModeTab tools;
    public static CreativeModeTab circuits;
    public static CreativeModeTab wiring;
    public static CreativeModeTab lighting;
    public static CreativeModeTab microblocks;

    static {

        blocks = new BPCreativeTab("bluepower:blocks") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {
                Block iconBlock = BPBlocks.amethyst_ore.get();
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

                Block iconBlock = BPBlocks.alloyfurnace.get();
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

                Item iconItem = BPItems.ruby_gem.get();
                if (iconItem != null) {
                    return new ItemStack(BPItems.ruby_gem.get());
                } else {
                    return new ItemStack(Items.DIAMOND);
                }
            }
        };

        tools = new BPCreativeTab("bluepower:tools") {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                Item iconItem = BPItems.screwdriver.get();
                if (iconItem != null) {
                    return new ItemStack(BPItems.screwdriver.get());
                } else {
                    return new ItemStack(Items.DIAMOND_PICKAXE);
                }
            }
        };

        circuits = new BPCreativeTab("bluepower:circuits", true) {

            @Override
            @OnlyIn(Dist.CLIENT)
            public ItemStack makeIcon() {

                ItemStack iconItem = new ItemStack(BPItems.redstone_pointer_tile.get());
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
                ItemStack iconItem = new ItemStack(BPBlocks.blulectric_cable.get());
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

                ItemStack iconItem = new ItemStack(BPBlocks.blockLampRGB.get());
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
                ItemStack iconItem = new ItemStack(BPBlocks.microblocks.get(0).get());
                if (!iconItem.isEmpty()) {
                    return iconItem;
                } else {
                    return new ItemStack(Blocks.STONE);
                }
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> items) {
                for (Block block : ForgeRegistries.BLOCKS.getValues().stream().filter(b -> !(b instanceof EntityBlock)).toList()) {
                    VoxelShape shape = null;
                    try{
                        shape = block.defaultBlockState().getShape(null, null);
                    }catch (NullPointerException ignored){
                        //Shulker Boxes try to query the Tile Entity
                    }
                    if(ForgeRegistries.BLOCKS.getKey(block) != null && shape == Shapes.block()) {
                        for (RegistryObject<Block> mb : BPBlocks.microblocks){
                            CompoundTag nbt = new CompoundTag();
                            nbt.putString("block", ForgeRegistries.BLOCKS.getKey(block).toString());
                            ItemStack stack = new ItemStack(mb.get());
                            stack.setTag(nbt);
                            stack.setHoverName(Component.translatable(block.getDescriptionId())
                                    .append(Component.literal(" "))
                                    .append(Component.translatable(mb.get().getDescriptionId())));
                            items.add(stack);
                        }
                    }
                }
            }
        };

    }

    private static abstract class BPCreativeTab extends CreativeModeTab {

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
