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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.wire.redstone.WireHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3dHelper;

;

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
    protected TextureAtlasSprite getSideIcon() {
        return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(Blocks.STONE.getRegistryName().toString());// IconSupplier.pneumaticTubeOpaqueSide;
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, VertexBuffer buffer, int pass) {

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

            TextureAtlasSprite icon = this instanceof RestrictionTubeOpaque ? (renderFully ? IconSupplier.restrictionTubeNodeOpaque
                    : IconSupplier.restrictionTubeSideOpaque) : renderFully ? IconSupplier.pneumaticTubeOpaqueNode
                    : IconSupplier.pneumaticTubeOpaqueSide;

            if (shouldRenderConnection(EnumFacing.EAST) || shouldRenderConnection(EnumFacing.WEST))
                renderer.setTextureRotations(1, 1, 0, 0, 1, 1);
            if (shouldRenderConnection(EnumFacing.NORTH) || shouldRenderConnection(EnumFacing.SOUTH))
                renderer.setTextureRotations(0, 0, 1, 1, 0, 0);

            renderer.renderBox(new Vec3dCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75), icon);

            renderer.resetTextureRotations();

            for (EnumFacing d : EnumFacing.VALUES) {
                if (shouldRenderConnection(d)) {
                    renderer.addTransformation(new Rotation(d));
                    renderer.renderBox(new Vec3dCube(0.25, 0, 0.25, 0.75, 0.25, 0.75), IconSupplier.pneumaticTubeOpaqueNode, null, icon, icon, icon,
                            icon);
                    renderer.removeTransformation();
                }
            }

            if (getRedwireType() != null) {
                double wireSize = getSize() / 16D;
                double frameSeparation = 6 / 16D - (wireSize - 2 / 16D);
                double frameThickness = 1 / 16D;
                frameThickness /= 1.5;
                frameSeparation -= 1 / 32D;

                renderFrame(renderer, wireSize, frameSeparation, frameThickness, renderFully || shouldRenderConnection(EnumFacing.DOWN),
                        renderFully || shouldRenderConnection(EnumFacing.UP), renderFully || shouldRenderConnection(EnumFacing.WEST),
                        renderFully || shouldRenderConnection(EnumFacing.EAST), renderFully || shouldRenderConnection(EnumFacing.NORTH),
                        renderFully || shouldRenderConnection(EnumFacing.SOUTH), redstoneConnections[EnumFacing.DOWN.ordinal()],
                        redstoneConnections[EnumFacing.UP.ordinal()], redstoneConnections[EnumFacing.WEST.ordinal()],
                        redstoneConnections[EnumFacing.EAST.ordinal()], redstoneConnections[EnumFacing.NORTH.ordinal()],
                        redstoneConnections[EnumFacing.SOUTH.ordinal()], getParent() != null && getWorld() != null, IconSupplier.wire,
                        WireHelper.getColorForPowerLevel(getRedwireType(), getPower()));
            }

            // Tube coloring
            {
                Vec3dCube side = new Vec3dCube(0.25 + 5 / 128D, 0, 0.25 - 1 / 128D, 0.25 + 9 / 128D, 0.25, 0.25 + 2 / 128D);
                Vec3dCube side2 = new Vec3dCube(0.25 - 1 / 128D, 0, 0.25 + 5 / 128D, 0.25 + 2 / 128D, 0.25, 0.25 + 9 / 128D);
                Vec3dCube side3 = new Vec3dCube(0.25 - 1 / 128D, 0.25 - 1 / 128D, 0.25 + 5 / 128D, 0.25 + 2 / 128D, 0.25 + 2 / 128D, 0.25 + 59 / 128D);
                Vec3dCube side4 = new Vec3dCube(0.25 + 5 / 128D, 0.25 - 1 / 128D, 0.25 + 5 / 128D, 0.25 + 9 / 128D, 0.25 + 2 / 128D, 0.25 + 56 / 128D);
                Vec3dCube side5 = new Vec3dCube(0.25 + 5 / 128D, 0.25 - 1 / 128D, 0.25 - 1 / 128D, 0.25 + 9 / 128D, 0.25 + 2 / 128D, 0.25 + 65 / 128D);
                for (EnumFacing d : EnumFacing.VALUES) {
                    TubeColor c = color[d.ordinal()];
                    if (c != TubeColor.NONE) {
                        try {
                            renderer.setColor(MinecraftColor.values()[15 - c.ordinal()].getHex());
                            if (connections[d.ordinal()]) {
                                for (int i = 0; i < 4; i++) {
                                    renderer.renderBox(side.clone().rotate(0, i * 90, 0, Vec3dHelper.CENTER).rotate(d, Vec3dHelper.CENTER),
                                            IconSupplier.pneumaticTubeColoring);
                                    renderer.renderBox(side2.clone().rotate(0, i * 90, 0, Vec3dHelper.CENTER).rotate(d, Vec3dHelper.CENTER),
                                            IconSupplier.pneumaticTubeColoring);
                                    if (renderFully)
                                        renderer.renderBox(side3.clone().rotate(0, i * 90, 0, Vec3dHelper.CENTER).rotate(d, Vec3dHelper.CENTER),
                                                IconSupplier.pneumaticTubeColoring);
                                }
                            } else if (renderFully) {
                                for (int i = 0; i < 4; i++)
                                    renderer.renderBox(side4.clone().rotate(0, i * 90, 0, Vec3dHelper.CENTER).rotate(d, Vec3dHelper.CENTER),
                                            IconSupplier.pneumaticTubeColoring);
                            } else {
                                for (int i = 1; i < 4; i += 2)
                                    renderer.renderBox(
                                            side5.clone()
                                                    .rotate(0,
                                                            (i + ((shouldRenderConnection(EnumFacing.NORTH) || (shouldRenderConnection(EnumFacing.UP) && (d == EnumFacing.NORTH || d == EnumFacing.SOUTH))) ? 1
                                                                    : 0)) * 90, 0, Vec3dHelper.CENTER).rotate(d, Vec3dHelper.CENTER),
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
