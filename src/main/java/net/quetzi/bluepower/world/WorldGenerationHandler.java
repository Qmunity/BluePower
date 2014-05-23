package net.quetzi.bluepower.world;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.quetzi.bluepower.init.Blocks;
import net.quetzi.bluepower.init.Config;
import cpw.mods.fml.common.IWorldGenerator;

public class WorldGenerationHandler implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world,
            IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        if (!world.provider.isSurfaceWorld()) {
            return;
        }
        Random rand = new Random(Integer.valueOf(chunkX).hashCode() + Integer.valueOf(chunkZ).hashCode());
        if (Config.generateMalachite) {
            this.addOreToGenerate(Config.veinCountMalachite, Config.veinSizeMalachite, Config.minMalachiteY, Config.maxMalachiteY, Blocks.malachite_ore, world, chunkX, chunkZ);
        }
        if (Config.generateRuby) {
            this.addOreToGenerate(Config.veinCountRuby, Config.veinSizeRuby, Config.minRubyY, Config.maxRubyY, Blocks.ruby_ore, world, chunkX, chunkZ);
        }
        if (Config.generateSapphire) {
            this.addOreToGenerate(Config.veinCountSapphire, Config.veinSizeSapphire, Config.minSapphireY, Config.maxSapphireY, Blocks.sapphire_ore, world, chunkX, chunkZ);
        }
        if (Config.generateSilver) {
            this.addOreToGenerate(Config.veinCountSilver, Config.veinSizeSilver, Config.minSilverY, Config.maxSilverY, Blocks.silver_ore, world, chunkX, chunkZ);
        }
        if (Config.generateNikolite) {
            this.addOreToGenerate(Config.veinCountNikolite, Config.veinSizeNikolite, Config.minNikoliteY, Config.maxNikoliteY, Blocks.nikolite_ore, world, chunkX, chunkZ);
        }
        if (Config.generateTin) {
            this.addOreToGenerate(Config.veinCountTin, Config.veinSizeTin, Config.minTinY, Config.maxTinY, Blocks.tin_ore, world, chunkX, chunkZ);
        }
        if (Config.generateCopper) {
            this.addOreToGenerate(Config.veinCountCopper, Config.veinSizeCopper, Config.minCopperY, Config.maxCopperY, Blocks.copper_ore, world, chunkX, chunkZ);
        }
    }
    private void addOreToGenerate(int veinCount, int veinSize, int minY, int maxY, Block block, World world, int chunkX, int chunkZ) {
        Random rand = new Random(Integer.valueOf(chunkX).hashCode() + Integer.valueOf(chunkZ).hashCode());
        for (int i=0;i<veinCount;i++){
            int x = chunkX * 16 + rand.nextInt(16);
            int y = rand.nextInt(maxY - minY) + minY;
            int z = chunkZ * 16 + rand.nextInt(16);
            new WorldGenMinable(block, veinSize).generate(world,  rand,  x,  y, z);
        }
    }
}
