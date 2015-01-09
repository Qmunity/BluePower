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
package com.bluepowermod.part.wire;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPartThruHole;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.BPPart;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PartWireFreestanding extends BPPart implements IPartThruHole {

    protected abstract boolean shouldRenderConnection(ForgeDirection side);

    protected abstract int getSize();

    protected int getColorMultiplier() {

        return 0xFFFFFF;
    }

    protected int getFrameColorMultiplier() {

        return 0xFFFFFF;
    }

    @SideOnly(Side.CLIENT)
    protected abstract IIcon getWireIcon(ForgeDirection side);

    @SideOnly(Side.CLIENT)
    protected abstract IIcon getFrameIcon();

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        double frameSize = getSize() / 16D;
        double frameSeparation = 4 / 16D - (frameSize - 2 / 16D);
        double frameThickness = 1 / 16D;

        IIcon[] icons = new IIcon[] { getWireIcon(ForgeDirection.DOWN), getWireIcon(ForgeDirection.UP), getWireIcon(ForgeDirection.WEST),
                getWireIcon(ForgeDirection.EAST), getWireIcon(ForgeDirection.NORTH), getWireIcon(ForgeDirection.SOUTH) };
        int color = getColorMultiplier();

        boolean isInWorld = getParent() != null;

        boolean down = shouldRenderConnection(ForgeDirection.DOWN);
        boolean up = shouldRenderConnection(ForgeDirection.UP);
        boolean north = shouldRenderConnection(ForgeDirection.NORTH);
        boolean south = shouldRenderConnection(ForgeDirection.SOUTH);
        boolean west = shouldRenderConnection(ForgeDirection.WEST);
        boolean east = shouldRenderConnection(ForgeDirection.EAST);

        renderer.setColor(color);

        // Wire
        renderer.renderBox(new Vec3dCube(0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 0.5 + (frameSize / 2),
                0.5 + (frameSize / 2), 0.5 + (frameSize / 2)), icons);
        if (up || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (frameSize / 2), 0.5 + (frameSize / 2), 0.5 - (frameSize / 2), 0.5 + (frameSize / 2), 1,
                    0.5 + (frameSize / 2)), icons);
        if (down || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (frameSize / 2), 0, 0.5 - (frameSize / 2), 0.5 + (frameSize / 2), 0.5 - (frameSize / 2),
                    0.5 + (frameSize / 2)), icons);
        if (north || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 0, 0.5 + (frameSize / 2), 0.5 + (frameSize / 2),
                    0.5 - (frameSize / 2)), icons);
        if (south || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 0.5 + (frameSize / 2), 0.5 + (frameSize / 2),
                    0.5 + (frameSize / 2), 1), icons);
        if (west || !isInWorld)
            renderer.renderBox(new Vec3dCube(0, 0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 0.5 + (frameSize / 2),
                    0.5 + (frameSize / 2)), icons);
        if (east || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 + (frameSize / 2), 0.5 - (frameSize / 2), 0.5 - (frameSize / 2), 1, 0.5 + (frameSize / 2),
                    0.5 + (frameSize / 2)), icons);

        renderer.setColor(getFrameColorMultiplier());

        // Frame
        {
            IIcon frame = getFrameIcon();

            // Top
            if (west == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                        0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness,
                        0.5 + ((frameSize + frameSeparation) / 2)), frame);
            if (east == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5
                        + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5 + ((frameSize + frameSeparation) / 2)), frame);
            if (south == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2),
                        0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2), 0.5
                        + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5 + ((frameSize + frameSeparation) / 2)
                        + frameThickness), frame);
            if (north == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2), 0.5
                        - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2), 0.5
                        + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5 - ((frameSize + frameSeparation) / 2)), frame);
            // Bottom
            if (west == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5
                        - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2),
                        0.5 + ((frameSize + frameSeparation) / 2)), frame);
            if (east == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)
                        - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2)
                        + frameThickness, 0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2)), frame);
            if (south == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)
                        - frameThickness, 0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness), frame);
            if (north == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)
                        - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2)), frame);

            // Sides
            if (north == west || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2)), frame);
            if (south == west || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2),
                        0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2), 0.5
                        + ((frameSize + frameSeparation) / 2) + frameThickness), frame);
            if (north == east || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2), 0.5
                        - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness,
                        0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)), frame);
            if (south == east || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2),
                        0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness,
                        0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness), frame);

            // Corners
            renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                    0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                    0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness,
                    0.5 - ((frameSize + frameSeparation) / 2)), frame);
            renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                    0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2),
                    0.5 - ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5
                    + ((frameSize + frameSeparation) / 2) + frameThickness), frame);
            renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2), 0.5
                    - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5
                    + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5 - ((frameSize + frameSeparation) / 2)), frame);
            renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2),
                    0.5 + ((frameSize + frameSeparation) / 2), 0.5 + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5
                    + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5 + ((frameSize + frameSeparation) / 2)
                    + frameThickness), frame);

            renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5
                    - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2) - frameThickness,
                    0.5 - ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2),
                    0.5 - ((frameSize + frameSeparation) / 2)), frame);
            renderer.renderBox(new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5
                    - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 + ((frameSize + frameSeparation) / 2),
                    0.5 - ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2), 0.5
                    + ((frameSize + frameSeparation) / 2) + frameThickness), frame);
            renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)
                    - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 + ((frameSize + frameSeparation) / 2)
                    + frameThickness, 0.5 - ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)), frame);
            renderer.renderBox(new Vec3dCube(0.5 + ((frameSize + frameSeparation) / 2), 0.5 - ((frameSize + frameSeparation) / 2)
                    - frameThickness, 0.5 + ((frameSize + frameSeparation) / 2),
                    0.5 + ((frameSize + frameSeparation) / 2) + frameThickness, 0.5 - ((frameSize + frameSeparation) / 2), 0.5
                    + ((frameSize + frameSeparation) / 2) + frameThickness), frame);

            if (isInWorld) {
                // Connections
                Vec3dCube box = new Vec3dCube(0.5 - ((frameSize + frameSeparation) / 2) - frameThickness, 0, 0.5
                        - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2), 0.5
                        - ((frameSize + frameSeparation) / 2) - frameThickness, 0.5 - ((frameSize + frameSeparation) / 2));
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (!shouldRenderConnection(d))
                        continue;
                    for (int i = 0; i < 4; i++)
                        renderer.renderBox(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(d, Vec3d.center), frame);
                }
            }
        }
        renderer.resetTransformations();

        return true;
    }

    @Override
    public int getHollowSize(ForgeDirection side) {

        return 8;
    }

}
