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
 * @author MineMaarten
 */
public class GateSynchronizer extends GateBase {
    
    private boolean rightTriggered, leftTriggered, oldLeftState, oldRightState;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
        
        // Init left
        left.enable();
        left.setInput();
        
        // Init back
        back.enable();
        back.setInput();
        
        // Init right
        right.enable();
        right.setInput();
    }
    
    @Override
    public String getGateID() {
    
        return "synchronizer";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -4D / 16D, 10D / 16D, front.getPower() > 0);
        renderTopTexture(FaceDirection.FRONT, front);
        renderTopTexture(FaceDirection.LEFT, left);
        renderTopTexture(FaceDirection.BACK, back);
        renderTopTexture(FaceDirection.RIGHT, right);
        renderTopTexture("frontleft", !leftTriggered);
        renderTopTexture("frontright", !rightTriggered);
        RenderHelper.renderRandomizerButton(this, -3 * pixel, 0, 4 * pixel, leftTriggered);
        RenderHelper.renderRandomizerButton(this, 3 * pixel, 0, 4 * pixel, rightTriggered);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 9D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (!oldLeftState && left.getPower() > 0) {
            leftTriggered = true;
        }
        if (!oldRightState && right.getPower() > 0) {
            rightTriggered = true;
        }
        
        if (back.getPower() > 0) {
            leftTriggered = false;
            rightTriggered = false;
        }
        
        if (leftTriggered && rightTriggered) {
            front.setPower(15);
            leftTriggered = false;
            rightTriggered = false;
        } else {
            front.setPower(0);
        }
        
        oldLeftState = left.getPower() > 0;
        oldRightState = right.getPower() > 0;
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setBoolean("leftTriggered", leftTriggered);
        tag.setBoolean("rightTriggered", rightTriggered);
        tag.setBoolean("oldLeftState", oldLeftState);
        tag.setBoolean("oldRightState", oldRightState);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        leftTriggered = tag.getBoolean("leftTriggered");
        rightTriggered = tag.getBoolean("rightTriggered");
        oldLeftState = tag.getBoolean("oldLeftState");
        oldRightState = tag.getBoolean("oldRightState");
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
