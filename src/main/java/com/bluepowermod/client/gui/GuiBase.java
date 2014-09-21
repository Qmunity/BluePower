/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.gui;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import codechicken.nei.VisiblityData;
import codechicken.nei.api.INEIGuiHandler;
import codechicken.nei.api.TaggedInventoryArea;

import com.bluepowermod.BluePower;
import com.bluepowermod.client.gui.widget.WidgetTabItemLister;
import com.bluepowermod.tileentities.TileMachineBase;
import com.bluepowermod.util.Refs;
import com.qmunity.lib.client.gui.widget.BaseWidget;
import com.qmunity.lib.client.gui.widget.GuiAnimatedStat;
import com.qmunity.lib.client.gui.widget.IGuiAnimatedStat;
import com.qmunity.lib.client.gui.widget.IGuiWidget;
import com.qmunity.lib.client.gui.widget.IWidgetListener;
import com.qmunity.lib.util.Dependencies;

import cpw.mods.fml.common.Optional;

/**
 * @author MineMaarten
 * @author K-4U
 */
public class GuiBase extends GuiContainer implements IWidgetListener, INEIGuiHandler {

    protected static final int COLOR_TEXT = 4210752;
    private final List<IGuiWidget> widgets = new ArrayList<IGuiWidget>();
    private final ResourceLocation resLoc;
    private IInventory inventory;
    private IGuiAnimatedStat lastLeftStat, lastRightStat;

    public GuiBase(Container mainContainer, ResourceLocation _resLoc) {

        super(mainContainer);
        resLoc = _resLoc;
    }

    public GuiBase(IInventory inventory, Container mainContainer, ResourceLocation _resLoc) {

        this(mainContainer, _resLoc);
        this.inventory = inventory;
    }

    @Override
    public void initGui() {
        super.initGui();
        lastLeftStat = lastRightStat = null;

        if (inventory instanceof TileMachineBase) {
            WidgetTabItemLister backlogTab = new WidgetTabItemLister(this, "gui.tab.stuffed", Refs.MODID + ":textures/gui/widgets/gui_stuffed.png",
                    guiLeft + xSize, guiTop + 5, 0xFFc13d40, null, false);
            lastRightStat = backlogTab;
            backlogTab.setItems(((TileMachineBase) inventory).getBacklog());
            addWidget(backlogTab);
        }

        String unlocalizedInfo = inventory.getInventoryName() + ".info";
        String localizedInfo = I18n.format(unlocalizedInfo);
        if (!unlocalizedInfo.equals(localizedInfo)) {
            addAnimatedStat("gui.tab.info", Refs.MODID + ":textures/gui/widgets/gui_info.png", 0xFF8888FF, isInfoStatLeftSided()).setText(
                    unlocalizedInfo);

        }
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
    public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {

        widgets.clear();
        lastLeftStat = lastRightStat = null;
        super.setWorldAndResolution(par1Minecraft, par2, par3);
    }

    public static void drawVerticalProgressBar(int xOffset, int yOffset, int h, int w, float value, float max, int color) {

        float perc = value / max;
        int height = (int) (h * perc);
        drawRect(xOffset, yOffset + h - height, xOffset + w, yOffset + h, color);
    }

    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow) {

        int stringWidth = fontRendererObj.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = w / 2 - stringWidth / 2 + xOffset;
        }

        fontRendererObj.drawString(text, newX, yOffset, COLOR_TEXT, useShadow);
    }

    public void drawString(int xOffset, int yOffset, String text, boolean useShadow) {

        fontRendererObj.drawString(text, xOffset, yOffset, COLOR_TEXT, useShadow);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 94 + 2, COLOR_TEXT);
        if (inventory != null) {
            drawHorizontalAlignedString(7, 5, xSize - 14, I18n.format(inventory.getInventoryName() + ".name"), false);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(resLoc);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

        for (IGuiWidget widget : widgets) {
            widget.render(i, j, f);
        }
    }

    @Override
    public void drawScreen(int x, int y, float partialTick) {

        super.drawScreen(x, y, partialTick);
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
            drawHoveringText(localizedTooltip, x, y, fontRendererObj);
        }

    }

    @Override
    protected void mouseClicked(int x, int y, int button) {

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
                if (w != widget && w instanceof GuiAnimatedStat && ((GuiAnimatedStat) w).isLeftSided() == ((GuiAnimatedStat) widget).isLeftSided())
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

    // -----------NEI support

    @Override
    @Optional.Method(modid = Dependencies.NEI)
    public VisiblityData modifyVisiblity(GuiContainer gui, VisiblityData currentVisibility) {
        for (IGuiWidget widget : widgets) {
            if (widget instanceof GuiAnimatedStat) {
                GuiAnimatedStat stat = (GuiAnimatedStat) widget;
                if (stat.isLeftSided()) {
                    if (stat.getWidth() > 20) {
                        currentVisibility.showUtilityButtons = false;
                        currentVisibility.showStateButtons = false;
                    }
                } else {
                    if (stat.getAffectedY() < 10) {
                        currentVisibility.showWidgets = false;
                    }
                }
            }
        }
        return currentVisibility;
    }

    /**
     * NEI will give the specified item to the InventoryRange returned if the player's inventory is full. return null for no range
     */
    @Override
    public Iterable<Integer> getItemSpawnSlots(GuiContainer gui, ItemStack item) {
        return null;
    }

    /**
     * @return A list of TaggedInventoryAreas that will be used with the savestates.
     */
    @Override
    @Optional.Method(modid = Dependencies.NEI)
    public List<TaggedInventoryArea> getInventoryAreas(GuiContainer gui) {
        return null;
    }

    /**
     * Handles clicks while an itemstack has been dragged from the item panel. Use this to set configurable slots and the like. Changes made to the
     * stackSize of the dragged stack will be kept
     * 
     * @param gui
     *            The current gui instance
     * @param mousex
     *            The x position of the mouse
     * @param mousey
     *            The y position of the mouse
     * @param draggedStack
     *            The stack being dragged from the item panel
     * @param button
     *            The button presed
     * @return True if the drag n drop was handled. False to resume processing through other routes. The held stack will be deleted if
     *         draggedStack.stackSize == 0
     */
    @Override
    public boolean handleDragNDrop(GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button) {
        return false;
    }

    /**
     * Used to prevent the item panel from drawing on top of other gui elements.
     * 
     * @param x
     *            The x coordinate of the rectangle bounding the slot
     * @param y
     *            The y coordinate of the rectangle bounding the slot
     * @param w
     *            The w coordinate of the rectangle bounding the slot
     * @param h
     *            The h coordinate of the rectangle bounding the slot
     * @return true if the item panel slot within the specified rectangle should not be rendered.
     */
    @Override
    public boolean hideItemPanelSlot(GuiContainer gui, int x, int y, int w, int h) {
        for (IGuiWidget stat : widgets) {
            if (stat.getBounds().intersects(new Rectangle(x, y, w, h)))
                return true;
        }
        return false;
    }

}
