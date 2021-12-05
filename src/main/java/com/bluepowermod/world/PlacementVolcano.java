package com.bluepowermod.world;

import com.bluepowermod.init.BPConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
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
    public Stream<BlockPos> m_183381_(PlacementContext placementContext, Random random, BlockPos pos) {
        WorldGenLevel world = placementContext.m_191831_();
        int chunkPosX = pos.getX() >> 8;
        int chuckPosZ = pos.getZ() >> 8;
        ((WorldgenRandom) random).setDecorationSeed(world.getSeed(), chunkPosX, chuckPosZ);
        if (random.nextDouble() < (BPConfig.CONFIG.volcanoSpawnChance.get() * 16)) {
            return Stream.of(pos);
        } else {
            return Stream.empty();
        }
    }

    @Override
    public PlacementModifierType<?> m_183327_() {
        return VOLCANO_PLACEMENT;
    }
}