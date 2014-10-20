package com.bluepowermod.part.gate;

import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.client.renderers.RenderHelper;

public class GateRandomizer extends GateBase {

    private static final Random random = new Random();

    private int ticks = 0;

    private final boolean out[] = new boolean[3];

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable().setOutputOnly();
        right().enable().setOutputOnly();
        back().enable();
    }

    @Override
    public String getId() {

        return "randomizer";
    }

    @Override
    public void renderTop(float frame) {

        renderTop("left", out[0]);
        renderTop("front", out[1]);
        renderTop("right", out[2]);
        renderTop("center", back().getInput() > 0);

        RenderHelper.renderRandomizerButton(this, 4 / 16D, 0, -6 / 16D, out[0]);
        RenderHelper.renderRandomizerButton(this, 0, 0, 0, out[1]);
        RenderHelper.renderRandomizerButton(this, -4 / 16D, 0, -6 / 16D, out[2]);
    }

    @Override
    public void tick() {

        if (!getWorld().isRemote) {
            if (back().getInput() > 0) {
                if (ticks % 5 == 0) {
                    out[0] = random.nextBoolean();
                    out[1] = random.nextBoolean();
                    out[2] = random.nextBoolean();
                    left().setOutput(out[0]);
                    front().setOutput(out[1]);
                    right().setOutput(out[2]);
                    sendUpdatePacket();
                }
                ticks++;
            } else {
                ticks = 0;
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setBoolean("out_0", out[0]);
        tag.setBoolean("out_1", out[1]);
        tag.setBoolean("out_2", out[2]);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        out[0] = tag.getBoolean("out_0");
        out[1] = tag.getBoolean("out_1");
        out[2] = tag.getBoolean("out_2");
    }

    @Override
    public void addWailaInfo(List<String> info) {

    }

    @Override
    public void doLogic() {

    }
}
