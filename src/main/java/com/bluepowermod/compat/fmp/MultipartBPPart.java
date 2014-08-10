/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.compat.fmp;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.lib.data.MCDataInput;
import codechicken.lib.data.MCDataOutput;
import codechicken.lib.raytracer.IndexedCuboid6;
import codechicken.lib.render.EntityDigIconFX;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.INeighborTileChange;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.JNormalOcclusion;
import codechicken.multipart.NormalOcclusionTest;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.redstone.IBPRedstonePart;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.util.Refs;

public class MultipartBPPart extends TMultiPart implements IRedstonePart, JNormalOcclusion, INeighborTileChange {
    
    private BPPart part;
    
    public MultipartBPPart(BPPart part) {
    
        setPart(part);
    }
    
    public MultipartBPPart() {
    
    }
    
    public BPPart getPart() {
    
        return part;
    }
    
    protected void setPart(BPPart part) {
    
        this.part = part;
    }
    
    @Override
    public String getType() {
    
        return Refs.MODID + "_" + getPart().getType();
    }
    
    @Override
    public Iterable<IndexedCuboid6> getSubParts() {
    
        List<IndexedCuboid6> cubes = new ArrayList<IndexedCuboid6>();
        List<AxisAlignedBB> aabbs = getPart().getSelectionBoxes();
        if (aabbs == null) return cubes;
        for (int i = 0; i < aabbs.size(); i++) {
            AxisAlignedBB aabb = aabbs.get(i);
            cubes.add(new IndexedCuboid6(i, new Cuboid6(aabb)));
        }
        return cubes;
    }
    
    @Override
    public Iterable<Cuboid6> getCollisionBoxes() {
    
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        List<AxisAlignedBB> aabbs = getPart().getCollisionBoxes();
        if (aabbs == null) return cubes;
        for (AxisAlignedBB aabb : aabbs)
            cubes.add(new Cuboid6(aabb));
        return cubes;
    }
    
    @Override
    public Iterable<Cuboid6> getOcclusionBoxes() {
    
        List<Cuboid6> cubes = new ArrayList<Cuboid6>();
        List<AxisAlignedBB> aabbs = getPart().getOcclusionBoxes();
        if (aabbs == null) return cubes;
        for (AxisAlignedBB aabb : aabbs)
            cubes.add(new Cuboid6(aabb));
        return cubes;
    }
    
    @Override
    public boolean occlusionTest(TMultiPart part) {
    
        return NormalOcclusionTest.apply(this, part);
    }
    
    @Override
    public void writeDesc(MCDataOutput packet) {
    
        super.writeDesc(packet);
        
        packet.writeString(getPart().getType());
        
        NBTTagCompound tag = new NBTTagCompound();
        getPart().writeUpdatePacket(tag);
        packet.writeNBTTagCompound(tag);
    }
    
    @Override
    public void readDesc(MCDataInput packet) {
    
        super.readDesc(packet);
        
        String type = packet.readString();
        if (getPart() == null) setPart(PartRegistry.getInstance().createPart(type));
        
        getPart().readUpdatePacket(packet.readNBTTagCompound());
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        String type = tag.getString("part_id");
        if (getPart() == null) setPart(PartRegistry.getInstance().createPart(type));
        
        NBTTagCompound t = tag.getCompoundTag("partData");
        getPart().load(t);
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setString("part_id", getPart().getType());
        
        NBTTagCompound t = new NBTTagCompound();
        getPart().save(t);
        tag.setTag("partData", t);
    }
    
    @Override
    public int getLightValue() {
    
        return getPart().getLightValue();
    }
    
    @Override
    public Iterable<ItemStack> getDrops() {
    
        return getPart().getDrops();
    }
    
    @Override
    public ItemStack pickItem(MovingObjectPosition hit) {
    
        return getPart().getPickedItem(hit);
    }
    
    @Override
    public float getStrength(MovingObjectPosition hit, EntityPlayer player) {
    
        float s = 50;
        float h = getPart().getHardness(hit, player) * (s / 2F);
        if (h == 0) return s;
        
        return s / h;
    }
    
    // Redstone
    
    @Override
    public int strongPowerLevel(int side) {
    
        return weakPowerLevel(side);
    }
    
    @Override
    public int weakPowerLevel(int side) {
    
        if (getPart() instanceof IBPRedstonePart) return ((IBPRedstonePart) getPart()).getWeakOutput(ForgeDirection.getOrientation(side));
        
        return getPart().getRedstonePower();
    }
    
    @Override
    public boolean canConnectRedstone(int side) {
    
        if (getPart() instanceof IBPRedstonePart) return ((IBPRedstonePart) getPart()).canConnect(ForgeDirection.getOrientation(side));
        
        return false;
    }
    
    // Events
    
    @Override
    public void onAdded() {
    
        getPart().onAdded();
    }
    
    @Override
    public void onRemoved() {
    
        getPart().onRemoved();
    }
    
    @Override
    public void onPartChanged(TMultiPart part) {
    
        getPart().onPartChanged();
    }
    
    @Override
    public void onEntityCollision(Entity entity) {
    
        getPart().onEntityCollision(entity);
    }
    
    @Override
    public void onNeighborChanged() {
    
        getPart().onNeighborUpdate();
    }
    
    @Override
    public void onNeighborTileChanged(int arg0, boolean arg1) {
    
        getPart().onNeighborTileUpdate();
    }
    
    @Override
    public boolean weakTileChanges() {
    
        return false;
    }
    
    @Override
    public boolean activate(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {
    
        return getPart().onActivated(player, hit, item);
    }
    
    @Override
    public void click(EntityPlayer player, MovingObjectPosition hit, ItemStack item) {
    
        getPart().click(player, hit, item);
    }
    
    // Rendering
    
    private static int emptyStaticRender = -1;
    private int        staticRender0     = -1;
    private int        staticRender1     = -1;
    
    @Override
    public boolean renderStatic(Vector3 pos, int pass) {
    
        if (getPart().shouldRenderStaticOnPass(0)) getPart().markPartForRenderUpdate();
        return false;
    }
    
    @Override
    public void renderDynamic(Vector3 pos, float frame, int pass) {
    
        getPart().renderDynamic(new com.bluepowermod.api.vec.Vector3(pos.x, pos.y, pos.z), pass, frame);
        
        if (emptyStaticRender == -1) {
            emptyStaticRender = GL11.glGenLists(1);
            GL11.glNewList(emptyStaticRender, GL11.GL_COMPILE);
            GL11.glEndList();
        }
        if (staticRender0 == -1 || getPart().shouldReRender()) reRenderStatic(new Vector3(0, 0, 0), 0);
        if (staticRender1 == -1 || getPart().shouldReRender()) reRenderStatic(new Vector3(0, 0, 0), 1);
        if (getPart().shouldReRender()) getPart().resetRenderUpdate();
        
        if (getPart().shouldRenderStaticOnPass(pass)) {
            GL11.glPushMatrix();
            {
                GL11.glTranslated(pos.x, pos.y, pos.z);
                if (pass == 0) {
                    GL11.glCallList(staticRender0);
                } else {
                    GL11.glCallList(staticRender1);
                }
            }
            GL11.glPopMatrix();
        }
        
    }
    
    protected void reRenderStatic(Vector3 pos, int pass) {
    
        if (pass == 0) {
            if (staticRender0 == -1 || staticRender0 == emptyStaticRender) staticRender0 = GL11.glGenLists(1);
            GL11.glNewList(staticRender0, GL11.GL_COMPILE);
        } else {
            if (staticRender1 == -1 || staticRender1 == emptyStaticRender) staticRender1 = GL11.glGenLists(1);
            GL11.glNewList(staticRender1, GL11.GL_COMPILE);
        }
        GL11.glPushMatrix();
        
        boolean result = getPart().shouldRenderStaticOnPass(pass);
        
        if (result) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            Tessellator t = Tessellator.instance;
            t.setTranslation(0, 0, 0);
            t.startDrawingQuads();
            result = getPart().renderStatic(new com.bluepowermod.api.vec.Vector3(pos.x, pos.y, pos.z), pass);
            t.draw();
            t.setTranslation(0, 0, 0);
        }
        
        GL11.glPopMatrix();
        GL11.glEndList();
        if (!result) {
            if (pass == 0) {
                staticRender0 = emptyStaticRender;
            } else {
                staticRender1 = emptyStaticRender;
            }
        }
    }
    
    @Override
    public boolean drawHighlight(MovingObjectPosition mop, EntityPlayer player, float frame) {
    
        ForgeDirection face = ForgeDirection.getOrientation(mop.sideHit);
        
        AxisAlignedBB c = com.bluepowermod.raytrace.RayTracer.getSelectedCuboid(mop, player, face, getSubParts(), true);
        
        if (c == null) return true;
        
        GL11.glPushMatrix();
        {
            GL11.glTranslated(x() - TileEntityRendererDispatcher.staticPlayerX, y() - TileEntityRendererDispatcher.staticPlayerY, z() - TileEntityRendererDispatcher.staticPlayerZ);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4d(0, 0, 0, 0);
            RenderUtils.drawCuboidOutline(new Cuboid6(c).expand(0.001));
            GL11.glColor4d(1, 1, 1, 1);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        GL11.glPopMatrix();
        
        return true;
    }
    
    @Override
    public void update() {
    
        getPart().setWorld(world());
        getPart().setX(x());
        getPart().setY(y());
        getPart().setZ(z());
        
        getPart().update();
    }
    
    @Override
    public void drawBreaking(RenderBlocks renderBlocks) {
    
        super.drawBreaking(renderBlocks);
    }
    
    @Override
    public void addHitEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
    
        if (!getPart().addHitEffects(hit, effectRenderer)) {
            IIcon icon = part.getBreakingIcon();
            if (icon == null) icon = Blocks.stone.getIcon(0, 0);
            
            Cuboid6 c = null;
            for (Cuboid6 cu : getSubParts()) {
                if (c == null) {
                    c = cu;
                } else {
                    c.expand(cu.min);
                    c.expand(cu.max);
                }
            }
            
            if (c != null) EntityDigIconFX.addBlockHitEffects(world(), c.copy().add(Vector3.fromTileEntity(tile())), hit.sideHit, icon, effectRenderer);
        }
    }
    
    @Override
    public void addDestroyEffects(MovingObjectPosition hit, EffectRenderer effectRenderer) {
    
        if (!getPart().addDestroyEffects(hit, effectRenderer)) {
            IIcon icon = part.getBreakingIcon();
            if (icon == null) icon = Blocks.stone.getIcon(0, 0);
            EntityDigIconFX.addBlockDestroyEffects(world(), Cuboid6.full.copy().add(Vector3.fromTileEntity(tile())), new IIcon[] { icon, icon, icon, icon, icon, icon }, effectRenderer);
        }
    }
    
}
