/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.gui.gate;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * @author MineMaarten
 */

@SideOnly(Side.CLIENT)
public abstract class GuiGateSingleTime extends GuiGate {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/gate.png");
    private static final String[] buttonTexts = { "-10s", "-1s", "-50ms", "+50ms", "+1s", "+10s" };
    private static final int[] buttonActions = { -200, -20, -1, 1, 20, 200 };

    public GuiGateSingleTime(GateBase gate) {

        super(gate, 228, 66);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {

        super.initGui();
        int buttonWidth = 35;
        for (int i = 0; i < buttonTexts.length; i++) {
            buttonList.add(new GuiButton(i, guiLeft + 4 + i * (buttonWidth + 2), guiTop + 35, buttonWidth, 20, buttonTexts[i]));
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {

        int newTimerValue = getCurrentIntervalTicks() + buttonActions[button.id];
        if (newTimerValue <= 1)
            newTimerValue = 2;
        sendToServer(0, newTimerValue);
    }

    @Override
    protected ResourceLocation getTexture() {

        return resLoc;
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {

        super.drawScreen(x, y, partialTicks);
        drawCenteredString(fontRendererObj, I18n.format("gui.timerInterval") + ": " + getTimerValue(getCurrentIntervalTicks()), guiLeft
                + xSize / 2, guiTop + 10, 0xFFFFFF);
    }

    private String getTimerValue(int ticks) {

        int time = ticks * 50;
        if (time >= 1000) {
            return time / 1000 + "." + time % 1000 + "s";
        } else {
            return time + "ms";
        }
    }

    protected abstract int getCurrentIntervalTicks();

}
