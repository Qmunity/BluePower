package com.bluepowermod.client.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.client.gui.widget.WidgetBarBase;
import com.bluepowermod.client.gui.widget.WidgetPowerBar;
import com.bluepowermod.container.ContainerBattery;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileBattery;

/**!
 * @author MineMaarten, Koen Beckers (K4Unl)
 */

public class GuiBattery extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/batterybox.png");
    private final TileBattery tile;

    public GuiBattery(InventoryPlayer invPlayer, TileBattery battery) {

        super(battery, new ContainerBattery(invPlayer, battery), resLoc);
        ySize = 170;
        tile = battery;
    }

    @Override
    public void initGui() {

        super.initGui();
        addWidget(new WidgetPowerBar(guiLeft + 59, guiTop + 24, tile.getPowerHandler(ForgeDirection.UNKNOWN)));
        addWidget(new WidgetBarBase(guiLeft + 66, guiTop + 24) {
            @Override
            protected double getBarPercentage() {
                return tile.getBufferPercentage();
            }

        });
    }
}
