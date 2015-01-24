package com.bluepowermod.client.gui;

import com.bluepowermod.container.ContainerBattery;
import com.bluepowermod.tile.tier2.TileBattery;
import com.bluepowermod.util.Refs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**!
 * @author Koen Beckers (K4Unl)
 */

public class GuiBattery extends GuiBase {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/batterybox.png");

    public GuiBattery(InventoryPlayer invPlayer, TileBattery battery) {

        super(battery, new ContainerBattery(invPlayer, battery), resLoc);
        ySize = 170;
    }
}
