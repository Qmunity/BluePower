package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.BPItems;
import com.qmunity.lib.raytrace.QMovingObjectPosition;

public class GateToggleLatch extends GateBase {

    private boolean power = false;
    private boolean state = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        right().enable();
        back().enable().setOutputOnly();
    }

    @Override
    public String getId() {

        return "toggle";
    }

    @Override
    protected void renderTop(float frame) {

        renderTop("centerleft", power);
        renderTop("left", power);
        renderTop("centerright", power);
        renderTop("right", power);
        RenderHelper.renderRedstoneTorch(-2.5D / 8D, 1D / 8D, 2.5D / 8D, 9D / 16D, !state);
        RenderHelper.renderRedstoneTorch(-2.5D / 8D, 1D / 8D, -2.5D / 8D, 9D / 16D, state);
        // RenderHelper.renderLever(this, 9 / 16D, 1 / 8D, 4 / 16D, !state);
    }

    @Override
    public void doLogic() {

        if ((power != right().getInput() > 0 || left().getInput() > 0) && !power) {
            state = !state;
            playTickSound();
        }
        power = right().getInput() > 0 || left().getInput() > 0;

        front().setOutput(!state ? 0 : 15);
        back().setOutput(state ? 0 : 15);
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (item == null || item.getItem() != BPItems.screwdriver) {
            state = !state;
            playTickSound();
            return true;
        } else {
            return super.onActivated(player, hit, item);
        }
    }

    @Override
    public void addWailaInfo(List<String> info) {

    }
}
