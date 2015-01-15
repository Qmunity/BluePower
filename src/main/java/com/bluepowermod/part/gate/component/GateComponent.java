package com.bluepowermod.part.gate.component;

import java.util.Arrays;
import java.util.List;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.gate.GateBase;

public abstract class GateComponent {

    private GateBase gate;

    public GateComponent(GateBase gate) {

        this.gate = gate;
    }

    public GateBase getGate() {

        return gate;
    }

    public void addCollisionBoxes(List<Vec3dCube> boxes) {

    }

    public List<Vec3dCube> getOcclusionBoxes() {

        return Arrays.asList();
    }

    public void tick() {

    }

    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

    }

    public void renderDynamic(Vec3d translation, double delta, int pass) {

    }

    public void onLayoutRefresh() {

    }

}
