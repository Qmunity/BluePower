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

package com.bluepowermod.part.tube;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.wire.redstone.WireCommons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PneumaticTubeOpaque extends PneumaticTube {

    @Override
    public String getType() {

        return "pneumaticTubeOpaque";
    }

    @Override
    public String getUnlocalizedName() {

        return "pneumaticTubeOpaque";
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon() {

        return Blocks.stone.getIcon(0, 0);// IconSupplier.pneumaticTubeOpaqueSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i loc, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        if (pass == 0) {
            boolean renderFully = false;
            int count = 0;

            for (int i = 0; i < 6; i++) {
                if (connections[i] || redstoneConnections[i])
                    count++;
                if (i % 2 == 0 && connections[i] != connections[i + 1])
                    renderFully = true;
            }

            renderFully |= count > 2 || count == 0;

            IIcon icon = this instanceof RestrictionTubeOpaque ? (renderFully ? IconSupplier.restrictionTubeNodeOpaque
                    : IconSupplier.restrictionTubeSideOpaque) : renderFully ? IconSupplier.pneumaticTubeOpaqueNode
                            : IconSupplier.pneumaticTubeOpaqueSide;

            if (shouldRenderConnection(ForgeDirection.EAST) || shouldRenderConnection(ForgeDirection.WEST))
                renderer.setTextureRotations(1, 1, 0, 0, 1, 1);
            if (shouldRenderConnection(ForgeDirection.NORTH) || shouldRenderConnection(ForgeDirection.SOUTH))
                renderer.setTextureRotations(0, 0, 1, 1, 0, 0);

            renderer.renderBox(new Vec3dCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75), icon);

            renderer.resetTextureRotations();

            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (shouldRenderConnection(d)) {
                    renderer.addTransformation(new Rotation(d));
                    renderer.renderBox(new Vec3dCube(0.25, 0, 0.25, 0.75, 0.25, 0.75), IconSupplier.pneumaticTubeOpaqueNode, null, icon,
                            icon, icon, icon);
                    renderer.removeTransformation();
                }
            }

            if (getRedwireType() != null) {
                double wireSize = getSize() / 16D;
                double frameSeparation = 6 / 16D - (wireSize - 2 / 16D);
                double frameThickness = 1 / 16D;
                frameThickness /= 1.5;
                frameSeparation -= 1 / 32D;

                renderFrame(renderer, wireSize, frameSeparation, frameThickness,
                        renderFully || shouldRenderConnection(ForgeDirection.DOWN), renderFully
                        || shouldRenderConnection(ForgeDirection.UP), renderFully || shouldRenderConnection(ForgeDirection.WEST),
                        renderFully || shouldRenderConnection(ForgeDirection.EAST), renderFully
                        || shouldRenderConnection(ForgeDirection.NORTH), renderFully
                        || shouldRenderConnection(ForgeDirection.SOUTH), redstoneConnections[ForgeDirection.DOWN.ordinal()],
                        redstoneConnections[ForgeDirection.UP.ordinal()], redstoneConnections[ForgeDirection.WEST.ordinal()],
                        redstoneConnections[ForgeDirection.EAST.ordinal()], redstoneConnections[ForgeDirection.NORTH.ordinal()],
                        redstoneConnections[ForgeDirection.SOUTH.ordinal()], getParent() != null && getWorld() != null, IconSupplier.wire,
                        WireCommons.getColorForPowerLevel(getRedwireType(), RedstoneConductorTube.getDevice(this).getPower()));
            }

            // Tube coloring
            {
                Vec3dCube side = new Vec3dCube(0.25 + 5 / 128D, 0, 0.25 - 1 / 128D, 0.25 + 9 / 128D, 0.25, 0.25 + 2 / 128D);
                Vec3dCube side2 = new Vec3dCube(0.25 - 1 / 128D, 0, 0.25 + 5 / 128D, 0.25 + 2 / 128D, 0.25, 0.25 + 9 / 128D);
                Vec3dCube side3 = new Vec3dCube(0.25 - 1 / 128D, 0.25 - 1 / 128D, 0.25 + 5 / 128D, 0.25 + 2 / 128D, 0.25 + 2 / 128D,
                        0.25 + 59 / 128D);
                Vec3dCube side4 = new Vec3dCube(0.25 + 5 / 128D, 0.25 - 1 / 128D, 0.25 + 5 / 128D, 0.25 + 9 / 128D, 0.25 + 2 / 128D,
                        0.25 + 56 / 128D);
                Vec3dCube side5 = new Vec3dCube(0.25 + 5 / 128D, 0.25 - 1 / 128D, 0.25 - 1 / 128D, 0.25 + 9 / 128D, 0.25 + 2 / 128D,
                        0.25 + 65 / 128D);
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    TubeColor c = color[d.ordinal()];
                    if (c != TubeColor.NONE) {
                        try {
                            renderer.setColor(MinecraftColor.values()[15 - c.ordinal()].getHex());
                            if (connections[d.ordinal()]) {
                                for (int i = 0; i < 4; i++) {
                                    renderer.renderBox(side.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                                    renderer.renderBox(side2.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                                    if (renderFully)
                                        renderer.renderBox(side3.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                                IconSupplier.pneumaticTubeColoring);
                                }
                            } else if (renderFully) {
                                for (int i = 0; i < 4; i++)
                                    renderer.renderBox(side4.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                            } else {
                                for (int i = 1; i < 4; i += 2)
                                    renderer.renderBox(
                                            side5.clone()
                                            .rotate(0,
                                                    (i + ((shouldRenderConnection(ForgeDirection.NORTH) || (shouldRenderConnection(ForgeDirection.UP) && (d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH))) ? 1
                                                            : 0)) * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                                            IconSupplier.pneumaticTubeColoring);
                            }
                            renderer.setColor(0xFFFFFF);
                        } catch (Exception ex) {
                            System.out.println("Err on side " + d + ". Color: " + c);
                        }
                    }
                }
            }
        }

        return true;
    }
}
