package com.bluepowermod.part.gate.ic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.IPartUpdateListener;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.raytrace.QRayTraceResult;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.transform.Scale;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec2d;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IGateComponent;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.gate.IIntegratedCircuitPart;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IBundledDeviceWrapper;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDeviceWrapper;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.util.DebugHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateIntegratedCircuit extends
        GateBase<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>
        implements IGateLogic<GateIntegratedCircuit>, IRedstoneDeviceWrapper, IBundledDeviceWrapper {

    private int size;
    private IIntegratedCircuitPart[][] parts = null;
    private TileMultipart[][] tiles = null;

    private double border = 1 / 16D;

    public GateIntegratedCircuit(Integer size) {

        this.size = size;
        parts = new IIntegratedCircuitPart[size][size];
        tiles = new TileMultipart[size][size];
    }

    // Gate methods

    @Override
    protected String getGateType() {

        return "integratedCircuit" + getSize() + "x" + getSize();
    }

    @Override
    protected void initConnections() {

    }

    @Override
    protected void initComponents() {

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    // Misc gate methods

    @Override
    public boolean changeMode() {

        return false;
    }

    @Override
    public IGateLogic<? extends GateBase<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>> logic() {

        return this;
    }

    @Override
    public GateIntegratedCircuit getGate() {

        return this;
    }

    // Misc IC methods

    @Override
    public boolean canPlaceOnIntegratedCircuit() {

        return false;
    }

    public int getSize() {

        return size;
    }

    public IIntegratedCircuitPart[][] getParts() {

        return parts;
    }

    public IIntegratedCircuitPart getPart(int x, int z) {

        if (x >= 0 && z >= 0 && x < getSize() && z < getSize())
            return parts[x][z];

        return null;
    }

    public TileMultipart[][] getTiles() {

        return tiles;
    }

    public TileMultipart getTile(int x, int z) {

        if (x >= 0 && z >= 0 && x < getSize() && z < getSize())
            return tiles[x][z];

        return null;
    }

    public void loadWorld() {

        FakeWorldIC.getInstance().setIC(this);
    }

    public void unloadWorld() {

        FakeWorldIC.getInstance().setIC(null);
    }

    private List<IIntegratedCircuitPart> allParts() {

        List<IIntegratedCircuitPart> l = new ArrayList<IIntegratedCircuitPart>();

        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                IIntegratedCircuitPart part = parts[x][z];
                if (part != null)
                    l.add(part);
            }
        }

        return l;
    }

    public void setPart(int x, int z, IIntegratedCircuitPart part) {

        loadWorld();

        FakeMultipartTileIC tmp = new FakeMultipartTileIC(this);
        tmp.setWorldObj(FakeWorldIC.getInstance());
        tmp.xCoord = x;
        tmp.yCoord = 64;
        tmp.zCoord = z;

        tiles[x][z] = tmp;
        parts[x][z] = part;

        if (part instanceof IPartFace)
            ((IPartFace) part).setFace(EnumFacing.DOWN);

        tmp.addPart(part);

        for (int x_ = x - 1; x_ <= x + 1; x_++) {
            if (x_ == x)
                continue;
            IPart p = getPart(x_, z);
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborBlockChange();
        }
        for (int z_ = z - 1; z_ <= z + 1; z_++) {
            if (z_ == z)
                continue;
            IPart p = getPart(x, z_);
            if (p != null && p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborBlockChange();
        }

        unloadWorld();
    }

    @Override
    public int getRotation() {

        // TODO Make this work as it actually should
        return 0;
    }

    // Logic and interaction

    @Override
    public void doLogic() {

        loadWorld();

        IPart part = getPart(0, (getSize() - 1) / 2);
        if (part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onNeighborBlockChange();

        part = getPart(getSize() - 1, (getSize() - 1) / 2);
        if (part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onNeighborBlockChange();

        part = getPart((getSize() - 1) / 2, 0);
        if (part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onNeighborBlockChange();

        part = getPart((getSize() - 1) / 2, getSize() - 1);
        if (part instanceof IPartUpdateListener)
            ((IPartUpdateListener) part).onNeighborBlockChange();

        unloadWorld();
    }

    @Override
    public void tick() {

        loadWorld();

        for (IIntegratedCircuitPart part : allParts())
            if (part instanceof IPartTicking)
                ((IPartTicking) part).update();

        unloadWorld();
    }

    @Override
    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        boxes.add(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1));
        boxes.add(new Vec3dCube(0, 1 / 16D, 0, 1, 2 / 16D, 1 / 16D));
        boxes.add(new Vec3dCube(0, 1 / 16D, 1 - 1 / 16D, 1, 2 / 16D, 1));
        boxes.add(new Vec3dCube(0, 1 / 16D, 0, 1 / 16D, 2 / 16D, 1));
        boxes.add(new Vec3dCube(1 - 1 / 16D, 1 / 16D, 0, 1, 2 / 16D, 1));
    }

    @Override
    public QRayTraceResult rayTrace(Vec3d start, Vec3d end) {

        QRayTraceResult mop = super.rayTrace(start, end);

        if (mop != null) {
            if (mop.getPart() == this)
                return new QRayTraceResult(mop, this, new Vec3dCube(0, 0, 0, 1, 2 / 16D, 1));
        }

        return mop;
    }

    @Override
    public boolean onActivated(final EntityPlayer player, QRayTraceResult hit, ItemStack item) {

        Vec2d v = new Vec2d(hit.hitVec.xCoord - hit.blockX, hit.hitVec.zCoord - hit.blockZ).sub(0.5, 0.5).rotate(90 * -getRotation())
                .add(0.5, 0.5);

        int x = (int) (v.getX() * getSize());
        int z = (int) (v.getY() * getSize());

        if (getPart(x, z) == null && item != null && item.getItem() instanceof ItemPart) {
            IPart part = ((ItemPart) item.getItem()).createPart(item, player, getWorld(), hit);
            if (part instanceof IIntegratedCircuitPart) {
                if (!getWorld().isRemote) {
                    setPart(x, z, (IIntegratedCircuitPart) part);
                    sendUpdatePacket(part, -1);
                }
                return true;
            }
        }

        return super.onActivated(player, hit, item);
    }

    // Rendering

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

        IIcon[] icons = new IIcon[] { getIcon(EnumFacing.DOWN), getIcon(EnumFacing.UP), getIcon(EnumFacing.WEST),
                getIcon(EnumFacing.EAST), getIcon(EnumFacing.NORTH), getIcon(EnumFacing.SOUTH) };

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1), icons);

        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1, 2 / 16D, 1 / 16D), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 15 / 16D, 1, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1 / 16D, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(15 / 16D, 1 / 16D, 0, 1, 2 / 16D, 1), icons);

        for (IGateComponent c : getComponents())
            c.renderStatic(translation, renderer, pass);

        double scale = (1 - border * 2D) / getSize();

        renderer.addTransformation(new Scale(scale, scale, scale));

        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                IIntegratedCircuitPart part = getPart(x, z);
                if (part == null)
                    continue;

                int s = getSize();
                if (s % 2 == 1)
                    s -= 1;

                double a = (getSize() - 1) / 2D;

                renderer.addTransformation(new Translation(-(s - x - a), -a, -(s - z - a)));
                part.renderStatic(translation, renderer, renderBlocks, pass);
                renderer.addTransformation(new Translation(s - x - a, a, s - z - a));
            }
        }

        renderer.removeTransformation();

        return true;
    }

    // NBT and update packets

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagList l = new NBTTagList();
        writeParts(l);
        tag.setTag("parts", l);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagList l = tag.getTagList("parts", new NBTTagCompound().getId());
        readParts(l);
    }

    private void writeParts(NBTTagList l) {

        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                IPart p = getPart(x, z);
                if (p == null)
                    continue;

                NBTTagCompound tag = new NBTTagCompound();

                tag.setInteger("x", x);
                tag.setInteger("z", z);
                tag.setString("type", p.getType());

                NBTTagCompound data = new NBTTagCompound();
                p.writeToNBT(data);
                tag.setTag("data", data);

                l.appendTag(tag);
            }
        }
    }

    private void readParts(NBTTagList l) {

        for (int i = 0; i < l.tagCount(); i++) {
            NBTTagCompound tag = l.getCompoundTagAt(i);

            int x = tag.getInteger("x");
            int z = tag.getInteger("z");

            IPart p = getPart(x, z);
            if (p == null) {
                p = PartRegistry.createPart(tag.getString("type"), false);
                if (p == null || !(p instanceof IIntegratedCircuitPart))
                    continue;

                setPart(x, z, (IIntegratedCircuitPart) p);
            }

            p.readFromNBT(tag.getCompoundTag("data"));
        }
    }

    private int updateChannel = -1;

    public void sendUpdatePacket(IPart part, int channel) {

        int x = 0;
        int z = 0;
        for (int x_ = 0; x_ < getSize(); x_++) {
            boolean found = false;
            for (int z_ = 0; z_ < getSize(); z_++) {
                IIntegratedCircuitPart p = getPart(x_, z_);
                if (p == part) {
                    found = true;
                    x = x_;
                    z = z_;
                    break;
                }
            }
            if (found)
                break;
        }

        int c = (z * getSize()) + x + 10;
        updateChannel = channel;

        sendUpdatePacket(c);
    }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        super.writeUpdateData(buffer, channel);

        if (channel < 10)
            return;

        int c = channel - 10;

        int x = c % getSize();
        int z = (c - x) / getSize();

        IPart p = getPart(x, z);
        if (p == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);

        buffer.writeUTF(p.getType());

        buffer.writeInt(updateChannel);

        // ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // DataOutputStream buf = new DataOutputStream(baos);
        p.writeUpdateData(buffer, updateChannel);
        //
        // byte[] data = baos.toByteArray();
        // buffer.write(data.length);
        // buffer.write(data);
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);

        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());

        if (channel < 10)
            return;

        int c = channel - 10;

        int x = c % getSize();
        int z = (c - x) / getSize();

        if (!buffer.readBoolean())
            return;

        String type = buffer.readUTF();

        IPart p = getPart(x, z);
        if (p == null)
            setPart(x, z, (IIntegratedCircuitPart) (p = PartRegistry.createPart(type, true)));
        if (p == null) {
            // buffer.readInt();
            // buffer.skipBytes(buffer.readInt());
            return;
        }

        updateChannel = buffer.readInt();

        // byte[] data = new byte[buffer.readInt()];
        // buffer.readFully(data, 0, data.length);
        // ByteArrayInputStream bais = new ByteArrayInputStream(data, 0, data.length);
        // DataInputStream buf = new DataInputStream(bais);
        p.readUpdateData(buffer, updateChannel);
    }

    // Connectivity

    public IIntegratedCircuitPart getCircuitPartOnSide(EnumFacing side) {

        if (side.offsetY != 0 || side == EnumFacing.UNKNOWN)
            return null;

        return getPart(side.offsetX == 0 ? (getSize() - 1) / 2 : (side.offsetX > 0 ? getSize() - 1 : 0),
                side.offsetZ == 0 ? (getSize() - 1) / 2 : (side.offsetZ > 0 ? getSize() - 1 : 0));
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(EnumFacing side) {

        side = new Vec3d(0, 0, 0).add(side).rotate(0, 90 * -getRotation(), 0).toEnumFacing();

        IPart p = getCircuitPartOnSide(side);
        if (p == null)
            return null;
        if (p instanceof IRedstoneDevice)
            return (IRedstoneDevice) p;
        return null;
    }

    @Override
    public IBundledDevice getBundledDeviceOnSide(EnumFacing side) {

        side = new Vec3d(0, 0, 0).add(side).rotate(0, 90 * -getRotation(), 0).toEnumFacing();

        IPart p = getCircuitPartOnSide(side);
        if (p == null)
            return null;
        if (p instanceof IBundledDevice)
            return (IBundledDevice) p;
        return null;
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, EnumFacing face, RayTraceResult mop,
            EntityPlayer player) {

        if (!DebugHelper.isDebugModeEnabled())
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    @Override
    public void addTooltip(ItemStack item, List<String> tip) {

        if (!DebugHelper.isDebugModeEnabled())
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
        else
            tip.add(MinecraftColor.CYAN + I18n.format("Disabled temporarily. Still not fully working."));
    }
}
