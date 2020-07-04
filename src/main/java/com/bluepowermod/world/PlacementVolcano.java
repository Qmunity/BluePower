package com.bluepowermod.world;

import com.bluepowermod.init.BPConfig;
import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.placement.NoPlacementConfig;
import net.minecraft.world.gen.placement.Placement;

import java.util.Random;
import java.util.stream.Stream;

public class PlacementVolcano extends Placement<NoPlacementConfig> {
    public PlacementVolcano(Codec<NoPlacementConfig> codec) {
        super(codec);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator generatorIn, Random random, NoPlacementConfig configIn, BlockPos pos) {
        int chunkPosX = pos.getX() >> 8;
        int chuckPosZ = pos.getZ() >> 8;

        ((SharedSeedRandom) random).setLargeFeatureSeed(world.func_234938_ad_(), chunkPosX, chuckPosZ);

        if (random.nextDouble() < (BPConfig.CONFIG.volcanoSpawnChance.get() * 16)) {
            return Stream.of(pos);
        } else {
            return Stream.empty();
        }
    }
}