package com.bluepowermod.client.gui;

import com.bluepowermod.client.gui.widget.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MineMaarten
 * @author K-4U
 */
public class GuiContainerBase<T extends Container> extends ContainerScreen<T> implements IWidgetListener {

    protected static final int COLOR_TEXT = 4210752;
    protected final List<IGuiWidget> widgets = new ArrayList<IGuiWidget>();
    protected IInventory inventory;
    protected final Container container;
    protected final ITextComponent title;
    protected final ResourceLocation resLoc;
    protected IGuiAnimatedStat lastLeftStat, lastRightStat;

    public GuiContainerBase(T container, PlayerInventory playerInventory, ITextComponent title, ResourceLocation resLoc){
        super(container, playerInventory, title);
        this.container = container;
        this.title = title;
        this.resLoc = resLoc;

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

    public void drawHorizontalAlignedString(int xOffset, int yOffset, int w, String text, boolean useShadow) {

        int stringWidth = font.getStringWidth(text);
        int newX = xOffset;
        if (stringWidth < w) {
            newX = w / 2 - stringWidth / 2 + xOffset;
        }

        font.drawString(text, newX, yOffset, COLOR_TEXT);
    }

    public void drawString(int xOffset, int yOffset, String text, boolean useShadow) {

        font.drawString(text, xOffset, yOffset, COLOR_TEXT);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        font.drawString(I18n.format("container.inventory"), 8, ySize - 94 + 2, COLOR_TEXT);
        if (inventory != null) {
            drawHorizontalAlignedString(7, 5, xSize - 14, I18n.format(container.getType() + ".name"), false);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(resLoc);

        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;

        blit(x, y, 0, 0, xSize, ySize);

        for (IGuiWidget widget : widgets) {
            widget.render(i, j, f);
        }
    }


    @Override
    public void render(int x, int y, float partialTick) {
        super.render(x, y, partialTick);
        this.renderHoveredToolTip(x, y);

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
    public void tick() {
        super.tick();
        for (IGuiWidget widget : widgets)
            widget.update();
    }

    @Override
    public void resize(Minecraft minecraft, int x, int y) {
        super.resize(minecraft, x, y);
        redraw();
    }

    public void redraw() {
        buttons.clear();
        widgets.clear();
        init();
    }

}