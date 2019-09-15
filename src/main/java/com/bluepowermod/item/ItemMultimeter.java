package com.bluepowermod.item;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.init.BPCreativeTabs;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author MoreThanHidden
 */
public class ItemMultimeter extends ItemBase {


    public ItemMultimeter() {
        super(new Properties(), BPCreativeTabs.tools);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
        if (tileEntity != null && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent()){
            if(!context.getWorld().isRemote) {
                double volts = tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null).getVoltage();
                double amps = tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null).getCurrent();
                String voltage = String.format("%.2f", volts);
                String ampere = String.format("%.2f", amps);
                String watts = String.format("%.2f", volts * amps);
                if (context.getPlayer() != null)
                    context.getPlayer().sendMessage(new StringTextComponent("Reading " + voltage + "V " + ampere + "A (" + watts + "W)"));
            }
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
