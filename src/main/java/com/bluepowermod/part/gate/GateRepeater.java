/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;

/**
 * @Author Koen Beckers (K4Unl)
 */
public class GateRepeater extends GateBase {

    private boolean power = false;
    private boolean powerBack = false;
    private int ticksRemaining = 0;
    private int location = 0;
    private int[] ticks = { 1, 2, 3, 4, 8, 16, 32, 64, 128, 256, 1024 };

    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

        // Init front
        front.enable();
        front.setOutput();

        // Init back
        back.enable();
        back.setInput();
    }

    @Override
    public String getGateID() {

        return "repeater";
    }

    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {

        renderTopTexture(FaceDirection.FRONT, power);
        RenderHelper.renderRedstoneTorch(pixel * -3, pixel * 2, pixel * -6, pixel * 8, !power);

        renderTopTexture(FaceDirection.BACK, powerBack);
        RenderHelper.renderRedstoneTorch(pixel * 4, pixel * 2, pixel * (4 - location), pixel * 8, (back.getPower() > 0));
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

        super.addOcclusionBoxes(boxes);

        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 9D / 16D, 9D / 16D));
    }

    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

        if (powerBack != back.getPower() > 0 && ticksRemaining == 0) {
            ticksRemaining = ticks[location];
        }
        powerBack = back.getPower() > 0;
        if (ticksRemaining == 0) {
            power = powerBack;
        } else {
            ticksRemaining--;
        }

        front.setPower(power ? 15 : 0);
    }

    @Override
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

        location++;
        if (location == 9) {
            location = 0;
        }
        return true;
    }

    @Override
    public void addWailaInfo(List<String> info) {

        /*
         * info.add(Color.YELLOW + I18n.format("gui.connections") + ":"); info.add("  " + FaceDirection.LEFT.getLocalizedName() + ": " +
         * (getConnection(FaceDirection.LEFT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED +
         * I18n.format("random.disabled"))); info.add("  " + FaceDirection.BACK.getLocalizedName() + ": " +
         * (getConnection(FaceDirection.BACK).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED +
         * I18n.format("random.disabled"))); info.add("  " + FaceDirection.RIGHT.getLocalizedName() + ": " +
         * (getConnection(FaceDirection.RIGHT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED +
         * I18n.format("random.disabled")));
         */
    }

    @Override
    public void save(NBTTagCompound tag) {

        super.save(tag);
        tag.setInteger("location", location);
    }

    @Override
    public void load(NBTTagCompound tag) {

        super.load(tag);
        location = tag.getInteger("location");
    }

}
