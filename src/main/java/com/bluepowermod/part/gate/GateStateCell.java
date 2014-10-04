/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import java.util.List;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.gui.gate.GuiGateSingleTime;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class GateStateCell extends GateBase implements IGuiButtonSensitive {
    
    private int     time      = 40;
    private int     ticks     = 0;
    private boolean triggered = false;
    private boolean mirrored  = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
        
        // Init left
        left.enable();
        left.setOutput();
        
        // Init back
        back.enable();
        back.setInput();
        
        // Init right
        right.enable();
        right.setInput();
    }
    
    @Override
    public String getId() {
    
        return "state";
    }
    
    @Override
    public void renderTop(float frame) {
    
        if (mirrored) {
            GL11.glPushMatrix();
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glScaled(1, 1, -1);
            GL11.glTranslated(-0.5, 0, -0.5);
            
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
        super.renderTop(frame);
        if (mirrored) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        // renderTopTexture(FaceDirection.FRONT, power);
        renderTopTexture(FaceDirection.LEFT, left);
        renderTopTexture(FaceDirection.FRONT, mirrored ? back : front);
        renderTopTexture(FaceDirection.BACK, mirrored ? front : back);
        renderTopTexture(FaceDirection.RIGHT, right);
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_" + (ticks == 0 ? "on" : "off") + ".png");
        RenderHelper.renderRandomizerButton(this, -2 / 16D, 0, 4 / 16D, left.getPower() > 0);
        RenderHelper.renderRedstoneTorch(4 / 16D, 1D / 8D, 0, 13D / 16D, ticks > 0);
        RenderHelper.renderRedstoneTorch(1 / 16D, 1D / 8D, -4 / 16D, 9D / 16D, mirrored ? back.getPower() > 0 : front.getPower() > 0);
        RenderHelper.renderPointer(4 / 16D, 7D / 16D, 0, ticks > 0 ? 1 - (ticks + frame) / (time * 7) + 0.25 : 0.25);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 12D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (mirrored) {
            back.setPower(0);
        } else {
            front.setPower(0);
        }
        boolean locked = false;
        if (mirrored ? front.getPower() > 0 : back.getPower() > 0) {
            triggered = true;
            locked = true;
        }
        if (locked || right.getPower() > 0) {
            ticks = 0;
            if (triggered) left.setPower(15);
        } else if (triggered) {
            left.setPower(15);
            if (ticks++ >= time) {
                ticks = 0;
                if (mirrored) {
                    back.setPower(15);
                } else {
                    front.setPower(15);
                }
                playTickSound();
                triggered = false;
            }
        } else {
            left.setPower(0);
        }
    }
    
    @Override
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        mirrored = !mirrored;
        if (mirrored) {
            back.setOutput();
            front.setInput();
        } else {
            back.setInput();
            front.setOutput();
        }
        return true;
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
        tag.setBoolean("triggered", triggered);
        tag.setBoolean("mirrored", mirrored);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        ticks = tag.getInteger("ticks");
        time = tag.getInteger("time");
        triggered = tag.getBoolean("triggered");
        mirrored = tag.getBoolean("mirrored");
    }
    
    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
    
        time = value;
        sendUpdatePacket();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui() {
    
        return new GuiGateSingleTime(this) {
            
            @Override
            protected int getCurrentIntervalTicks() {
            
                return time;
            }
            
        };
    }
    
    @Override
    protected boolean hasGUI() {
    
        return true;
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        String t = "";
        
        int time = this.time * 50;
        if (time >= 1000) {
            t = time / 1000 + "." + time % 1000 + "s";
        } else {
            t = time + "ms";
        }
        
        info.add(I18n.format("gui.timerInterval") + ": " + SpecialChars.WHITE + t);
    }
    
}
