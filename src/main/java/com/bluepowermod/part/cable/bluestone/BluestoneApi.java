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
    public void renderExtraCables(Vector3 block, IBluestoneWire wire, ForgeDirection side) {

        for (ABluestoneConnect c : connectionHandlers)
            c.renderExtraCables(block, wire, side);
    }

    // Register default handling
    static {

        BluestoneApi api = getInstance();

        api.registerSpecialConnection(new BluestoneConnectVanilla());
        api.registerSpecialConnection(new BluestoneConnectBPMultipart());

        if (Loader.isModLoaded(Dependencies.FMP))
            api.registerSpecialConnection(new BluestoneConnectFMP());

    }

}
