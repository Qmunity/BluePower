package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.WidgetPowerBar;
import com.bluepowermod.container.ContainerBattery;
import com.bluepowermod.tile.tier2.TileBattery;
import com.bluepowermod.util.Refs;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

/**!
 * @author Koen Beckers (K4Unl)
 */

public class GuiBattery extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/batterybox.png");
    private TileBattery tile;

    public GuiBattery(InventoryPlayer invPlayer, TileBattery battery) {

        super(battery, new ContainerBattery(invPlayer, battery), resLoc);
        ySize = 170;
        tile = battery;
    }


    @Override
    public void initGui() {

        super.initGui();
        WidgetPowerBar mainPowerWidget = new WidgetPowerBar(0, guiLeft + 59, guiTop + 25, tile.getAmpStored(), tile.getMaxAmp());
        addWidget(mainPowerWidget);
    }
}
