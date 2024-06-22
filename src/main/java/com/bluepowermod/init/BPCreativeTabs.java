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

import com.bluepowermod.item.ItemSaw;
import com.bluepowermod.item.ItemScrewdriver;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.stream.Collectors;

public class BPCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Refs.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> blocks = CREATIVE_TABS.register("blocks", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.bluepower:blocks")).icon(() -> new ItemStack(BPBlocks.amethyst_ore.get())).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> machines = CREATIVE_TABS.register("machines",() -> CreativeModeTab.builder().title(Component.translatable("itemGroup.bluepower:machines")).icon(() -> new ItemStack(BPBlocks.alloyfurnace.get())).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> items = CREATIVE_TABS.register("items", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.bluepower:items")).icon(() -> new ItemStack(BPItems.ruby_gem.get())).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> tools = CREATIVE_TABS.register("tools", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.bluepower:tools")).icon(() ->  new ItemStack(BPItems.screwdriver.get())).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> lighting = CREATIVE_TABS.register("lighting", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.bluepower:lighting")).icon(() ->  new ItemStack(BPBlocks.fixedLampRGB.get())).backgroundTexture(CreativeModeTab.createTextureLocation("bp_search")).withSearchBar(62).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> microblocks = CREATIVE_TABS.register( "microblocks", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.bluepower:microblocks")).icon(() ->  new ItemStack(BPBlocks.microblocks.get(0).get())).backgroundTexture(CreativeModeTab.createTextureLocation("bp_search")).withSearchBar(62).build());

    @SubscribeEvent
    public void creativeTabEvent(BuildCreativeModeTabContentsEvent event) {
        if(event.getTab() == blocks.get()) {
            event.acceptAll(BPBlocks.regularBlocks.stream().map(block -> new ItemStack(block.get())).collect(Collectors.toList()));
        }else if(event.getTab() == items.get()){
            event.accept(BPBlocks.indigo_flower.get());
            BPItems.ITEMS.getEntries().forEach((item) -> {if(!(item.get() instanceof TieredItem || item.get() instanceof ItemSaw || item.get() instanceof ItemScrewdriver || item.get() instanceof BlockItem)){event.accept(item.get());}});
        }else if(event.getTab() == tools.get()){
            BPItems.ITEMS.getEntries().forEach((item) -> {if(item.get() instanceof TieredItem || item.get() instanceof ItemSaw || item.get() instanceof ItemScrewdriver){event.accept(item.get());}});
        }else if(event.getTab() == machines.get()){
            event.acceptAll(BPBlocks.machines.stream().map(block -> new ItemStack(block.get())).collect(Collectors.toList()));
            event.accept(BPBlocks.blulectric_cable.get());
            event.accept(BPBlocks.blockGateAND.get());
            event.accept(BPBlocks.blockGateNAND.get());
            event.accept(BPBlocks.blockNullCell.get());
        }else if(event.getTab() == lighting.get()){
            event.acceptAll(BPBlocks.allLamps.stream().map(block -> new ItemStack(block.get())).collect(Collectors.toList()));
        }else if(event.getTab() == microblocks.get()){
            for (Block block : BuiltInRegistries.BLOCK.stream().filter(b -> !(b instanceof EntityBlock)).toList()) {
                VoxelShape shape = null;
                try{
                    shape = block.defaultBlockState().getShape(null, null);
                }catch (NullPointerException ignored){
                    //Shulker Boxes try to query the Tile Entity
                }
                if(shape == Shapes.block()) {
                    for (DeferredHolder<Block, Block> mb : BPBlocks.microblocks){
                        CompoundTag nbt = new CompoundTag();
                        nbt.putString("block", BuiltInRegistries.BLOCK.getKey(block).toString());
                        ItemStack stack = new ItemStack(mb.get());
                        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
                        stack.set(DataComponents.ITEM_NAME, Component.translatable(block.getDescriptionId())
                                .append(Component.literal(" "))
                                .append(Component.translatable(mb.get().getDescriptionId())));
                        event.accept(stack);
                    }
                }
            }
        }
    }

}
