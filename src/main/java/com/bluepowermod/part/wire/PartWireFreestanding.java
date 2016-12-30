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

import com.bluepowermod.part.BPPart;
import net.minecraft.util.EnumFacing;
import uk.co.qmunity.lib.part.IPartThruHole;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.util.ArrayList;
import java.util.List;

public abstract class PartWireFreestanding extends BPPart implements IPartThruHole {

    protected abstract boolean shouldRenderConnection(EnumFacing side);

    protected abstract int getSize();

    protected int getColorMultiplier() {

        return 0xFFFFFF;
    }

    protected int getFrameColorMultiplier() {

        return 0xFFFFFF;
    }


    protected List<Vec3dCube> getFrameBoxes() {

        double wireSize = getSize() / 16D;
        double frameSeparation = 4 / 16D - (wireSize - 2 / 16D);
        double frameThickness = 1 / 16D;

        boolean isInWorld = getParent() != null;

        boolean down = shouldRenderConnection(EnumFacing.DOWN);
        boolean up = shouldRenderConnection(EnumFacing.UP);
        boolean north = shouldRenderConnection(EnumFacing.NORTH);
        boolean south = shouldRenderConnection(EnumFacing.SOUTH);
        boolean west = shouldRenderConnection(EnumFacing.WEST);
        boolean east = shouldRenderConnection(EnumFacing.EAST);

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
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(EnumFacing.DOWN, Vec3d.center));
            if (sideUp)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(EnumFacing.UP, Vec3d.center));
            if (sideWest)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(EnumFacing.WEST, Vec3d.center));
            if (sideEast)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(EnumFacing.EAST, Vec3d.center));
            if (sideNorth)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(EnumFacing.NORTH, Vec3d.center));
            if (sideSouth)
                for (int i = 0; i < 4; i++)
                    boxes.add(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(EnumFacing.SOUTH, Vec3d.center));
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

        boolean down = shouldRenderConnection(EnumFacing.DOWN);
        boolean up = shouldRenderConnection(EnumFacing.UP);
        boolean north = shouldRenderConnection(EnumFacing.NORTH);
        boolean south = shouldRenderConnection(EnumFacing.SOUTH);
        boolean west = shouldRenderConnection(EnumFacing.WEST);
        boolean east = shouldRenderConnection(EnumFacing.EAST);

        renderer.setColor(color);

        // Wire
        renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2),
                0.5 + (wireSize / 2), 0.5 + (wireSize / 2)), getIcons(EnumFacing.UNKNOWN));
        if (up || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 1,
                    0.5 + (wireSize / 2)), getIcons(EnumFacing.UP));
        if (down || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0, 0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 0.5 - (wireSize / 2),
                    0.5 + (wireSize / 2)), getIcons(EnumFacing.DOWN));
        if (north || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0, 0.5 + (wireSize / 2), 0.5 + (wireSize / 2),
                    0.5 - (wireSize / 2)), getIcons(EnumFacing.NORTH));
        if (south || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2), 0.5 + (wireSize / 2),
                    0.5 + (wireSize / 2), 1), getIcons(EnumFacing.SOUTH));
        if (west || !isInWorld)
            renderer.renderBox(new Vec3dCube(0, 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 0.5 + (wireSize / 2),
                    0.5 + (wireSize / 2)), getIcons(EnumFacing.WEST));
        if (east || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 + (wireSize / 2), 0.5 - (wireSize / 2), 0.5 - (wireSize / 2), 1, 0.5 + (wireSize / 2),
                    0.5 + (wireSize / 2)), getIcons(EnumFacing.EAST));

        renderer.setColor(getFrameColorMultiplier());

        // Frame
        renderFrame(renderer, wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, isInWorld, getFrameIcon(),
                getFrameColorMultiplier());

        return true;
    }

    @Override
    public int getHollowSize(EnumFacing side) {

        return 8;
    }

}
