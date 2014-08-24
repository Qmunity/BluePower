package com.bluepowermod.client.gui;

import java.util.List;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.WidgetFuzzySetting;
import com.bluepowermod.client.gui.widget.WidgetMode;
import com.bluepowermod.containers.ContainerItemDetector;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.tileentities.tier1.TileItemDetector;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GuiItemDetector extends GuiBase {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/item_detector.png");
    private final TileItemDetector itemDetector;

    public GuiItemDetector(InventoryPlayer invPlayer, TileItemDetector itemDetector) {

        super(itemDetector, new ContainerItemDetector(invPlayer, itemDetector), resLoc);
        this.itemDetector = itemDetector;
    }

    @Override
    public void initGui() {

        super.initGui();
        WidgetMode modeWidget = new WidgetMode(0, guiLeft + 152, guiTop + 10, 176, 3, Refs.MODID + ":textures/gui/item_detector.png") {

            @Override
            public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

                curTip.add("gui.mode");
                String mode;
                switch (value) {
                case 0:
                    mode = "gui.itemDetector.mode.item";
                    break;
                case 1:
                    mode = "gui.itemDetector.mode.stack";
                    break;
                default:
                    mode = "gui.itemDetector.mode.stuffed";

                }
                curTip.add(mode);
                if (shiftPressed) {
                    curTip.add(mode + ".info");
                } else {
                    curTip.add("gui.sneakForInfo");
                }
            }
        };
        modeWidget.value = itemDetector.mode;
        addWidget(modeWidget);

        WidgetFuzzySetting fuzzyWidget = new WidgetFuzzySetting(1, guiLeft + 152, guiTop + 55);
        fuzzyWidget.value = itemDetector.fuzzySetting;
        addWidget(fuzzyWidget);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        BaseWidget baseWidget = (BaseWidget) widget;
        NetworkHandler.sendToServer(new MessageGuiUpdate(itemDetector, widget.getID(), baseWidget.value));
    }
}
