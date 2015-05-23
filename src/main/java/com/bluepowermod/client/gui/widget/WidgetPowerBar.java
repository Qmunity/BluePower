package com.bluepowermod.client.gui.widget;

import java.util.List;

import com.bluepowermod.api.power.IPowerBase;

public class WidgetPowerBar extends WidgetBarBase {
    private final IPowerBase handler;

    public WidgetPowerBar(int x, int y, IPowerBase handler) {
        super(x, y);
        this.handler = handler;
    }

    @Override
    protected double getBarPercentage() {
        return handler.getVoltage() / handler.getMaxVoltage();
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {
        curTip.add(String.format("%.1f", handler.getVoltage()) + " V");
    }
}
