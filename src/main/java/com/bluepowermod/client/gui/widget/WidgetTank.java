package com.bluepowermod.client.gui.widget;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
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

        Fluid fluid = null;
        int amt = 0;
        int capacity = 0;
        if (tank != null) {
            if (tank.getFluid() != null) {
                fluid = tank.getFluid().getFluid();
                amt = tank.getFluidAmount();
            }
            capacity = tank.getCapacity();
        } else {
            if (this.fluid != null) {
                fluid = this.fluid.getFluid();
                amt = this.fluid.amount;
                capacity = this.capacity;
            }
        }
        IIcon icon = fluid != null ? fluid.getStillIcon() : null;

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
                // Middle
                RenderHelper.addVertexWithTexture(x - 1, y, 0, 0, pixel * 1);
                RenderHelper.addVertexWithTexture(x - 1, y + height, 0, 0, pixel * 2);
                RenderHelper.addVertexWithTexture(x + width + 1, y + height, 0, pixel * 18, pixel * 2);
                RenderHelper.addVertexWithTexture(x + width + 1, y, 0, pixel * 18, pixel * 1);
            }
            GL11.glEnd();

            if (fluid != null && amt > 0 && capacity > 0) {
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                double fluidPercentage = amt / (double) capacity;
                double fluidHeight = height * fluidPercentage;

                GL11.glPushMatrix();
                {
                    GL11.glTranslated(0, height, 0);
                    GL11.glEnable(GL11.GL_BLEND);
                    while (fluidHeight > 0) {
                        double moved = Math.min(fluidHeight, icon.getIconHeight());
                        GL11.glTranslated(0, -moved, 0);
                        GL11.glBegin(GL11.GL_QUADS);
                        {
                            RenderHelper.addVertexWithTexture(x, y, 0, icon.getMinU(), icon.getMinV()
                                    + ((icon.getMaxV() - icon.getMinV()) * (1 - (moved / (icon.getIconHeight())))));
                            RenderHelper.addVertexWithTexture(x, y + moved, 0, icon.getMinU(), icon.getMaxV());
                            RenderHelper.addVertexWithTexture(x + width, y + moved, 0, icon.getMaxU(), icon.getMaxV());
                            RenderHelper.addVertexWithTexture(x + width, y, 0, icon.getMaxU(), icon.getMinV()
                                    + ((icon.getMaxV() - icon.getMinV()) * (1 - (moved / (icon.getIconHeight())))));
                        }
                        GL11.glEnd();
                        fluidHeight -= moved;
                    }
                    GL11.glDisable(GL11.GL_BLEND);
                }
                GL11.glPopMatrix();
            }

            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/gui/widgets/tank.png"));

            int scaleHeight = height - 3;
            GL11.glPushMatrix();
            {
                GL11.glTranslated(0, height, 0);
                GL11.glEnable(GL11.GL_BLEND);
                while (scaleHeight > 0) {
                    double moved = Math.min(scaleHeight, 15);
                    GL11.glTranslated(0, -moved, 0);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture(x, y, 0, pixel * 18, pixel * 0);
                        RenderHelper.addVertexWithTexture(x, y + moved, 0, pixel * 18, pixel * 15);
                        RenderHelper.addVertexWithTexture(x + width, y + moved, 0, pixel * 32, pixel * 15);
                        RenderHelper.addVertexWithTexture(x + width, y, 0, pixel * 32, pixel * 0);
                    }
                    GL11.glEnd();
                    scaleHeight -= moved;
                }
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void addTooltip(List<String> curTip, boolean shiftPressed) {

        Fluid fluid = null;
        int amt = 0;
        int capacity = 0;
        if (tank != null) {
            if (tank.getFluid() != null) {
                fluid = tank.getFluid().getFluid();
                amt = tank.getFluidAmount();
            }
            capacity = tank.getCapacity();
        } else {
            if (this.fluid != null) {
                fluid = this.fluid.getFluid();
                amt = this.fluid.amount;
                capacity = this.capacity;
            }
        }
        if (fluid == null || amt == 0 || capacity == 0) {
            curTip.add(amt + "/" + capacity + " mb");
            curTip.add(EnumChatFormatting.GRAY + I18n.format("hud.empty"));
        } else {
            curTip.add(amt + "/" + capacity + " mb");
            curTip.add(EnumChatFormatting.GRAY + fluid.getLocalizedName(new FluidStack(fluid, amt)));
        }
    }

    public void setFluid(FluidStack fluid) {

        this.fluid = fluid;
    }

    public void setCapacity(int capacity) {

        this.capacity = capacity;
    }

}
