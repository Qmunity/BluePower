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

package com.bluepowermod.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenMinable;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.Config;

import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenerationHandler implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

        if (!world.provider.isSurfaceWorld()) {
            return;
        }
        if (Config.generateAmethyst) {
            addOreToGenerate(random, Config.veinCountAmethyst, Config.veinSizeAmethyst, Config.minAmethystY, Config.maxAmethystY,
                    BPBlocks.amethyst_ore, world, chunkX, chunkZ);
        }
        if (Config.generateRuby) {
            addOreToGenerate(random, Config.veinCountRuby, Config.veinSizeRuby, Config.minRubyY, Config.maxRubyY, BPBlocks.ruby_ore, world, chunkX,
                    chunkZ);
        }
        if (Config.generateSapphire) {
            addOreToGenerate(random, Config.veinCountSapphire, Config.veinSizeSapphire, Config.minSapphireY, Config.maxSapphireY,
                    BPBlocks.sapphire_ore, world, chunkX, chunkZ);
        }
        if (Config.generateSilver) {
            addOreToGenerate(random, Config.veinCountSilver, Config.veinSizeSilver, Config.minSilverY, Config.maxSilverY, BPBlocks.silver_ore, world,
                    chunkX, chunkZ);
        }
        if (Config.generateTeslatite) {
            addOreToGenerate(random, Config.veinCountTeslatite, Config.veinSizeTeslatite, Config.minTeslatiteY, Config.maxTeslatiteY,
                    BPBlocks.teslatite_ore, world, chunkX, chunkZ);
        }
        if (Config.generateZinc) {
            addOreToGenerate(random, Config.veinCountZinc, Config.veinSizeZinc, Config.minZincY, Config.maxZincY, BPBlocks.zinc_ore, world, chunkX,
                    chunkZ);
        }
        if (Config.generateCopper) {
            addOreToGenerate(random, Config.veinCountCopper, Config.veinSizeCopper, Config.minCopperY, Config.maxCopperY, BPBlocks.copper_ore, world,
                    chunkX, chunkZ);
        }
        if (Config.generateTungsten) {
            addOreToGenerate(random, Config.veinCountTungsten, Config.veinSizeTungsten, Config.minTungstenY, Config.maxTungstenY,
                    BPBlocks.tungsten_ore, world, chunkX, chunkZ);
        }

        BiomeGenBase bgb = world.getWorldChunkManager().getBiomeGenAt(chunkX * 16 + 16, chunkZ * 16 + 16);

        int n = 0;
        if (bgb == BiomeGenBase.birchForest)
            n = 1;
        else if (bgb == BiomeGenBase.birchForestHills)
            n = 1;
        else if (bgb == BiomeGenBase.plains)
            n = 1;
        else if (bgb == BiomeGenBase.forest)
            n = 4;
        else if (bgb == BiomeGenBase.roofedForest)
            n = 4;

        for (int i = 0; i < n; i++) {
            int x = chunkX * 16 + random.nextInt(16) + 8;
            int y = random.nextInt(128);
            int z = chunkZ * 16 + random.nextInt(16) + 8;
            new WorldGenFlowers(BPBlocks.indigo_flower).generate(world, random, x, y, z);
        }

        if (Config.veinSizeMarble > 0) {
            for (int i = 0; i < 4; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = 32 + random.nextInt(32);
                int z = chunkZ * 16 + random.nextInt(16);
                new WorldGenMarble(BPBlocks.marble, random.nextInt(Config.veinSizeMarble)).generate(world, random, x, y, z);
            }
        }
        if (random.nextDouble() < Config.volcanoSpawnChance) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);//20
            int y = world.getHeightValue(x, z) + 50 + random.nextInt(40);

            if (world.getBlock(x, 10, z) == Blocks.lava && world.getHeightValue(x, z) <= 120) {
                new WorldGenVolcano().generate(world, random, x, y, z);
            }
        }
    }

    private void addOreToGenerate(Random random, int veinCount, int veinSize, int minY, int maxY, Block block, World world, int chunkX, int chunkZ) {

        for (int i = 0; i < veinCount; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = random.nextInt(maxY - minY) + minY;
            int z = chunkZ * 16 + random.nextInt(16);
            new WorldGenMinable(block, veinSize).generate(world, random, x, y, z);
        }
    }
}
