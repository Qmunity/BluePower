/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.tube;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartThruHole;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.api.tube.ITubeConnection;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.PartCache;
import com.bluepowermod.helper.TileEntityCache;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.item.ItemDamageableColorableOverlay;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.wire.PartWireFreestanding;
import com.bluepowermod.part.wire.redstone.PartRedwireFace.PartRedwireFaceUninsulated;
import com.bluepowermod.part.wire.redstone.WireHelper;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import com.bluepowermod.util.Color;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 *
 * @author MineMaarten
 */

public class PneumaticTube extends PartWireFreestanding implements IPartTicking, IPartThruHole, IPartRedstone, IRedstoneConductor,
        IConnectionListener, IRedwire {

    public final boolean[] connections = new boolean[6];
    public final boolean[] redstoneConnections = new boolean[6];
    /**
     * true when != 2 connections, when this is true the logic doesn't have to 'think' which way an item should go.
     */
    public boolean isCrossOver;
    protected final Vec3dCube sideBB = new Vec3dCube(AxisAlignedBB.getBoundingBox(0.25, 0, 0.25, 0.75, 0.25, 0.75));
    private TileEntityCache tileCache;
    private PartCache<PneumaticTube> partCache;
    protected final TubeColor[] color = { TubeColor.NONE, TubeColor.NONE, TubeColor.NONE, TubeColor.NONE, TubeColor.NONE, TubeColor.NONE };
    private final TubeLogic logic = new TubeLogic(this);
    public boolean initialized; // workaround to the connections not properly initialized, but being tried to be used.
    private int tick;

    private RedwireType redwireType = null;

    @Override
    public String getType() {

        return "pneumaticTube";
    }

    @Override
    public String getUnlocalizedName() {

        return "pneumaticTube";
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        boxes.addAll(getSelectionBoxes());
    }

    /**
     * Gets all the selection boxes for this block
     *
     * @return A list with the selection boxes
     */
    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return getTubeBoxes();
    }

    protected List<Vec3dCube> getTubeBoxes() {

        List<Vec3dCube> aabbs = getOcclusionBoxes();
        for (int i = 0; i < 6; i++) {
            ForgeDirection d = ForgeDirection.getOrientation(i);
            if (connections[i] || redstoneConnections[i] || getDeviceOnSide(d) != null) {
                Vec3dCube c = sideBB.clone().rotate(d, Vec3d.center);
                aabbs.add(c);
            }
        }
        return aabbs;
    }

    /**
     * Gets all the occlusion boxes for this block
     *
     * @return A list with the occlusion boxes
     */
    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> aabbs = new ArrayList<Vec3dCube>();
        aabbs.add(new Vec3dCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75));
        return aabbs;
    }

    @Override
    public void update() {

        if (initialized)
            logic.update();
        if (tick++ == 3) {
            clearCache();
            updateConnections();
        } else if (tick == 40) {
            sendUpdatePacket();
        }
        if (getWorld().isRemote && tick % 40 == 0)
            clearCache();// reset on the client, as it doesn't get update on neighbor block updates (as the
        // method isn't called on the client)
    }

    /**
     * Event called whenever a nearby block updates
     */
    @Override
    public void onUpdate() {

        if (getParent() != null && getWorld() != null) {

            // Redstone update

            // Don't to anything if propagation-related stuff is going on
            if (RedstoneApi.getInstance().shouldWiresHandleUpdates()) {
                getRedstoneConnectionCache().recalculateConnections();

                ForgeDirection d = ForgeDirection.UNKNOWN;
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                    if (getDeviceOnSide(dir) != null)
                        d = dir;

                RedstoneApi.getInstance().getRedstonePropagator(this, d).propagate();

                sendUpdatePacket();
            }

            // Cache and connection refresh
            clearCache();
            updateConnections();
        }
    }

    public TileEntity getTileCache(ForgeDirection d) {

        if (tileCache == null) {
            tileCache = new TileEntityCache(getWorld(), getX(), getY(), getZ());
        }
        return tileCache.getValue(d);
    }

    public PneumaticTube getPartCache(ForgeDirection d) {

        if (partCache == null) {
            partCache = new PartCache<PneumaticTube>(getWorld(), getX(), getY(), getZ(), PneumaticTube.class);
        }
        return partCache.getValue(d);
    }

    public void clearCache() {

        tileCache = null;
        partCache = null;
    }

    public TubeLogic getLogic() {

        return logic;
    }

    protected boolean canConnectToInventories() {

        return true;
    }

    private void updateConnections() {

        if (getWorld() != null && !getWorld().isRemote) {
            int connectionCount = 0;
            boolean clearedCache = false;
            clearCache();
            for (int i = 0; i < 6; i++) {
                boolean oldState = connections[i];
                ForgeDirection d = ForgeDirection.getOrientation(i);
                TileEntity neighbor = getTileCache(d);
                connections[i] = IOHelper.canInterfaceWith(neighbor, d.getOpposite(), this, canConnectToInventories());

                if (!connections[i])
                    connections[i] = neighbor instanceof ITubeConnection && ((ITubeConnection) neighbor).isConnectedTo(d.getOpposite());
                if (connections[i]) {
                    connections[i] = isConnected(d, null);
                }
                if (connections[i])
                    connectionCount++;
                if (!clearedCache && oldState != connections[i]) {
                    if (Config.enableTubeCaching)
                        getLogic().clearNodeCaches();
                    clearedCache = true;
                }
            }
            isCrossOver = connectionCount != 2;
            sendUpdatePacket();
        }
        initialized = true;
    }

    public boolean isConnected(ForgeDirection dir, PneumaticTube otherTube) {

        if (otherTube != null) {
            if (!(this instanceof Accelerator) && this instanceof MagTube != otherTube instanceof MagTube && !(otherTube instanceof Accelerator))
                return false;
            TubeColor otherTubeColor = otherTube.getColor(dir.getOpposite());
            if (otherTubeColor != TubeColor.NONE && getColor(dir) != TubeColor.NONE && getColor(dir) != otherTubeColor)
                return false;
        }
        return getWorld() == null || OcclusionHelper.microblockOcclusionTest(getParent(), true, MicroblockShape.FACE_HOLLOW, 8, dir);
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        for (int i = 0; i < 6; i++) {
            tag.setBoolean("connections" + i, connections[i]);
            tag.setBoolean("redstoneConnections" + i, getDeviceOnSide(ForgeDirection.getOrientation(i)) != null);
        }
        for (int i = 0; i < color.length; i++)
            tag.setByte("tubeColor" + i, (byte) color[i].ordinal());

        if (redwireType != null)
            tag.setInteger("wireType", redwireType.ordinal());
        tag.setByte("power", getPower());

        NBTTagCompound logicTag = new NBTTagCompound();
        logic.writeToNBT(logicTag);
        tag.setTag("logic", logicTag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        int connectionCount = 0;
        for (int i = 0; i < 6; i++) {
            connections[i] = tag.getBoolean("connections" + i);
            redstoneConnections[i] = tag.getBoolean("redstoneConnections" + i);
            if (connections[i])
                connectionCount++;
        }
        isCrossOver = connectionCount != 2;
        for (int i = 0; i < color.length; i++)
            color[i] = TubeColor.values()[tag.getByte("tubeColor" + i)];

        if (tag.hasKey("wireType"))
            redwireType = RedwireType.values()[tag.getInteger("wireType")];
        else
            redwireType = null;
        setRedstonePower(null, tag.getByte("power"));

        if (getParent() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());

        NBTTagCompound logicTag = tag.getCompoundTag("logic");
        logic.readFromNBT(logicTag);
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);

        // Connections
        for (int i = 0; i < 6; i++)
            buffer.writeBoolean(connections[i]);
        for (int i = 0; i < 6; i++)
            buffer.writeBoolean(getDeviceOnSide(ForgeDirection.getOrientation(i)) != null);

        // Colors
        for (int i = 0; i < color.length; i++)
            buffer.writeInt(color[i].ordinal());

        // Redwire
        if (redwireType != null) {
            buffer.writeBoolean(true);
            buffer.writeInt(redwireType.ordinal());
            buffer.writeByte(getPower());
        } else {
            buffer.writeBoolean(false);
        }

        // Logic
        logic.writeData(buffer);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);

        // Connections
        for (int i = 0; i < 6; i++)
            connections[i] = buffer.readBoolean();
        for (int i = 0; i < 6; i++)
            redstoneConnections[i] = buffer.readBoolean();

        int connectionCount = 0;
        for (int i = 0; i < 6; i++)
            if (connections[i] || redstoneConnections[i])
                connectionCount++;
        isCrossOver = connectionCount != 2;

        // Colors
        for (int i = 0; i < color.length; i++)
            color[i] = TubeColor.values()[buffer.readInt()];

        // Redwire
        if (buffer.readBoolean()) {
            redwireType = RedwireType.values()[buffer.readInt()];
            setRedstonePower(null, buffer.readByte());
        } else {
            redwireType = null;
        }

        // Logic
        logic.readData(buffer);

        // Render update
        if (getParent() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    /**
     * Event called when the part is activated (right clicked)
     *
     * @param player
     *            Player that right clicked the part
     * @param item
     *            Item that was used to click it
     * @return Whether or not an action occurred
     */
    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition mop, ItemStack item) {

        if (getWorld() == null)
            return false;

        if (item != null) {
            TubeColor newColor = null;
            if (item.getItem() == BPItems.paint_brush && ((ItemDamageableColorableOverlay) BPItems.paint_brush).tryUseItem(item)) {
                newColor = TubeColor.values()[item.getItemDamage()];
            } else if (item.getItem() == Items.water_bucket || (item.getItem() == BPItems.paint_brush && item.getItemDamage() == 16)) {
                newColor = TubeColor.NONE;
            }
            if (newColor != null) {
                if (!getWorld().isRemote) {
                    List<Vec3dCube> boxes = getTubeBoxes();
                    Vec3dCube box = mop.getCube();
                    int face = -1;
                    if (box.equals(boxes.get(0))) {
                        face = mop.sideHit;
                    } else {
                        face = getSideFromAABBIndex(boxes.indexOf(box));
                    }
                    color[face] = newColor;
                    updateConnections();
                    getLogic().clearNodeCaches();
                    notifyUpdate();
                }
                return true;
            }

            if (item.getItem() instanceof ItemPart) {
                BPPart part = PartManager.getExample(item);
                if (redwireType == null && part instanceof PartRedwireFaceUninsulated) {
                    if (!getWorld().isRemote) {
                        redwireType = ((IRedwire) part).getRedwireType(ForgeDirection.UNKNOWN);
                        if (!player.capabilities.isCreativeMode)
                            item.stackSize--;

                        // Redstone update
                        getRedstoneConnectionCache().recalculateConnections();
                        RedstoneApi.getInstance().getRedstonePropagator(this, ForgeDirection.DOWN).propagate();

                        updateConnections();
                        getLogic().clearNodeCaches();
                        notifyUpdate();
                        sendUpdatePacket();
                    }
                    return true;
                }
            }
            // Removing redwire
            if (redwireType != null && item.getItem() instanceof IScrewdriver && player.isSneaking()) {
                if (!getWorld().isRemote) {
                    IOHelper.spawnItemInWorld(getWorld(), PartManager.getPartInfo("wire." + redwireType.getName()).getStack(), getX() + 0.5,
                            getY() + 0.5, getZ() + 0.5);
                    redwireType = null;

                    // Redstone update
                    getRedstoneConnectionCache().recalculateConnections();
                    RedstoneApi.getInstance().getRedstonePropagator(this, ForgeDirection.DOWN).propagate();

                    ((IScrewdriver) item.getItem()).damage(item, 1, player, false);

                    updateConnections();
                    getLogic().clearNodeCaches();
                    notifyUpdate();
                    sendUpdatePacket();
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (TubeStack stack : logic.tubeStacks) {
            drops.add(stack.stack);
        }
        return drops;
    }

    /**
     * How 'dense' the tube is to the pathfinding algorithm. Is altered in the RestrictionTube
     *
     * @return
     */
    public int getWeight() {

        return 1;
    }

    public TubeColor getColor(ForgeDirection dir) {

        return color[dir.ordinal()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        if (pass == 0 && !(this instanceof PneumaticTubeOpaque)) {
            logic.renderDynamic(translation, (float) delta);
            if (!shouldRenderNode())
                renderSide();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        GL11.glPushMatrix();
        GL11.glTranslated(0, -0.125D, 0);

        Tessellator t = Tessellator.instance;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        t.startDrawingQuads();

        connections[ForgeDirection.DOWN.ordinal()] = true;
        connections[ForgeDirection.UP.ordinal()] = true;

        RenderHelper renderer = RenderHelper.instance;
        renderer.fullReset();
        RenderBlocks rb = new RenderBlocks();

        renderStatic(new Vec3i(0, 0, 0), renderer, rb, 0);
        renderStatic(new Vec3i(0, 0, 0), renderer, rb, 1);

        t.draw();
        renderSide();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

        GL11.glPopMatrix();
    }

    protected boolean shouldRenderNode() {

        boolean shouldRenderNode = false;
        int connectionCount = 0;
        for (int i = 0; i < 6; i += 2) {
            if (connections[i] != connections[i + 1] || redstoneConnections[i] != redstoneConnections[i + 1]) {
                shouldRenderNode = true;
                break;
            }
            if (connections[i] || redstoneConnections[i])
                connectionCount++;
            if (connections[i + 1] || redstoneConnections[i + 1])
                connectionCount++;
        }
        return shouldRenderNode || connectionCount == 0 || connectionCount > 2;
    }

    private double getAddedThickness() {

        return 0;// 0.125 / 16D;
    }

    protected boolean shouldRenderFully() {

        boolean renderFully = false;
        int count = 0;

        for (int i = 0; i < 6; i++) {
            if (shouldRenderConnection(ForgeDirection.getOrientation(i)))
                count++;
            if (i % 2 == 0 && connections[i] != connections[i + 1])
                renderFully = true;
        }

        renderFully |= count > 2 || count < 2;
        renderFully |= getParent() == null || getWorld() == null;

        return renderFully;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i loc, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        boolean down = shouldRenderConnection(ForgeDirection.DOWN);
        boolean up = shouldRenderConnection(ForgeDirection.UP);
        boolean north = shouldRenderConnection(ForgeDirection.NORTH);
        boolean south = shouldRenderConnection(ForgeDirection.SOUTH);
        boolean west = shouldRenderConnection(ForgeDirection.WEST);
        boolean east = shouldRenderConnection(ForgeDirection.EAST);

        boolean renderFully = shouldRenderFully();

        if (this instanceof RestrictionTube) {
            IIcon icon = IconSupplier.restrictionTubeSide;
            renderer.renderBox(new Vec3dCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75), icon);
        }
        double addedThickness = getAddedThickness();

        double wireSize = getSize() / 16D;
        double frameSeparation = 6 / 16D - addedThickness - addedThickness - 0.001;
        double frameThickness = 1 / 16D + addedThickness;

        if (pass == 0) {

            if (this instanceof RestrictionTube) {
                IIcon icon = IconSupplier.restrictionTubeSide;
                renderer.renderBox(new Vec3dCube(0.25, 0.25, 0.25, 0.75, 0.75, 0.75), icon);
            }

            if (renderFully) {
                renderer.setColor(getColorMultiplier());

                renderFrame(renderer, wireSize, frameSeparation, frameThickness, true, true, true, true, true, true, down, up, west, east, north,
                        south, true, getFrameIcon(), getFrameColorMultiplier());

                renderer.setColor(0xFFFFFF);
            } else {
                boolean isInWorld = getParent() != null;
                renderer.setColor(getFrameColorMultiplier());

                // Frame
                renderFrame(renderer, wireSize, frameSeparation, frameThickness, down, up, west, east, north, south, isInWorld, getFrameIcon(),
                        getFrameColorMultiplier());
            }

            // Tube coloring
            {
                Vec3dCube side = new Vec3dCube(0.25 + 5 / 128D, 0, 0.25 - addedThickness, 0.25 + 9 / 128D + addedThickness, 0.25, 0.25 + 2 / 128D);
                Vec3dCube side2 = new Vec3dCube(0.25 - addedThickness, 0, 0.25 + 5 / 128D, 0.25 + 2 / 128D, 0.25, 0.25 + 9 / 128D + addedThickness);
                Vec3dCube side3 = new Vec3dCube(0.25 - addedThickness, 0.25 - addedThickness, 0.25 + 5 / 128D, 0.25 + 2 / 128D, 0.25 + 4 / 128D,
                        0.25 + 59 / 128D);
                Vec3dCube side4 = new Vec3dCube(0.25 + 5 / 128D, 0.25 - addedThickness, 0.25 + 5 / 128D, 0.25 + 9 / 128D + addedThickness,
                        0.25 + 2 / 128D, 0.25 + 56 / 128D);
                Vec3dCube side5 = new Vec3dCube(0.25 + 5 / 128D, 0.25 - addedThickness, 0.25 - 1 / 128D, 0.25 + 9 / 128D + addedThickness,
                        0.25 + 2 / 128D, 0.25 + 65 / 128D);
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    TubeColor c = color[d.ordinal()];
                    if (c != TubeColor.NONE) {
                        try {
                            renderer.setColor(MinecraftColor.values()[15 - c.ordinal()].getHex());
                            if (connections[d.ordinal()]) {
                                for (int i = 0; i < 4; i++) {
                                    renderer.renderBox(side.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                                    renderer.renderBox(side2.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                                    if (renderFully)
                                        renderer.renderBox(side3.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                                IconSupplier.pneumaticTubeColoring);
                                }
                            } else if (renderFully) {
                                for (int i = 0; i < 4; i++)
                                    renderer.renderBox(side4.clone().rotate(0, i * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                            } else {
                                for (int i = 1; i < 4; i += 2)
                                    renderer.renderBox(
                                            side5.clone()
                                                    .rotate(0,
                                                            (i + ((shouldRenderConnection(ForgeDirection.NORTH) || (shouldRenderConnection(ForgeDirection.UP) && (d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH))) ? 1
                                                                    : 0)) * 90, 0, Vec3d.center).rotate(d, Vec3d.center),
                                            IconSupplier.pneumaticTubeColoring);
                            }
                            renderer.setColor(0xFFFFFF);
                        } catch (Exception ex) {
                            System.out.println("Err on side " + d + ". Color: " + c);
                        }
                    }
                }
            }

            if (redwireType != null) {
                frameThickness /= 1.5;
                frameSeparation -= 1 / 32D;

                renderFrame(renderer, wireSize, frameSeparation, frameThickness, renderFully || shouldRenderConnection(ForgeDirection.DOWN),
                        renderFully || shouldRenderConnection(ForgeDirection.UP), renderFully || shouldRenderConnection(ForgeDirection.WEST),
                        renderFully || shouldRenderConnection(ForgeDirection.EAST), renderFully || shouldRenderConnection(ForgeDirection.NORTH),
                        renderFully || shouldRenderConnection(ForgeDirection.SOUTH), redstoneConnections[ForgeDirection.DOWN.ordinal()],
                        redstoneConnections[ForgeDirection.UP.ordinal()], redstoneConnections[ForgeDirection.WEST.ordinal()],
                        redstoneConnections[ForgeDirection.EAST.ordinal()], redstoneConnections[ForgeDirection.NORTH.ordinal()],
                        redstoneConnections[ForgeDirection.SOUTH.ordinal()], getParent() != null && getWorld() != null, IconSupplier.wire,
                        WireHelper.getColorForPowerLevel(redwireType, getPower()));

                Vec3dCube c = new Vec3dCube(0.5 - 1 / 56D, 0, 0.2, 0.5 + 1 / 56D, 1 / 32D, 0.8);

                renderer.setColor(WireHelper.getColorForPowerLevel(redwireType, getPower()));
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (redstoneConnections[d.ordinal()] && !connections[d.ordinal()]) {
                        renderer.addTransformation(new Rotation(d));
                        for (int i = 0; i < 2; i++) {
                            renderer.addTransformation(new Rotation(0, 45 + 90 * i, 0));
                            renderer.renderBox(c.clone(), IconSupplier.wire);
                            renderer.removeTransformation();
                        }
                        renderer.removeTransformation();
                    }
                }
                renderer.setColor(0xFFFFFF);
            }
        } else {
            IIcon glass = this instanceof MagTube ? IconSupplier.magTubeGlass : IconSupplier.pneumaticTubeGlass;
            IIcon glass2 = this instanceof MagTube ? IconSupplier.magTubeGlass2 : IconSupplier.pneumaticTubeGlass2;

            if (!(this instanceof RestrictionTube)) {
                renderer.setRenderSides(!down, !up, !west, !east, !north, !south);
                if (renderFully) {
                    renderer.renderBox(new Vec3dCube(4.5 / 16D, 4.5 / 16D, 4.5 / 16D, 11.5 / 16D, 11.5 / 16D, 11.5 / 16D), glass);
                } else {
                    boolean ud = west || east;
                    boolean we = north || south;
                    boolean ns = west || east;

                    renderer.setTextureRotations(ud ? 1 : 0, ud ? 1 : 0, we ? 1 : 0, we ? 1 : 0, ns ? 1 : 0, ns ? 1 : 0);
                    renderer.renderBox(new Vec3dCube(4.5 / 16D, 4.5 / 16D, 4.5 / 16D, 11.5 / 16D, 11.5 / 16D, 11.5 / 16D), glass2);
                }
                renderer.resetRenderedSides();
                renderer.resetTextureRotations();
            }

            Vec3dCube c = new Vec3dCube(4.5 / 16D, 0, 4.5 / 16D, 11.5 / 16D, 4.5 / 16D, 11.5 / 16D);
            if (renderFully) {
                boolean ud = west || east;
                boolean we = north || south;
                boolean ns = west || east;

                renderer.setTextureRotations(ud ? 1 : 0, ud ? 1 : 0, we ? 1 : 0, we ? 1 : 0, ns ? 1 : 0, ns ? 1 : 0);
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (shouldRenderConnection(d)) {
                        renderer.setRenderSide(d, false);
                        renderer.setRenderSide(d.getOpposite(), false);

                        renderer.renderBox(c.clone().rotate(d, Vec3d.center), glass);

                        renderer.resetRenderedSides();
                    }
                }
            } else {
                boolean ud = west || east;
                boolean we = north || south;
                boolean ns = west || east;

                renderer.setTextureRotations(ud ? 1 : 0, ud ? 1 : 0, we ? 1 : 0, we ? 1 : 0, ns ? 1 : 0, ns ? 1 : 0);
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (shouldRenderConnection(d)) {
                        renderer.setRenderSide(d, false);
                        renderer.setRenderSide(d.getOpposite(), false);

                        renderer.renderBox(c.clone().rotate(d, Vec3d.center), glass2);

                        renderer.resetRenderedSides();
                    }
                }

            }
        }

        return true;
    }

    @Override
    public boolean shouldRenderOnPass(int pass) {

        return true;
    }

    protected void renderSide() {

    }

    /**
     * Hacky method to get the right side
     *
     * @return
     */
    private int getSideFromAABBIndex(int index) {

        int curIndex = 0;
        for (int side = 0; side < 6; side++) {
            if (connections[side]) {
                curIndex++;
                if (index == curIndex)
                    return side;
            }
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon() {

        return IconSupplier.pneumaticTubeSide;
    }

    /**
     * Adds information to the waila tooltip
     *
     * @author amadornes
     *
     * @param info
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> info) {

        boolean addTooltip = false;
        for (TubeColor col : color) {
            if (col != TubeColor.NONE) {
                addTooltip = true;
                break;
            }
        }
        if (addTooltip) {
            info.add(Color.YELLOW + I18n.format("waila.bluepower:pneumaticTube.color"));
            for (int i = 0; i < 6; i++) {
                if (color[i] != TubeColor.NONE) {
                    if (color[i] != TubeColor.NONE)
                        info.add(EnumChatFormatting.DARK_AQUA
                                + I18n.format("bluepower:face." + ForgeDirection.getOrientation(i).toString().toLowerCase()) + ": "
                                + EnumChatFormatting.WHITE + I18n.format("bluepower:color." + ItemDye.field_150923_a[color[i].ordinal()]));
                }
            }
        }
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.machines;
    }

    @Override
    public int getHollowSize(ForgeDirection side) {

        return 8;
    }

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        return connections[side.ordinal()] || redstoneConnections[side.ordinal()];
    }

    @Override
    protected int getSize() {

        return 0;
    }

    @Override
    protected IIcon getWireIcon(ForgeDirection side) {

        return null;
    }

    @Override
    protected IIcon getFrameIcon() {

        return getSideIcon();
    }

    public RedwireType getRedwireType() {

        return redwireType;
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition mop = super.rayTrace(start, end);
        if (mop == null)
            return null;

        EntityPlayer player = BluePower.proxy.getPlayer();
        if (redwireType != null && player != null && player.isSneaking()) {
            double wireSize = getSize() / 16D;
            double frameSeparation = 4 / 16D - (wireSize - 2 / 16D);
            double frameThickness = 1 / 16D;
            frameThickness /= 1.5;
            frameSeparation -= 1 / 32D;

            QMovingObjectPosition wire = RayTracer.instance().rayTraceCubes(
                    getFrameBoxes(wireSize, frameSeparation, frameThickness, shouldRenderConnection(ForgeDirection.DOWN),
                            shouldRenderConnection(ForgeDirection.UP), shouldRenderConnection(ForgeDirection.WEST),
                            shouldRenderConnection(ForgeDirection.EAST), shouldRenderConnection(ForgeDirection.NORTH),
                            shouldRenderConnection(ForgeDirection.SOUTH), redstoneConnections[ForgeDirection.DOWN.ordinal()],
                            redstoneConnections[ForgeDirection.UP.ordinal()], redstoneConnections[ForgeDirection.WEST.ordinal()],
                            redstoneConnections[ForgeDirection.EAST.ordinal()], redstoneConnections[ForgeDirection.NORTH.ordinal()],
                            redstoneConnections[ForgeDirection.SOUTH.ordinal()], getParent() != null && getWorld() != null), start, end,
                    new Vec3i(this));
            QMovingObjectPosition frame = RayTracer.instance().rayTraceCubes(getFrameBoxes(), start, end, new Vec3i(this));

            if (wire != null) {
                if (frame != null) {
                    if (wire.hitVec.distanceTo(start.toVec3()) < frame.hitVec.distanceTo(start.toVec3()))
                        mop.hitInfo = PartManager.getPartInfo("wire." + redwireType.getName()).getStack();
                } else {
                    mop.hitInfo = PartManager.getPartInfo("wire." + redwireType.getName()).getStack();
                }
            }
        }

        return mop;
    }

    @Override
    public ItemStack getPickedItem(QMovingObjectPosition mop) {

        Object o = mop.hitInfo;
        if (o != null && o instanceof ItemStack)
            return (ItemStack) o;

        return super.getPickedItem(mop);
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        return 0;
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        if (getRedwireType() == null)
            return 0;

        return MathHelper.map(getPower() & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        if (getRedwireType() == null)
            return false;

        return true;
    }

    private RedstoneConnectionCache redConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);

    private byte power = 0;

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        if (type == ConnectionType.STRAIGHT) {
            if (getRedwireType(side) == null)
                return false;

            if (device instanceof IRedwire) {
                RedwireType rwt = getRedwireType(side);
                if (type == null)
                    return false;
                RedwireType rwt_ = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite() : side.getOpposite());
                if (rwt_ == null)
                    return false;
                if (!rwt.canConnectTo(rwt_))
                    return false;
            }

            if (device instanceof IFace)
                return ((IFace) device).getFace() == side.getOpposite();
            if (!OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
                return false;
            if (device instanceof PneumaticTube)
                if (device instanceof MagTube != this instanceof MagTube)
                    return false;

            return true;
        }

        return false;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

        return redConnections;
    }

    @Override
    public void onConnect(IConnection<?> connection) {

        sendUpdatePacket();
    }

    @Override
    public void onDisconnect(IConnection<?> connection) {

        sendUpdatePacket();
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
            return 0;

        if (!isAnalogue(side))
            return (byte) ((power & 0xFF) > 0 ? 255 : 0);

        return power;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        this.power = power;
    }

    @Override
    public void onRedstoneUpdate() {

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            IConnection<IRedstoneDevice> c = redConnections.getConnectionOnSide(dir);
            IRedstoneDevice dev = null;
            if (c != null)
                dev = c.getB();
            if (dev == null || dev instanceof DummyRedstoneDevice)
                RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, false);
        }

        sendUpdatePacket();
    }

    @Override
    public boolean hasLoss(ForgeDirection side) {

        if (getRedwireType() == null)
            return false;

        return getRedwireType().hasLoss();
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        if (getRedwireType() == null)
            return false;

        return getRedwireType().isAnalogue();
    }

    @Override
    public boolean canPropagateFrom(ForgeDirection fromSide) {

        return true;// getRedwireType() != null;
    }

    public byte getPower() {

        return power;
    }

    public IRedstoneDevice getDeviceOnSide(ForgeDirection d) {

        @SuppressWarnings("unchecked")
        IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) getRedstoneConnectionCache().getConnectionOnSide(d);

        if (c == null)
            return null;

        return c.getB();
    }

    @Override
    public RedwireType getRedwireType(ForgeDirection side) {

        return getRedwireType();
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return false;
    }
}
