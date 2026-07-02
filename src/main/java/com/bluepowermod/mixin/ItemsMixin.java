package com.bluepowermod.mixin;

import com.bluepowermod.item.RedstoneTorchItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Items.class)
public class ItemsMixin {
    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/StandingAndWallBlockItem;", ordinal = 12))
    private static StandingAndWallBlockItem bluepower$redirectRedstoneTorch(Block block, Block wallBlock, Properties properties, Direction direction){
        return new RedstoneTorchItem(block, wallBlock, properties, direction);
    }
}
