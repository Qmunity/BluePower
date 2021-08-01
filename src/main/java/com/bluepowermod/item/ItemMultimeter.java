package com.bluepowermod.item;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.init.BPCreativeTabs;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;

import net.minecraft.world.item.Item.Properties;

/**
 * @author MoreThanHidden
 */
public class ItemMultimeter extends ItemBase {


    public ItemMultimeter() {
        super(new Properties(), BPCreativeTabs.tools);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (tileEntity != null && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent()){
            if(!context.getLevel().isClientSide) {
                double volts = tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null).getVoltage();
                double amps = tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null).getCurrent();
                String voltage = String.format("%.2f", volts);
                String ampere = String.format("%.2f", amps);
                String watts = String.format("%.2f", volts * amps);
                if (context.getPlayer() != null)
                    context.getPlayer().sendMessage(new StringTextComponent("Reading " + voltage + "V " + ampere + "A (" + watts + "W)"), Util.NIL_UUID);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
