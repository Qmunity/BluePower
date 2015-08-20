package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.WidgetBarBase;
import com.bluepowermod.container.ContainerBattery;
import com.bluepowermod.container.ContainerChargingBench;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileBattery;
import com.bluepowermod.tile.tier2.TileChargingBench;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.List;

/**
 * @author Koen Beckers (K-4U)
 */
public class GuiChargingBench extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/chargingbench.png");
    private final TileChargingBench tile;

    public GuiChargingBench(InventoryPlayer invPlayer, TileChargingBench bench) {

        super(bench, new ContainerChargingBench(invPlayer, bench), resLoc);
        ySize = 187;
        tile = bench;
    }

    @Override
    public void initGui() {

        super.initGui();
        addWidget(new WidgetBarBase(guiLeft + 38, guiTop + 37, 18) {
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
        return 12;
    }

    @Override
    protected int getPowerBarYPos() {
        return 37;
    }
}
