package com.bluepowermod.block.gates;

import com.bluepowermod.tile.tier1.gate.TileGate;
import com.bluepowermod.tile.tier1.gate.TileRepeater;
import com.bluepowermod.util.MultipartUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BlockGateRepeater extends BlockGateBase{
    @Override
    protected boolean isSideSource(Side side, BlockState blockState, TileGate gate) {
        return side == Side.FRONT;
    }

    @Override
    protected boolean checkPower(BlockState state, TileGate gate, boolean onTick) {
        boolean in = MultipartUtils.getRedstonePower(Side.BACK, state, gate.getLevel(), gate.getBlockPos()) > 0;
        gate.setPowered(Side.BACK, in);
        return false;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TileRepeater(blockPos, blockState);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        TileGate gate = getGateTile(state, level.getBlockEntity(pos));
        if (gate instanceof TileRepeater repeater){
            repeater.cycleDelay(player.isCrouching());
            player.displayClientMessage(Component.translatable("info.bluepower.repeater.delay", repeater.getDelay()), true);
            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public boolean ticks() {
        return true;
    }
}
