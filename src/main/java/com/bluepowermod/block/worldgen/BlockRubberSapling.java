package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockRubberSapling extends SaplingBlock {

    public BlockRubberSapling(Properties properties) {
        super(new AbstractTreeGrower() {
            @Override
            protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(@NotNull RandomSource source, boolean pHasFlowers) {
                return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(Refs.MODID, "rubber_tree"));
            }
        }, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
        BPBlocks.blockList.add(this);
    }

}
