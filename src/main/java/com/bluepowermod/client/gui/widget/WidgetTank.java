package com.bluepowermod.client.gui.widget;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

public class WidgetTank extends BaseWidget {

    private IFluidTank tank;
    private FluidStack fluid;
    private int capacity;

    public WidgetTank(int id, int x, int y, int width, int height, IFluidTank tank) {

        super(id, x, y, width, height, new String[] {});

        this.tank = tank;
    }

    public WidgetTank(int id, int x, int y, int width, int height, FluidStack fluid, int capacity) {

        super(id, x, y, width, height, new String[] {});

        this.fluid = fluid;
        this.capacity = capacity;
    }

    @Override
    public void render(int mouseX, int mouseY) {

        FluidStack fluid;
        int capacity;
        if (tank != null) {
            fluid = tank.getFluid();
            capacity = tank.getCapacity();
        } else {
            fluid = this.fluid;
            capacity = this.capacity;
        }

        int width = getBounds().width;
        int height = getBounds().height;

        GL11.glPushMatrix();
        {
            GL11.glColor4d(1, 1, 1, 1);

            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/gui/widgets/tank.png"));

            double pixel = 1 / 32D;

            GL11.glBegin(GL11.GL_QUADS);
            {
                // Top
                RenderHelper.addVertexWithTexture(x - 1, y - 1, 0, 0, 0);
                RenderHelper.addVertexWithTexture(x - 1, y, 0, 0, pixel);
                RenderHelper.addVertexWithTexture(x + width + 1, y, 0, pixel * 18, pixel);
                RenderHelper.addVertexWithTexture(x + width + 1, y - 1, 0, pixel * 18, 0);
                // Bottom
                RenderHelper.addVertexWithTexture(x - 1, y + height, 0, 0, pixel * 2);
                RenderHelper.addVertexWithTexture(x - 1, y + height + 1, 0, 0, pixel * 3);
                RenderHelper.addVertexWithTexture(x + width + 1, y + height + 1, 0, pixel * 18, pixel * 3);
                RenderHelper.addVertexWithTexture(x + width + 1, y + height, 0, pixel * 18, pixel * 2);
                // middle
                RenderHelper.addVertexWithTexture(x - 1, y, 0, 0, pixel * 1);
                RenderHelper.addVertexWithTexture(x - 1, y + height, 0, 0, pixel * 2);
                RenderHelper.addVertexWithTexture(x + width + 1, y + height, 0, pixel * 18, pixel * 2);
                RenderHelper.addVertexWithTexture(x + width + 1, y, 0, pixel * 18, pixel * 1);
            }
            GL11.glEnd();

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4d(1, 0, 0, 1);

            GL11.glBegin(GL11.GL_QUADS);
            {
                RenderHelper.addVertex(x, y, 0);
                RenderHelper.addVertex(x, y + height, 0);
                RenderHelper.addVertex(x + width, y + height, 0);
                RenderHelper.addVertex(x + width, y, 0);
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4d(1, 1, 1, 1);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void addTooltip(List<String> curTip, boolean shiftPressed) {

    }

}
