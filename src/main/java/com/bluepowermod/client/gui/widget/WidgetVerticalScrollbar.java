package com.bluepowermod.client.gui.widget;

import com.bluepowermod.reference.Refs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import org.lwjgl.opengl.GL11;

public class WidgetVerticalScrollbar extends BaseWidget {
    public float currentScroll;
    private int states;
    private boolean listening;
    private boolean dragging;
    private boolean wasClicking;

    public WidgetVerticalScrollbar(int x, int y, int height) {
        this(-1, x, y, height);
    }

    public WidgetVerticalScrollbar(int id, int x, int y, int height) {
        super(id, x, y, 14, height, Refs.MODID + ":textures/gui/widgets/verticalScrollbar.png");
    }

    public WidgetVerticalScrollbar setStates(int states) {
        this.states = states;
        return this;
    }

    public WidgetVerticalScrollbar setCurrentState(int state) {
        if (state >= states)
            throw new IllegalArgumentException("State to high! max = " + states + ", tried to assign " + state);
        currentScroll = (float) state / states;
        return this;
    }

    public WidgetVerticalScrollbar setListening(boolean listening) {
        this.listening = listening;
        return this;
    }

    public int getState() {
        float scroll = currentScroll;
        scroll += 0.5F / states;
        return Mth.clamp((int) (scroll * states), 0, states);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        GL11.glColor4d(1, 1, 1, 1);
        if (dragging)
            currentScroll = (float) (mouseY - 7 - getBounds().y) / (getBounds().height - 17);
        currentScroll = Mth.clamp(currentScroll, 0, 1);
        guiGraphics.blit(textures[0], x, y, 12, 0, getBounds().width, 1, 26, 15);
        for (int i = 0; i < getBounds().height - 2; i++)
            guiGraphics.blit(textures[0], x, y + 1 + i, 12, 1, getBounds().width, 1, 26, 15);
        guiGraphics.blit(textures[0], x, y + getBounds().height - 1, 12, 14, getBounds().width, 1, 26, 15);

        if (!enabled)
            GL11.glColor4d(0.8, 0.8, 0.8, 1);
        guiGraphics.blit(textures[0], x + 1, y + 1 + (int) ((getBounds().height - 17) * currentScroll), 0, 0, 12, 15, 26, 15);
        GL11.glColor4d(1, 1, 1, 1);
    }

}
