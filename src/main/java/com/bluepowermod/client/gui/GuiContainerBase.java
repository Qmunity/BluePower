package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MineMaarten
 * @author K-4U
 */
public class GuiContainerBase<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> implements IWidgetListener {

    protected static final int COLOR_TEXT = 4210752;
    protected final List<IGuiWidget> widgets = new ArrayList<IGuiWidget>();
    protected Container inventory;
    protected final AbstractContainerMenu container;
    protected final Component title;
    protected final ResourceLocation resLoc;
    protected IGuiAnimatedStat lastLeftStat, lastRightStat;

    public GuiContainerBase(T container, Inventory playerInventory, Component title, ResourceLocation resLoc){
        super(container, playerInventory, title);
        this.container = container;
        this.title = title;
        this.resLoc = resLoc;

    }

    protected boolean isInfoStatLeftSided() {
        return true;
    }

    protected GuiAnimatedStat addAnimatedStat(String title, ItemStack icon, int color, boolean leftSided) {

        GuiAnimatedStat stat = new GuiAnimatedStat(this, title, icon, leftPos + (leftSided ? 0 : imageWidth), leftSided && lastLeftStat != null
                || !leftSided && lastRightStat != null ? 3 : topPos + 5, color, leftSided ? lastLeftStat : lastRightStat, leftSided);
        addWidget(stat);
        if (leftSided) {
            lastLeftStat = stat;
        } else {
            lastRightStat = stat;
        }
        return stat;
    }

    protected GuiAnimatedStat addAnimatedStat(String title, String icon, int color, boolean leftSided) {

        GuiAnimatedStat stat = new GuiAnimatedStat(this, title, icon, leftPos + (leftSided ? 0 : imageWidth), leftSided && lastLeftStat != null
                || !leftSided && lastRightStat != null ? 3 : topPos + 5, color, leftSided ? lastLeftStat : lastRightStat, leftSided);
        addWidget(stat);
        if (leftSided) {
            lastLeftStat = stat;
        } else {
            lastRightStat = stat;
        }
        return stat;
    }

    protected void addWidget(IGuiWidget widget) {

        widgets.add(widget);
        widget.setListener(this);
    }

   /* TODO: Check the resize
    @Override
    public void setSize( int par2, int par3) {
        widgets.clear();
        lastLeftStat = lastRightStat = null;
        super.setSize( par2, par3);
    }
   */

    public void drawHorizontalAlignedString(GuiGraphics guiGraphics, int xOffset, int yOffset, int w, String text, boolean useShadow) {

        int stringWidth = font.width(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = w / 2 - stringWidth / 2 + xOffset;
        }

        guiGraphics.drawString(font, text, newX, yOffset, COLOR_TEXT);
    }

    public void drawString(GuiGraphics guiGraphics, String text, int xOffset, int yOffset, boolean useShadow) {
        guiGraphics.drawString(font, text, xOffset, yOffset, COLOR_TEXT, useShadow);
    }


    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, I18n.get("block.bluepower." + title.getString()), (this.imageWidth / 2 - this.font.width(I18n.get("block.bluepower." + title.getString())) / 2), 6, COLOR_TEXT, false);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(resLoc, x, y, 0, 0, imageWidth, imageHeight);

        for (IGuiWidget widget : widgets) {
            widget.render(guiGraphics, i, j, f);
        }
    }


    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, x, y, partialTick);
        this.renderTooltip(guiGraphics, x, y);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y) && (!(widget instanceof BaseWidget) || ((BaseWidget) widget).enabled))
                widget.onMouseClicked((int) x, (int) y, button);
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public void actionPerformed(IGuiWidget widget) {

        if (widget instanceof GuiAnimatedStat && ((GuiAnimatedStat) widget).isClicked()) {
            for (IGuiWidget w : widgets) {
                if (w != widget && w instanceof GuiAnimatedStat
                        && ((GuiAnimatedStat) w).isLeftSided() == ((GuiAnimatedStat) widget).isLeftSided())
                    ((GuiAnimatedStat) w).closeWindow();
            }
        }
    }

    @Override
    protected void clearWidgets() {
        for (IGuiWidget widget : widgets)
            widget.update();
        super.clearWidgets();
    }

    @Override
    public void containerTick() {
        super.containerTick();
        for (IGuiWidget widget : widgets)
            widget.update();
    }

    @Override
    public void resize(Minecraft minecraft, int x, int y) {
        super.resize(minecraft, x, y);
        redraw();
    }

    public void redraw() {
        //TODO 1.17
        //buttons.clear();
        widgets.clear();
        init();
    }

}