package com.bluepowermod.item;

import com.bluepowermod.api.power.CapabilityBlutricity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;

/**
 * @author MoreThanHidden
 */
public class ItemMultimeter extends ItemBase {


    public ItemMultimeter() {
        super(new Properties());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        var blulectricDevice = context.getLevel().getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, context.getClickedPos(), null);
        if (blulectricDevice != null){
            if(!context.getLevel().isClientSide) {
                double volts = blulectricDevice.getVoltage();
                double amps = blulectricDevice.getCurrent();
                String voltage = String.format("%.2f", volts);
                String ampere = String.format("%.2f", amps);
                String watts = String.format("%.2f", volts * amps);
                if (context.getPlayer() != null)
                    context.getPlayer().sendSystemMessage(Component.literal("Reading " + voltage + "V " + ampere + "A (" + watts + "W)"));
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
