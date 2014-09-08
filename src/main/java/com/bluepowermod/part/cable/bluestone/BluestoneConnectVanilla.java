/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.cable.bluestone;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.bluestone.ABluestoneConnect;
import com.bluepowermod.api.bluestone.IBluestoneWire;
import com.bluepowermod.api.vec.Vector3;
import com.qmunity.lib.util.Dependencies;

import cpw.mods.fml.common.Loader;

public class BluestoneConnectVanilla extends ABluestoneConnect {

    protected BluestoneConnectVanilla() {

    }

    @Override
    public int getExtraLength(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        if (block.getBlock() == Blocks.lever)
            return 5;
        if (block.getBlock() == Blocks.wooden_button || block.getBlock() == Blocks.stone_button) {
            if (cableSide == ForgeDirection.UP || cableSide == ForgeDirection.DOWN) {
                return 6;
            } else {
                return 5;
            }
        }
        if (block.getBlock() == Blocks.wooden_pressure_plate || block.getBlock() == Blocks.stone_pressure_plate
                || block.getBlock() == Blocks.heavy_weighted_pressure_plate || block.getBlock() == Blocks.light_weighted_pressure_plate)
            return 1;
        if (block.getBlock() == Blocks.redstone_torch || block.getBlock() == Blocks.unlit_redstone_torch)
            return 7;
        if (block.getBlock() == Blocks.fence_gate)
            if (block.getBlockMeta() == 0 || block.getBlockMeta() == 2 || block.getBlockMeta() == 4 || block.getBlockMeta() == 6)
                return 6;
        if (block.getBlock() == Blocks.hopper)
            if (block.getBlockMeta() == 0)
                return 6;
        if (block.getBlock() == Blocks.wooden_door || block.getBlock() == Blocks.iron_door)
            if (block.getBlockMeta() == 1)
                return 13;

        return 0;
    }

    @Override
    public boolean canConnect(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        if (block.getBlock() == Blocks.redstone_lamp || block.getBlock() == Blocks.lit_redstone_lamp || block.getBlock() == Blocks.noteblock
                || block.getBlock() == Blocks.piston || block.getBlock() == Blocks.sticky_piston || block.getBlock() == Blocks.dispenser
                || block.getBlock() == Blocks.dropper || block.getBlock() == Blocks.hopper || block.getBlock() == Blocks.tnt
                || block.getBlock() == Blocks.fence_gate || block.getBlock() == Blocks.iron_door || block.getBlock() == Blocks.wooden_door)
            return true;

        return false;
    }

    @Override
    public void renderExtraCables(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        BluestoneApi api = BluestoneApi.getInstance();

        if ((block.getBlock() == Blocks.redstone_torch || block.getBlock() == Blocks.unlit_redstone_torch) && !Loader.isModLoaded(Dependencies.FMP))
            renderExtraCablesTorch(api, block, wire, cableSide);
    }

    private void renderExtraCablesTorch(BluestoneApi api, Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        GL11.glPushMatrix();

        int meta = block.getBlockMeta();
        meta %= 5;
        if (ForgeDirection.getOrientation(wire.getFace()) == ForgeDirection.UP) {
            switch (cableSide) {
            case EAST:
                meta += 3;
                if (meta == 5)
                    meta = 3;
                break;
            case WEST:
                switch (meta) {
                case 0:
                    break;
                case 1:
                    meta = 3;
                    break;
                case 2:
                    break;
                case 3:
                    meta = 2;
                    break;
                case 4:
                    meta = 1;
                    break;
                }
                break;
            case SOUTH:
                switch (meta) {
                case 0:
                    break;
                case 1:
                    meta = 2;
                    break;
                case 2:
                    meta = 1;
                    break;
                case 3:
                    break;
                case 4:
                    meta = 3;
                    break;
                }
                break;
            default:
                break;
            }
            if (meta == 1) {
                api.renderBox(-9, 0, 7, -7, 1, 16);
                api.renderBox(-9, 1, 15, -7, 3, 16);
            } else if (meta == 2) {
                api.renderBox(-9, 0, 0, -7, 1, 9);
                api.renderBox(-9, 1, 0, -7, 3, 1);
            } else if (meta == 3) {
                api.renderBox(-16, 0, 7, -7, 1, 9);
                api.renderBox(-16, 1, 7, -15, 3, 9);
            }
            // } else if (cableFace == ForgeDirection.DOWN) {
            // if (cableSide == ForgeDirection.EAST || cableSide == ForgeDirection.WEST) {
            // GL11.glTranslated(0.5, 0.5, 0.5);
            // GL11.glRotated(180, 0, 1, 0);
            // GL11.glTranslated(-0.5, -0.5, -0.5);
            // }
            // switch (cableSide) {
            // case EAST:
            // meta += 3;
            // if (meta == 5)
            // meta = 3;
            // break;
            // case WEST:
            // switch (meta) {
            // case 0:
            // break;
            // case 1:
            // meta = 3;
            // break;
            // case 2:
            // break;
            // case 3:
            // meta = 2;
            // break;
            // case 4:
            // meta = 1;
            // break;
            // }
            // break;
            // case SOUTH:
            // switch (meta) {
            // case 0:
            // break;
            // case 1:
            // meta = 2;
            // break;
            // case 2:
            // meta = 1;
            // break;
            // case 3:
            // break;
            // case 4:
            // meta = 3;
            // break;
            // }
            // break;
            // default:
            // break;
            // }
            // if (meta == 1) {
            // api.renderBox(16 + 7, 0, 7, 16 + 9, 1, 16);
            // api.renderBox(16 + 7, 1, 15, 16 + 9, 3, 16);
            // } else if (meta == 2) {
            // api.renderBox(16 + 7, 0, 0, 16 + 9, 1, 9);
            // api.renderBox(16 + 7, 1, 0, 16 + 9, 3, 1);
            // } else if (meta == 3) {
            // api.renderBox(16 + 7, 0, 7, 16 + 16, 1, 9);
            // api.renderBox(16 + 15, 1, 7, 16 + 16, 3, 9);
            // }
        }

        GL11.glPopMatrix();
    }
}