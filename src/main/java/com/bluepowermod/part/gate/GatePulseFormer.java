package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.renderers.RenderHelper;

public class GatePulseFormer extends GateBase {

    private final boolean power[] = new boolean[4];

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().disable();
        back().enable();
        right().disable();
    }

    @Override
    public String getId() {

        return "pulseformer";
    }

    @Override
    public void renderTop(float frame) {

        RenderHelper.renderRedstoneTorch(3 / 16D, 1D / 8D, -1 / 16D, 9D / 16D, !power[0]);
        RenderHelper.renderRedstoneTorch(-3 / 16D, 1D / 8D, -1 / 16D, 9D / 16D, power[2]);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 5 / 16D, 9D / 16D, !power[2] && power[1]);

        renderTop("center", !power[1]);
        renderTop("back", power[0]);
        renderTop("left", !power[1]);
        renderTop("right", power[2]);
    }

    @Override
    public void doLogic() {
        power[0] = back().getInput() > 0;
    }

    @Override
    public void tick() {
        power[3] = power[2];
        power[2] = power[1];
        power[1] = power[0];
        front().setOutput(!power[2] && power[1]);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {
        super.writeUpdateToNBT(tag);
        tag.setBoolean("back", power[0]);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {
        super.readUpdateFromNBT(tag);
        power[0] = tag.getBoolean("back");
    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

}
