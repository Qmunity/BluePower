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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class BPCreativeTabs {

    CreativeModeTab blocks;
    CreativeModeTab machines;
    CreativeModeTab items;
    CreativeModeTab tools;
    CreativeModeTab lighting;
    CreativeModeTab microblocks;

    @SubscribeEvent
    public void onRegisterCreativeModeTabs(CreativeModeTabEvent.Register event) {
        blocks = event.registerCreativeModeTab(new ResourceLocation(Refs.MODID, "blocks"), (builder) -> builder.title(Component.translatable("itemGroup.bluepower:blocks")).icon(() -> new ItemStack(BPBlocks.amethyst_ore.get())));
        machines = event.registerCreativeModeTab(new ResourceLocation(Refs.MODID, "machines"),(builder) -> builder.title(Component.translatable("itemGroup.bluepower:machines")).icon(() -> new ItemStack(BPBlocks.alloyfurnace.get())));
        items = event.registerCreativeModeTab(new ResourceLocation(Refs.MODID, "items"), (builder) -> builder.title(Component.translatable("itemGroup.bluepower:items")).icon(() -> new ItemStack(BPItems.ruby_gem.get())));
        tools = event.registerCreativeModeTab(new ResourceLocation(Refs.MODID, "tools"), (builder) -> builder.title(Component.translatable("itemGroup.bluepower:tools")).icon(() ->  new ItemStack(BPItems.screwdriver.get())));
        lighting = event.registerCreativeModeTab(new ResourceLocation(Refs.MODID, "lighting"), (builder) -> builder.title(Component.translatable("itemGroup.bluepower:lighting")).icon(() ->  new ItemStack(BPBlocks.fixedLampRGB.get())).backgroundSuffix("bp_search.png").withSearchBar(62));
        microblocks = event.registerCreativeModeTab(new ResourceLocation(Refs.MODID, "microblocks"), (builder) -> builder.title(Component.translatable("itemGroup.bluepower:microblocks")).icon(() ->  new ItemStack(BPBlocks.microblocks.get(0).get())).backgroundSuffix("bp_search.png").withSearchBar(62));
    }

    @SubscribeEvent
    public void creativeTabEvent(CreativeModeTabEvent.BuildContents event) {
        if(event.getTab() == blocks) {
            event.acceptAll(BPBlocks.regularBlocks.stream().map(block -> new ItemStack(block.get())).collect(Collectors.toList()));
        }else if(event.getTab() == items){
            event.accept(BPBlocks.indigo_flower.get());
            BPItems.ITEMS.getEntries().forEach((item) -> {if(!(item.get() instanceof TieredItem || item.get() instanceof ItemSaw || item.get() instanceof ItemScrewdriver || item.get() instanceof BlockItem)){event.accept(item.get());}});
        }else if(event.getTab() == tools){
            BPItems.ITEMS.getEntries().forEach((item) -> {if(item.get() instanceof TieredItem || item.get() instanceof ItemSaw || item.get() instanceof ItemScrewdriver){event.accept(item.get());}});
        }else if(event.getTab() == machines){
            event.acceptAll(BPBlocks.machines.stream().map(block -> new ItemStack(block.get())).collect(Collectors.toList()));
            event.accept(BPBlocks.blulectric_cable.get());
            event.accept(BPBlocks.blockGateAND.get());
            event.accept(BPBlocks.blockGateNAND.get());
            event.accept(BPBlocks.blockNullCell.get());
        }else if(event.getTab() == lighting){
            event.acceptAll(BPBlocks.allLamps.stream().map(block -> new ItemStack(block.get())).collect(Collectors.toList()));
        }else if(event.getTab() == microblocks){
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
                        event.accept(stack);
                    }
                }
            }
        }
    }

}
