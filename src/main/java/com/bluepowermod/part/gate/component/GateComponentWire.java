package com.bluepowermod.part.gate.component;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.RedstoneConnection;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.wire.redstone.RedwireType;
import com.bluepowermod.part.wire.redstone.WireCommons;

public class GateComponentWire extends GateComponentLocationArray {

    private RedwireType type;

    private RedstoneConnection connection;

    public GateComponentWire(GateBase gate, int color, RedwireType type) {

        super(gate, color);

        this.type = type;
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        byte power = 0;
        int color = type.getColor();
        if (connection != null) {
            if (connection.isEnabled()) {
                int src = connection.getOutput();
                if (!connection.isOutputOnly())
                    src = Math.max(src, connection.getInput());
                power = (byte) ((src / 15D) * 255);
            } else {
                power = (byte) (255 / 2);
                color = 0x999999;
            }
        }

        renderer.setColor(WireCommons.getColorForPowerLevel(color, power));
        double height = 1 / 48D;
        double size = 1 / ((double) pixels.length);

        for (int x = 0; x < pixels.length; x++) {
            boolean[] p = pixels[x];
            for (int y = 0; y < p.length; y++) {
                if (p[y]) {
                    double dx = x / (double) pixels.length;
                    double dy = y / (double) p.length;

                    boolean west = x == 0 || !pixels[x - 1][y];
                    boolean east = x == pixels.length - 1 || !pixels[x + 1][y];
                    boolean north = y == 0 || !pixels[x][y - 1];
                    boolean south = y == p.length - 1 || !pixels[x][y + 1];

                    renderer.setRenderSides(false, true, west, east, north, south);
                    renderer.renderBox(new Vec3dCube(dx, 2 / 16D, dy, dx + size, 2 / 16D + height, dy + size), IconSupplier.wire);
                }
            }
        }

        renderer.resetRenderedSides();
        renderer.setColor(0xFFFFFF);
    }

    public GateComponentWire bind(RedstoneConnection connection) {

        this.connection = connection;

        return this;
    }

}
