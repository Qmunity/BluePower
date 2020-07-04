package com.bluepowermod.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

/**
 * @author MineMaarten
 */
public class BaseWidget implements IGuiWidget {

    private final int id;
    public int value; // just a generic value
    protected final int x, y;
    protected final int width;
    protected final int height;
    private final int textureU;
    private final int textureV;
    protected final ResourceLocation[] textures;
    protected int textureIndex = 0;
    protected IWidgetListener gui;
    public boolean enabled = true;

    public BaseWidget(int id, int x, int y, int width, int height, String... textureLocs) {

        this(id, x, y, width, height, 0, 0, textureLocs);
    }

    public BaseWidget(int id, int x, int y, int width, int height, int textureU, int textureV, String... textureLocs) {

        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textureU = textureU;
        this.textureV = textureV;
        textures = new ResourceLocation[textureLocs.length];
        for (int i = 0; i < textures.length; i++) {
            textures[i] = new ResourceLocation(textureLocs[i]);
        }
    }

    @Override
    public int getID() {

        return id;
    }

    @Override
    public void setListener(IWidgetListener gui) {

        this.gui = gui;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTick) {

        if (enabled) {
            GL11.glColor4d(1, 1, 1, 1);
        } else {
            GL11.glColor4d(0.2, 0.2, 0.2, 1);
        }
        if (textures.length > 0)
            Minecraft.getInstance().getTextureManager().bindTexture(textures[textureIndex]);
        AbstractGui.blit(matrixStack, x, y, getTextureU(), getTextureV(), width, height, getTextureWidth(), getTextureHeight());
    }

    protected int getTextureU() {

        return textureU;
    }

    protected int getTextureV() {

        return textureV;
    }

    protected int getTextureWidth() {

        return width;
    }

    protected int getTextureHeight() {

        return height;
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {

        Minecraft.getInstance().getSoundHandler()
                .play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        gui.actionPerformed(this);
    }

    @Override
    public Rectangle getBounds() {

        return new Rectangle(x, y, width, height);
    }

    @Override
    public void addTooltip(int mouseX, int mouseY, List<String> curTip, boolean shiftPressed) {

    }

    @Override
    public void update() {

    }

}
