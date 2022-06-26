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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.helper.GemItemTier;
import com.bluepowermod.item.*;
import com.bluepowermod.reference.Refs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;

public class BPItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Refs.MODID);

    public static final RegistryObject<Item> amethyst_gem = ITEMS.register(Refs.AMETHYST_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> sapphire_gem = ITEMS.register(Refs.SAPPHIRE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> green_sapphire_gem = ITEMS.register(Refs.GREENSAPPHIRE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> ruby_gem = ITEMS.register(Refs.RUBY_NAME, ItemCrafting::new);

    public static final RegistryObject<Item> teslatite_dust = ITEMS.register(Refs.TESLATITE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> silver_dust = ITEMS.register(Refs.SILVERDUST_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> zinc_dust = ITEMS.register(Refs.ZINCDUST_NAME, () -> new ItemBase(new Item.Properties()));
    public static final RegistryObject<Item> tungsten_dust = ITEMS.register(Refs.TUNGSTENDUST_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> brass_dust = ITEMS.register(Refs.BRASSDUST_NAME, ItemCrafting::new);

    public static final RegistryObject<Item> copper_nugget = ITEMS.register(Refs.COPPERNUGGET_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> silver_nugget = ITEMS.register(Refs.SILVERNUGGET_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> zinc_nugget = ITEMS.register(Refs.ZINCNUGGET_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> tungsten_nugget = ITEMS.register(Refs.TUNGSTENNUGGET_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> brass_nugget = ITEMS.register(Refs.BRASSNUGGET_NAME, ItemCrafting::new);

    public static final RegistryObject<Item> silver_raw = ITEMS.register(Refs.SILVERRAW_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> zinc_raw = ITEMS.register(Refs.ZINCRAW_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> tungsten_raw = ITEMS.register(Refs.TUNGSTENRAW_NAME, ItemCrafting::new);

    public static final RegistryObject<Item> silver_ingot = ITEMS.register(Refs.SILVERINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> zinc_ingot = ITEMS.register(Refs.ZINCINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> tungsten_ingot = ITEMS.register(Refs.TUNGSTENINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> tungsten_carbide = ITEMS.register(Refs.TUNGCARBINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> brass_ingot = ITEMS.register(Refs.BRASSINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> blue_alloy_ingot = ITEMS.register(Refs.BLUEALLOYINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> red_alloy_ingot = ITEMS.register(Refs.REDALLOYINGOT_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> purple_alloy_ingot = ITEMS.register(Refs.PURPLEALLOYINGOT_NAME, ItemCrafting::new);

    public static final RegistryObject<Item> zinc_ore_crushed = ITEMS.register(Refs.ZINC_ORE_CRUSHED_NAME, () -> new ItemBase(new Item.Properties()));
    public static final RegistryObject<Item> zinc_ore_purified = ITEMS.register(Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME, () -> new ItemBase(new Item.Properties()));
    public static final RegistryObject<Item> zinc_tiny_dust = ITEMS.register(Refs.ZINCDUST_TINY_NAME, () -> new ItemBase(new Item.Properties()));

    public static final RegistryObject<Item> wood_sickle = ITEMS.register(Refs.WOODSICKLE_NAME, () -> new ItemSickle(Tiers.WOOD, Item.byBlock(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Item> stone_sickle = ITEMS.register(Refs.STONESICKLE_NAME, () -> new ItemSickle(Tiers.STONE, Item.byBlock(Blocks.COBBLESTONE)));
    public static final RegistryObject<Item> iron_sickle = ITEMS.register(Refs.IRONSICKLE_NAME, () -> new ItemSickle(Tiers.IRON, Items.IRON_INGOT));
    public static final RegistryObject<Item> gold_sickle = ITEMS.register(Refs.GOLDSICKLE_NAME, () -> new ItemSickle(Tiers.GOLD, Items.GOLD_INGOT));
    public static final RegistryObject<Item> diamond_sickle = ITEMS.register(Refs.DIAMONDSICKLE_NAME, () -> new ItemSickle(Tiers.DIAMOND, Items.DIAMOND));
    public static final RegistryObject<Item> iron_saw = ITEMS.register(Refs.IRONSAW_NAME, () -> new ItemSaw(Tiers.IRON.getLevel()));
    public static final RegistryObject<Item> diamond_saw = ITEMS.register(Refs.DIAMONDSAW_NAME, () -> new ItemSaw(Tiers.DIAMOND.getLevel()));
    public static final RegistryObject<Item> flax_seeds = ITEMS.register(Refs.FLAXSEED_NAME, () -> new ItemCropSeed(BPBlocks.flax_crop, Blocks.FARMLAND));
    public static final RegistryObject<Item> indigo_dye = ITEMS.register(Refs.INDIGODYE_NAME, ItemIndigoDye::new);
    public static final RegistryObject<Item> silicon_boule = ITEMS.register(Refs.SILICONBOULE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> silicon_wafer = ITEMS.register(Refs.SILICONWAFER_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> red_doped_wafer = ITEMS.register(Refs.REDDOPEDWAFER_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> blue_doped_wafer = ITEMS.register(Refs.BLUEDOPEDWAFER_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> infused_teslatite_dust = ITEMS.register(Refs.INFUSEDTESLATITEDUST_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> screwdriver = ITEMS.register(Refs.SCREWDRIVER_NAME, ItemScrewdriver::new);
    public static final RegistryObject<Item> silky_screwdriver = ITEMS.register(Refs.SILKYSCREWDRIVER_NAME, ItemSilkyScrewdriver::new);
    public static final RegistryObject<Item> athame = ITEMS.register(Refs.ATHAME_NAME, ItemAthame::new);
    public static final RegistryObject<Item> zincplate = ITEMS.register(Refs.ZINCPLATE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> stone_tile = ITEMS.register(Refs.STONETILE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> bluestone_wire_tile = ITEMS.register(Refs.BLUESTONEWIRETILE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> bluestone_anode_tile = ITEMS.register(Refs.BLUESTONEANODETILE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> bluestone_cathode_tile = ITEMS.register(Refs.BLUESTONECATHODE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> bluestone_pointer_tile = ITEMS.register(Refs.BLUESTONEPOINTER_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> silicon_chip_tile = ITEMS.register(Refs.SILICONCHIP_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> tainted_silicon_chip_tile = ITEMS.register(Refs.TAINTEDSILICONCHIP_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> quartz_resonator_tile = ITEMS.register(Refs.QUARTZRESONATOR_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> redstone_wire_tile = ITEMS.register(Refs.REDSTONEWIRETILE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> redstone_anode_tile = ITEMS.register(Refs.REDSTONEANODETILE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> redstone_cathode_tile = ITEMS.register(Refs.REDSTONECATHODE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> redstone_pointer_tile = ITEMS.register(Refs.REDSTONEPOINTER_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> multimeter = ITEMS.register(Refs.MULTIMETER_NAME, ItemMultimeter::new);
    public static final RegistryObject<Item> battery = ITEMS.register(Refs.BATTERY_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> motor = ITEMS.register(Refs.MOTOR_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> copper_coil = ITEMS.register(Refs.COPPERCOIL_NAME, ItemCrafting::new);
    // stone_redwire = ITEMS.register(Refs.STONEREDWIRE_NAME, ItemCrafting::new);
    // plate_assembly = ITEMS.register(Refs.PLATEASSEMBLY_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> stone_bundle = ITEMS.register(Refs.STONEBUNDLE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> screwdriver_handle = ITEMS.register(Refs.SCREWDRIVERHANDLE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> seed_bag = ITEMS.register(Refs.SEEDBAG_NAME, ItemSeedBag::new);
    public static final List<RegistryObject<Item>> canvas_bag = new ArrayList<>();
    public static final RegistryObject<Item> canvas = ITEMS.register(Refs.CANVAS_NAME, ItemCrafting::new);
    public static final List<RegistryObject<Item>> lumar = new ArrayList<>();
    public static final RegistryObject<Item> wool_card = ITEMS.register(Refs.WOOLCARD_NAME, () -> new ItemLimitedCrafting(64));
    public static final RegistryObject<Item> diamond_drawplate = ITEMS.register(Refs.DIAMONDDRAWPLATE_NAME, () -> new ItemLimitedCrafting(256));
    public static final RegistryObject<Item> copper_wire = ITEMS.register(Refs.COPPERWIRE_NAME, ItemCrafting::new);
    public static final RegistryObject<Item> iron_wire = ITEMS.register(Refs.IRONWIRE_NAME, ItemCrafting::new);
    public static final List<RegistryObject<Item>> paint_can = new ArrayList<>();
    public static final List<RegistryObject<Item>> paint_brush = new ArrayList<>();
    public static Tier gemItemTier = new GemItemTier(750, 6, 2.0F, 4, 18);
    public static final RegistryObject<Item> ruby_axe = ITEMS.register(Refs.RUBYAXE_NAME,() ->  new ItemGemAxe(gemItemTier, BPItems.ruby_gem.get()));
    public static final RegistryObject<Item> ruby_sword = ITEMS.register(Refs.RUBYSWORD_NAME,() ->  new ItemGemSword(gemItemTier, BPItems.ruby_gem.get()));
    public static final RegistryObject<Item> ruby_pickaxe = ITEMS.register(Refs.RUBYPICKAXE_NAME,() ->  new ItemGemPickaxe(gemItemTier, BPItems.ruby_gem.get()));
    public static final RegistryObject<Item> ruby_shovel = ITEMS.register(Refs.RUBYSPADE_NAME,() ->  new ItemGemSpade(gemItemTier, BPItems.ruby_gem.get()));
    public static final RegistryObject<Item> ruby_hoe = ITEMS.register(Refs.RUBYHOE_NAME,() ->  new ItemGemHoe(gemItemTier, BPItems.ruby_gem.get()));
    public static final RegistryObject<Item> ruby_sickle = ITEMS.register(Refs.RUBYSICKLE_NAME,() ->  new ItemSickle(gemItemTier, BPItems.ruby_gem.get()));
    public static final RegistryObject<Item> ruby_saw = ITEMS.register(Refs.RUBYSAW_NAME,() ->  new ItemSaw(gemItemTier.getLevel()));
    public static final RegistryObject<Item> sapphire_axe = ITEMS.register(Refs.SAPPHIREAXE_NAME,() ->  new ItemGemAxe(gemItemTier, BPItems.sapphire_gem.get()));
    public static final RegistryObject<Item> sapphire_sword = ITEMS.register(Refs.SAPPHIRESWORD_NAME,() ->  new ItemGemSword(gemItemTier, BPItems.sapphire_gem.get()));
    public static final RegistryObject<Item> sapphire_pickaxe = ITEMS.register(Refs.SAPPHIREPICKAXE_NAME,() ->  new ItemGemPickaxe(gemItemTier, BPItems.sapphire_gem.get()));
    public static final RegistryObject<Item> sapphire_shovel = ITEMS.register(Refs.SAPPHIRESPADE_NAME,() ->  new ItemGemSpade(gemItemTier, BPItems.sapphire_gem.get()));
    public static final RegistryObject<Item> sapphire_hoe = ITEMS.register(Refs.SAPPHIREHOE_NAME,() ->  new ItemGemHoe(gemItemTier, BPItems.sapphire_gem.get()));
    public static final RegistryObject<Item> sapphire_sickle = ITEMS.register(Refs.SAPPHIRESICKLE_NAME,() ->  new ItemSickle(gemItemTier, BPItems.sapphire_gem.get()));
    public static final RegistryObject<Item> sapphire_saw = ITEMS.register(Refs.SAPPHIRESAW_NAME,() ->  new ItemSaw(gemItemTier.getLevel()));
    public static final RegistryObject<Item> green_sapphire_axe = ITEMS.register(Refs.GREENSAPPHIREAXE_NAME,() ->  new ItemGemAxe(gemItemTier, BPItems.green_sapphire_gem.get()));
    public static final RegistryObject<Item> green_sapphire_sword = ITEMS.register(Refs.GREENSAPPHIRESWORD_NAME,() ->  new ItemGemSword(gemItemTier, BPItems.green_sapphire_gem.get()));
    public static final RegistryObject<Item> green_sapphire_pickaxe = ITEMS.register(Refs.GREENSAPPHIREPICKAXE_NAME,() ->  new ItemGemPickaxe(gemItemTier, BPItems.green_sapphire_gem.get()));
    public static final RegistryObject<Item> green_sapphire_shovel = ITEMS.register(Refs.GREENSAPPHIRESPADE_NAME,() ->  new ItemGemSpade(gemItemTier, BPItems.green_sapphire_gem.get()));
    public static final RegistryObject<Item> green_sapphire_hoe = ITEMS.register(Refs.GREENSAPPHIREHOE_NAME,() ->  new ItemGemHoe(gemItemTier, BPItems.green_sapphire_gem.get()));
    public static final RegistryObject<Item> green_sapphire_sickle = ITEMS.register(Refs.GREENSAPPHIRESICKLE_NAME,() ->  new ItemSickle(gemItemTier, BPItems.green_sapphire_gem.get()));
    public static final RegistryObject<Item> green_sapphire_saw = ITEMS.register(Refs.GREENSAPPHIRESAW_NAME,() ->  new ItemSaw(gemItemTier.getLevel()));
    public static final RegistryObject<Item> amethyst_axe = ITEMS.register(Refs.AMETHYSTAXE_NAME,() ->  new ItemGemAxe(gemItemTier, BPItems.amethyst_gem.get()));
    public static final RegistryObject<Item> amethyst_sword = ITEMS.register(Refs.AMETHYSTSWORD_NAME,() ->  new ItemGemSword(gemItemTier, BPItems.amethyst_gem.get()));
    public static final RegistryObject<Item> amethyst_pickaxe = ITEMS.register(Refs.AMETHYSTPICKAXE_NAME,() ->  new ItemGemPickaxe(gemItemTier, BPItems.amethyst_gem.get()));
    public static final RegistryObject<Item> amethyst_shovel = ITEMS.register(Refs.AMETHYSTSPADE_NAME,() ->  new ItemGemSpade(gemItemTier, BPItems.amethyst_gem.get()));
    public static final RegistryObject<Item> amethyst_hoe = ITEMS.register(Refs.AMETHYSTHOE_NAME,() ->  new ItemGemHoe(gemItemTier, BPItems.amethyst_gem.get()));
    public static final RegistryObject<Item> amethyst_sickle = ITEMS.register(Refs.AMETHYSTSICKLE_NAME,() ->  new ItemSickle(gemItemTier, BPItems.amethyst_gem.get()));
    public static final RegistryObject<Item> amethyst_saw = ITEMS.register(Refs.AMETHYSTSAW_NAME,() ->  new ItemSaw(gemItemTier.getLevel()));
    public static final RegistryObject<Item> tungcarb_axe = ITEMS.register(Refs.TUNGCARBAXE_NAME,() ->  new ItemGemAxe(gemItemTier, BPItems.tungsten_carbide.get()));
    public static final RegistryObject<Item> tungcarb_sword = ITEMS.register(Refs.TUNGCARBSWORD_NAME,() ->  new ItemGemSword(gemItemTier, BPItems.tungsten_carbide.get()));
    public static final RegistryObject<Item> tungcarb_pickaxe = ITEMS.register(Refs.TUNGCARBPICKAXE_NAME,() ->  new ItemGemPickaxe(gemItemTier, BPItems.tungsten_carbide.get()));
    public static final RegistryObject<Item> tungcarb_shovel = ITEMS.register(Refs.TUNGCARBSPADE_NAME,() ->  new ItemGemSpade(gemItemTier, BPItems.tungsten_carbide.get()));
    public static final RegistryObject<Item> tungcarb_hoe = ITEMS.register(Refs.TUNGCARBHOE_NAME,() ->  new ItemGemHoe(gemItemTier, BPItems.tungsten_carbide.get()));
    public static final RegistryObject<Item> tungcarb_sickle = ITEMS.register(Refs.TUNGCARBSICKLE_NAME,() ->  new ItemSickle(gemItemTier, BPItems.tungsten_carbide.get()));
    public static final RegistryObject<Item> tungcarb_saw = ITEMS.register(Refs.TUNGCARBSAW_NAME,() ->  new ItemSaw(gemItemTier.getLevel()));

    static {
        //Lumar
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            MinecraftColor color = MinecraftColor.values()[i];
            lumar.add(ITEMS.register(Refs.LUMAR_NAME + "_" + color.name().toLowerCase(), () -> new ItemLumar(color)));
        }

        //Canvas Bag
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            MinecraftColor color = MinecraftColor.values()[i];
            canvas_bag.add(ITEMS.register(Refs.CANVASBAG_NAME + "_" + color.name().toLowerCase(), () -> new ItemCanvasBag(color)));
        }

        //Paint Can
        paint_can.add(ITEMS.register(Refs.PAINTCAN_NAME, ItemPaintCan::new));
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            MinecraftColor color = MinecraftColor.values()[i];
            paint_can.add(ITEMS.register(Refs.PAINTCAN_NAME + "_" + color.name().toLowerCase(), () -> new ItemPaintCan(color)));
        }

        //Paint Brush
        paint_brush.add(ITEMS.register(Refs.PAINTBRUSH_NAME + "_blank", ItemPaintBrush::new));
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            MinecraftColor color = MinecraftColor.values()[i];
            paint_brush.add(ITEMS.register(Refs.PAINTBRUSH_NAME + "_" + color.name().toLowerCase(), () -> new ItemPaintBrush(color)));
        }

    }
}
