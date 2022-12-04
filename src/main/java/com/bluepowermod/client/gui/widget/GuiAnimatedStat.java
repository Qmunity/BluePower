package com.bluepowermod.client.gui.widget;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GuiAnimatedStat extends BaseWidget implements IGuiAnimatedStat, IGuiWidget {

    private IGuiAnimatedStat affectingStat;
    private ItemStack iStack;
    private String texture = "";
    private final Screen gui;
    private final List<String> textList = new ArrayList<String>();
    private int baseX;
    private int baseY;
    private int affectedY;
    private int width;
    private int height;

    private int oldBaseX;
    private int oldAffectedY;
    private int oldWidth;
    private int oldHeight;
    private boolean isClicked = false;
    private int minWidth = 17;
    private int minHeight = 17;
    private final int backGroundColor;
    private String title;
    private boolean leftSided; // this boolean determines if the stat is going
    // to expand to the left or right.
    private boolean doneExpanding;
    private ItemRenderer itemRenderer;
    private float textSize;
    private float textScale = 1F;
    public static final int MAX_CHAR = 28;
    public static final int ANIMATED_STAT_SPEED = 10;
    private ResourceLocation iconResLoc;
    private IWidgetListener listener;

    public GuiAnimatedStat(Screen gui, String title, int xPos, int yPos, int backGroundColor, IGuiAnimatedStat affectingStat,
                           boolean leftSided) {

        super(-1, xPos, yPos, yPos, backGroundColor);

        this.gui = gui;
        baseX = xPos;
        baseY = yPos;
        this.affectingStat = affectingStat;
        width = minWidth;
        height = minHeight;
        this.backGroundColor = backGroundColor;
        setTitle(title);
        texture = "";
        this.leftSided = leftSided;
        if (gui != null) {
            Window sr = Minecraft.getInstance().getWindow();
            if (sr.getGuiScaledWidth() < 520) {
            textSize = (sr.getGuiScaledWidth() - 220) * 0.0033F;
            } else {
                textSize = 1F;
            }
        } else {
            textSize = 1;
        }

        affectedY = baseY;
        if (affectingStat != null) {
            affectedY += affectingStat.getAffectedY() + affectingStat.getHeight();
        }
    }

    public GuiAnimatedStat(Screen gui, int backgroundColor) {

        this(gui, "", 0, 0, backgroundColor, null, false);
    }

    public GuiAnimatedStat(Screen gui, int backgroundColor, ItemStack icon) {

        this(gui, backgroundColor);
        iStack = icon;
    }

    public GuiAnimatedStat(Screen gui, int backgroundColor, String texture) {

        this(gui, backgroundColor);
        this.texture = texture;
    }

    public GuiAnimatedStat(Screen gui, String title, ItemStack icon, int xPos, int yPos, int backGroundColor,
                           IGuiAnimatedStat affectingStat, boolean leftSided) {

        this(gui, title, xPos, yPos, backGroundColor, affectingStat, leftSided);
        iStack = icon;
    }

    public GuiAnimatedStat(Screen gui, String title, String texture, int xPos, int yPos, int backGroundColor,
                           IGuiAnimatedStat affectingStat, boolean leftSided) {

        this(gui, title, xPos, yPos, backGroundColor, affectingStat, leftSided);
        this.texture = texture;
    }

    @Override
    public void setParentStat(IGuiAnimatedStat stat) {

        affectingStat = stat;
    }

    @Override
    public Rectangle getButtonScaledRectangle(int origX, int origY, int width, int height) {

        int scaledX = (int) ((origX - baseX - (leftSided ? width : 0)) * textSize);
        int scaledY = (int) ((origY - affectedY) * textSize);

        // scaledX = (int)(origX * textSize);
        // scaledY = (int)(origY * textSize);
        return new Rectangle(scaledX + baseX + (leftSided ? (int) (width * textSize) : 0), scaledY + affectedY, (int) (width * textSize),
                (int) (height * textSize));
    }

    @Override
    public void scaleTextSize(float scale) {

        textSize *= scale;
        textScale = scale;
    }

    @Override
    public boolean isLeftSided() {

        return leftSided;
    }

    @Override
    public void setLeftSided(boolean leftSided) {

        this.leftSided = leftSided;
    }

    @Override
    public IGuiAnimatedStat setText(List<String> text) {

        textList.clear();
        for (String line : text) {
            textList.addAll(Arrays.asList(WordUtils.wrap(I18n.get(line), (int) (MAX_CHAR / textScale)).split(System.getProperty("line.separator"))));
        }
        return this;
    }

    @Override
    public IGuiAnimatedStat setText(String text) {

        textList.clear();
        textList.addAll(Arrays.asList(WordUtils.wrap(I18n.get(text), (int) (MAX_CHAR / textScale)).split(System.getProperty("line.separator"))));
        return this;
    }

    @Override
    public void setTextWithoutCuttingString(List<String> text) {

        textList.clear();
        textList.addAll(text);
    }

    @Override
    public void setMinDimensionsAndReset(int minWidth, int minHeight) {

        this.minWidth = minWidth;
        this.minHeight = minHeight;
        width = minWidth;
        height = minHeight;
    }

    @Override
    public void update() {

        oldBaseX = baseX;
        oldAffectedY = affectedY;
        oldWidth = width;
        oldHeight = height;

        Font fontRenderer = Minecraft.getInstance().font;
        doneExpanding = true;
        if (isClicked) {
            // calculate the width and height needed for the box to fit the
            // strings.
            int maxWidth = getMaxWidth(fontRenderer);
            int maxHeight = getMaxHeight(fontRenderer);
            // expand the box

            for (int i = 0; i < ANIMATED_STAT_SPEED; i++) {
                if (width < maxWidth) {
                    width++;
                    doneExpanding = false;
                }
                if (height < maxHeight) {
                    height++;
                    doneExpanding = false;
                }
                if (width > maxWidth)
                    width--;
                if (height > maxHeight)
                    height--;
            }

        } else {
            for (int i = 0; i < ANIMATED_STAT_SPEED; i++) {
                if (width > minWidth)
                    width--;
                if (height > minHeight)
                    height--;
            }
            doneExpanding = false;
        }

        affectedY = baseY;
        if (affectingStat != null) {
            affectedY += affectingStat.getAffectedY() + affectingStat.getHeight();
        }
    }

    protected int getMaxWidth(Font fontRenderer) {

        int maxWidth = fontRenderer.width(title);

        for (String line : textList) {
            if (fontRenderer.width(line) > maxWidth)
                maxWidth = fontRenderer.width(line);
        }
        maxWidth = (int) (maxWidth * textSize);
        maxWidth += 20;
        return maxWidth;
    }

    protected int getMaxHeight(Font fontRenderer) {

        int maxHeight = 12;
        if (textList.size() > 0) {
            maxHeight += 4 + textList.size() * 10;
        }
        maxHeight = (int) (maxHeight * textSize);
        return maxHeight;
    }

    @Override
    public void render(PoseStack matrixStack, Font fontRenderer, float zLevel, float partialTicks) {

        int renderBaseX = (int) (oldBaseX + (baseX - oldBaseX) * partialTicks);
        int renderAffectedY = (int) (oldAffectedY + (affectedY - oldAffectedY) * partialTicks);
        int renderWidth = (int) (oldWidth + (width - oldWidth) * partialTicks);
        int renderHeight = (int) (oldHeight + (height - oldHeight) * partialTicks);

        if (leftSided)
            renderWidth *= -1;
        GuiComponent.fill(matrixStack, renderBaseX, renderAffectedY /* + 1 */, renderBaseX + renderWidth /*- 1*/, renderAffectedY + renderHeight,
                backGroundColor);

        RenderSystem.disableTexture();
        RenderSystem.lineWidth(3.0F);
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buff = tess.getBuilder();
        buff.begin(VertexFormat.Mode.LINE_STRIP, DefaultVertexFormat.POSITION_TEX);
        buff.vertex(renderBaseX, renderAffectedY, zLevel);
        buff.vertex(renderBaseX + renderWidth, renderAffectedY, zLevel);
        buff.vertex(renderBaseX + renderWidth, renderAffectedY + renderHeight, zLevel);
        buff.vertex(renderBaseX, renderAffectedY + renderHeight, zLevel);
        tess.end();
        RenderSystem.enableTexture();
        if (leftSided)
            renderWidth *= -1;
        // if done expanding, draw the information
        if (doneExpanding) {
            matrixStack.pushPose();
            matrixStack.translate(renderBaseX + (leftSided ? -renderWidth : 16), renderAffectedY, 0);
            matrixStack.scale(textSize, textSize, textSize);
            matrixStack.translate(-renderBaseX - (leftSided ? -renderWidth : 16), -renderAffectedY, 0);
            fontRenderer.drawShadow(matrixStack, title, renderBaseX + (leftSided ? -renderWidth + 2 : 18), renderAffectedY + 2, 0xFFFF00);
            for (int i = 0; i < textList.size(); i++) {

                if (textList.get(i).contains("\u00a70") || textList.get(i).contains(ChatFormatting.DARK_RED.toString())) {
                    fontRenderer.draw(matrixStack, textList.get(i), renderBaseX + (leftSided ? -renderWidth + 2 : 18), renderAffectedY + i * 10
                            + 12, 0xFFFFFF);
                } else {
                    fontRenderer.drawShadow(matrixStack, textList.get(i), renderBaseX + (leftSided ? -renderWidth + 2 : 18), renderAffectedY
                            + i * 10 + 12, 0xFFFFFF);
                }
            }

            matrixStack.popPose();
        }
        if (renderHeight > 16 && renderWidth > 16) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (iStack == null) {
                if (iconResLoc == null)
                    iconResLoc = new ResourceLocation(texture);
                drawTexture(matrixStack.last().pose(), iconResLoc, renderBaseX - (leftSided ? 16 : 0), renderAffectedY);
            } else if (gui != null || !(iStack.getItem() instanceof BlockItem)) {
                renderItem(matrixStack, fontRenderer, renderBaseX - (leftSided ? 16 : 0), renderAffectedY, iStack);
            }
        }
    }

    protected void renderItem(PoseStack matrixStack, Font fontRenderer, int x, int y, ItemStack stack) {

        if (itemRenderer == null)
            itemRenderer = new ItemRenderer(Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getItemRenderer().getItemModelShaper().getModelManager(), Minecraft.getInstance().getItemColors(), null);
        matrixStack.pushPose();
        matrixStack.translate(0, 0, -50);
        //GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        //TODO: RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.renderAndDecorateItem(stack, x, y);
        itemRenderer.renderGuiItemDecorations(fontRenderer, stack, x, y, null);

        //GL11.glEnable(GL11.GL_ALPHA_TEST);
        //TODO: RenderHelper.turnOff();
        //GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        matrixStack.popPose();
    }

    public static void drawTexture(Matrix4f matrixStack, ResourceLocation texture, int x, int y) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrixStack, x, y + 16, 0).uv(0.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrixStack,x + 16, y + 16, 0).uv(1.0F, 1.0F).endVertex();
        bufferbuilder.vertex(matrixStack,x + 16, y, 0).uv(1.0F, 0.0F).endVertex();
        bufferbuilder.vertex(matrixStack, x, y, 0).uv(0.0F, 0.0F).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    /*
     * button: 0 = left 1 = right 2 = middle
     */
    @Override
    public void onMouseClicked(int x, int y, int button) {

        if (button == 0 && mouseIsHoveringOverStat(x, y)) {
            isClicked = !isClicked;
            listener.actionPerformed(this);
        }
    }

    @Override
    public void closeWindow() {

        isClicked = false;
    }

    @Override
    public void openWindow() {

        isClicked = true;
    }

    @Override
    public boolean isClicked() {

        return isClicked;
    }

    private boolean mouseIsHoveringOverIcon(int x, int y) {

        if (leftSided) {
            return x <= baseX && x >= baseX - 16 && y >= affectedY && y <= affectedY + 16;
        } else {
            return x >= baseX && x <= baseX + 16 && y >= affectedY && y <= affectedY + 16;
        }
    }

    private boolean mouseIsHoveringOverStat(int x, int y) {

        if (leftSided) {
            return x <= baseX && x >= baseX - width && y >= affectedY && y <= affectedY + height;
        } else {
            return x >= baseX && x <= baseX + width && y >= affectedY && y <= affectedY + height;
        }
    }

    @Override
    public int getAffectedY() {

        return affectedY;
    }

    @Override
    public int getBaseX() {

        return baseX;
    }

    @Override
    public int getBaseY() {

        return baseY;
    }

    @Override
    public int getHeight() {

        return height;
    }

    @Override
    public int getWidth() {

        return width;
    }

    @Override
    public void setBaseY(int y) {

        baseY = y;
    }

    @Override
    public void setTitle(String title) {

        this.title = I18n.get(title);
    }

    @Override
    public boolean isDoneExpanding() {

        return doneExpanding;
    }

    @Override
    public void setBaseX(int x) {

        baseX = x;
    }

    @Override
    public String getTitle() {

        return title;
    }

    @Override
    public void setListener(IWidgetListener gui) {

        listener = gui;
    }

    @Override
    public int getID() {

        return -1;
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTick) {

        render(matrixStack, Minecraft.getInstance().font, 0, partialTick);

    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

        if (mouseIsHoveringOverIcon(mouseX, mouseY)) {
            curTip.add(title);
        }
    }

    @Override
    public Rectangle getBounds() {

        return new Rectangle(baseX - (leftSided ? width : 0), affectedY, width, height);
    }

}
