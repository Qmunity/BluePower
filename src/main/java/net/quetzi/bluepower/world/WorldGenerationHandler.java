package net.quetzi.bluepower.world;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenFlowers;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.init.Config;

import java.util.Random;

public class WorldGenerationHandler implements IWorldGenerator
{

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider)
    {
        if (!world.provider.isSurfaceWorld()) {
            return;
        }
        if (Config.generateAmethyst) {
            this.addOreToGenerate(Config.veinCountAmethyst, Config.veinSizeAmethyst, Config.minAmethystY, Config.maxAmethystY, BPBlocks.amethyst_ore, world, chunkX, chunkZ);
        }
        if (Config.generateRuby) {
            this.addOreToGenerate(Config.veinCountRuby, Config.veinSizeRuby, Config.minRubyY, Config.maxRubyY, BPBlocks.ruby_ore, world, chunkX, chunkZ);
        }
        if (Config.generateSapphire) {
            this.addOreToGenerate(Config.veinCountSapphire, Config.veinSizeSapphire, Config.minSapphireY, Config.maxSapphireY, BPBlocks.sapphire_ore, world, chunkX, chunkZ);
        }
        if (Config.generateSilver) {
            this.addOreToGenerate(Config.veinCountSilver, Config.veinSizeSilver, Config.minSilverY, Config.maxSilverY, BPBlocks.silver_ore, world, chunkX, chunkZ);
        }
        if (Config.generateNikolite) {
            this.addOreToGenerate(Config.veinCountNikolite, Config.veinSizeNikolite, Config.minNikoliteY, Config.maxNikoliteY, BPBlocks.nikolite_ore, world, chunkX, chunkZ);
        }
        if (Config.generateTin) {
            this.addOreToGenerate(Config.veinCountTin, Config.veinSizeTin, Config.minTinY, Config.maxTinY, BPBlocks.tin_ore, world, chunkX, chunkZ);
        }
        if (Config.generateCopper) {
            this.addOreToGenerate(Config.veinCountCopper, Config.veinSizeCopper, Config.minCopperY, Config.maxCopperY, BPBlocks.copper_ore, world, chunkX, chunkZ);
        }

        BiomeGenBase bgb = world.getWorldChunkManager().getBiomeGenAt(chunkX * 16 + 16, chunkZ * 16 + 16);

        int n = 0;
        if (bgb == BiomeGenBase.birchForest) n = 1;
        else if (bgb == BiomeGenBase.birchForestHills) n = 1;
        else if (bgb == BiomeGenBase.plains) n = 1;
        else if (bgb == BiomeGenBase.forest) n = 4;
        else if (bgb == BiomeGenBase.roofedForest) n = 4;

        for (int i = 0; i < n; i++) {
            int x = chunkX * 16 + random.nextInt(16) + 8;
            int y = random.nextInt(128);
            int z = chunkZ * 16 + random.nextInt(16) + 8;
            new WorldGenFlowers(BPBlocks.indigo_flower).generate(world, random, x, y, z);
        }

        for (int i = 0; i < 4; i++) {
            int x = chunkX * 16 + random.nextInt(16);
            int y = 32 + random.nextInt(32);
            int z = chunkZ * 16 + random.nextInt(16);
            new WorldGenMarble(BPBlocks.marble, random.nextInt(4096)).generate(world, random, x, y, z);
        }
        if (random.nextDouble() < Config.volcanoSpawnChance) {
            int x = chunkX * 16 + random.nextInt(16);
            int z = chunkZ * 16 + random.nextInt(16);
            int y = world.getHeightValue(x, z) + 20 + random.nextInt(10);//This number determines the topmost block of the volcano, it increases generation time exponentially when increased!
            if (world.getBlock(x, 10, z) == Blocks.lava) new WorldGenVolcano().generate(world, random, x, y, z);
        }
    }

    private void addOreToGenerate(int veinCount, int veinSize, int minY, int maxY, Block block, World world, int chunkX, int chunkZ)
    {
        Random rand = new Random(Integer.valueOf(chunkX).hashCode() + Integer.valueOf(chunkZ).hashCode());
        for (int i = 0; i < veinCount; i++) {
            int x = chunkX * 16 + rand.nextInt(16);
            int y = rand.nextInt(maxY - minY) + minY;
            int z = chunkZ * 16 + rand.nextInt(16);
            (new WorldGenMinable(block, veinSize)).generate(world, rand, x, y, z);
        }
    }
}
