/*
 * This file is part of Blue Power.
 *
 *      Blue Power is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      Blue Power is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.gui.gate.GuiGateCounter;
import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.util.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by Quetzi on 04/11/14.
 */
public class GateCounter extends GateBase implements IGuiButtonSensitive {

    private int count = 0, max = 10, increment = 1, decrement = 1;

    private boolean wasOnLeft = false, wasOnRight = false;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        back().enable().setOutputOnly();
        right().enable();
    }

    @Override
    public String getId() {

        return "counter";
    }

    @Override
    public void renderTop(float frame) {

        renderTop("left", left().getInput() > 0);
        renderTop("right", right().getInput() > 0);
        renderTop("centerleft", left().getInput() > 0);

        RenderHelper.renderDigitalRedstoneTorch(-2 / 16D, 2D / 8D, 0, 13D / 16D, true);
        RenderHelper.renderDigitalRedstoneTorch(0, 2D / 8D, -5D / 16D, 8D / 16D, count == 0);
        RenderHelper.renderDigitalRedstoneTorch(0, 2D / 8D, 5D / 16D, 8D / 16D, count == max);
        GL11.glPushMatrix();
        {
            GL11.glTranslated(2 / 16D, 0, 0);
            double min = 0.555;
            double max = 0.385;

            double angle = min + max * (count / (double) this.max);

            RenderHelper.renderPointer(-4 / 16D, 7 / 16D, 0, -angle + 0.5);
        }
        GL11.glPopMatrix();
    }

    @Override
    public void doLogic() {

        if (left().getInput() > 0 && !wasOnLeft) {
            wasOnLeft = true;
            count -= decrement;
            playTickSound();
        }
        if (left().getInput() == 0)
            wasOnLeft = false;

        if (right().getInput() > 0 && !wasOnRight) {
            wasOnRight = true;
            count += increment;
            playTickSound();
        }
        if (right().getInput() == 0)
            wasOnRight = false;

        count = Math.max(Math.min(count, max), 0);
        increment = Math.max(Math.min(increment, max), 0);
        decrement = Math.max(Math.min(decrement, max), 0);

        front().setOutput(count == max ? 15 : 0);
        back().setOutput(count == 0 ? 15 : 0);
    }

    @Override
    public void tick() {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        writeUpdateToNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setInteger("count", count);
        tag.setInteger("decrement", decrement);
        tag.setInteger("increment", increment);
        tag.setInteger("max", max);
        tag.setBoolean("wasOnLeft", wasOnLeft);
        tag.setBoolean("wasOnRight", wasOnRight);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        readUpdateFromNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        count = tag.getInteger("count");
        decrement = tag.getInteger("decrement");
        increment = tag.getInteger("increment");
        max = tag.getInteger("max");
        wasOnLeft = tag.getBoolean("wasOnLeft");
        wasOnRight = tag.getBoolean("wasOnRight");
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        switch (messageId) {
        case 0:
            max = value;
            break;
        case 1:
            increment = value;
            break;
        case 2:
            decrement = value;
            break;
        }
        count = Math.max(Math.min(count, max), 0);
        increment = Math.max(Math.min(increment, max), 0);
        decrement = Math.max(Math.min(decrement, max), 0);
        sendUpdatePacket();
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui() {

        return new GuiGateCounter(this) {

            @Override
            protected int getCurrentMax() {

                return max;
            }

            @Override
            protected int getCurrentIncrement() {

                return increment;
            }

            @Override
            protected int getCurrentDecrement() {

                return decrement;
            }
        };
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }

    @Override
    public void addWAILABody(List<String> info) {

        info.add(I18n.format("gui.counterMax") + ": " + Color.YELLOW + max);
        info.add(I18n.format("gui.counterCount") + ": " + Color.YELLOW + count);
        info.add(I18n.format("gui.counterIncrement") + ": " + Color.WHITE + increment);
        info.add(I18n.format("gui.counterDecrement") + ": " + Color.WHITE + decrement);
    }
}
