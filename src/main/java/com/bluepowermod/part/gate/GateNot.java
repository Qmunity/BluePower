/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;

public class GateNot extends GateBase {
    
    private boolean power = false;
    
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
        right.setOutput();
    }
    
    @Override
    public String getId() {
    
        return "not";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, !power);
        renderTopTexture(FaceDirection.LEFT, !power);
        renderTopTexture(FaceDirection.RIGHT, !power);
        renderTopTexture(FaceDirection.BACK, back.getPower() > 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 9D / 16D, !power);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        power = back.getPower() > 0;
        
        left.setPower(!power ? 15 : 0);
        front.setPower(!power ? 15 : 0);
        right.setPower(!power ? 15 : 0);
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
