package com.bluepowermod.part.gate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
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
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartRenderPlacement;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.api.gate.IGate;
import com.bluepowermod.api.gate.IGateComponent;
import com.bluepowermod.api.gate.IGateConnection;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.gate.IIntegratedCircuitPart;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.Config;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import com.bluepowermod.util.Layout;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GateBase<C_BOTTOM extends GateConnectionBase, C_TOP extends GateConnectionBase, C_LEFT extends GateConnectionBase, C_RIGHT extends GateConnectionBase, C_FRONT extends GateConnectionBase, C_BACK extends GateConnectionBase>
extends BPPartFaceRotate implements IGate<C_BOTTOM, C_TOP, C_LEFT, C_RIGHT, C_FRONT, C_BACK>, IPartRedstone, IConnectionListener,
IRedstoneDevice, IBundledDevice, IPartTicking, IPartRenderPlacement, IIntegratedCircuitPart {

    // Static var declarations
    private static Vec3dCube BOX = new Vec3dCube(0, 0, 0, 1, 2D / 16D, 1);

    // Abstract methods

    protected abstract String getGateType();

    protected abstract void initConnections();

    protected abstract void initComponents();

    // Implementation of IGate

    private List<IGateComponent> components = new ArrayList<IGateComponent>();

    private C_BOTTOM connectionBottom = null;
    private C_TOP connectionTop = null;
    private C_LEFT connectionLeft = null;
    private C_RIGHT connectionRight = null;
    private C_FRONT connectionFront = null;
    private C_BACK connectionBack = null;

    private Layout layout = null;

    public GateBase() {

        initConnections();
        if (getLayout() != null)
            initComponents();

        IConnectionCache<IRedstoneDevice> c1 = getRedstoneConnectionCache();
        if (c1 != null)
            c1.listen();
        IConnectionCache<IBundledDevice> c2 = getBundledConnectionCache();
        if (c2 != null)
            c2.listen();
    }

    @Override
    public String getType() {

        return getGateType();
    }

    @Override
    public String getUnlocalizedName() {

        return "gate." + getGateType();
    }

    @Override
    public List<IGateComponent> getComponents() {

        return components;
    }

    @Override
    public void addComponent(IGateComponent component) {

        if (component == null)
            return;
        if (components.contains(component))
            return;

        components.add(component);
    }

    @Override
    public C_BOTTOM bottom() {

        return connectionBottom;
    }

    @Override
    public C_TOP top() {

        return connectionTop;
    }

    @Override
    public C_LEFT left() {

        return connectionLeft;
    }

    @Override
    public C_RIGHT right() {

        return connectionRight;
    }

    @Override
    public C_FRONT front() {

        return connectionFront;
    }

    @Override
    public C_BACK back() {

        return connectionBack;
    }

    public C_BOTTOM bottom(C_BOTTOM connection) {

        return connectionBottom = connection;
    }

    public C_TOP top(C_TOP connection) {

        return connectionTop = connection;
    }

    public C_LEFT left(C_LEFT connection) {

        return connectionLeft = connection;
    }

    public C_RIGHT right(C_RIGHT connection) {

        return connectionRight = connection;
    }

    public C_FRONT front(C_FRONT connection) {

        return connectionFront = connection;
    }

    public C_BACK back(C_BACK connection) {

        return connectionBack = connection;
    }

    public GateConnectionBase[] getConnections() {

        GateConnectionBase[] connections = new GateConnectionBase[6];

        connections[0] = bottom();
        connections[1] = top();
        connections[2] = left();
        connections[3] = right();
        connections[4] = front();
        connections[5] = back();

        return connections;
    }

    @Override
    public abstract IGateLogic<? extends GateBase<C_BOTTOM, C_TOP, C_LEFT, C_RIGHT, C_FRONT, C_BACK>> logic();

    // Layout

    public Layout getLayout() {

        BPPart part = this;
        try {
            part = PartManager.getExample(getType());
        } catch (Exception ex) {
        }

        return ((GateBase<?, ?, ?, ?, ?, ?>) part).layout;
    }

    // Overridable methods

    public boolean isCraftableInCircuitTable() {

        return true;
    }

    public boolean canBePlacedOnIC() {

        return true;
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.circuits;
    }

    // Basic gate logic

    @Override
    public final void update() {

        logic().tick();

        for (GateConnectionBase c : getConnections())
            if (c != null)
                c.notifyUpdateIfNeeded();

        for (IGateComponent c : getComponents())
            c.tick();

        sendUpdateIfNeeded();
    }

    @Override
    public void onUpdate() {

        getRedstoneConnectionCache().recalculateConnections();
        getBundledConnectionCache().recalculateConnections();

        for (GateConnectionBase c : getConnections())
            if (c != null)
                RedstoneApi.getInstance().getRedstonePropagator(this, c.getForgeDirection()).propagate();

        doLogicStuff();
    }

    private void doLogicStuff() {

        logic().doLogic();

        // sendUpdateIfNeeded();
    }

    @Override
    public void onRemoved() {

        super.onRemoved();

        // FIXME WireCommons.disconnect(this, this);
    }

    private void sendUpdateIfNeeded() {

        boolean send = false;
        for (IGateComponent c : getComponents()) {
            if (c.needsSyncing()) {
                send = true;
                break;
            }
        }
        if (!send) {
            for (GateConnectionBase c : getConnections()) {
                if (c != null && c.needsSyncing()) {
                    send = true;
                    break;
                }
            }
        }

        if (send)
            sendUpdatePacket();
    }

    // Interaction

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (item != null && item.getItem() == BPItems.screwdriver) {
            if (player.isSneaking()) {
                if (logic().changeMode()) {
                    if (!getWorld().isRemote) {
                        for (IGateConnection c : getConnections())
                            if (c != null)
                                c.notifyUpdate();
                        // FIXME WireCommons.refreshConnectionsRedstone(this);
                        sendUpdateIfNeeded();
                    }
                    return true;
                }
                return false;
            } else {
                setRotation((getRotation() + 1) % 4);
                for (GateConnectionBase c : getConnections())
                    if (c != null)
                        c.notifyUpdate();
                // FIXME WireCommons.refreshConnectionsRedstone(this);
                sendUpdateIfNeeded();
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

    // Redstone logic and connectivity

    public GateConnectionBase getConnection(ForgeDirection side) {

        if (bottom() != null && bottom().getForgeDirection() == side)
            return bottom();

        if (top() != null && top().getForgeDirection() == side)
            return top();

        if (left() != null && left().getForgeDirection() == side)
            return left();

        if (right() != null && right().getForgeDirection() == side)
            return right();

        if (front() != null && front().getForgeDirection() == side)
            return front();

        if (back() != null && back().getForgeDirection() == side)
            return back();

        return null;
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return 0;

        return MathHelper.map(con.getRedstoneOutput() & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return 0;

        return MathHelper.map(con.getRedstoneOutput() & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return false;

        return con.isEnabled() && con.canConnectRedstone();
    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return false;

        return con.isEnabled() && con.canConnect(device);
    }

    @Override
    public void onConnect(IConnection<?> connection) {

    }

    @Override
    public void onDisconnect(IConnection<?> connection) {

        GateConnectionBase c = getConnection(connection.getSideA());
        if (c == null)
            return;
        c.setRedstonePower((byte) 0);
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return 0;

        return con.getRedstoneOutput();
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return;

        con.setRedstonePower(power);
    }

    @Override
    public void onRedstoneUpdate() {

        doLogicStuff();
    }

    @Override
    public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return false;

        return con.isEnabled() && con.canConnect(device);
    }

    @Override
    public byte[] getBundledOutput(ForgeDirection side) {

        return getBundledPower(side);
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return;

        con.setBundledPower(power);
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        GateConnectionBase con = getConnection(side);
        if (con == null)
            return new byte[16];

        return con.getBundledOutput();
    }

    @Override
    public void onBundledUpdate() {

        doLogicStuff();
    }

    @Override
    public MinecraftColor getBundledColor(ForgeDirection side) {

        return MinecraftColor.NONE;
    }

    // RS connection handling

    private RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {

        return redstoneConnections;
    }

    @Override
    public BundledConnectionCache getBundledConnectionCache() {

        return bundledConnections;
    }

    // Occlusion, selection and collision boxes

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

        boxes.add(new Vec3dCube(2 / 16D, 0, 0, 1 - 2 / 16D, 2D / 16D, 1));
        boxes.add(new Vec3dCube(0, 0, 2 / 16D, 1, 2D / 16D, 1 - 2 / 16D));
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

    // Rendering

    @SideOnly(Side.CLIENT)
    private static IIcon icon;
    @SideOnly(Side.CLIENT)
    private static IIcon iconSide;
    @SideOnly(Side.CLIENT)
    private static IIcon iconDark;

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

        renderer.renderBox(BOX, getIcon(ForgeDirection.DOWN), getIcon(ForgeDirection.UP), getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH));

        for (IGateComponent c : getComponents())
            c.renderStatic(translation, renderer, pass);

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        GL11.glPushMatrix();
        {
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

            for (IGateComponent c : getComponents()) {
                GL11.glPushMatrix();
                c.renderDynamic(translation, delta, pass);
                GL11.glPopMatrix();
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        if (this instanceof ISilkyRemovable) {
            if (this instanceof IAdvancedSilkyRemovable) {
                ((IAdvancedSilkyRemovable) this).readSilkyData(null, 0, 0, 0,
                        item.hasTagCompound() ? item.getTagCompound().getCompoundTag("tileData") : new NBTTagCompound());
            } else {
                readFromNBT(item.hasTagCompound() ? item.getTagCompound().getCompoundTag("tileData") : new NBTTagCompound());
            }
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

            if (type == ItemRenderType.INVENTORY && BluePower.proxy.isSneakingInGui()) {
                GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                if (gui != null && gui instanceof GuiContainer) {
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-45F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-210F, 1.0F, 0.0F, 0.0F);
                    GL11.glScaled(1.5, 1.5, 1.5);
                    GL11.glTranslated(-0.5, 0, 0);

                    GL11.glTranslated(0, -0.5, 0);
                    GL11.glRotated(-90, 1, 0, 0);
                    GL11.glTranslated(0, 0.5, 0);
                }
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
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister reg) {

        icon = reg.registerIcon(Refs.MODID + ":gates/gate");
        iconSide = reg.registerIcon(Refs.MODID + ":gates/side");
        iconDark = reg.registerIcon(Refs.MODID + ":gates/gate_dark");

        if (layout == null)
            layout = new Layout("/assets/" + Refs.MODID + "/textures/blocks/gates/" + getGateType());
        layout.reload();
        components.clear();
        initComponents();
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIcon(ForgeDirection face) {

        if (face == ForgeDirection.DOWN)
            return icon;
        if (face == ForgeDirection.UP)
            return icon;

        return iconSide;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getDarkTop() {

        return iconDark;
    }

    // NBT and update packets

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagCompound connections = new NBTTagCompound();
        for (GateConnectionBase c : getConnections()) {
            if (c == null)
                continue;
            NBTTagCompound data = new NBTTagCompound();
            c.writeToNBT(data);
            connections.setTag(c.getDirection().name(), data);
        }
        tag.setTag("connections", connections);

        NBTTagCompound t = new NBTTagCompound();
        int i = 0;
        for (IGateComponent c : getComponents()) {
            NBTTagCompound data = new NBTTagCompound();
            c.writeToNBT(data);
            t.setTag(i + "", data);
            i++;
        }
        tag.setTag("components", t);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagCompound connections = tag.getCompoundTag("connections");
        for (GateConnectionBase c : getConnections())
            if (c != null)
                c.readFromNBT(connections.getCompoundTag(c.getDirection().name()));

        NBTTagCompound components = tag.getCompoundTag("components");
        int i = 0;
        for (IGateComponent c : getComponents()) {
            c.readFromNBT(components.getCompoundTag(i + ""));
            i++;
        }
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);

        for (IGateComponent c : getComponents())
            c.writeData(buffer);

        for (GateConnectionBase c : getConnections())
            if (c != null)
                c.writeData(buffer);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);

        for (IGateComponent c : getComponents())
            c.readData(buffer);

        for (GateConnectionBase c : getConnections())
            if (c != null)
                c.readData(buffer);

        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    // GUIs

    protected void handleGUIServer(EntityPlayer player) {

    }

    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return null;
    }

    protected boolean hasGUI() {

        return false;
    }

    // Misc

    protected void playTickSound() {

        if (getWorld().isRemote && Config.enableGateSounds)
            getWorld().playSound(getX(), getY(), getZ(), "gui.button.press", 0.5F, 0.5F, false);

    }

    @Override
    public boolean canPlaceOnIntegratedCircuit() {

        return true;
    }

}
