package com.bluepowermod.part.gate;

import java.util.List;

import com.qmunity.lib.util.Dir;

public class GateWire extends GateBase {

    public static final String ID = "icWire";

    @Override
    public void initializeConnections() {

        front().enable();
        left().enable();
        back().enable();
        right().enable();

        /*  front.setOutput();
          left.setOutput();
          back.setOutput();
          right.setOutput();*/
    }

    @Override
    public String getId() {

        return ID;
    }

    @Override
    protected void renderTop(float frame) {

        boolean isOn = false;
        for (Dir dir : Dir.values()) {
            if (getConnection(dir).isEnabled()) {
                isOn = getConnection(dir).getInput() > 0 || getConnection(dir).getOutput() > 0;
                break;
            }
        }
        this.renderTop("front", front());
        renderTop("back", back());
        renderTop("left", left());
        renderTop("right", right());
        renderTop("center_" + (isOn ? "on" : "off"));
    }

    @Override
    public boolean changeMode() {

        if (front().isEnabled() && left().isEnabled() && back().isEnabled() && right().isEnabled()) {
            right().disable();
        } else if (front().isEnabled() && left().isEnabled() && back().isEnabled()) {
            left().disable();
        } else if (front().isEnabled() && back().isEnabled()) {
            left().enable();
            front().disable();
        } else if (left().isEnabled() && back().isEnabled()) {
            front().enable();
            right().enable();
        }
        return true;
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

}
