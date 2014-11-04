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
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.RedstoneConnection;
import com.bluepowermod.util.Refs;
import com.qmunity.lib.part.IPartLightEmitter;
import com.qmunity.lib.part.IPartRedstone;
import com.qmunity.lib.part.IPartTicking;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.util.Dir;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GateBase extends BPPartFaceRotate implements IPartRedstone, IPartTicking, IPartLightEmitter {

    private static Vec3dCube BOX = new Vec3dCube(0, 0, 0, 1, 2D / 16D, 1);

    private final RedstoneConnection[] connections = new RedstoneConnection[] { new RedstoneConnection(this, Dir.FRONT),
            new RedstoneConnection(this, Dir.RIGHT), new RedstoneConnection(this, Dir.BACK), new RedstoneConnection(this, Dir.LEFT),
            new RedstoneConnection(this, Dir.TOP), new RedstoneConnection(this, Dir.BOTTOM) };

    private boolean needsUpdate;//flag that is set when a neighbor block update occurs.

    private static IIcon iconBottom;
    private static IIcon iconSide;
    private IIcon iconTop;

    private static GateBase rendering;
    private static final Block blockFake = new Block(Material.rock) {

        @Override
        public IIcon getIcon(IBlockAccess w, int x, int y, int z, int face) {

            ForgeDirection f = ForgeDirection.getOrientation(face);

            if (f == rendering.getFace())
                return iconBottom;
            if (f == rendering.getFace().getOpposite())
                return ((GateBase) PartManager.getExample(rendering.getType())).getTopIcon();

            return iconSide;
        };
    };

    private final Random rnd = new Random();

    public GateBase() {

        initializeConnections();
    }

    public abstract void initializeConnections();

    @Override
    public String getType() {

        return getId();
    }

    @Override
    public String getUnlocalizedName() {

        return "gate." + getId();
    }

    public abstract String getId();

    protected String getTextureName() {
        return getId();
    }

    @Override
    public final void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        List<Vec3dCube> boxes_ = new ArrayList<Vec3dCube>();
        addCollisionBoxes(boxes_, entity);
        VectorHelper.rotateBoxes(boxes_, getFace(), getRotation());

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
        VectorHelper.rotateBoxes(boxes, getFace(), getRotation());

        return boxes;
    }

    public void addOcclusionBoxes(List<Vec3dCube> boxes) {

        boxes.add(BOX.clone());
    }

    @Override
    public final List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        addSelectionBoxes(boxes);
        VectorHelper.rotateBoxes(boxes, getFace(), getRotation());

        return boxes;
    }

    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        boxes.add(BOX.clone());
    }

    protected void playTickSound() {

        if (getWorld().isRemote && Config.enableGateSounds)
            getWorld().playSound(getX(), getY(), getZ(), "gui.button.press", 0.3F, 0.5F, false);

    }

    protected final void transformDynamic(Vec3d translation) {

        GL11.glTranslated(translation.getX(), translation.getY(), translation.getZ());

        GL11.glTranslated(0.5, 0.5, 0.5);
        {
            switch (getFace()) {
            case DOWN:
                break;
            case UP:
                GL11.glRotated(180, 1, 0, 0);
                break;
            case NORTH:
                GL11.glRotated(90, 1, 0, 0);
                break;
            case SOUTH:
                GL11.glRotated(90, -1, 0, 0);
                break;
            case WEST:
                GL11.glRotated(90, 0, 0, -1);
                break;
            case EAST:
                GL11.glRotated(90, 0, 0, 1);
                break;
            default:
                break;
            }
            int rotation = getRotation();
            GL11.glRotated(90 * (rotation == 0 || rotation == 2 ? (rotation + 2) % 4 : rotation), 0, 1, 0);
        }
        GL11.glTranslated(-0.5, -0.5, -0.5);
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        transformDynamic(translation);

        GL11.glPushMatrix();
        renderTop((float) delta);
        GL11.glPopMatrix();
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderBlocks renderer, int pass) {

        rendering = this;

        Vec3dCube c = BOX.clone().rotate(getFace(), Vec3d.center);

        if (getFace() == ForgeDirection.UP) {
            renderer.uvRotateEast = 3;
            renderer.uvRotateWest = 3;
            renderer.uvRotateNorth = 3;
            renderer.uvRotateSouth = 3;
            renderer.uvRotateBottom = getTopRotation();
        } else if (getFace() == ForgeDirection.EAST) {
            renderer.uvRotateTop = 2;
            renderer.uvRotateBottom = 1;
            renderer.uvRotateEast = 1;
            renderer.uvRotateWest = 2;

            int amt = (getRotation() - 1) % 4;
            while (amt < 0)
                amt += 4;
            if (amt == 2 || amt == 3)
                amt = amt ^ 1;
            renderer.uvRotateNorth = amt;
        } else if (getFace() == ForgeDirection.WEST) {
            renderer.uvRotateTop = 1;
            renderer.uvRotateBottom = 2;
            renderer.uvRotateEast = 2;
            renderer.uvRotateWest = 1;

            int amt = (getRotation() + 1) % 4;
            while (amt < 0)
                amt += 4;
            if (amt == 2 || amt == 3)
                amt = amt ^ 1;
            renderer.uvRotateSouth = amt;
        } else if (getFace() == ForgeDirection.SOUTH) {
            renderer.uvRotateSouth = 1;
            renderer.uvRotateNorth = 2;

            int amt = (getRotation() - 2) % 4;
            while (amt < 0)
                amt += 4;
            if (amt == 2 || amt == 3)
                amt = amt ^ 1;
            renderer.uvRotateEast = amt;
        } else if (getFace() == ForgeDirection.NORTH) {
            renderer.uvRotateTop = 3;
            renderer.uvRotateBottom = 3;
            renderer.uvRotateSouth = 2;
            renderer.uvRotateNorth = 1;
            renderer.uvRotateWest = getTopRotation();
        } else {
            renderer.uvRotateTop = getTopRotation();
        }

        renderer.setRenderAllFaces(true);
        renderer.setRenderBounds(c.getMinX(), c.getMinY(), c.getMinZ(), c.getMaxX(), c.getMaxY(), c.getMaxZ());
        renderer.overrideBlockTexture = null;
        boolean rendered = renderer.renderStandardBlock(blockFake, getX(), getY(), getZ());
        renderer.setRenderAllFaces(false);

        renderer.uvRotateEast = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;

        return rendered;
    }

    private int getTopRotation() {

        switch (getRotation()) {
        case 0:
            return 0;
        case 1:
            return 1;
        case 3:
            return 2;
        case 2:
            return 3;
        }
        return -1;
    }

    protected abstract void renderTop(float frame);

    protected final void renderTop() {

        Tessellator t = Tessellator.instance;

        double y = 2 / 16D;

        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        {
            t.addVertexWithUV(0, y, 0, 1, 1);
            t.addVertexWithUV(0, y, 1, 1, 0);
            t.addVertexWithUV(1, y, 1, 0, 0);
            t.addVertexWithUV(1, y, 0, 0, 1);
        }
        t.draw();
    }

    protected final void renderTop(String texture) {

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/"
                + texture + ".png"));
        renderTop();
    }

    protected final void renderTop(String texture, boolean status) {

        renderTop(texture, status ? "on" : "off");
    }

    protected final void renderTop(String texture, String status) {

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/"
                + texture + "_" + status + ".png"));

        boolean isOn = status.equals("on");

        float bX = OpenGlHelper.lastBrightnessX;
        float bY = OpenGlHelper.lastBrightnessY;

        if (isOn)
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, Math.max(125, bX), Math.max(125, bY));

        renderTop();

        if (isOn)
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, bX, bY);
    }

    protected final void renderTop(String name, RedstoneConnection con) {

        boolean isOn = con.getOutput() + (!con.isOutputOnly() ? con.getInput() : 0) > 0;

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/" + name
                + "_" + (con.isEnabled() ? isOn ? "on" : "off" : "disabled") + ".png"));

        float bX = OpenGlHelper.lastBrightnessX;
        float bY = OpenGlHelper.lastBrightnessY;

        if (isOn)
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, Math.max(125, bX), Math.max(125, bY));

        renderTop();

        if (isOn)
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, bX, bY);
    }

    protected final void spawnBlueParticle(double x, double y, double z) {

        if (!getWorld().isRemote)
            return;

        if (rnd.nextInt(5) == 0)
            getWorld().spawnParticle("reddust", getX() + x, getY() + y, getZ() + z, -1, 0, 1);
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

    public void tick() {

    }

    @Override
    public final void update() {
        if (needsUpdate) {
            needsUpdate = false;
            for (RedstoneConnection c : connections)
                c.update();
            doLogic();

            sendUpdatePacket();
        }

        tick();
    }

    @Override
    public void onUpdate() {

        needsUpdate = true;
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (item != null && item.getItem() == BPItems.screwdriver) {
            if (player.isSneaking()) {
                if (!getWorld().isRemote) {
                    if (changeMode()) {
                        sendUpdatePacket();
                        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                setRotation((getRotation() + 1) % 4);
                getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
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

        RedstoneConnection c = getConnection(side);
        if (c == null)
            return 0;

        return c.getOutput();
    }

    @Override
    public final int getWeakPower(ForgeDirection side) {

        RedstoneConnection c = getConnection(side);
        if (c == null)
            return 0;

        return c.getOutput();
    }

    @Override
    public final boolean canConnectRedstone(ForgeDirection side) {

        RedstoneConnection c = getConnection(side);
        if (c == null)
            return false;

        return c.isEnabled();
    }

    public RedstoneConnection getConnection(Dir direction) {

        return connections[direction.ordinal()];
    }

    public RedstoneConnection getConnection(ForgeDirection direction) {

        try {
            return connections[Dir.getDirection(direction, getFace(), getRotation()).ordinal()];
        } catch (Exception ex) {
        }
        return null;
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

    @Override
    public void registerIcons(IIconRegister reg) {

        iconBottom = reg.registerIcon(Refs.MODID + ":gates/bottom");
        iconSide = reg.registerIcon(Refs.MODID + ":gates/side");
        iconTop = reg.registerIcon(Refs.MODID + ":gates/" + getTextureName() + "/base");
    }

    public IIcon getTopIcon() {

        return iconTop;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        writeConnections(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        readConnections(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        writeConnections(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        readConnections(tag);
    }

    private void writeConnections(NBTTagCompound tag) {

        NBTTagCompound t = new NBTTagCompound();

        for (RedstoneConnection c : connections) {
            NBTTagCompound data = new NBTTagCompound();
            c.writeToNBT(data);
            t.setTag(c.getDirection().name(), data);
        }

        tag.setTag("connections", t);
    }

    private void readConnections(NBTTagCompound tag) {

        NBTTagCompound t = tag.getCompoundTag("connections");
        for (RedstoneConnection c : connections)
            c.readFromNBT(t.getCompoundTag(c.getDirection().name()));
    }

    @Override
    public int getLightValue() {

        int on = 0;

        if (front().isEnabled())
            on += front().getOutput() / 4;
        if (right().isEnabled())
            on += right().getOutput() / 4;
        if (back().isEnabled())
            on += back().getOutput() / 4;
        if (left().isEnabled())
            on += left().getOutput() / 4;

        return on;
    }

}
