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
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.BPPartFace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PartWireFace extends BPPartFace {

    protected abstract boolean shouldRenderConnection(EnumFacing side);

    protected abstract double getWidth();

    protected abstract double getHeight();

    protected int getColorMultiplier() {

        return 0xFFFFFF;
    }

    protected boolean extendsToCorner(EnumFacing side) {

        return false;
    }

    @SideOnly(Side.CLIENT)
    protected abstract IIcon getWireIcon(EnumFacing side);

    @SideOnly(Side.CLIENT)
    protected IIcon getWireIcon(EnumFacing side, EnumFacing face) {

        return getWireIcon(face);
    }

    @SideOnly(Side.CLIENT)
    protected IIcon[] getIcons(EnumFacing side) {

        return new IIcon[] { getWireIcon(side, EnumFacing.DOWN), getWireIcon(side, EnumFacing.UP),
                getWireIcon(side, EnumFacing.WEST), getWireIcon(side, EnumFacing.EAST), getWireIcon(side, EnumFacing.NORTH),
                getWireIcon(side, EnumFacing.SOUTH) };
    }

    protected boolean shouldIgnoreLighting() {

        return false;
    }

    protected int getBrightness() {

        return 0xF000F0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        renderer.setIgnoreLighting(shouldIgnoreLighting());
        renderer.setLightingOverride(getBrightness());

        double height = (getHeight() / 16D) - 0.001;
        double width = getWidth() / 32D;
        int color = getColorMultiplier();

        EnumFacing d1 = EnumFacing.NORTH;
        EnumFacing d2 = EnumFacing.SOUTH;
        EnumFacing d3 = EnumFacing.WEST;
        EnumFacing d4 = EnumFacing.EAST;

        if (getFace() == EnumFacing.NORTH) {
            d1 = EnumFacing.UP;
            d2 = EnumFacing.DOWN;
        } else if (getFace() == EnumFacing.SOUTH) {
            d1 = EnumFacing.DOWN;
            d2 = EnumFacing.UP;
        } else if (getFace() == EnumFacing.WEST) {
            d3 = EnumFacing.UP;
            d4 = EnumFacing.DOWN;
        } else if (getFace() == EnumFacing.EAST) {
            d3 = EnumFacing.DOWN;
            d4 = EnumFacing.UP;
        } else if (getFace() == EnumFacing.UP) {
            d3 = EnumFacing.EAST;
            d4 = EnumFacing.WEST;
        }

        switch (getFace()) {
        case DOWN:
            break;
        case UP:
            renderer.addTransformation(new Rotation(180, 180, 0, Vec3d.center));
            break;
        case NORTH:
            renderer.addTransformation(new Rotation(90, 90, 0, Vec3d.center));
            d1 = d1.getRotation(getFace());
            d2 = d2.getRotation(getFace());
            d3 = d3.getRotation(getFace());
            d4 = d4.getRotation(getFace());
            break;
        case SOUTH:
            renderer.addTransformation(new Rotation(-90, 90, 0, Vec3d.center));
            d1 = d1.getRotation(getFace());
            d2 = d2.getRotation(getFace());
            d3 = d3.getRotation(getFace());
            d4 = d4.getRotation(getFace());
            break;
        case WEST:
            renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
            break;
        case EAST:
            renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
            break;
        default:
            break;
        }

        boolean s1 = shouldRenderConnection(d1);
        boolean s2 = shouldRenderConnection(d2);
        boolean s3 = shouldRenderConnection(d3);
        boolean s4 = shouldRenderConnection(d4);

        renderer.setColor(color);

        // Center
        renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D - width, 8 / 16D + width, height, 8 / 16D + width),
                getIcons(EnumFacing.UNKNOWN));
        // Sides
        if (s4 || s3) {
            if (s3 || (!s3 && s4 && !s1 && !s2))
                renderer.renderBox(new Vec3dCube(s3 ? (extendsToCorner(d3) ? -height : 0) : 4 / 16D, 0, 8 / 16D - width, 8 / 16D - width,
                        height, 8 / 16D + width), getIcons(EnumFacing.WEST));
            if (s4 || (s3 && !s4 && !s1 && !s2))
                renderer.renderBox(new Vec3dCube(8 / 16D + width, 0, 8 / 16D - width, s4 ? 1 + (extendsToCorner(d4) ? height : 0)
                        : 12 / 16D, height, 8 / 16D + width), getIcons(EnumFacing.EAST));
            if (s1)
                renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, s1 ? (extendsToCorner(d1) ? -height : 0) : 4 / 16D, 8 / 16D + width,
                        height, 8 / 16D - width), getIcons(EnumFacing.NORTH));
            if (s2)
                renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height,
                        s2 ? 1 + (extendsToCorner(d2) ? height : 0) : 12 / 16D), getIcons(EnumFacing.SOUTH));
        } else {
            renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, s1 ? (extendsToCorner(d1) ? -height : 0) : 4 / 16D, 8 / 16D + width,
                    height, 8 / 16D - width), getIcons(EnumFacing.NORTH));
            renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height,
                    s2 ? 1 + (extendsToCorner(d2) ? height : 0) : 12 / 16D), getIcons(EnumFacing.SOUTH));
        }

        renderer.setIgnoreLighting(false);
        renderer.setColor(0xFFFFFF);

        return true;
    }

}
