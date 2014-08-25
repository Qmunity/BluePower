package com.bluepowermod.part.cable.bluestone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.bluestone.ABluestoneConnect;
import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.bluestone.IBluestoneWire;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Dependencies;
import com.jcraft.jorbis.Block;

import cpw.mods.fml.common.Loader;
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
    public int getExtraLength(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        int extra = 0;
        for (ABluestoneConnect c : connectionHandlers)
            extra = Math.max(extra, c.getExtraLength(block, wire, cableSide));

        return extra;
    }

    @Override
    public boolean canConnect(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide) {

        for (ABluestoneConnect c : connectionHandlers)
            if (c.canConnect(block, wire, cableSide))
                return true;

        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz) {

        renderBox(minx, miny, minz, maxx, maxy, maxz, 16);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz, int textureSize) {

        double size = textureSize;

        GL11.glBegin(GL11.GL_QUADS);
        {
            // Top
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(minx / size, maxy / size, minz / size, minx / size, minz / size);
            RenderHelper.addVertexWithTexture(minx / size, maxy / size, maxz / size, minx / size, maxz / size);
            RenderHelper.addVertexWithTexture(maxx / size, maxy / size, maxz / size, maxx / size, maxz / size);
            RenderHelper.addVertexWithTexture(maxx / size, maxy / size, minz / size, maxx / size, minz / size);
            // Bottom
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(minx / size, miny / size, minz / size, minx / size, minz / size);
            RenderHelper.addVertexWithTexture(maxx / size, miny / size, minz / size, maxx / size, minz / size);
            RenderHelper.addVertexWithTexture(maxx / size, miny / size, maxz / size, maxx / size, maxz / size);
            RenderHelper.addVertexWithTexture(minx / size, miny / size, maxz / size, minx / size, maxz / size);
            // West
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(minx / size, maxy / size, minz / size, (minx - miny) / size, minz / size);
            RenderHelper.addVertexWithTexture(minx / size, miny / size, minz / size, (minx - maxy) / size, minz / size);
            RenderHelper.addVertexWithTexture(minx / size, miny / size, maxz / size, (minx - maxy) / size, maxz / size);
            RenderHelper.addVertexWithTexture(minx / size, maxy / size, maxz / size, (minx - miny) / size, maxz / size);
            // East
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(maxx / size, maxy / size, minz / size, (maxx + miny) / size, minz / size);
            RenderHelper.addVertexWithTexture(maxx / size, maxy / size, maxz / size, (maxx + miny) / size, maxz / size);
            RenderHelper.addVertexWithTexture(maxx / size, miny / size, maxz / size, (maxx + maxy) / size, maxz / size);
            RenderHelper.addVertexWithTexture(maxx / size, miny / size, minz / size, (maxx + maxy) / size, minz / size);
            // South
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(minx / size, miny / size, minz / size, minx / size, (minz - maxy) / size);
            RenderHelper.addVertexWithTexture(minx / size, maxy / size, minz / size, minx / size, (minz - miny) / size);
            RenderHelper.addVertexWithTexture(maxx / size, maxy / size, minz / size, maxx / size, (minz - miny) / size);
            RenderHelper.addVertexWithTexture(maxx / size, miny / size, minz / size, maxx / size, (minz - maxy) / size);
            // North
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(minx / size, miny / size, maxz / size, minx / size, (maxz + maxy) / size);
            RenderHelper.addVertexWithTexture(maxx / size, miny / size, maxz / size, maxx / size, (maxz + maxy) / size);
            RenderHelper.addVertexWithTexture(maxx / size, maxy / size, maxz / size, maxx / size, (maxz + miny) / size);
            RenderHelper.addVertexWithTexture(minx / size, maxy / size, maxz / size, minx / size, (maxz + miny) / size);
        }
        GL11.glEnd();
    }

    @Override
    public void renderExtraCables(Vector3 block, IBluestoneWire wire, ForgeDirection side) {

        for (ABluestoneConnect c : connectionHandlers)
            c.renderExtraCables(block, wire, side);
    }

    // Register default handling
    static {

        BluestoneApi api = getInstance();

        api.registerSpecialConnection(new BluestoneConnectVanilla());

        if (Loader.isModLoaded(Dependencies.FMP)) {
            api.registerSpecialConnection(new BluestoneConnectBPMultipart());
            api.registerSpecialConnection(new BluestoneConnectFMP());
        }

    }

}
