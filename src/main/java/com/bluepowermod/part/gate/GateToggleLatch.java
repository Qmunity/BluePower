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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.util.Refs;

public class GateToggleLatch extends GateBase {
    
    private boolean power = false;
    private boolean state = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        front.enable();
        front.setOutput();
        left.enable();
        left.setInput();
        right.enable();
        right.setInput();
        back.enable();
        back.setOutput();
    }
    
    @Override
    public String getId() {
    
        return "toggle";
    }
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/centerleft_" + (power ? "on" : "off") + ".png");
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/left_" + (power ? "on" : "off") + ".png");
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/centerright_" + (power ? "on" : "off") + ".png");
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/right_" + (power ? "on" : "off") + ".png");
        RenderHelper.renderRedstoneTorch(-2.5D / 8D, 1D / 8D, 2.5D / 8D, 9D / 16D, !state);
        RenderHelper.renderRedstoneTorch(-2.5D / 8D, 1D / 8D, -2.5D / 8D, 9D / 16D, state);
        RenderHelper.renderLever(this, 9 / 16D, 1 / 8D, 4 / 16D, !state);
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if ((power != right.getPower() > 0 || left.getPower() > 0) && !power) {
            state = !state;
            playTickSound();
        }
        power = right.getPower() > 0 || left.getPower() > 0;
        
        front.setPower(!state ? 0 : 15);
        back.setPower(state ? 0 : 15);
    }
    
    @Override
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        if (item == null || item.getItem() != BPItems.screwdriver) {
            state = !state;
            playTickSound();
            return true;
        } else {
            return super.onActivated(player, mop, item);
        }
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
}
