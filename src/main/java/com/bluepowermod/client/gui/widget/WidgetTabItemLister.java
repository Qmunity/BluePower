package com.bluepowermod.client.gui.widget;

import java.util.ArrayList;
import java.util.List;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.container.stack.TubeStack;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;

public class WidgetTabItemLister extends GuiAnimatedStat {

    private List<TubeStack> showingItems = new ArrayList<TubeStack>();
    private static final int MAX_ITEMS_X = 5;

    public WidgetTabItemLister(Screen gui, int backgroundColor) {

        super(gui, backgroundColor);
    }

    public WidgetTabItemLister(Screen gui, int backgroundColor, ItemStack icon) {

        super(gui, backgroundColor, icon);
    }

    public WidgetTabItemLister(Screen gui, int backgroundColor, String texture) {

        super(gui, backgroundColor, texture);
    }

    public WidgetTabItemLister(Screen gui, String title, ItemStack icon, int xPos, int yPos, int backGroundColor,
                               IGuiAnimatedStat affectingStat, boolean leftSided) {

        super(gui, title, icon, xPos, yPos, backGroundColor, affectingStat, leftSided);
    }

    public WidgetTabItemLister(Screen gui, String title, String texture, int xPos, int yPos, int backGroundColor,
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
    public void render(MatrixStack matrixStack, FontRenderer fontRenderer, float zLevel, float partialTicks) {

        this.setText(showingItems.size() == 0 ? "gui.bluepower:tab.stuffed.noItems" : "");
        super.render(matrixStack, fontRenderer, zLevel, partialTicks);
        if (isDoneExpanding()) {
            if (showingItems.size() > 0) {
                AbstractGui.fill(matrixStack, getBaseX() + 7, getAffectedY() + 16, getBaseX() + Math.min(MAX_ITEMS_X, showingItems.size()) * 18 + 9,
                        getAffectedY() + 36 + (showingItems.size() - 1) / MAX_ITEMS_X * 18, 0xFFAAAAAA);
            }
            for (int i = 0; i < MAX_ITEMS_X; i++) {
                for (int j = 0; i + j * MAX_ITEMS_X < showingItems.size(); j++) {
                    TubeStack stack = showingItems.get(i + j * MAX_ITEMS_X);
                    int x = getBaseX() + i * 18 + 9;
                    int y = getAffectedY() + j * 18 + 18;
                    if (stack.color != TubeColor.NONE) {
                        AbstractGui.fill(matrixStack, x, y, x + 16, y + 16, 0xFF000000 + MinecraftColor.values()[stack.color.ordinal()].getHex());
                    }
                    renderItem(fontRenderer, x, y, stack.stack);
                }
            }
        }
    }

}
