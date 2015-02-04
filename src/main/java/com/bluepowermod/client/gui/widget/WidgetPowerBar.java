package com.bluepowermod.client.gui.widget;

import com.bluepowermod.util.Refs;

/**
 * Created by K-4U on 31-1-2015.
 */
public class WidgetPowerBar extends BaseWidget {

    public WidgetPowerBar(int id, int x, int y) {

        super(id, x, y, 7, 50, Refs.MODID + ":textures/gui/widgets/powerbar_widget_fill.png", Refs.MODID + ":textures/gui/widgets/powerbar_widget.png");
    }

    @Override
    protected int getTextureV() {

        return 0;
    }

    @Override
    protected int getTextureWidth() {

        return 7;
    }

    @Override
    protected int getTextureHeight() {

        return 50;
    }
}
