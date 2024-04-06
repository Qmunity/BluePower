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

import net.neoforged.neoforge.common.ModConfigSpec;

public class BPConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final General CONFIG = new General(BUILDER);

    public static class General {
        public final ModConfigSpec.BooleanValue generateVolcano;
        public final ModConfigSpec.ConfigValue<Double> volcanoActiveToInactiveRatio;
        public final ModConfigSpec.ConfigValue<Double> volcanoSpawnChance; // chance of a volcano spawning per chunk.
        public final ModConfigSpec.ConfigValue<String> volcanoBiomeCategoryWhitelist;
        public final ModConfigSpec.BooleanValue generateTungstenInVolcano;
        public final ModConfigSpec.ConfigValue<String> alloyFurnaceBlacklist;
        public final ModConfigSpec.ConfigValue<Boolean> alloyFurnaceDatapackGenerator;
        public final ModConfigSpec.ConfigValue<Float> albedoBrightness;
        //public final ModConfigSpec.BooleanValue enableTubeCaching;
        public final ModConfigSpec.BooleanValue enableGateSounds;
        public final ModConfigSpec.BooleanValue serverCircuitSavingOpOnly;

        General(ModConfigSpec.Builder builder) {
            builder.push("WorldGen").comment("Toggle blocks being generated into the world");
                builder.push("Volcano").comment("Volcano related configs");
                    generateVolcano = builder.comment("Generate Volcano").translation("bluepower.config.volcano.generate").define("generateVolcano", true);
                    volcanoSpawnChance = builder.comment("Volcano Spawn Chance").translation("bluepower.config.volcano_spawn_chance").define("volcanoSpawnChance", 0.005);
                    generateTungstenInVolcano = builder.comment("Possible to generate Tungsten in the Volcano").translation("bluepower.config.volcano.tungsten.generate").define("generateTungstenInVolcano", true);
                    volcanoActiveToInactiveRatio = builder.comment("Volcano Active To Inactive Ratio").translation("bluepower.config.volcano_inactive_ratio").define("volcanoActiveToInactiveRatio", 0.3);
                    volcanoBiomeCategoryWhitelist = builder.comment("Biomes that volcanoes should generate").translation("bluepower.config.volcano_biomecategory_whitelist").define("volcanoBiomeCategoryWhitelist", "taiga,extreme_hills,jungle,mesa,plains,savanna,icy,beach,forest,ocean,desert,river,swamp,mushroom");
                builder.pop();
            builder.pop();
            builder.push("Recipes").comment("Toggle recipes to be enabled or not");
                alloyFurnaceBlacklist = builder.comment( "Any item name (minecraft:bucket,minecraft:minecart) added here will be blacklisted from being able to melt down into its raw materials.").translation("bluepower.config.alloy_furnace.blacklist").define("alloyFurnaceBlacklist", "minecraft:iron_nugget,minecraft:gold_nugget,minecraft:gold_ingot,minecraft:iron_ingot");
                alloyFurnaceDatapackGenerator = builder.comment( "Generate Json Datapack for Alloy Furnace (Only used to generate recycling recipes)").translation("bluepower.config.alloy_furnace.datapack").define("alloyFurnaceDatapackGenerator", true);
            builder.pop();

            /*
            builder.push(Refs.CONFIG_TUBES).comment("Tube Options");
            Property prop = config.get(Refs.CONFIG_TUBES, "Enable Tube Caching", true);
            prop.setComment("When enabled, the Tube routing is more friendly for the CPU. In return it uses a bit more RAM. Caching also may contain bugs still.");
            enableTubeCaching = prop.getBoolean();

            prop = config.get(Refs.CONFIG_TUBES, "Tube Render Mode", "auto");
            prop.setComment("When encountering FPS issues with tubes with lots of items in it. Valid modes: \"normal\": Normal rendering, \"reduced\": All items going through tubes will display as 'one' item, \"none\": Only a small dot renders, \"auto\": will switch to \"normal\" on fancy graphics mode, and to \"reduced\" otherwise.");
            String tubeRenderMode = prop.getString();
            if (!tubeRenderMode.equals("normal") && !tubeRenderMode.equals("reduced") && !tubeRenderMode.equals("none")) {
                tubeRenderMode = "auto";
            }*/

            builder.push("Other").comment("Miscellaneous other configs");
                serverCircuitSavingOpOnly = builder.comment("Server Template Saving by Ops only").translation("bluepower.config.template_ops_only").define("ServerTemplateOpsonly", false);
                enableGateSounds = builder.comment("Enable Gate Ticking Sounds").translation("bluepower.config.ticking_sounds").define("tickingSounds", true);
                albedoBrightness = builder.comment("Albedo Support Lamp Brightness").translation("bluepower.config.albedo_brightness").define("albedoBrightness", 0.01F);
            builder.pop();
            }

    }
    public static final ModConfigSpec spec = BUILDER.build();

}
