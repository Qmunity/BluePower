package com.bluepowermod.part.gate.component;

import uk.co.qmunity.lib.vec.Vec3d;

import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.part.gate.GateBase;

public class GateComponentPointer extends GateComponentTorch {

    public GateComponentPointer(GateBase gate, int color, double height, boolean digital) {

        super(gate, color, height, digital);
    }

    public GateComponentPointer(GateBase gate, double x, double z, double height, boolean digital) {

        super(gate, x, z, height, digital);
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        RenderHelper.renderPointer((1 - x) - 9 / 16D, height - 4 / 32D, z - 7 / 16D, 0);
    }

}
