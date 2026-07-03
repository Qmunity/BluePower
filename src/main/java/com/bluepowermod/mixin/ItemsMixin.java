package com.bluepowermod.mixin;

import com.bluepowermod.item.ItemBPPart;
import com.bluepowermod.item.RedstoneTorchItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Items.class)
public class ItemsMixin {
    @Shadow
    public static Item registerBlock(BlockItem item) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/world/item/Item$Properties;Lnet/minecraft/core/Direction;)Lnet/minecraft/world/item/StandingAndWallBlockItem;", ordinal = 12))
    private static StandingAndWallBlockItem bluepower$redirectRedstoneTorch(Block block, Block wallBlock, Properties properties, Direction direction){
        return new RedstoneTorchItem(block, wallBlock, properties, direction);
    }

    @Inject(method = "registerBlock(Lnet/minecraft/world/level/block/Block;)Lnet/minecraft/world/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void bluepower$injectRegisterBlock(Block block, CallbackInfoReturnable<Item> cir){
        if (block == Blocks.LEVER){
            cir.setReturnValue(registerBlock(new ItemBPPart(block, new Item.Properties())));
        }
    }
}
