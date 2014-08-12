/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GateBase extends BPPartFace {
    
    protected static Vector3Cube BOX = new Vector3Cube(0, 0, 0, 1, 1D / 8D, 1);
    
    public GateBase() {
    
        for (int i = 0; i < 4; i++)
            connections[i] = new RedstoneConnection(this, i + "", true, false);
        
        initializeConnections(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT));
    }
    
    public abstract void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right);
    
    @Override
    public void setFace(int face) {
    
        super.setFace(face);
    }
    
    @Override
    public String getType() {
    
        return getGateID();
    }
    
    protected String getTextureName() {
    
        return getType();
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "gate." + getGateID();
    }
    
    public abstract String getGateID();
    
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(BOX.clone().toAABB());
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(BOX.clone().toAABB());
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(BOX.clone().toAABB());
    }
    
    protected void playTickSound() {
    
        if (getWorld().isRemote && Config.enableGateSounds) getWorld().playSound(getX(), getY(), getZ(), "gui.button.press", 0.3F, 0.5F, false);
        
    }
    
    @Override
    public final void renderDynamic(Vector3 loc, int pass, float frame) {
    
        if (pass != 0) return;
        
        GL11.glPushMatrix();
        {
            super.rotateAndTranslateDynamic(loc, pass, frame);
            
            /* Top */
            renderTop(frame);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* Bottom */
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            GL11.glEnd();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/side.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* East */
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            /* West */
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* North */
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 1);
            /* South */
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 0);
            GL11.glEnd();
        }
        GL11.glPopMatrix();
    }
    
    protected void renderTopTexture(FaceDirection side, RedstoneConnection connection) {
    
        if (connection.isEnabled()) {
            renderTopTexture(side, connection.getPower() > 0);
        } else {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/" + side.getName() + "_disabled.png");
        }
    }
    
    protected void renderTopTexture(FaceDirection side, boolean state) {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/" + side.getName() + "_" + (state ? "on" : "off") + ".png");
    }
    
    public void renderTopTexture(String texture) {
    
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(texture));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3d(0, 1, 0);
        RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 0, 0);
        RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 0, 1);
        RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
        RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
        GL11.glEnd();
    }
    
    @Override
    public final boolean renderStatic(Vector3 loc, int pass) {
    
        return super.renderStatic(loc, pass);
    }
    
    @Override
    public final void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        if (this instanceof ISilkyRemovable) {
            load(item.getTagCompound().getCompoundTag("tileData"));
        }
        GL11.glPushMatrix();
        {
            
            if (type == ItemRenderType.INVENTORY) {
                GL11.glTranslated(0, 0.5, 0);
                GL11.glRotated(-12, -1, 0, 1);
            }
            /* Top */
            renderTop();
            
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* Bottom */
            GL11.glNormal3d(0, -1, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            GL11.glEnd();
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/side.png"));
            GL11.glBegin(GL11.GL_QUADS);
            /* East */
            GL11.glNormal3d(1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            /* West */
            GL11.glNormal3d(-1, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            /* North */
            GL11.glNormal3d(0, 0, -1);
            RenderHelper.addVertexWithTexture(0, 0, 0, 0, 0);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 1, 0);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 1);
            RenderHelper.addVertexWithTexture(1, 0, 0, 0, 1);
            /* South */
            GL11.glNormal3d(0, 0, 1);
            RenderHelper.addVertexWithTexture(0, 0, 1, 0, 0);
            RenderHelper.addVertexWithTexture(1, 0, 1, 0, 1);
            RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
            RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 1, 0);
            GL11.glEnd();
            
        }
        GL11.glPopMatrix();
    }
    
    public void renderTop(float frame) {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/base.png");
        renderTop(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT), frame);
    }
    
    public void renderTop() {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/base.png");
        renderTopItem(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT));
    }
    
    protected void renderTopItem(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        renderTop(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT), 0);
    }
    
    protected abstract void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame);
    
    @Override
    public void update() {
    
        super.update();
        doLogic(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT));
    }
    
    public abstract void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right);
    
    @Override
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        if (item != null && item.getItem() == BPItems.screwdriver) {
            if (player.isSneaking()) {
                if (!getWorld().isRemote) {
                    if (changeMode(getConnection(FaceDirection.FRONT), getConnection(FaceDirection.LEFT), getConnection(FaceDirection.BACK), getConnection(FaceDirection.RIGHT))) {
                        notifyUpdate();
                        sendUpdatePacket();
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                setRotation(getRotation() + 1);
            }
            return true;
        } else if (hasGUI()) {
            if (getWorld().isRemote) {
                FMLCommonHandler.instance().showGuiScreen(getGui());
            }
            return true;
        }
        return super.onActivated(player, mop, item);
    }
    
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui() {
    
        return null;
    }
    
    protected boolean hasGUI() {
    
        return false;
    }
    
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        return false;
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
    
        return CustomTabs.tabBluePowerCircuits;
    }
    
    @Override
    public abstract void addWailaInfo(List<String> info);
    
    @Override
    public float getHardness() {
    
        return 1;
    }
    
    public boolean isCraftableInCircuitTable() {
    
        return true;
    }
    
    @Override
    public int getStrongOutput(ForgeDirection side) {
    
        return super.getWeakOutput(side);
    }
    
}
