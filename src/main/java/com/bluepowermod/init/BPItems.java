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
import com.bluepowermod.helper.BPItemTier;
import com.bluepowermod.item.*;
import com.bluepowermod.reference.Refs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPItems {
    public static List<Item> itemList = new ArrayList<Item>();
    public static final Item amethyst_gem = new ItemCrafting(Refs.AMETHYST_NAME);
    public static final Item sapphire_gem = new ItemCrafting(Refs.SAPPHIRE_NAME);
    public static final Item green_sapphire_gem = new ItemCrafting(Refs.GREENSAPPHIRE_NAME);
    public static final Item ruby_gem = new ItemCrafting(Refs.RUBY_NAME);

    public static final Item teslatite_dust = new ItemCrafting(Refs.TESLATITE_NAME);
    public static final Item silver_dust = new ItemCrafting(Refs.SILVERDUST_NAME);
    public static final Item zinc_dust = new ItemBase(new Item.Properties()).setRegistryName(Refs.MODID + ":" + Refs.ZINCDUST_NAME);
    public static final Item tungsten_dust = new ItemCrafting(Refs.TUNGSTENDUST_NAME);
    public static final Item brass_dust = new ItemCrafting(Refs.BRASSDUST_NAME);

    public static final Item copper_nugget = new ItemCrafting(Refs.COPPERNUGGET_NAME);
    public static final Item silver_nugget = new ItemCrafting(Refs.SILVERNUGGET_NAME);
    public static final Item zinc_nugget = new ItemCrafting(Refs.ZINCNUGGET_NAME);
    public static final Item tungsten_nugget = new ItemCrafting(Refs.TUNGSTENNUGGET_NAME);
    public static final Item brass_nugget = new ItemCrafting(Refs.BRASSNUGGET_NAME);

    public static final Item silver_raw = new ItemCrafting(Refs.SILVERRAW_NAME);
    public static final Item zinc_raw = new ItemCrafting(Refs.ZINCRAW_NAME);
    public static final Item tungsten_raw = new ItemCrafting(Refs.TUNGSTENRAW_NAME);

    public static final Item silver_ingot = new ItemCrafting(Refs.SILVERINGOT_NAME);
    public static final Item zinc_ingot = new ItemCrafting(Refs.ZINCINGOT_NAME);
    public static final Item tungsten_ingot = new ItemCrafting(Refs.TUNGSTENINGOT_NAME);
    public static final Item tungsten_carbide = new ItemCrafting(Refs.TUNGCARBINGOT_NAME);
    public static final Item brass_ingot = new ItemCrafting(Refs.BRASSINGOT_NAME);
    public static final Item blue_alloy_ingot = new ItemCrafting(Refs.BLUEALLOYINGOT_NAME);
    public static final Item red_alloy_ingot = new ItemCrafting(Refs.REDALLOYINGOT_NAME);
    public static final Item purple_alloy_ingot = new ItemCrafting(Refs.PURPLEALLOYINGOT_NAME);

    public static final Item zinc_ore_crushed = new ItemBase(new Item.Properties())
                .setRegistryName(Refs.MODID + ":" + Refs.ZINC_ORE_CRUSHED_NAME);
    public static final Item zinc_ore_purified = new ItemBase(new Item.Properties())
                .setRegistryName(Refs.MODID + ":" + Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME);
    public static final Item zinc_tiny_dust = new ItemBase(new Item.Properties()).setRegistryName(Refs.MODID + ":" + Refs.ZINCDUST_TINY_NAME);

    public static final Item wood_sickle = new ItemSickle(Tiers.WOOD, Refs.WOODSICKLE_NAME, Item.byBlock(Blocks.OAK_PLANKS));
    public static final Item stone_sickle = new ItemSickle(Tiers.STONE, Refs.STONESICKLE_NAME, Item.byBlock(Blocks.COBBLESTONE));
    public static final Item iron_sickle = new ItemSickle(Tiers.IRON, Refs.IRONSICKLE_NAME, Items.IRON_INGOT);
    public static final Item gold_sickle = new ItemSickle(Tiers.GOLD, Refs.GOLDSICKLE_NAME, Items.GOLD_INGOT);
    public static final Item diamond_sickle = new ItemSickle(Tiers.DIAMOND, Refs.DIAMONDSICKLE_NAME, Items.DIAMOND);
    public static final Item iron_saw = new ItemSaw(Tiers.IRON.getLevel(), Refs.IRONSAW_NAME);
    public static final Item diamond_saw = new ItemSaw(Tiers.DIAMOND.getLevel(), Refs.DIAMONDSAW_NAME);
    public static final Item flax_seeds = new ItemCropSeed(BPBlocks.flax_crop, Blocks.FARMLAND);
    public static final Item indigo_dye = new ItemIndigoDye(Refs.INDIGODYE_NAME);
    public static final Item silicon_boule = new ItemCrafting(Refs.SILICONBOULE_NAME);
    public static final Item silicon_wafer = new ItemCrafting(Refs.SILICONWAFER_NAME);
    public static final Item red_doped_wafer = new ItemCrafting(Refs.REDDOPEDWAFER_NAME);
    public static final Item blue_doped_wafer = new ItemCrafting(Refs.BLUEDOPEDWAFER_NAME);
    public static final Item infused_teslatite_dust = new ItemCrafting(Refs.INFUSEDTESLATITEDUST_NAME);
    public static final Item screwdriver = new ItemScrewdriver();
    public static final Item silky_screwdriver = new ItemSilkyScrewdriver();
    public static final Item athame = new ItemAthame();
    public static final Item zincplate = new ItemCrafting(Refs.ZINCPLATE_NAME);
    public static final Item stone_tile = new ItemCrafting(Refs.STONETILE_NAME);
    public static final Item bluestone_wire_tile = new ItemCrafting(Refs.BLUESTONEWIRETILE_NAME);
    public static final Item bluestone_anode_tile = new ItemCrafting(Refs.BLUESTONEANODETILE_NAME);
    public static final Item bluestone_cathode_tile = new ItemCrafting(Refs.BLUESTONECATHODE_NAME);
    public static final Item bluestone_pointer_tile = new ItemCrafting(Refs.BLUESTONEPOINTER_NAME);
    public static final Item silicon_chip_tile = new ItemCrafting(Refs.SILICONCHIP_NAME);
    public static final Item tainted_silicon_chip_tile = new ItemCrafting(Refs.TAINTEDSILICONCHIP_NAME);
    public static final Item quartz_resonator_tile = new ItemCrafting(Refs.QUARTZRESONATOR_NAME);
    public static final Item redstone_wire_tile = new ItemCrafting(Refs.REDSTONEWIRETILE_NAME);
    public static final Item redstone_anode_tile = new ItemCrafting(Refs.REDSTONEANODETILE_NAME);
    public static final Item redstone_cathode_tile = new ItemCrafting(Refs.REDSTONECATHODE_NAME);
    public static final Item redstone_pointer_tile = new ItemCrafting(Refs.REDSTONEPOINTER_NAME);
    public static final Item multimeter = new ItemMultimeter().setRegistryName(Refs.MODID, Refs.MULTIMETER_NAME);
    public static final Item battery = new ItemCrafting(Refs.BATTERY_NAME);
    public static final Item motor = new ItemCrafting(Refs.MOTOR_NAME);
    public static final Item copper_coil = new ItemCrafting(Refs.COPPERCOIL_NAME);
    // stone_redwire = new ItemCrafting(Refs.STONEREDWIRE_NAME);
    // plate_assembly = new ItemCrafting(Refs.PLATEASSEMBLY_NAME);
    public static final Item stone_bundle = new ItemCrafting(Refs.STONEBUNDLE_NAME);
    public static final Item screwdriver_handle = new ItemCrafting(Refs.SCREWDRIVERHANDLE_NAME);
    public static final Item seed_bag = new ItemSeedBag(Refs.SEEDBAG_NAME);
    public static final List<Item> canvas_bag = new ArrayList<>();
    public static final Item canvas = new ItemCrafting(Refs.CANVAS_NAME);
    public static final List<Item> lumar = new ArrayList<>();
    public static final Item wool_card = new ItemLimitedCrafting(Refs.WOOLCARD_NAME, 64);
    public static final Item diamond_drawplate = new ItemLimitedCrafting(Refs.DIAMONDDRAWPLATE_NAME, 256);
    public static final Item copper_wire = new ItemCrafting(Refs.COPPERWIRE_NAME);
    public static final Item iron_wire = new ItemCrafting(Refs.IRONWIRE_NAME);
    public static final List<Item> paint_can = new ArrayList<>();
    public static final List<Item> paint_brush = new ArrayList<>();
    public static Tier gemItemTier = new BPItemTier(750, 6, 2.0F, 4, 18, Ingredient.of(BPItems.amethyst_gem, BPItems.ruby_gem, BPItems.sapphire_gem, BPItems.green_sapphire_gem));
    public static final Item ruby_axe = new ItemGemAxe(gemItemTier, Refs.RUBYAXE_NAME, BPItems.ruby_gem);
    public static final Item ruby_sword = new ItemGemSword(gemItemTier, Refs.RUBYSWORD_NAME, BPItems.ruby_gem);
    public static final Item ruby_pickaxe = new ItemGemPickaxe(gemItemTier, Refs.RUBYPICKAXE_NAME, BPItems.ruby_gem);
    public static final Item ruby_shovel = new ItemGemSpade(gemItemTier, Refs.RUBYSPADE_NAME, BPItems.ruby_gem);
    public static final Item ruby_hoe = new ItemGemHoe(gemItemTier, Refs.RUBYHOE_NAME, BPItems.ruby_gem);
    public static final Item ruby_sickle = new ItemSickle(gemItemTier, Refs.RUBYSICKLE_NAME, BPItems.ruby_gem);
    public static final Item ruby_saw = new ItemSaw(gemItemTier.getLevel(), Refs.RUBYSAW_NAME);
    public static final Item sapphire_axe = new ItemGemAxe(gemItemTier, Refs.SAPPHIREAXE_NAME, BPItems.sapphire_gem);
    public static final Item sapphire_sword = new ItemGemSword(gemItemTier, Refs.SAPPHIRESWORD_NAME, BPItems.sapphire_gem);
    public static final Item sapphire_pickaxe = new ItemGemPickaxe(gemItemTier, Refs.SAPPHIREPICKAXE_NAME, BPItems.sapphire_gem);
    public static final Item sapphire_shovel = new ItemGemSpade(gemItemTier, Refs.SAPPHIRESPADE_NAME, BPItems.sapphire_gem);
    public static final Item sapphire_hoe = new ItemGemHoe(gemItemTier, Refs.SAPPHIREHOE_NAME, BPItems.sapphire_gem);
    public static final Item sapphire_sickle = new ItemSickle(gemItemTier, Refs.SAPPHIRESICKLE_NAME, BPItems.sapphire_gem);
    public static final Item sapphire_saw = new ItemSaw(gemItemTier.getLevel(), Refs.SAPPHIRESAW_NAME);
    public static final Item green_sapphire_axe = new ItemGemAxe(gemItemTier, Refs.GREENSAPPHIREAXE_NAME, BPItems.green_sapphire_gem);
    public static final Item green_sapphire_sword = new ItemGemSword(gemItemTier, Refs.GREENSAPPHIRESWORD_NAME, BPItems.green_sapphire_gem);
    public static final Item green_sapphire_pickaxe = new ItemGemPickaxe(gemItemTier, Refs.GREENSAPPHIREPICKAXE_NAME, BPItems.green_sapphire_gem);
    public static final Item green_sapphire_shovel = new ItemGemSpade(gemItemTier, Refs.GREENSAPPHIRESPADE_NAME, BPItems.green_sapphire_gem);
    public static final Item green_sapphire_hoe = new ItemGemHoe(gemItemTier, Refs.GREENSAPPHIREHOE_NAME, BPItems.green_sapphire_gem);
    public static final Item green_sapphire_sickle = new ItemSickle(gemItemTier, Refs.GREENSAPPHIRESICKLE_NAME, BPItems.green_sapphire_gem);
    public static final Item green_sapphire_saw = new ItemSaw(gemItemTier.getLevel(), Refs.GREENSAPPHIRESAW_NAME);
    public static final Item amethyst_axe = new ItemGemAxe(gemItemTier, Refs.AMETHYSTAXE_NAME, BPItems.amethyst_gem);
    public static final Item amethyst_sword = new ItemGemSword(gemItemTier, Refs.AMETHYSTSWORD_NAME, BPItems.amethyst_gem);
    public static final Item amethyst_pickaxe = new ItemGemPickaxe(gemItemTier, Refs.AMETHYSTPICKAXE_NAME, BPItems.amethyst_gem);
    public static final Item amethyst_shovel = new ItemGemSpade(gemItemTier, Refs.AMETHYSTSPADE_NAME, BPItems.amethyst_gem);
    public static final Item amethyst_hoe = new ItemGemHoe(gemItemTier, Refs.AMETHYSTHOE_NAME, BPItems.amethyst_gem);
    public static final Item amethyst_sickle = new ItemSickle(gemItemTier, Refs.AMETHYSTSICKLE_NAME, BPItems.amethyst_gem);
    public static final Item amethyst_saw = new ItemSaw(gemItemTier.getLevel(), Refs.AMETHYSTSAW_NAME);
    public static final Item tungcarb_axe = new ItemGemAxe(gemItemTier, Refs.TUNGCARBAXE_NAME, BPItems.tungsten_carbide);
    public static final Item tungcarb_sword = new ItemGemSword(gemItemTier, Refs.TUNGCARBSWORD_NAME,BPItems.tungsten_carbide);
    public static final Item tungcarb_pickaxe = new ItemGemPickaxe(gemItemTier, Refs.TUNGCARBPICKAXE_NAME, BPItems.tungsten_carbide);
    public static final Item tungcarb_shovel = new ItemGemSpade(gemItemTier, Refs.TUNGCARBSPADE_NAME, BPItems.tungsten_carbide);
    public static final Item tungcarb_hoe = new ItemGemHoe(gemItemTier, Refs.TUNGCARBHOE_NAME, BPItems.tungsten_carbide);
    public static final Item tungcarb_sickle = new ItemSickle(gemItemTier, Refs.TUNGCARBSICKLE_NAME, BPItems.tungsten_carbide);
    public static final Item tungcarb_saw = new ItemSaw(gemItemTier.getLevel(), Refs.TUNGCARBSAW_NAME);

    static {
        //Lumar
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            lumar.add(new ItemLumar(MinecraftColor.values()[i]));

        //Canvas Bag
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            canvas_bag.add(new ItemCanvasBag(MinecraftColor.values()[i]));

        //Paint Can
        paint_can.add(new ItemPaintCan());
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            paint_can.add(new ItemPaintCan(MinecraftColor.values()[i]));

        //Paint Brush
        paint_brush.add(new ItemPaintBrush());
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            paint_brush.add(new ItemPaintBrush(MinecraftColor.values()[i]));


    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : itemList) {
            event.getRegistry().register(item);
        }
    }

}
