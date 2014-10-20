package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.resources.I18n;

import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Color;

public class GateMux extends GateBase {

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable();
        right().enable();
    }

    @Override
    public String getId() {

        return "multiplexer";
    }

    @Override
    public void renderTop(float frame) {

        // renderTopTexture(FaceDirection.FRONT, false);
        renderTop("left", left().getInput() > 0 || back().getInput() == 0);
        renderTop("right", right().getInput() > 0 || back().getInput() > 0);
        renderTop("back", back().getInput() > 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -2 / 16D, 9D / 16D, back().getInput() == 0);
        boolean frontLeft = !(left().getInput() > 0 || back().getInput() == 0);
        boolean frontRight = !(right().getInput() > 0 || back().getInput() > 0);
        RenderHelper.renderRedstoneTorch(-4 / 16D, 1D / 8D, 1 / 16D, 9D / 16D, frontRight);
        RenderHelper.renderRedstoneTorch(4 / 16D, 1D / 8D, 1 / 16D, 9D / 16D, frontLeft);

        renderTop("frontleft", frontLeft);
        renderTop("frontRight", frontRight);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 4 / 16D, 9D / 16D, !frontLeft && !frontRight);
    }

    @Override
    public void doLogic() {

        boolean selected = back().getInput() > 0;
        int out = 0;

        if (selected) {
            out = left().getInput();
        } else {
            out = right().getInput();
        }

        front().setOutput(out);
    }

    @Override
    public void addWailaInfo(List<String> info) {

        info.add(I18n.format("gui.passThrough") + ": " + Color.YELLOW
                + (back().getInput() > 0 ? I18n.format("direction.left") : I18n.format("direction.right")));
    }

}
