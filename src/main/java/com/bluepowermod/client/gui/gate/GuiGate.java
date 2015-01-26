/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui.gate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.text.WordUtils;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.gui.GuiScreenBase;
import com.bluepowermod.client.gui.widget.BaseWidget;
import com.bluepowermod.client.gui.widget.IGuiWidget;
import com.bluepowermod.client.gui.widget.IWidgetListener;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.message.MessageGuiUpdate;
import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * @author MineMaarten
 */

@SideOnly(Side.CLIENT)
public class GuiGate extends GuiScreenBase implements IWidgetListener {

    private final GateBase<?, ?, ?, ?, ?, ?> gate;
    private final List<IGuiWidget> widgets = new ArrayList<IGuiWidget>();

    public GuiGate(GateBase<?, ?, ?, ?, ?, ?> gate, int xSize, int ySize) {

        super(xSize, ySize);
        this.gate = gate;
    }

    public GateBase<?, ?, ?, ?, ?, ?> getGate() {

        return gate;
    }

    protected void sendToServer(int id, int value) {

        NetworkHandler.sendToServer(new MessageGuiUpdate(gate, id, value));
    }

    @Override
    public boolean doesGuiPauseGame() {

        return false;
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

    }

    protected void addWidget(IGuiWidget widget) {

        widgets.add(widget);
        widget.setListener(this);
    }

    @Override
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {

        widgets.clear();
        super.setWorldAndResolution(par1Minecraft, par2, par3);
    }

    public void redraw() {

        buttonList.clear();
        widgets.clear();
        initGui();
    }

    @Override
    public final void drawScreen(int x, int y, float partialTick) {

        super.drawScreen(x, y, partialTick);

        for (IGuiWidget widget : widgets)
            widget.render(x, y);

        renderGUI(x, y, partialTick);

        List<String> tooltip = new ArrayList<String>();
        boolean shift = BluePower.proxy.isSneakingInGui();
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y))
                widget.addTooltip(x, y, tooltip, shift);
        }
        if (!tooltip.isEmpty()) {
            List<String> localizedTooltip = new ArrayList<String>();
            for (String line : tooltip) {
                String localizedLine = I18n.format(line);
                String[] lines = WordUtils.wrap(localizedLine, 50).split(System.getProperty("line.separator"));
                for (String locLine : lines) {
                    localizedTooltip.add(locLine);
                }
            }
            drawHoveringText(localizedTooltip, x, y, fontRendererObj);
        }
    }

    protected void renderGUI(int x, int y, float partialTick) {

    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

        super.mouseClicked(x, y, button);
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y) && (!(widget instanceof BaseWidget) || ((BaseWidget) widget).enabled))
                widget.onMouseClicked(x, y, button);
        }
    }

    @Override
    protected ResourceLocation getTexture() {

        return null;
    }
}
