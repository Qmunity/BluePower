package com.bluepowermod.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class GuiRenderHelper {

    private static IIcon icon = null;

    public static void setIcon(IIcon icon) {

        GuiRenderHelper.icon = icon;
    }

    public static IIcon getIcon() {

        return icon;
    }

    private static double getU(double u) {

        return icon == null ? u : icon.getInterpolatedU(u * 16);
    }

    private static double getV(double v) {

        return icon == null ? v : icon.getInterpolatedV(v * 16);
    }

    public static void renderTexturedBox(double x, double y, double width, double height, double z, double u1, double v1, double u2, double v2) {

        renderTexturedBox(x, y, width, height, z, u1, v1, u2, v2, 0xFFFFFF);
    }

    public static void renderTexturedBox(double x, double y, double width, double height, double z, double u1, double v1, double u2, double v2,
            int color) {

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(color);
        tessellator.addVertexWithUV(x, y + height, z, getU(u1), getV(v2));
        tessellator.addVertexWithUV(x + width, y + height, z, getU(u2), getV(v2));
        tessellator.addVertexWithUV(x + width, y, z, getU(u2), getV(v1));
        tessellator.addVertexWithUV(x, y, z, getU(u1), getV(v1));
        tessellator.draw();
    }
}
