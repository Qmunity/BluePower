/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.part.gate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.references.Refs;

import org.lwjgl.opengl.GL11;

public class GateBase extends BPPartFace {
    
    private static Vector3Cube HITBOX    = new Vector3Cube(0, 0, 0, 1, 1D / 8D, 1);
    private static Vector3Cube OCCLUSION = new Vector3Cube(1D / 8D, 0, 1D / 8D, 7D / 8D, 1D / 8D, 7D / 8D);
    
    private Vector3Cube        hitbox    = HITBOX.clone();
    private Vector3Cube        occlusion = OCCLUSION.clone();
    
    public GateBase() {
    
        for (int i = 0; i < 4; i++)
            connections[i] = new RedstoneConnection(this, i + "", true, false);
        
        for (int i = 0; i < 4; i++)
            getConnection(FaceDirection.getDirection(i)).enable();
        
        getConnection(FaceDirection.FRONT).setOutput();
    }
    
    @Override
    public void setFace(int face) {
    
        super.setFace(face);
        ForgeDirection d = ForgeDirection.getOrientation(face);
        
        hitbox = HITBOX.clone().rotate90Degrees(d);
        occlusion = OCCLUSION.clone();
    }
    
    @Override
    public String getType() {
    
        return "gatebase";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "gate.gatebase";
    }
    
    @Override
    public List<AxisAlignedBB> getCollisionBoxes() {
    
        return getSelectionBoxes();
    }
    
    @Override
    public List<AxisAlignedBB> getSelectionBoxes() {
        
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        
        aabbs.add(hitbox.toAABB());
        
        return aabbs;
    }
    
    @Override
    public List<AxisAlignedBB> getOcclusionBoxes() {
    
        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();
        
        aabbs.add(occlusion.toAABB());
        
        return aabbs;
    }
    
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glPushMatrix();
        {
            super.renderDynamic(loc, pass, frame);
            
            GL11.glDisable(GL11.GL_CULL_FACE);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* Top */
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            /* Bottom */
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 0.001, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0.001, 0, 1, 0);
            GL11.glEnd();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/side.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* East */
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            /* West */
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* North */
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(0, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 0, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* South */
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(0, 0.001, 1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 0);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        GL11.glPushMatrix();
        {
            GL11.glDisable(GL11.GL_CULL_FACE);
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* Top */
            GL11.glNormal3d(0, 1, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            /* Bottom */
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 0.001, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0.001, 0, 1, 0);
            GL11.glEnd();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/side.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* East */
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            /* West */
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* North */
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(0, 0.001, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 0, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* South */
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(0, 0.001, 1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0.001, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 0);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void update() {
    
        super.update();
        
        int power = 0;

        power = Math.max(power, getConnection(FaceDirection.LEFT).getPower() - 1);
        power = Math.max(power, getConnection(FaceDirection.BACK).getPower() - 1);
        power = Math.max(power, getConnection(FaceDirection.RIGHT).getPower() - 1);
        
        getConnection(FaceDirection.FRONT).setPower(power);
    }
    
    @Override
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        if (item != null && item.getItem() == BPItems.screwdriver) {
            setRotation(getRotation() + 1);
            return true;
        }
        
        return super.onActivated(player, mop, item);
    }
    
}
