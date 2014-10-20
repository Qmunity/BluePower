package com.bluepowermod.part.gate;

import java.util.List;

public class GateLightCell extends GateBase {

    @Override
    public void initializeConnections() {
        front().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "lightCell";
    }

    @Override
    public void renderTop(float frame) {
        renderTop("front", front());
    }

    @Override
    public void tick() {
        if (getWorld().getWorldTime() % 60 == 0) {
            int light = getWorld().getBlockLightValue(getX(), getY(), getZ());
            front().setOutput(light);
        }
    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

    @Override
    public void doLogic() {

    }

}
