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
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartRenderPlacement;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.util.Dir;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.RedstoneConnection;
import com.bluepowermod.part.gate.component.GateComponent;
import com.bluepowermod.part.gate.ic.IntegratedCircuit;
import com.bluepowermod.part.wire.redstone.PartRedwireFace;
import com.bluepowermod.part.wire.redstone.WireCommons;
import com.bluepowermod.util.Layout;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GateBase extends BPPartFaceRotate implements IPartRedstone, IPartTicking, IPartRenderPlacement, IFaceRedstoneDevice {

    private static Vec3dCube BOX = new Vec3dCube(0, 0, 0, 1, 2D / 16D, 1);

    private final RedstoneConnection[] connections = new RedstoneConnection[] { new RedstoneConnection(this, Dir.FRONT),
            new RedstoneConnection(this, Dir.RIGHT), new RedstoneConnection(this, Dir.BACK), new RedstoneConnection(this, Dir.LEFT),
            new RedstoneConnection(this, Dir.TOP), new RedstoneConnection(this, Dir.BOTTOM) };

    private boolean needsUpdate;// flag that is set when a neighbor block update occurs.
    public IntegratedCircuit parentCircuit;

    @SideOnly(Side.CLIENT)
    private static IIcon icon;
    @SideOnly(Side.CLIENT)
    private static IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private static IIcon iconDark;

    protected static GateBase rendering;

    private Layout layout = null;

    private List<GateComponent> components = new ArrayList<GateComponent>();

    public GateBase() {

        initializeConnections();
        if (getLayout() != null)
            initializeComponents();
    }

    public abstract void initializeConnections();

    public abstract void initializeComponents();

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

        boxes.add(BOX.clone().expand(-0.001));
    }

    protected void playTickSound() {

        if (getWorld().isRemote && Config.enableGateSounds)
            getWorld().playSound(getX(), getY(), getZ(), "gui.button.press", 0.5F, 0.5F, false);

    }

    @SideOnly(Side.CLIENT)
    protected final void transformDynamic(Vec3d translation) {

        GL11.glTranslated(translation.getX(), translation.getY(), translation.getZ());

        GL11.glTranslated(0.5, 0.5, 0.5);
        {
            switch (getFace()) {
            case DOWN:
                break;
            case UP:
                GL11.glRotated(180, 0, 1, 0);
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
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        transformDynamic(translation);

        for (GateComponent c : components) {
            GL11.glPushMatrix();
            c.renderDynamic(translation, delta, pass);
            GL11.glPopMatrix();
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        switch (getFace()) {
        case DOWN:
            break;
        case UP:
            renderer.addTransformation(new Rotation(180, 180, 0, Vec3d.center));
            break;
        case NORTH:
            renderer.addTransformation(new Rotation(90, 0, 0, Vec3d.center));
            break;
        case SOUTH:
            renderer.addTransformation(new Rotation(-90, 0, 0, Vec3d.center));
            break;
        case WEST:
            renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
            break;
        case EAST:
            renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
            break;
        default:
            break;
        }

        int rotation = getRotation();
        if (rotation != -1)
            renderer.addTransformation(new Rotation(0, 90 * -rotation, 0));

        boolean wasNull = rendering == null;
        if (rendering == null)
            rendering = this;

        renderer.renderBox(BOX, getIcon(ForgeDirection.DOWN), getIcon(ForgeDirection.UP), getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH));

        for (GateComponent c : components)
            c.renderStatic(translation, renderer, pass);

        if (wasNull)
            rendering = null;

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        rendering = this;

        if (this instanceof ISilkyRemovable) {
            readFromNBT(item.hasTagCompound() ? item.getTagCompound().getCompoundTag("tileData") : new NBTTagCompound());
        }
        GL11.glPushMatrix();
        {
            RenderHelper rh = RenderHelper.instance;
            rh.reset();

            if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                GL11.glTranslated(-0.25, 0.75, 0.25);
            }
            if (type == ItemRenderType.ENTITY && item.getItemFrame() != null) {
                GL11.glTranslated(19 / 32D, 8 / 16D, 1);
                GL11.glRotated(-90, 0, 0, 1);
                GL11.glRotated(90, 0, 1, 0);
            }

            Tessellator.instance.startDrawingQuads();
            if (shouldRenderOnPass(0))
                renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 0);
            rh.reset();
            if (shouldRenderOnPass(1))
                renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 1);
            rh.reset();
            Tessellator.instance.draw();
            if (shouldRenderOnPass(0))
                renderDynamic(new Vec3d(0, 0, 0), 0, 0);
            if (shouldRenderOnPass(1))
                renderDynamic(new Vec3d(0, 0, 0), 0, 1);
        }
        GL11.glPopMatrix();

        rendering = null;
    }

    protected void addComponent(GateComponent component) {

        if (component != null && !components.contains(component))
            components.add(component);
    }

    protected List<GateComponent> getComponents() {

        return components;
    }

    public abstract void doLogic();

    public void tick() {

    }

    @Override
    public final void update() {

        if (needsUpdate) {
            needsUpdate = false;
            if (parentCircuit == null) {
                WireCommons.refreshConnectionsRedstone(this);
                for (RedstoneConnection c : connections)
                    c.update();
            }
            doLogic();
            for (IRedstoneDevice d : devices) {
                if (d != null && d instanceof PartRedwireFace)
                    ((BPPart) d).onUpdate();
            }

            sendUpdatePacket();
        }

        if (parentCircuit == null)
            for (GateComponent c : components)
                c.tick();

        tick();
    }

    @Override
    public void onUpdate() {

        needsUpdate = true;

        WireCommons.refreshConnectionsRedstone(this);
    }

    @Override
    public void onRemoved() {

        super.onRemoved();
        WireCommons.disconnectRedstone(this);
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (item != null && item.getItem() == BPItems.screwdriver) {
            if (player.isSneaking()) {
                if (changeMode()) {
                    if (!getWorld().isRemote) {
                        for (RedstoneConnection c : connections)
                            if (c != null)
                                c.forceUpdateNeighbor();
                        if (parentCircuit == null)
                            WireCommons.refreshConnectionsRedstone(this);
                        sendUpdatePacket();
                    }
                    return true;
                }
                return false;
            } else {
                setRotation((getRotation() + 1) % 4);
                for (RedstoneConnection c : connections)
                    if (c != null)
                        c.forceUpdateNeighbor();
                if (parentCircuit == null)
                    WireCommons.refreshConnectionsRedstone(this);
                sendUpdatePacket();
            }

            return true;
        } else if (hasGUI()) {
            if (getWorld().isRemote) {
                FMLCommonHandler.instance().showGuiScreen(getGui(player));
            } else {
                handleGUIServer(player);
            }
            return true;
        }
        return false;
    }

    protected void handleGUIServer(EntityPlayer player) {

    }

    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

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

        for (RedstoneConnection c : connections)
            if (c.getDirection().toForgeDirection(getFace(), getRotation()) == direction)
                return c;
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
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        icon = reg.registerIcon(Refs.MODID + ":gates/gate");
        iconSide = reg.registerIcon(Refs.MODID + ":gates/side");
        iconDark = reg.registerIcon(Refs.MODID + ":gates/gate_dark");

        if (layout == null)
            layout = new Layout("/assets/" + Refs.MODID + "/textures/blocks/gates/" + getTextureName());
        layout.reload();
        components.clear();
        initializeComponents();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getTopIcon() {

        return icon;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ForgeDirection face) {

        if (face == ForgeDirection.DOWN)
            return icon;
        if (face == ForgeDirection.UP)
            return ((GateBase) PartManager.getExample(rendering.getType())).getTopIcon();

        return iconSide;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getDarkTop() {

        return iconDark;
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

        return 0;
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.circuits;
    }

    protected IRedstoneDevice[] devices = new IRedstoneDevice[6];

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return getConnection(side).isEnabled();
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;
        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;
        if (device instanceof GateBase)
            return false;

        return canConnectStraight(side, device);
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side == getFace())
            return false;
        if (side == getFace().getOpposite())
            return false;
        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return canConnectStraight(side, device);
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        devices[side.ordinal()] = device;
        onUpdate();
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
        onUpdate();
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        RedstoneConnection con = getConnection(side);

        if (con != null && con.isEnabled())
            return (byte) MathHelper.map(con.getOutput(), 0, 15, 0, 255);

        return 0;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        RedstoneConnection con = getConnection(side);

        if (con != null && con.isEnabled())
            con.setInput(MathHelper.map(power & 0xFF, 0, 255, 0, 15));
    }

    @Override
    public void onRedstoneUpdate() {

        onUpdate();
    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public boolean occlusionTest(IPart part) {

        if (part instanceof GateBase)
            return ((GateBase) part).getFace() != getFace().getOpposite();

        return false;
    }

    public Layout getLayout() {

        try {
            return ((GateBase) PartManager.getExample(getType())).layout;
        } catch (Exception ex) {
            // This is the example part
        }
        return null;
    }

}
