/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 *
 *     @author Quetzi
 */

package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.client.gui.widget.WidgetColor;
import com.bluepowermod.client.gui.widget.WidgetFuzzySetting;
import com.bluepowermod.container.ContainerFilter;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileFilter;

/**
 * @author MineMaarten
 */
public class GuiFilter extends GuiContainerBaseBP {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/seedbag.png");
    protected TileFilter filter;

    public GuiFilter(ContainerFilter container, TileFilter filter, ResourceLocation resLoc) {

        super(filter, container, resLoc);
        this.filter = filter;
    }

    public GuiFilter(PlayerInventory invPlayer, TileFilter filter) {

        super(filter, new ContainerFilter(invPlayer, filter), resLoc);
        this.filter = filter;
    }

    @Override
    public void initGui() {

        super.initGui();
        WidgetColor colorWidget = new WidgetColor(0, guiLeft + 117, guiTop + 55);
        colorWidget.value = filter.filterColor.ordinal();
        addWidget(colorWidget);

        WidgetFuzzySetting fuzzyWidget = new WidgetFuzzySetting(1, guiLeft + 134, guiTop + 55);
        fuzzyWidget.value = filter.fuzzySetting;
        addWidget(fuzzyWidget);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        BaseWidget baseWidget = (BaseWidget) widget;
        BPNetworkHandler.INSTANCE.sendToServer(new MessageGuiUpdate(filter, widget.getID(), baseWidget.value));
    }
}
