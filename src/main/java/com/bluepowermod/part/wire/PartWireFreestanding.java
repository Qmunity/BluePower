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

import java.util.ArrayList;
import java.util.List;

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
    protected IIcon getWireIcon(ForgeDirection side, ForgeDirection face) {

        return getWireIcon(face);
    }

    @SideOnly(Side.CLIENT)
    protected IIcon[] getIcons(ForgeDirection side) {

        return new IIcon[] { getWireIcon(side, ForgeDirection.DOWN), getWireIcon(side, ForgeDirection.UP),
                getWireIcon(side, ForgeDirection.WEST), getWireIcon(side, ForgeDirection.EAST), getWireIcon(side, ForgeDirection.NORTH),
                getWireIcon(side, ForgeDirection.SOUTH) };
    }

    @SideOnly(Side.CLIENT)
    protected abstract IIcon getFrameIcon();

    protected List<Vec3dCube> getFrameBoxes() {

        double wireSize = getSize() / 16D;
        double frameSeparation = 4 / 16D - (wireSize - 2 / 16D);
        double frameThickness = 1 / 16D;

        boolean isInWorld = getParent() != null;

        boolean down = shouldRenderConnection(ForgeDirection.DOWN);
        boolean up = shouldRenderConnection(ForgeDirection.UP);
        boolean north = shouldRenderConnection(ForgeDirection.NORTH);
        boolean south = shouldRenderConnection(ForgeDirection.SOUTH);
        boolean west = shouldRenderConnection(ForgeDirection.WEST);
        boolean east = shouldRenderConnection(ForgeDirection.EAST);

        return getFrameBoxes(wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, isInWorld);
    }

    protected List<Vec3dCube> getFrameBoxes(double wireSize, double frameSeparation, double frameThickness, boolean down, boolean up,
            boolean west, boolean east, boolean north, boolean south, boolean isInWorld) {

        return getFrameBoxes(wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, down, up, west, east, north,
                south, isInWorld);
    }

    protected List<Vec3dCube> getFrameBoxes(double wireSize, double frameSeparation, double frameThickness, boolean down, boolean up,
            boolean west, boolean east, boolean north, boolean south, boolean sideDown, boolean sideUp, boolean sideWest, boolean sideEast,
            boolean sideNorth, boolean sideSouth, boolean isInWorld) {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        // Top
        if (west == up || !isInWorld)
            boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2),
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                            + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 + ((wireSize + frameSeparation) / 2)));
        if (east == up || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2),
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5
                            + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 + ((wireSize + frameSeparation) / 2)));
        if (south == up || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2),
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                            + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 + ((wireSize + frameSeparation) / 2)
                            + frameThickness));
        if (north == up || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2), 0.5
                    - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                    + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 - ((wireSize + frameSeparation) / 2)));
        // Bottom
        if (west == down || !isInWorld)
            boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2)
                    - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2)));
        if (east == down || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2) - frameThickness,
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness,
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2)));
        if (south == down || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2) - frameThickness,
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));
        if (north == down || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2) - frameThickness,
                    0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 - ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2)));

        // Sides
        if (north == west || !isInWorld)
            boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2)));
        if (south == west || !isInWorld)
            boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));
        if (north == east || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                    - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness,
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2)));
        if (south == east || !isInWorld)
            boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2),
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness,
                    0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));

        // Corners
        boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2), 0.5
                - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 - ((wireSize + frameSeparation) / 2)));
        boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2),
                0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                        + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));
        boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2), 0.5
                - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5
                + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 - ((wireSize + frameSeparation) / 2)));
        boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2),
                0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5
                        + ((wireSize + frameSeparation) / 2) + frameThickness, 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));

        boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2)
                - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2),
                0.5 - ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2)));
        boxes.add(new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2)
                - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2),
                0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));
        boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5
                - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness,
                0.5 - ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2)));
        boxes.add(new Vec3dCube(0.5 + ((wireSize + frameSeparation) / 2), 0.5 - ((wireSize + frameSeparation) / 2) - frameThickness,
                0.5 + ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness,
                0.5 - ((wireSize + frameSeparation) / 2), 0.5 + ((wireSize + frameSeparation) / 2) + frameThickness));

        if (isInWorld) {
            // Connections
            Vec3dCube box = new Vec3dCube(0.5 - ((wireSize + frameSeparation) / 2) - frameThickness, 0, 0.5
                    - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2), 0.5
                    - ((wireSize + frameSeparation) / 2) - frameThickness, 0.5 - ((wireSize + frameSeparation) / 2));

            if (sideDown)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(ForgeDirection.DOWN, Vec3d.center));
            if (sideUp)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(ForgeDirection.UP, Vec3d.center));
            if (sideWest)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(ForgeDirection.WEST, Vec3d.center));
            if (sideEast)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(ForgeDirection.EAST, Vec3d.center));
            if (sideNorth)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(ForgeDirection.NORTH, Vec3d.center));
            if (sideSouth)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(ForgeDirection.SOUTH, Vec3d.center));
        }

        return boxes;
    }

    protected void renderFrame(RenderHelper helper, double wireSize, double frameSeparation, double frameThickness, boolean down,
            boolean up, boolean west, boolean east, boolean north, boolean south, boolean sideDown, boolean sideUp, boolean sideWest,
            boolean sideEast, boolean sideNorth, boolean sideSouth, boolean isInWorld, IIcon texture, int color) {

        helper.setColor(color);

        for (Vec3dCube box : getFrameBoxes(wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, sideDown, sideUp,
                sideWest, sideEast, sideNorth, sideSouth, isInWorld))
            helper.renderBox(box, texture);

        helper.setColor(0xFFFFFF);
    }

    protected void renderFrame(RenderHelper helper, double wireSize, double frameSeparation, double frameThickness, boolean down,
            boolean up, boolean west, boolean east, boolean north, boolean south, boolean isInWorld, IIcon texture, int color) {

        renderFrame(helper, wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, down, up, west, east, north,
                south, isInWorld, texture, color);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        double wireSize = getSize() / 16D;
        double frameSeparation = 4 / 16D - (wireSize - 2 / 16D);
        double frameThickness = 1 / 16D;

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
        renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2),
                0.5 + (wireSize / 2), 0.5 + (wireSize / 2)), getIcons(ForgeDirection.UNKNOWN));
        if (up || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 1,
                    0.5 + (wireSize / 2)), getIcons(ForgeDirection.UP));
        if (down || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0, 0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 0.5 - (wireSize / 2),
                    0.5 + (wireSize / 2)), getIcons(ForgeDirection.DOWN));
        if (north || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0, 0.5 + (wireSize / 2), 0.5 + (wireSize / 2),
                    0.5 - (wireSize / 2)), getIcons(ForgeDirection.NORTH));
        if (south || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 0.5 + (wireSize / 2),
                    0.5 + (wireSize / 2), 1), getIcons(ForgeDirection.SOUTH));
        if (west || !isInWorld)
            renderer.renderBox(new Vec3dCube(0, 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2),
                    0.5 + (wireSize / 2)), getIcons(ForgeDirection.WEST));
        if (east || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 + (wireSize / 2), 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 1, 0.5 + (wireSize / 2),
                    0.5 + (wireSize / 2)), getIcons(ForgeDirection.EAST));

        renderer.setColor(getFrameColorMultiplier());

        // Frame
        renderFrame(renderer, wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, isInWorld, getFrameIcon(),
                getFrameColorMultiplier());

        return true;
    }

    @Override
    public int getHollowSize(ForgeDirection side) {

        return 8;
    }

}
