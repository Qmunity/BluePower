package com.bluepowermod.world;

import com.bluepowermod.init.BPConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.Random;
import java.util.stream.Stream;

import static com.bluepowermod.world.BPWorldGen.VOLCANO_PLACEMENT;

public class PlacementVolcano extends PlacementModifier {
    private static final PlacementVolcano INSTANCE = new PlacementVolcano();
    public static final Codec<PlacementModifier> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Stream<BlockPos> getPositions(PlacementContext placementContext, RandomSource random, BlockPos pos) {
        WorldGenLevel world = placementContext.getLevel();
        int chunkPosX = pos.getX() >> 8;
        int chuckPosZ = pos.getZ() >> 8;
        ((WorldgenRandom) random).setDecorationSeed(world.getSeed(), chunkPosX, chuckPosZ);
        if (random.nextDouble() < (BPConfig.CONFIG.volcanoSpawnChance.get() * 6) && BPConfig.CONFIG.generateVolcano.get()) {
            return Stream.of(pos);
        } else {
            return Stream.empty();
        }
    }

    public static PlacementVolcano instance() {
        return INSTANCE;
    }

    @Override
    public PlacementModifierType<?> type() {
        return VOLCANO_PLACEMENT;
    }
}