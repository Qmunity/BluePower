/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 *
 * @author Quetzi
 */

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

public class GateRSLatch extends GateBase {
    
    private int mode;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        front.enable();
        front.setOutput();
        front.setPower(15);
        
        left.enable();
        left.setInput();
        
        right.enable();
        right.setInput();
        
        back.enable();
        back.setOutput();
        
    }
    
    @Override
    public String getGateID() {
    
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
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + (mode > 1 ? "2" : "") + "/base.png");
        renderTop(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT), frame);
        if (mode % 2 == 1) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + (mode > 1 ? "2" : "") + "/left_" + ((mode % 2 == 0 ? left.getPower() > 0 : right.getPower() > 0) ? "on" : "off") + ".png");
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + (mode > 1 ? "2" : "") + "/right_" + ((mode % 2 == 0 ? right.getPower() > 0 : left.getPower() > 0) ? "on" : "off") + ".png");
        if (mode > 1) {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "2/front" + "_" + (front.getPower() > 0 ? "on" : "off") + ".png");
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "2/back" + "_" + (back.getPower() > 0 ? "on" : "off") + ".png");
            
        }
        
        RenderHelper.renderRedstoneTorch(-1D / 8D, 1D / 8D, 2D / 8D, 9D / 16D, front.getPower() == 0);
        RenderHelper.renderRedstoneTorch(1D / 8D, 1D / 8D, -2D / 8D, 9D / 16D, back.getPower() == 0);
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (left.getPower() > 0 && right.getPower() > 0) {
            left.setInput();
            right.setInput();
            front.setPower(0);
            back.setPower(0);
        } else {
            boolean mirrored = mode % 2 == 0;
            if (mirrored ? left.getPower() > 0 : right.getPower() > 0) {
                front.setPower(15);
                back.setPower(0);
                if (mode < 2) {
                    if (mirrored) {
                        left.setOutput();
                        left.setPower(15);
                        right.setInput();
                    } else {
                        right.setOutput();
                        right.setPower(15);
                        left.setInput();
                    }
                }
            }
            if (mirrored ? right.getPower() > 0 : left.getPower() > 0) {
                front.setPower(0);
                back.setPower(15);
                if (mode < 2) {
                    if (mirrored) {
                        right.setOutput();
                        right.setPower(15);
                        left.setInput();
                    } else {
                        left.setOutput();
                        left.setPower(15);
                        right.setInput();
                    }
                }
            }
        }
        
    }
    
    @Override
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (++mode > 3) mode = 0;
        return true;
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setByte("mode", (byte) mode);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        mode = tag.getByte("mode");
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        info.add(I18n.format("gui.mode") + ": " + I18n.format("bluepower.waila.rsLatch." + (mode < 2 ? "feedback" : "noFeedback")));
    }
}
