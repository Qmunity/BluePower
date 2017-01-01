/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.tube;

import com.bluepowermod.client.render.IconSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.util.List;

/**
 * @author MineMaarten
 */
public class MagTube extends PneumaticTube {

    @Override
    public String getType() {

        return "magTube";
    }

    @Override
    public String getUnlocalizedName() {

        return "magTube";
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected TextureAtlasSprite getSideIcon() {

        return IconSupplier.magTubeSide;
    }

    @Override
    protected boolean canConnectToInventories() {

        return false;
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> aabbs = super.getSelectionBoxes();
        if (!shouldRenderNode()) {
            if (connections[0]) {
                aabbs.add(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 6 / 16D, 14 / 16D));
                aabbs.add(new Vec3dCube(2 / 16D, 10 / 16D, 2 / 16D, 14 / 16D, 14 / 16D, 14 / 16D));
            } else if (connections[2]) {
                aabbs.add(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 14 / 16D, 6 / 16D));
                aabbs.add(new Vec3dCube(2 / 16D, 2 / 16D, 10 / 16D, 14 / 16D, 14 / 16D, 14 / 16D));
            } else if (connections[4]) {
                aabbs.add(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 6 / 16D, 14 / 16D, 14 / 16D));
                aabbs.add(new Vec3dCube(10 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 14 / 16D, 14 / 16D));
            }
        }
        return aabbs;
    }

    /**
     * Render method that works, and now should be buried under the ground so no-one looks at it
     */
    @Override
    @SideOnly(Side.CLIENT)
    protected void renderSide() {

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Tessellator t = Tessellator.getInstance();
        VertexBuffer b = t.getBuffer();
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        if (getParent() == null || getWorld() == null) {

        } else {
            if (connections[2]) {
                GL11.glRotated(90, 1, 0, 0);
            } else if (connections[4]) {
                GL11.glRotated(90, 0, 0, 1);
            }
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        b.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        double min = 2 / 16D;
        double max = 14 / 16D;
        double inMin = 12.001 / 16D;
        double inMax = 3.999 / 16D;

        TextureAtlasSprite icon = IconSupplier.magCoilSide;

        double minX = icon.getInterpolatedU(min * 16);
        double maxX = icon.getInterpolatedU(max * 16);
        double minY = icon.getInterpolatedV(min * 16);
        double maxY = icon.getInterpolatedV(max * 16);

        b.normal(0, 0, 1);
        b.pos(min, min, max).tex(maxX, maxY).endVertex();// maxZ
        b.pos(max, min, max).tex(minX, maxY).endVertex();
        b.pos(max, max, max).tex(minX, minY).endVertex();
        b.pos(min, max, max).tex(maxX, minY).endVertex();

        b.pos(min, min, inMax).tex(maxX, maxY).endVertex();// inside maxZ
        b.pos(max, min, inMax).tex(minX, maxY).endVertex();
        b.pos(max, max, inMax).tex(minX, minY).endVertex();
        b.pos(min, max, inMax).tex(maxX, minY).endVertex();

        b.normal(0, 0, -1);
        b.pos(min, min, min).tex(minX, maxY).endVertex();// minZ
        b.pos(min, max, min).tex(minX, minY).endVertex();
        b.pos(max, max, min).tex(maxX, minY).endVertex();
        b.pos(max, min, min).tex(maxX, maxY).endVertex();

        b.pos(min, min, inMin).tex(maxX, maxY).endVertex();// inside minZ
        b.pos(min, max, inMin).tex(minX, minY).endVertex();
        b.pos(max, max, inMin).tex(minX, minY).endVertex();
        b.pos(max, min, inMin).tex(maxX, maxY).endVertex();

        b.normal(-1, 0, 0);
        b.pos(min, min, min).tex(maxX, maxY).endVertex();// minX
        b.pos(min, min, max).tex(minX, maxY).endVertex();
        b.pos(min, max, max).tex(minX, minY).endVertex();
        b.pos(min, max, min).tex(maxX, minY).endVertex();

        b.pos(inMin, min, min).tex(maxX, maxY).endVertex();// inside minX
        b.pos(inMin, min, max).tex(minX, maxY).endVertex();
        b.pos(inMin, max, max).tex(minX, minY).endVertex();
        b.pos(inMin, max, min).tex(maxX, minY).endVertex();

        b.normal(1, 0, 0);
        b.pos(max, min, min).tex(minX, minY).endVertex();// maxX
        b.pos(max, max, min).tex(minX, maxY).endVertex();
        b.pos(max, max, max).tex(maxX, maxY).endVertex();
        b.pos(max, min, max).tex(maxX, minY).endVertex();

        b.pos(inMax, min, min).tex(maxX, minY).endVertex();// maxX
        b.pos(inMax, max, min).tex(minX, maxY).endVertex();
        b.pos(inMax, max, max).tex(minX, maxY).endVertex();
        b.pos(inMax, min, max).tex(maxX, minY).endVertex();

        icon = IconSupplier.magCoilFront;
        minX = icon.getInterpolatedU(min * 16);
        maxX = icon.getInterpolatedU(max * 16);
        minY = icon.getInterpolatedV(min * 16);
        maxY = icon.getInterpolatedV(max * 16);
        for (int i = 2; i < 16; i += 8) {
            b.normal(0, 1, 0);
            b.pos(min, 1 - i / 16D, min).tex(maxX, maxY).endVertex();// maxY
            b.pos(min, 1 - i / 16D, max).tex(minX, maxY).endVertex();
            b.pos(max, 1 - i / 16D, max).tex(minX, minY).endVertex();
            b.pos(max, 1 - i / 16D, min).tex(maxX, minY).endVertex();

            b.normal(0, -1, 0);
            b.pos(min, i / 16D, min).tex(maxX, maxY).endVertex();// minY
            b.pos(max, i / 16D, min).tex(minX, maxY).endVertex();
            b.pos(max, i / 16D, max).tex(minX, minY).endVertex();
            b.pos(min, i / 16D, max).tex(maxX, minY).endVertex();
        }
        t.draw();
        GL11.glPopMatrix();
    }

    @Override
    public void update() {

        super.update();

        TubeLogic logic = getLogic();
        for (TubeStack stack : logic.tubeStacks)
            stack.setSpeed(Math.max(stack.getSpeed() - 1 / 32D, 1 / 16D));
    }

}

/*
 * 
 * Code for when a real static render is applied, and no changes of render states are allowed. This is not finished
 * 
 * @Override protected void renderSide() {
 * 
 * double min = 2 / 16D; double max = 14 / 16D;
 * 
 * Tessellator t = Tessellator.instance; TextureAtlasSprite icon = IconSupplier.magCoilSide;
 * 
 * double minX = icon.getInterpolatedU(min * 16); double maxX = icon.getInterpolatedU(max * 16); double minY = icon.getInterpolatedV(min * 16); double
 * maxY = icon.getInterpolatedV(max * 16);
 * 
 * if (connections[0]) { t.setNormal(0, 0, 1); t.addVertexWithUV(min, min, max, maxX, maxY);// maxZ t.addVertexWithUV(max, min, max, minX, maxY);
 * t.addVertexWithUV(max, max, max, minX, minY); t.addVertexWithUV(min, max, max, maxX, minY);
 * 
 * t.setNormal(0, 0, -1); t.addVertexWithUV(min, min, min, maxX, maxY);// minZ t.addVertexWithUV(min, max, min, minX, minY); t.addVertexWithUV(max,
 * max, min, minX, minY); t.addVertexWithUV(max, min, min, maxX, maxY);
 * 
 * t.setNormal(-1, 0, 0); t.addVertexWithUV(min, min, min, maxX, maxY);// minX t.addVertexWithUV(min, min, max, minX, maxY); t.addVertexWithUV(min,
 * max, max, minX, minY); t.addVertexWithUV(min, max, min, maxX, minY);
 * 
 * t.setNormal(1, 0, 0); t.addVertexWithUV(max, min, min, maxX, minY);// maxX t.addVertexWithUV(max, max, min, minX, maxY); t.addVertexWithUV(max,
 * max, max, minX, maxY); t.addVertexWithUV(max, min, max, maxX, minY); } else if (connections[2]) { t.setNormal(0, -1, 0); t.addVertexWithUV(min,
 * minY, min, maxX, maxY);// minY t.addVertexWithUV(max, minY, min, minX, maxY); t.addVertexWithUV(max, minY, max, minX, minY); t.addVertexWithUV(min,
 * minY, max, maxX, minY);
 * 
 * t.setNormal(0, 1, 0); t.addVertexWithUV(min, max, min, maxX, maxY);// maxY t.addVertexWithUV(min, max, max, minX, minY); t.addVertexWithUV(max,
 * max, max, minX, minY); t.addVertexWithUV(max, max, min, maxX, maxY);
 * 
 * t.setNormal(-1, 0, 0); t.addVertexWithUV(min, min, min, maxX, maxY);// minX t.addVertexWithUV(min, min, max, minX, minY); t.addVertexWithUV(min,
 * max, max, minX, minY); t.addVertexWithUV(min, max, min, maxX, maxY);
 * 
 * t.setNormal(1, 0, 0); t.addVertexWithUV(max, min, min, maxX, minY);// maxX t.addVertexWithUV(max, max, min, minX, minY); t.addVertexWithUV(max,
 * max, max, minX, maxY); t.addVertexWithUV(max, min, max, maxX, maxY);
 * 
 * } else if (connections[4]) { t.setNormal(0, 0, 1); t.addVertexWithUV(min, min, max, maxX, maxY);// maxZ t.addVertexWithUV(max, min, max, minX,
 * minY); t.addVertexWithUV(max, max, max, minX, minY); t.addVertexWithUV(min, max, max, maxX, maxY);
 * 
 * t.setNormal(0, 0, -1); t.addVertexWithUV(min, min, min, maxX, maxY);// minZ t.addVertexWithUV(min, max, min, minX, maxY); t.addVertexWithUV(max,
 * max, min, minX, minY); t.addVertexWithUV(max, min, min, maxX, minY);
 * 
 * t.setNormal(0, 1, 0); t.addVertexWithUV(min, max, min, maxX, maxY);// maxY t.addVertexWithUV(min, max, max, minX, maxY); t.addVertexWithUV(max,
 * max, max, minX, minY); t.addVertexWithUV(max, max, min, maxX, minY);
 * 
 * t.setNormal(0, -1, 0); t.addVertexWithUV(min, min, min, maxX, minY);// minY t.addVertexWithUV(max, min, min, minX, maxY); t.addVertexWithUV(max,
 * min, max, minX, maxY); t.addVertexWithUV(min, min, max, maxX, minY); }
 * 
 * icon = IconSupplier.magCoilFront; minX = icon.getInterpolatedU(min * 16); maxX = icon.getInterpolatedU(max * 16); minY = icon.getInterpolatedV(min
 * * 16); maxY = icon.getInterpolatedV(max * 16); for (int i = 2; i < 16; i += 8) { if (connections[0]) { t.setNormal(0, 1, 0); t.addVertexWithUV(min,
 * 1 - i / 16D, min, maxX, maxY);// maxY t.addVertexWithUV(min, 1 - i / 16D, max, minX, maxY); t.addVertexWithUV(max, 1 - i / 16D, max, minX, minY);
 * t.addVertexWithUV(max, 1 - i / 16D, min, maxX, minY);
 * 
 * t.setNormal(0, -1, 0); t.addVertexWithUV(min, i / 16D, min, maxX, maxY);// minY t.addVertexWithUV(max, i / 16D, min, minX, maxY);
 * t.addVertexWithUV(max, i / 16D, max, minX, minY); t.addVertexWithUV(min, i / 16D, max, maxX, minY); } else if (connections[2]) { t.setNormal(0, 0,
 * 1); t.addVertexWithUV(min, min, 1 - i / 16D, maxX, maxY);// maxZ t.addVertexWithUV(max, min, 1 - i / 16D, minX, maxY); t.addVertexWithUV(max, max,
 * 1 - i / 16D, minX, minY); t.addVertexWithUV(min, max, 1 - i / 16D, maxX, minY);
 * 
 * t.setNormal(0, 0, -1); t.addVertexWithUV(min, min, i / 16D, maxX, maxY);//minZ t.addVertexWithUV(min, max, i / 16D, minX, maxY);
 * t.addVertexWithUV(max, max, i / 16D, minX, minY); t.addVertexWithUV(max, min, i / 16D, maxX, minY); } else if (connections[4]) { t.setNormal(-1, 0,
 * 0); t.addVertexWithUV(i / 16D, min, min, maxX, maxY);// minX t.addVertexWithUV(i / 16D, min, max, minX, maxY); t.addVertexWithUV(i / 16D, max, max,
 * minX, minY); t.addVertexWithUV(i / 16D, max, min, maxX, minY);
 * 
 * t.setNormal(1, 0, 0); t.addVertexWithUV(1 - i / 16D, min, min, maxX, minY);// maxX t.addVertexWithUV(1 - i / 16D, max, min, minX, minY);
 * t.addVertexWithUV(1 - i / 16D, max, max, minX, maxY); t.addVertexWithUV(1 - i / 16D, min, max, maxX, maxY); } } }
 */

