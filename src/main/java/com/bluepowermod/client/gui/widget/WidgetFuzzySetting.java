package com.bluepowermod.client.gui.widget;

import java.util.List;

import com.bluepowermod.util.Refs;

public class WidgetFuzzySetting extends WidgetMode {

    public WidgetFuzzySetting(int id, int x, int y) {
        super(id, x, y, 0, 3, new String[] { Refs.MODID + ":textures/gui/widgets/fuzzy_widget_normal.png",
                Refs.MODID + ":textures/gui/widgets/fuzzy_widget_fuzzy.png", Refs.MODID + ":textures/gui/widgets/fuzzy_widget_exact.png" });

    }

    @Override
    public void render(int mouseX, int mouseY) {
        textureIndex = value;
        super.render(mouseX, mouseY);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {
        curTip.add("gui.widget.fuzzy");
        switch (value) {
        case 0:
            curTip.add("gui.widget.fuzzy.normal");
            if (shiftPressed)
                curTip.add("gui.widget.fuzzy.normal.info");
            break;
        case 1:
            curTip.add("gui.widget.fuzzy.fuzzy");
            if (shiftPressed)
                curTip.add("gui.widget.fuzzy.fuzzy.info");
            break;
        case 2:
            curTip.add("gui.widget.fuzzy.exact");
            if (shiftPressed)
                curTip.add("gui.widget.fuzzy.exact.info");
            break;
        }
        if (!shiftPressed)
            curTip.add("gui.sneakForInfo");
    }

    @Override
    protected int getTextureV() {

        return 0;
    }

    @Override
    protected int getTextureWidth() {

        return 14;
    }

    @Override
    protected int getTextureHeight() {

        return 14;
    }
}
