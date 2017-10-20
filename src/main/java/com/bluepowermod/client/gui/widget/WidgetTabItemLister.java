package com.bluepowermod.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

import com.bluepowermod.part.tube.TubeStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;

public class WidgetTabItemLister extends GuiAnimatedStat {

    private List<TubeStack> showingItems = new ArrayList<TubeStack>();
    private static final int MAX_ITEMS_X = 5;

    public WidgetTabItemLister(GuiScreen gui, int backgroundColor) {

        super(gui, backgroundColor);
    }

    public WidgetTabItemLister(GuiScreen gui, int backgroundColor, ItemStack icon) {

        super(gui, backgroundColor, icon);
    }

    public WidgetTabItemLister(GuiScreen gui, int backgroundColor, String texture) {

        super(gui, backgroundColor, texture);
    }

    public WidgetTabItemLister(GuiScreen gui, String title, ItemStack icon, int xPos, int yPos, int backGroundColor,
            IGuiAnimatedStat affectingStat, boolean leftSided) {

        super(gui, title, icon, xPos, yPos, backGroundColor, affectingStat, leftSided);
    }

    public WidgetTabItemLister(GuiScreen gui, String title, String texture, int xPos, int yPos, int backGroundColor,
            IGuiAnimatedStat affectingStat, boolean leftSided) {

        super(gui, title, texture, xPos, yPos, backGroundColor, affectingStat, leftSided);
    }

    public WidgetTabItemLister setItems(List<TubeStack> showingItems) {

        this.showingItems = showingItems;
        return this;
    }

    @Override
    protected int getMaxWidth(FontRenderer fontRenderer) {

        return Math.max(super.getMaxWidth(fontRenderer), Math.min(showingItems.size(), MAX_ITEMS_X) * 18 + 18);
    }

    @Override
    protected int getMaxHeight(FontRenderer fontRenderer) {

        if (showingItems.size() == 0)
            return super.getMaxHeight(fontRenderer);
        return Math.max(super.getMaxHeight(fontRenderer), (showingItems.size() - 1) / MAX_ITEMS_X * 18 + 44);
    }

    @Override
    public void render(FontRenderer fontRenderer, float zLevel, float partialTicks) {

        this.setText(showingItems.size() == 0 ? "gui.bluepower:tab.stuffed.noItems" : "");
        super.render(fontRenderer, zLevel, partialTicks);
        if (isDoneExpanding()) {
            if (showingItems.size() > 0) {
                Gui.drawRect(getBaseX() + 7, getAffectedY() + 16, getBaseX() + Math.min(MAX_ITEMS_X, showingItems.size()) * 18 + 9,
                        getAffectedY() + 36 + (showingItems.size() - 1) / MAX_ITEMS_X * 18, 0xFFAAAAAA);
            }
            for (int i = 0; i < MAX_ITEMS_X; i++) {
                for (int j = 0; i + j * MAX_ITEMS_X < showingItems.size(); j++) {
                    TubeStack stack = showingItems.get(i + j * MAX_ITEMS_X);
                    int x = getBaseX() + i * 18 + 9;
                    int y = getAffectedY() + j * 18 + 18;
                    if (stack.color != TubeColor.NONE) {
                        Gui.drawRect(x, y, x + 16, y + 16, 0xFF000000 + ItemDye.DYE_COLORS[stack.color.ordinal()]);
                    }
                    renderItem(fontRenderer, x, y, stack.stack);
                }
            }
        }
    }

}
