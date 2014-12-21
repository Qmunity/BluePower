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
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.BPPartFace;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class PartWireFace extends BPPartFace {

    protected abstract boolean shouldRenderConnection(ForgeDirection side);

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected int getColorMultiplier() {

        return 0xFFFFFF;
    }

    @SideOnly(Side.CLIENT)
    protected abstract IIcon getWireIcon(ForgeDirection side);

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        double height = getHeight() / 16D;
        double width = getWidth() / 32D;
        IIcon[] icons = new IIcon[] { getWireIcon(ForgeDirection.DOWN), getWireIcon(ForgeDirection.UP), getWireIcon(ForgeDirection.WEST),
                getWireIcon(ForgeDirection.EAST), getWireIcon(ForgeDirection.NORTH), getWireIcon(ForgeDirection.SOUTH) };
        int color = getColorMultiplier();

        ForgeDirection d1 = ForgeDirection.NORTH;
        ForgeDirection d2 = ForgeDirection.SOUTH;
        ForgeDirection d3 = ForgeDirection.WEST;
        ForgeDirection d4 = ForgeDirection.EAST;

        if (getFace() == ForgeDirection.NORTH) {
            d1 = ForgeDirection.UP;
            d2 = ForgeDirection.DOWN;
        } else if (getFace() == ForgeDirection.SOUTH) {
            d1 = ForgeDirection.DOWN;
            d2 = ForgeDirection.UP;
        } else if (getFace() == ForgeDirection.WEST) {
            d3 = ForgeDirection.UP;
            d4 = ForgeDirection.DOWN;
        } else if (getFace() == ForgeDirection.EAST) {
            d3 = ForgeDirection.DOWN;
            d4 = ForgeDirection.UP;
        } else if (getFace() == ForgeDirection.UP) {
            d3 = ForgeDirection.EAST;
            d4 = ForgeDirection.WEST;
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
        renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D - width, 8 / 16D + width, height, 8 / 16D + width), icons);
        // Sides
        if (s4 || s3) {
            if (s3 || (!s3 && s4 && !s1 && !s2))
                renderer.renderBox(new Vec3dCube(s3 ? 0 : 4 / 16D, 0, 8 / 16D - width, 8 / 16D - width, height, 8 / 16D + width), icons);
            if (s4 || (s3 && !s4 && !s1 && !s2))
                renderer.renderBox(new Vec3dCube(8 / 16D + width, 0, 8 / 16D - width, s4 ? 1 : 12 / 16D, height, 8 / 16D + width), icons);
            if (s1)
                renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, s1 ? 0 : 4 / 16D, 8 / 16D + width, height, 8 / 16D - width), icons);
            if (s2)
                renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height, s2 ? 1 : 12 / 16D), icons);
        } else {
            renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, s1 ? 0 : 4 / 16D, 8 / 16D + width, height, 8 / 16D - width), icons);
            renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height, s2 ? 1 : 12 / 16D), icons);
        }

        return true;
    }

}
