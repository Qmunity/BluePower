package net.quetzi.bluepower.world;

import java.util.Random;

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
        for (int i = 0; i < 20; i++) {
            if (i<2) {
                if (Config.generateMalachite) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxMalachiteY - Config.minMalachiteY) + Config.minMalachiteY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.malachite_ore, 7).generate(world, rand, x, y, z);
                }
                if (Config.generateRuby) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxRubyY - Config.minRubyY) + Config.minRubyY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.ruby_ore, 7).generate(world, rand, x, y, z);
                }
                if (Config.generateSapphire) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxSapphireY - Config.minSapphireY) + Config.minSapphireY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.sapphire_ore, 7).generate(world, rand, x, y, z);
                }
            }
            if (i<4) {
                if (Config.generateSilver) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxSilverY - Config.minSilverY) + Config.minSilverY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.silver_ore, 8).generate(world, rand, x, y, z);
                }
                if (Config.generateNikolite) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxNikoliteY - Config.minNikoliteY) + Config.minNikoliteY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.nikolite_ore, 10).generate(world, rand, x, y, z);
                }
            }
    
            if (i<10) {
                if (Config.generateTin) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxTinY - Config.minTinY) + Config.minTinY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.tin_ore, 8).generate(world, rand, x, y, z);
                }
            }
            if (i<20) {
                if (Config.generateCopper) {
                    int x = chunkX * 16 + rand.nextInt(16);
                    int y = rand.nextInt(Config.maxCopperY - Config.minCopperY) + Config.minCopperY;
                    int z = chunkZ * 16 + rand.nextInt(16);
                    new WorldGenMinable(Blocks.copper_ore, 8).generate(world, rand, x, y, z);
                }
            }
        }
    }
}
