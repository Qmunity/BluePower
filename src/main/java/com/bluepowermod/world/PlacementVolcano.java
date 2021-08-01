package com.bluepowermod.world;

import com.bluepowermod.init.BPConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.util.Random;
import java.util.stream.Stream;

public class PlacementVolcano extends FeatureDecorator<NoneDecoratorConfiguration> {
    public PlacementVolcano(Codec<NoneDecoratorConfiguration> codec) {
        super(codec);
    }


    @Override
    public Stream<BlockPos> getPositions(DecorationContext decoratingHelper, Random random, NoneDecoratorConfiguration configIn, BlockPos pos) {
        WorldGenLevel world = ObfuscationReflectionHelper.getPrivateValue(DecorationContext.class, decoratingHelper, "field_242889_a");
        int chunkPosX = pos.getX() >> 8;
        int chuckPosZ = pos.getZ() >> 8;
        ((WorldgenRandom) random).setLargeFeatureSeed(world.getHeight(), chunkPosX, chuckPosZ);
        if (random.nextDouble() < (BPConfig.CONFIG.volcanoSpawnChance.get() * 16)) {
            return Stream.of(pos);
        } else {
            return Stream.empty();
        }
    }
}