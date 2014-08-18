package com.bluepowermod.part.fluid;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;

public class PartCastingTable extends BPPart {

    public final static int wallThickness = 2;

    private ResourceLocation side;
    private ResourceLocation top;
    private ResourceLocation bottom;

    @Override
    public String getType() {

        return "castingTable";
    }

    @Override
    public String getUnlocalizedName() {

        return "castingTable";
    }

    @Override
    public List<AxisAlignedBB> getCollisionBoxes() {

        return getSelectionBoxes();
    }

    @Override
    public List<AxisAlignedBB> getSelectionBoxes() {

        return Arrays.asList(new AxisAlignedBB[] { AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 8 / 16D, 1) });
    }

    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {

        GL11.glPushMatrix();
        {
            GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
            if (side == null) {
                side = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_side.png");
                Minecraft.getMinecraft().renderEngine.bindTexture(side);
                top = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_top.png");
                Minecraft.getMinecraft().renderEngine.bindTexture(top);
                bottom = new ResourceLocation(Refs.MODID + ":textures/blocks/casting_table_bottom.png");
                Minecraft.getMinecraft().renderEngine.bindTexture(bottom);
            }

            int height = 8;

            // Outer area
            GL11.glPushMatrix();
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(top);
                // Top
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(0, height / 16D, 0, 0, 0);
                    RenderHelper.addVertexWithTexture(0, height / 16D, 1, 0, 1);
                    RenderHelper.addVertexWithTexture(1, height / 16D, 1, 1, 1);
                    RenderHelper.addVertexWithTexture(1, height / 16D, 0, 1, 0);
                }
                GL11.glEnd();

                Minecraft.getMinecraft().renderEngine.bindTexture(bottom);
                // Bottom
                GL11.glNormal3d(0, -1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
                    RenderHelper.addVertexWithTexture(1, 0, 0, 1, 0);
                    RenderHelper.addVertexWithTexture(1, 0, 1, 1, 1);
                    RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
                }
                GL11.glEnd();

                Minecraft.getMinecraft().renderEngine.bindTexture(side);
                // Side
                GL11.glNormal3d(1, 0, 0);
                for (int i = 0; i < 4; i++) {
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture(0, 0, 0, 0, 1);
                        RenderHelper.addVertexWithTexture(0, height / 16D, 0, 0, 0.5);
                        RenderHelper.addVertexWithTexture(1, height / 16D, 0, 1, 0.5);
                        RenderHelper.addVertexWithTexture(1, 0, 0, 1, 1);
                    }
                    GL11.glEnd();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated(90, 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                }
            }
            GL11.glPopMatrix();

            // Inner area
            GL11.glPushMatrix();
            {
                Minecraft.getMinecraft().renderEngine.bindTexture(bottom);
                // Bottom
                GL11.glNormal3d(0, 1, 0);
                GL11.glBegin(GL11.GL_QUADS);
                {
                    RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1) / 16D, wallThickness / 16D, wallThickness / 16D,
                            wallThickness / 16D);
                    RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1) / 16D, 1 - (wallThickness / 16D), wallThickness / 16D,
                            1 - (wallThickness / 16D));
                    RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1) / 16D, 1 - (wallThickness / 16D),
                            1 - (wallThickness / 16D), 1 - (wallThickness / 16D));
                    RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1) / 16D, wallThickness / 16D, 1 - (wallThickness / 16D),
                            wallThickness / 16D);
                }
                GL11.glEnd();

                Minecraft.getMinecraft().renderEngine.bindTexture(side);
                // Side
                GL11.glNormal3d(-1.5, 0, 0);
                for (int i = 0; i < 4; i++) {
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        RenderHelper.addVertexWithTexture(wallThickness / 16D, (height - 1) / 16D, wallThickness / 16D, wallThickness / 16D,
                                1 - (7 / 16D));
                        RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), (height - 1) / 16D, wallThickness / 16D,
                                1 - (wallThickness / 16D), 1 - (7 / 16D));
                        RenderHelper.addVertexWithTexture(1 - (wallThickness / 16D), height / 16D, wallThickness / 16D, 1 - (wallThickness / 16D),
                                0.5);
                        RenderHelper.addVertexWithTexture(wallThickness / 16D, height / 16D, wallThickness / 16D, wallThickness / 16D, 0.5);
                    }
                    GL11.glEnd();
                    GL11.glTranslated(0.5, 0, 0.5);
                    GL11.glRotated(90, 0, 1, 0);
                    GL11.glTranslated(-0.5, 0, -0.5);
                }
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerMachines;
    }

}
