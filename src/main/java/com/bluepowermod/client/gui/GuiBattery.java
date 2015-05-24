package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.client.gui.widget.WidgetBarBase;
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
        addWidget(new WidgetBarBase(guiLeft + 87, guiTop + 24, 18) {
            @Override
            protected double getBarPercentage() {
                return tile.getBufferPercentage();
            }

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {
                curTip.add((int) (getBarPercentage() * 100) + "%%");
            }
        });
    }

    @Override
    protected int getPowerBarXPos() {
        return 58;
    }

    @Override
    protected int getPowerBarYPos() {
        return 24;
    }
}
