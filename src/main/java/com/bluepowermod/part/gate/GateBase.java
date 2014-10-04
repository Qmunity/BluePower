/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.part.RedstoneConnection;
import com.qmunity.lib.part.IPartRedstone;
import com.qmunity.lib.util.Dir;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GateBase extends BPPartFaceRotate implements IPartRedstone {

    protected static Vec3dCube BOX = new Vec3dCube(0, 0, 0, 1, 1D / 8D, 1);
    protected static Vec3dCube OCCLUSION = new Vec3dCube(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 9D / 16D, 9D / 16D);

    private RedstoneConnection[] connections = new RedstoneConnection[] { new RedstoneConnection(this, Dir.FRONT),
            new RedstoneConnection(this, Dir.RIGHT), new RedstoneConnection(this, Dir.BACK), new RedstoneConnection(this, Dir.LEFT),
            new RedstoneConnection(this, Dir.TOP), new RedstoneConnection(this, Dir.BOTTOM) };

    public GateBase() {

        initializeConnections();
    }

    public abstract void initializeConnections();

    @Override
    public String getType() {

        return getId();
    }

    protected String getTextureName() {

        return getType();
    }

    @Override
    public String getUnlocalizedName() {

        return "gate." + getId();
    }

    public abstract String getId();

    protected final void rotateBoxes(List<Vec3dCube> boxes) {

        for (Vec3dCube c : boxes)
            c.rotate(getFace(), Vec3d.center);
    }

    @Override
    public final void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        List<Vec3dCube> boxes_ = new ArrayList<Vec3dCube>();
        addCollisionBoxes(boxes_, entity);
        rotateBoxes(boxes_);

        for (Vec3dCube c : boxes_)
            boxes.add(c);
    }

    public void addCollisionBoxes(List<Vec3dCube> boxes, Entity entity) {

        boxes.add(BOX.clone());
    }

    @Override
    public final List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        addOcclusionBoxes(boxes);
        rotateBoxes(boxes);

        return boxes;
    }

    public void addOcclusionBoxes(List<Vec3dCube> boxes) {

        boxes.add(BOX.clone());
    }

    @Override
    public final List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        addSelectionBoxes(boxes);
        rotateBoxes(boxes);

        return boxes;
    }

    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        boxes.add(BOX.clone());
    }

    protected void playTickSound() {

        if (getWorld().isRemote && Config.enableGateSounds)
            getWorld().playSound(getX(), getY(), getZ(), "gui.button.press", 0.3F, 0.5F, false);

    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

    }

    @Override
    public final boolean renderStatic(Vec3i translation, RenderBlocks renderer, int pass) {

        Vec3dCube c = BOX.clone().rotate(getFace(), Vec3d.center);

        renderer.setRenderAllFaces(true);
        renderer.setRenderBounds(c.getMinX(), c.getMinY(), c.getMinZ(), c.getMaxX(), c.getMaxY(), c.getMaxZ());
        renderer.overrideBlockTexture = Blocks.stone_slab.getIcon(0, 0);
        boolean rendered = renderer.renderStandardBlock(Blocks.stone_slab, getX(), getY(), getZ());
        renderer.overrideBlockTexture = null;
        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.setRenderAllFaces(false);

        return rendered;
    }

    @Override
    public final void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        if (this instanceof ISilkyRemovable && item.hasTagCompound()) {
            readFromNBT(item.getTagCompound().getCompoundTag("tileData"));
        }
        GL11.glPushMatrix();
        {

            if (type == ItemRenderType.INVENTORY) {
                GL11.glTranslated(0, 0.5, 0);
                GL11.glRotated(-12, -1, 0, 1);
            }
        }
        GL11.glPopMatrix();
    }

    public abstract void doLogic();

    @Override
    public boolean onActivated(EntityPlayer player, ItemStack item) {

        if (item != null && item.getItem() == BPItems.screwdriver) {
            if (player.isSneaking()) {
                if (!getWorld().isRemote) {
                    if (changeMode()) {
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
        return false;
    }

    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui() {

        return null;
    }

    protected boolean hasGUI() {

        return false;
    }

    protected boolean changeMode() {

        return false;
    }

    public boolean isCraftableInCircuitTable() {

        return true;
    }

    @Override
    public final int getStrongPower(ForgeDirection side) {

        return getConnection(side).getOutput();
    }

    @Override
    public final int getWeakPower(ForgeDirection side) {

        return getConnection(side).getOutput();
    }

    @Override
    public final boolean canConnectRedstone(ForgeDirection side) {

        return getConnection(side).isEnabled();
    }

    public RedstoneConnection getConnection(Dir direction) {

        return connections[direction.ordinal()];
    }

    public RedstoneConnection getConnection(ForgeDirection direction) {

        return connections[Dir.getDirection(direction, getFace(), getRotation()).ordinal()];
    }

    protected final RedstoneConnection front() {

        return getConnection(Dir.FRONT);
    }

    protected final RedstoneConnection right() {

        return getConnection(Dir.RIGHT);
    }

    protected final RedstoneConnection back() {

        return getConnection(Dir.BACK);
    }

    protected final RedstoneConnection left() {

        return getConnection(Dir.LEFT);
    }

    protected final RedstoneConnection top() {

        return getConnection(Dir.TOP);
    }

    protected final RedstoneConnection bottom() {

        return getConnection(Dir.BOTTOM);
    }

}
