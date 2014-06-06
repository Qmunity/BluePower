package net.quetzi.bluepower.events;


import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.quetzi.bluepower.blocks.BlockCrop;
import net.quetzi.bluepower.init.BPBlocks;

public class PlayerEventHandler {

    @SubscribeEvent
    public void onBoneMealEvent(BonemealEvent event) {
        if(event.block instanceof BlockCrop) {
            int meta = event.world.getBlockMetadata(event.x, event.y, event.z);
            if (meta >= 5) {
                return;
            }
            if (((BlockCrop) BPBlocks.flax_crop).fertilize(event.world, event.x, event.y, event.z)) {
                event.setResult(Result.ALLOW);
            }
        }
    }
}
