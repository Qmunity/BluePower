package com.bluepowermod.part.cable;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.bluestone.ABluestoneConnect;
import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderHelper;
import com.jcraft.jorbis.Block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BluestoneApi implements IBluestoneApi {

    private static BluestoneApi INSTANCE = new BluestoneApi();

    private BluestoneApi() {

    }

    public static BluestoneApi getInstance() {

        return INSTANCE;
    }

    private List<ABluestoneConnect> connectionHandlers = new ArrayList<ABluestoneConnect>();

    @Override
    public void registerSpecialConnection(Block block, int metadata, ForgeDirection cableSide, int extraLength) {

    }

    @Override
    public void registerSpecialConnection(TileEntity te, ForgeDirection cableSide, int extraLength) {

    }

    @Override
    public void registerSpecialConnection(ABluestoneConnect connection) {

        if (connection == null)
            return;
        if (connectionHandlers.contains(connection))
            return;

        connectionHandlers.add(connection);
    }

    @Override
    public int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

        int extra = 0;
        for (ABluestoneConnect c : connectionHandlers)
            extra = Math.max(extra, c.getExtraLength(block, cableFace, cableSide));

        return extra;
    }

    @Override
    public boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

        for (ABluestoneConnect c : connectionHandlers)
            if (c.canConnect(block, cableFace, cableSide))
                return true;

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz) {

        GL11.glBegin(GL11.GL_QUADS);
        {
            // Top
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, maxy / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, maxy / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, maxy / 16D, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, maxy / 16D, minz / 16D, maxx / 16D, minz / 16D);
            // Bottom
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, miny / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, miny / 16D, minz / 16D, maxx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, miny / 16D, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, miny / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            // South
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(maxx / 16D, maxy / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, maxy / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, miny / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, miny / 16D, minz / 16D, minx / 16D, minz / 16D);
            // North
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, maxy / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, miny / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, miny / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, maxy / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            // East
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, miny / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, maxy / 16D, minz / 16D, minx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, maxy / 16D, minz / 16D, maxx / 16D, minz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, miny / 16D, minz / 16D, maxx / 16D, minz / 16D);
            // West
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(minx / 16D, miny / 16D, maxz / 16D, minx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, miny / 16D, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(maxx / 16D, maxy / 16D, maxz / 16D, maxx / 16D, maxz / 16D);
            RenderHelper.addVertexWithTexture(minx / 16D, maxy / 16D, maxz / 16D, minx / 16D, maxz / 16D);
        }
        GL11.glEnd();
    }

    @Override
    public void renderExtraCables(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

        for (ABluestoneConnect c : connectionHandlers)
            c.renderExtraCables(block, cableFace, cableSide);
    }

    // Register default handling
    static {

        BluestoneApi api = getInstance();

        api.registerSpecialConnection(new BluestoneConnectVanilla());

    }

    // Vanilla handling
    private static class BluestoneConnectVanilla extends ABluestoneConnect {

        @Override
        public int getExtraLength(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

            if (block.getBlock() == Blocks.lever)
                return 5;
            if (block.getBlock() == Blocks.wooden_pressure_plate || block.getBlock() == Blocks.stone_pressure_plate
                    || block.getBlock() == Blocks.heavy_weighted_pressure_plate || block.getBlock() == Blocks.light_weighted_pressure_plate)
                return 1;
            if (block.getBlock() == Blocks.redstone_torch)
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
        public boolean canConnect(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

            if (block.getBlock() == Blocks.redstone_lamp || block.getBlock() == Blocks.noteblock || block.getBlock() == Blocks.piston
                    || block.getBlock() == Blocks.sticky_piston || block.getBlock() == Blocks.dispenser || block.getBlock() == Blocks.dropper
                    || block.getBlock() == Blocks.hopper || block.getBlock() == Blocks.tnt || block.getBlock() == Blocks.fence_gate
                    || block.getBlock() == Blocks.iron_door || block.getBlock() == Blocks.wooden_door)
                return true;

            return false;
        }

        @Override
        public void renderExtraCables(Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

            BluestoneApi api = BluestoneApi.getInstance();

            if (block.getBlock() == Blocks.redstone_torch)
                renderExtraCablesTorch(api, block, cableFace, cableSide);
        }

        private void renderExtraCablesTorch(BluestoneApi api, Vector3 block, ForgeDirection cableFace, ForgeDirection cableSide) {

            GL11.glPushMatrix();

            int meta = block.getBlockMeta();
            meta %= 5;
            if (cableFace == ForgeDirection.UP) {
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

}
