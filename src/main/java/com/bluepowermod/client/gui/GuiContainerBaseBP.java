package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.WidgetTabItemLister;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class GuiContainerBaseBP<T extends AbstractContainerMenu> extends GuiContainerBase<T> {

    public GuiContainerBaseBP(T container, Inventory playerInventory, Component title, ResourceLocation resloc){
        super(container, playerInventory, title, resloc);
    }

    @Override
    public void init() {

        super.init();
        lastLeftStat = lastRightStat = null;

        if (inventory instanceof TileMachineBase) {
            WidgetTabItemLister backlogTab = new WidgetTabItemLister(this, "gui.bluepower:tab.stuffed", Refs.MODID
                    + ":textures/gui/widgets/gui_stuffed.png", leftPos + imageWidth, topPos + 5, 0xFFc13d40, null, false);
            lastRightStat = backlogTab;
            backlogTab.setItems(((TileMachineBase) inventory).getBacklog());
            addWidget(backlogTab);
        }

        //TODO: Widgets currently conflicting with progress GUI
        if(container.getType() != BPMenuType.ALLOY_FURNACE
                && container.getType() != BPMenuType.BLULECTRIC_ALLOY_FURNACE
                && container.getType() != BPMenuType.BLULECTRIC_FURNACE
                && container.getType() != BPMenuType.CANVAS_BAG
                && container.getType() != BPMenuType.SEEDBAG) {
            String unlocalizedInfo = title.getString() + ".info";
            addAnimatedStat("gui.bluepower:tab.info", Refs.MODID + ":textures/gui/widgets/gui_info.png", 0xFF8888FF, isInfoStatLeftSided()).setText(
                    unlocalizedInfo);
        }

    }

}
