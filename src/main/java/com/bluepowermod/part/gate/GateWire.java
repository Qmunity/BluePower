/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate;

import java.util.List;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.util.Refs;

/**
 * @author MineMaarten
 */
public class GateWire extends GateBase {
    
    public static final String ID = "icWire";
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        front.enable();
        left.enable();
        back.enable();
        right.enable();
        
        front.setOutput();
        left.setOutput();
        back.setOutput();
        right.setOutput();
    }
    
    @Override
    public String getId() {
    
        return ID;
    }
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        boolean isOn = false;
        for (FaceDirection dir : FaceDirection.values()) {
            if (getConnection(dir).isEnabled()) {
                isOn = getConnection(dir).getPower() > 0;
                break;
            }
        }
        this.renderTopTexture(FaceDirection.FRONT, front);
        renderTopTexture(FaceDirection.BACK, back);
        renderTopTexture(FaceDirection.LEFT, left);
        renderTopTexture(FaceDirection.RIGHT, right);
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/center_" + (isOn ? "on" : "off") + ".png");
    }
    
    @Override
    public boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (front.isEnabled() && left.isEnabled() && back.isEnabled() && right.isEnabled()) {
            right.disable();
        } else if (front.isEnabled() && left.isEnabled() && back.isEnabled()) {
            left.disable();
        } else if (front.isEnabled() && back.isEnabled()) {
            left.enable();
            front.disable();
        } else if (left.isEnabled() && back.isEnabled()) {
            front.enable();
            right.enable();
        }
        return true;
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        // TODO Auto-generated method stub
        
    }
    
}
