package com.bluepowermod.client.gui;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.gui.widget.*;
import com.bluepowermod.container.ContainerAlloyFurnace;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author MineMaarten
 * @author K-4U
 */
public class GuiContainerBase<T extends Container> extends ContainerScreen<T> implements IWidgetListener {

    protected static final int COLOR_TEXT = 4210752;
    protected final List<IGuiWidget> widgets = new ArrayList<IGuiWidget>();
    protected IInventory inventory;
    protected IGuiAnimatedStat lastLeftStat, lastRightStat;

    public GuiContainerBase(T container, PlayerInventory playerInventory, ITextComponent title){
        super(container, playerInventory, title);

    }

    protected boolean isInfoStatLeftSided() {
        return true;
    }

    protected GuiAnimatedStat addAnimatedStat(String title, ItemStack icon, int color, boolean leftSided) {

        GuiAnimatedStat stat = new GuiAnimatedStat(this, title, icon, guiLeft + (leftSided ? 0 : xSize), leftSided && lastLeftStat != null
                || !leftSided && lastRightStat != null ? 3 : guiTop + 5, color, leftSided ? lastLeftStat : lastRightStat, leftSided);
        addWidget(stat);
        if (leftSided) {
            lastLeftStat = stat;
        } else {
            lastRightStat = stat;
        }
        return stat;
    }

    protected GuiAnimatedStat addAnimatedStat(String title, String icon, int color, boolean leftSided) {

        GuiAnimatedStat stat = new GuiAnimatedStat(this, title, icon, guiLeft + (leftSided ? 0 : xSize), leftSided && lastLeftStat != null
                || !leftSided && lastRightStat != null ? 3 : guiTop + 5, color, leftSided ? lastLeftStat : lastRightStat, leftSided);
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

    @Override
    public void setSize(int par2, int par3) {
        widgets.clear();
        lastLeftStat = lastRightStat = null;
        super.setSize( par2, par3);
    }

    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color) {

        float perc = value / max;
        int height = (int) (h * perc);
        drawRect(xOffset, yOffset + h - height, xOffset + w, yOffset + h, color);
    }

    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow) {

        int stringWidth = fontRenderer.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = w / 2 - stringWidth / 2 + xOffset;
        }

        fontRenderer.drawString(text, newX, yOffset, COLOR_TEXT, useShadow);
    }

    public void drawString(int xOffset, int yOffset, String text, boolean useShadow) {

        fontRenderer.drawString(text, xOffset, yOffset, COLOR_TEXT, useShadow);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        fontRenderer.drawString(I18n.format("container.inventory"), 8, ySize - 94 + 2, COLOR_TEXT);
        if (inventory != null) {
            drawHorizontalAlignedString(7, 5, xSize - 14, I18n.format(inventory.getName() + ".name"), false);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resLoc);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        blit(x, y, 0, 0, xSize, ySize);

        for (IGuiWidget widget : widgets) {
            widget.render(i, j, f);
        }
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        super.drawScreen(x, y, partialTick);
        this.renderHoveredToolTip(x, y);
        List<String> tooltip = new ArrayList<String>();
        boolean shift = BluePower.proxy.isSneakingInGui();
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y))
                widget.addTooltip(x, y, tooltip, shift);
        }
        if (!tooltip.isEmpty()) {
            List<String> localizedTooltip = new ArrayList<String>();
            for (String line : tooltip) {
                String localizedLine = I18n.format(line);
                String[] lines = WordUtils.wrap(localizedLine, 50).split(System.getProperty("line.separator"));
                for (String locLine : lines) {
                    localizedTooltip.add(locLine);
                }
            }
            drawHoveringText(localizedTooltip, x, y, fontRenderer);
        }

    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        super.mouseClicked(x, y, button);
        for (IGuiWidget widget : widgets) {
            if (widget.getBounds().contains(x, y) && (!(widget instanceof BaseWidget) || ((BaseWidget) widget).enabled))
                widget.onMouseClicked(x, y, button);
        }
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
    public void updateScreen() {

        super.updateScreen();
        for (IGuiWidget widget : widgets)
            widget.update();
    }

    public void redraw() {

        buttonList.clear();
        List<IGuiWidget> stats = new ArrayList<IGuiWidget>();
        for (IGuiWidget widget : widgets) {
            if (widget instanceof IGuiAnimatedStat) {
                stats.add(widget);
            }
        }
        widgets.clear();
        initGui();

        Iterator<IGuiWidget> iterator = widgets.iterator();
        while (iterator.hasNext()) {
            IGuiWidget widget = iterator.next();
            if (widget instanceof IGuiAnimatedStat) {
                iterator.remove();
            }
        }
        widgets.addAll(stats);
    }

}