package com.bluepowermod.world;

import com.bluepowermod.init.BPConfig;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class PlacementVolcano extends Placement<NoPlacementConfig> {
    public PlacementVolcano(Function<Dynamic<?>, ? extends NoPlacementConfig> p_i51373_1_) {
        super(p_i51373_1_);
    }

    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends GenerationSettings> p_212848_2_, Random random, NoPlacementConfig p_212848_4_, BlockPos blockPos) {
        int chunkPosX = blockPos.getX() >> 8;
        int chuckPosZ = blockPos.getZ() >> 8;

        ((SharedSeedRandom) random).setLargeFeatureSeed(world.getSeed(), chunkPosX, chuckPosZ);

        if (random.nextDouble() < (BPConfig.CONFIG.volcanoSpawnChance.get() * 16)) {
            return Stream.of(blockPos);
        } else {
            return Stream.empty();
        }
    }
}