/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.util.QLog;

import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.util.Refs;

public class GateRSLatch extends GateBase {

    private int mode;

    @Override
    public void initializeConnections() {

        front().enable().setOutputOnly();
        left().enable();
        right().enable();
        back().enable().setOutputOnly();

    }

    @Override
    public String getId() {

        return "rs";
    }

    @Override
    public void renderTop(float frame) {

        if (mode % 2 == 1) {
            GL11.glPushMatrix();
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glScaled(-1, 1, 1);
            GL11.glTranslated(-0.5, 0, -0.5);

            GL11.glDisable(GL11.GL_CULL_FACE);
        }

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName()
                + (mode > 1 ? "2" : "") + "/base.png"));
        this.renderTop();

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName()
                + (mode > 1 ? "2" : "") + "/left_" + ((mode % 2 == 0 ? left().getInput() > 0 : right().getInput() > 0) ? "on" : "off") + ".png"));
        renderTop();
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName()
                + (mode > 1 ? "2" : "") + "/right_" + ((mode % 2 == 0 ? right().getInput() > 0 : left().getInput() > 0) ? "on" : "off") + ".png"));
        renderTop();
        if (mode > 1) {
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName()
                    + "2/front" + "_" + (front().getInput() > 0 ? "on" : "off") + ".png"));
            renderTop();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName()
                    + "2/back" + "_" + (back().getInput() > 0 ? "on" : "off") + ".png"));
            renderTop();
        }

        RenderHelper.renderRedstoneTorch(1D / 8D, 1D / 8D, -2D / 8D, 9D / 16D, front().getInput() == 0);
        RenderHelper.renderRedstoneTorch(-1D / 8D, 1D / 8D, 2D / 8D, 9D / 16D, back().getInput() == 0);

        if (mode % 2 == 1) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }
    }

    @Override
    public void doLogic() {
        if ((left().getOutput() > 0 || left().getInput() > 0) && (right().getOutput() > 0 || right().getInput() > 0)) {
            //  left().setBidirectional();
            left().setOutput(0);
            //   right().setBidirectional();
            right().setOutput(0);
            front().setOutput(0);
            back().setOutput(0);
        } else {
            boolean mirrored = mode % 2 == 0;
            if (mirrored ? left().getInput() > 0 || left().getOutput() > 0 : right().getInput() > 0 || right().getOutput() > 0) {
                front().setOutput(15);
                back().setOutput(0);
                if (mode < 2) {
                    if (mirrored) {
                        // left().setOutputOnly();
                        left().setOutput(15);
                        right().setOutput(0);
                    } else {
                        // right().setOutputOnly();
                        right().setOutput(15);
                        left().setOutput(0);
                    }
                }
            }
            if (mirrored ? right().getInput() > 0 || right().getOutput() > 0 : left().getInput() > 0 || left().getOutput() > 0) {
                front().setOutput(0);
                back().setOutput(15);
                if (mode < 2) {
                    if (mirrored) {
                        //   right().setOutputOnly();
                        right().setOutput(15);
                        left().setOutput(0);
                    } else {
                        //  left().setOutputOnly();
                        left().setOutput(15);
                        right().setOutput(0);
                    }
                }
            }
        }

    }

    @Override
    protected boolean changeMode() {
        if (++mode > 3)
            mode = 0;
        QLog.info("mode:" + mode);
        return true;
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
        tag.setByte("mode", (byte) mode);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        mode = tag.getByte("mode");
    }

    @Override
    public void addWailaInfo(List<String> info) {

        info.add(I18n.format("gui.mode") + ": " + I18n.format("bluepower.waila.rsLatch." + (mode < 2 ? "feedback" : "noFeedback")));
    }
}
