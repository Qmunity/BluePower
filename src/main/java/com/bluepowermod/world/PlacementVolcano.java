package com.bluepowermod.world;

import com.bluepowermod.init.BPConfig;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
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
        if (random.nextDouble() < BPConfig.CONFIG.volcanoSpawnChance.get()) {
            if (world.getBlockState(new BlockPos(blockPos.getX(), 10, blockPos.getZ())).getBlock() == Blocks.LAVA && world.getHeight(Heightmap.Type.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) <= 90) {
                return Stream.of(blockPos);
            }
        }

        return Stream.empty();
    }
}