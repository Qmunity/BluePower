package com.bluepowermod.client.gui.gate;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import com.bluepowermod.api.Refs;
import com.bluepowermod.part.gate.GateBase;

public abstract class GuiGateCounter extends GuiGate {

    private static final ResourceLocation resLoc        = new ResourceLocation(Refs.MODID, "textures/gui/gateBig.png");
    private static final String[]         buttonTexts   = { "-25", "-5", "-1", "+1", "+5", "+25" };
    private static final int[]            buttonActions = { -25, -5, -1, +1, +5, +25 };

    public GuiGateCounter(GateBase gate) {

        super(gate, 228, 120);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {

        super.initGui();
        int buttonWidth = 35;
        for (int y = 0; y < 3; y++) {
            for (int i = 0; i < buttonTexts.length; i++) {
                buttonList.add(new GuiButton(y * buttonTexts.length + i, guiLeft + 4 + i * (buttonWidth + 2), guiTop + 25 + (y * 35), buttonWidth, 20, buttonTexts[i]));
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

        if (val < min) val = min;
        if (val > max) val = max;

        System.out.println("Val: " + val + " - " + id);

        sendToServer(id, val);
    }

    @Override
    protected ResourceLocation getTexture() {

        return resLoc;
    }

    @Override
    public void drawScreen(int x, int y, float partialTicks) {

        super.drawScreen(x, y, partialTicks);
        drawCenteredString(fontRendererObj, I18n.format("gui.counterMax") + ": " + getCurrentMax(), guiLeft + xSize / 2, guiTop + 10, 0xFFFFFF);
        drawCenteredString(fontRendererObj, I18n.format("gui.counterIncrement") + ": " + getCurrentIncrement(), guiLeft + xSize / 2, guiTop + 10 + 38, 0xFFFFFF);
        drawCenteredString(fontRendererObj, I18n.format("gui.counterDecrement") + ": " + getCurrentDecrement(), guiLeft + xSize / 2, guiTop + 10 + 38 + 35, 0xFFFFFF);
    }

    protected abstract int getCurrentMax();

    protected abstract int getCurrentIncrement();

    protected abstract int getCurrentDecrement();

}
