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
import com.bluepowermod.part.gate.digital.GateCounter;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiGateCounter extends GuiGate<GateBase<?, ?, ?, ?, ?, ?>> {

    private static final ResourceLocation resLoc = new ResourceLocation(Refs.MODID, "textures/gui/gateBig.png");
    private static final String[] buttonTexts = { "-25", "-5", "-1", "+1", "+5", "+25" };
    private static final int[] buttonActions = { -25, -5, -1, +1, +5, +25 };

    public GuiGateCounter(GateCounter gate) {

        super(gate, 228, 120);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {

        super.initGui();
        int buttonWidth = 35;
        for (int y = 0; y < 3; y++) {
            for (int i = 0; i < buttonTexts.length; i++) {
                buttonList.add(new GuiButton(y * buttonTexts.length + i, guiLeft + 4 + i * (buttonWidth + 2), guiTop + 25 + (y * 35), buttonWidth,
                        20, buttonTexts[i]));
            }
        }
    }

    @Override
    public void actionPerformed(GuiButton button) {

        int id = (int) Math.floor(button.id / buttonTexts.length);
        int subButton = button.id % buttonTexts.length;
        int val = 0;
        int min = 0;
        int max = 0;

        switch (id) {
        case 0:
            val = getCurrentMax();
            min = 1;
            max = Short.MAX_VALUE;
            break;
        case 1:
            val = getCurrentIncrement();
            min = 1;
            max = Short.MAX_VALUE;
            break;
        case 2:
            val = getCurrentDecrement();
            min = 1;
            max = Short.MAX_VALUE;
            break;
        }

        val += buttonActions[subButton];

        if (val < min)
            val = min;
        if (val > max)
            val = max;

        sendToServer(id, val);
    }

    @Override
    protected ResourceLocation getTexture() {

        return resLoc;
    }

    @Override
    public void renderGUI(int x, int y, float partialTicks) {

        drawCenteredString(fontRendererObj, I18n.format("gui.bluepower:counter.max") + ": " + getCurrentMax(), guiLeft + xSize / 2, guiTop + 10,
                0xFFFFFF);
        drawCenteredString(fontRendererObj, I18n.format("gui.bluepower:counter.increment") + ": " + getCurrentIncrement(), guiLeft + xSize / 2,
                guiTop + 10 + 38, 0xFFFFFF);
        drawCenteredString(fontRendererObj, I18n.format("gui.bluepower:counter.decrement") + ": " + getCurrentDecrement(), guiLeft + xSize / 2,
                guiTop + 10 + 38 + 35, 0xFFFFFF);
    }

    protected abstract int getCurrentMax();

    protected abstract int getCurrentIncrement();

    protected abstract int getCurrentDecrement();

}
