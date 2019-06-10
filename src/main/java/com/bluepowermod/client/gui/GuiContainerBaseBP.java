package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.WidgetTabItemLister;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiContainerBaseBP extends GuiContainerBase {

    public GuiContainerBaseBP(Container mainContainer, ResourceLocation _resLoc) {

        super(mainContainer, _resLoc);
    }

    public GuiContainerBaseBP(IInventory inventory, Container mainContainer, ResourceLocation _resLoc) {

        super(inventory, mainContainer, _resLoc);
    }

    @Override
    public void initGui() {

        super.initGui();
        lastLeftStat = lastRightStat = null;

        if (inventory instanceof TileMachineBase) {
            WidgetTabItemLister backlogTab = new WidgetTabItemLister(this, "gui.bluepower:tab.stuffed", Refs.MODID
                    + ":textures/gui/widgets/gui_stuffed.png", guiLeft + xSize, guiTop + 5, 0xFFc13d40, null, false);
            lastRightStat = backlogTab;
            backlogTab.setItems(((TileMachineBase) inventory).getBacklog());
            addWidget(backlogTab);
        }

        String unlocalizedInfo = inventory.getName() + ".info";
        String localizedInfo = I18n.format(unlocalizedInfo);
        if (!unlocalizedInfo.equals(localizedInfo)) {
            addAnimatedStat("gui.bluepower:tab.info", Refs.MODID + ":textures/gui/widgets/gui_info.png", 0xFF8888FF, isInfoStatLeftSided()).setText(
                    unlocalizedInfo);

        }
    }

}
