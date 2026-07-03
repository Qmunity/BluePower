package com.bluepowermod.mixin;

import com.bluepowermod.api.multipart.IBPPartBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.WallTorchBlock;;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;

@Debug(export = true)
@Mixin(RedstoneWallTorchBlock.class)
public class RedstoneWallTorchMixin implements IBPPartBlock {
    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return WallTorchBlock.getShape(state);
    }
}
