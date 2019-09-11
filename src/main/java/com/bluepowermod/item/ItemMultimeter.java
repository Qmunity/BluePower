package com.bluepowermod.item;

import com.bluepowermod.api.power.CapabilityBlutricity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author MoreThanHidden
 */
public class ItemMultimeter extends ItemBase {


    public ItemMultimeter() {
        super(new Properties());
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        TileEntity tileEntity = context.getWorld().getTileEntity(context.getPos());
        if (context.getWorld().isRemote && tileEntity != null && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).isPresent()){
            String voltage = String.format("%.2f", tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null).getVoltage());
            String amps = String.format("%.2f", tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY).orElse(null).getCurrent());
            if(context.getPlayer() != null)
                context.getPlayer().sendMessage(new StringTextComponent( "Reading " + voltage + "V " + amps + "A"));
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }
}
