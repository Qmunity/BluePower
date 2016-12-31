package com.bluepowermod.part.gate.component;

import com.bluepowermod.part.gate.GateBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class GateComponentLever extends GateComponent {

    private boolean state = false;

    private int layoutColor = -1;
    protected double x = 0, z = 0;

    public GateComponentLever(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();
    }

    public GateComponentLever(GateBase<?, ?, ?, ?, ?, ?> gate, double x, double z) {

        super(gate);

        this.x = x;
        this.z = z;
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        renderer.addTransformation(new Translation(x, 0, z));
        renderer.renderBox(new Vec3dCube(0, 2 / 16D, 0, 4 / 16D, 4 / 16D, 7 / 16D), Blocks.COBBLESTONE.getIcon(0, 0));
        renderer.removeTransformation();
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GL11.glPushMatrix();
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            Tessellator t = Tessellator.getInstance();

            TextureAtlasSprite icon = Blocks.LEVER.getIcon(0, 0);
            double minU = icon.getMinU();
            double minV = icon.getMinV();
            double maxU = icon.getMaxU();
            double maxV = icon.getMaxV();

            GL11.glTranslated(-x + 6 / 16D, 2 / 16D, -z + 11.5 / 16D);

            GL11.glTranslated(0, 0, 1 / 16D);
            GL11.glRotated(state ? 40 : -40, 1, 0, 0);
            GL11.glTranslated(0, 0, -1 / 16D);

            t.startDrawingQuads();
            {
                t.setNormal(0, 1, 0);

                t.addVertexWithUV(0, 0, 0, minU, maxV);
                t.addVertexWithUV(0, 6F / 8F, 0, minU, minV);
                t.addVertexWithUV(1, 6F / 8F, 0, maxU, minV);
                t.addVertexWithUV(1, 0, 0, maxU, maxV);

                t.addTranslation(0, 0, 2 / 16F);
                t.addVertexWithUV(0, 0, 0, maxU, maxV);
                t.addVertexWithUV(1, 0, 0, minU, maxV);
                t.addVertexWithUV(1, 6F / 8F, 0, minU, minV);
                t.addVertexWithUV(0, 6F / 8F, 0, maxU, minV);
                t.addTranslation(0, 0, -2 / 16F);

                t.addTranslation(7 / 16F, 0, -7 / 16F);
                t.addVertexWithUV(0, 0, 0, maxU, maxV);
                t.addVertexWithUV(0, 0, 1, minU, maxV);
                t.addVertexWithUV(0, 6F / 8F, 1, minU, minV);
                t.addVertexWithUV(0, 6F / 8F, 0, maxU, minV);
                t.addTranslation(-7 / 16F, 0, 7 / 16F);

                t.addTranslation(9 / 16F, 0, -7 / 16F);
                t.addVertexWithUV(0, 0, 0, minU, maxV);
                t.addVertexWithUV(0, 6F / 8F, 0, minU, minV);
                t.addVertexWithUV(0, 6F / 8F, 1, maxU, minV);
                t.addVertexWithUV(0, 0, 1, maxU, maxV);
                t.addTranslation(-9 / 16F, 0, 7 / 16F);

                t.addTranslation(7 / 16F, 0, 0);
                minU = icon.getInterpolatedU(7);
                maxU = icon.getInterpolatedU(9);
                minV = icon.getInterpolatedV(6);
                maxV = icon.getInterpolatedV(8);
                t.addVertexWithUV(0, 15 / 32D, 0, minU, maxV);
                t.addVertexWithUV(0, 15 / 32D, 1 / 8D, minU, minV);
                t.addVertexWithUV(1 / 8D, 15 / 32D, 1 / 8D, maxU, minV);
                t.addVertexWithUV(1 / 8D, 15 / 32D, 0, maxU, maxV);
                t.addTranslation(-7 / 16F, 0, 0);
            }
            t.draw();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void onLayoutRefresh() {

        if (layoutColor == -1)
            return;

        BufferedImage img = getGate().getLayout().getLayout(layoutColor);
        x = img.getWidth();
        z = img.getHeight();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getWidth(); y++) {
                if ((img.getRGB(x, y) & 0xFFFFFF) != 0) {
                    this.x = Math.min(this.x, x);
                    z = Math.min(z, y);
                }
            }
        }
        x = x / (img.getWidth());
        z = z / (img.getHeight());
    }

    public GateComponentLever setState(boolean state) {

        this.state = state;

        return this;
    }

    public boolean getState() {

        return state;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("state", state);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        state = tag.getBoolean("state");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeBoolean(state);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        state = buffer.readBoolean();
    }

}
