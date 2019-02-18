package com.bluepowermod.client.gui.widget;

/**
 * @author MineMaarten
 */
public class WidgetMode extends BaseWidget {

    public final int maxMode;

    public WidgetMode(int id, int x, int y, int textureX, int textureY, int maxMode, String... texture) {

        super(id, x, y, 14, 14, textureX, textureY, texture);
        this.maxMode = maxMode;
    }

    public WidgetMode(int id, int x, int y, int textureX, int maxMode, String... texture) {

        super(id, x, y, 14, 14, textureX, 0, texture);
        this.maxMode = maxMode;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        if (button == 0) {
            if (++value >= maxMode)
                value = 0;
        } else if (button == 1) {
            if (--value < 0)
                value = maxMode - 1;
        }
        super.onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected int getTextureV() {

        return super.getTextureV() + value * 14;
    }

    @Override
    protected int getTextureWidth() {

        return 256;
    }

    @Override
    protected int getTextureHeight() {

        return 256;
    }

}
