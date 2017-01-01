package com.bluepowermod.part.gate.supported;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.gate.IGate;
import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.gate.ic.FakeMultipartTileIC;
import com.bluepowermod.part.wire.redstone.PartRedwireFace;
import com.bluepowermod.part.wire.redstone.PartRedwireFace.PartRedwireFaceUninsulated;
import com.bluepowermod.part.wire.redstone.WireHelper;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.raytrace.QRayTraceResult;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3dHelper;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

;

public class GateNullCell extends GateSupported<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>
implements IAdvancedSilkyRemovable, IAdvancedRedstoneConductor, IRedwire {

    private RedwireType typeA = null, typeB = null;
    private boolean bundledA = false, bundledB = false;
    private boolean inWorldA = false, inWorldB = false;
    private byte powerA, powerB;
    private boolean[] nullcells = new boolean[7];

    public GateNullCell() {

        redstoneConnections.listen();
    }

    public GateNullCell(RedwireType typeA, boolean bundledA, RedwireType typeB, boolean bundledB) {

        this.typeA = typeA;
        this.bundledA = bundledA;

        this.typeB = typeB;
        this.bundledB = bundledB;

        redstoneConnections.listen();
    }

    public RedwireType getTypeA() {

        return typeA;
    }

    public RedwireType getTypeB() {

        return typeB;
    }

    public boolean isBundledA() {

        return bundledA;
    }

    public boolean isBundledB() {

        return bundledB;
    }

    @Override
    protected String getGateType() {

        return "nullcell";
    }

    // Rendering

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, VertexBuffer buffer, int pass) {

        super.renderStatic(translation, renderer, buffer, pass);

        double height = 2 / 16D;

        TextureAtlasSprite wire = IconSupplier.wire;

        if (typeA != null) { // Flat
            renderer.setColor(WireHelper.getColorForPowerLevel(typeA, powerA));

            EnumFacing dir = EnumFacing.NORTH;
            for (int i = 0; i < getRotation(); i++)
                dir = dir.rotateAround(getFace().getOpposite().getAxis());
            dir = Vec3dHelper.toEnumFacing(VectorHelper.rotateUndo(new BlockPos(0, 0, 0).offset(dir), getFace(), Vec3dHelper.CENTER));

            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D), wire);
            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + (height / (nullcells[dir.ordinal()] ? 1 : 2)),
                    1 / 16D), wire);
            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / (nullcells[dir.getOpposite()
                                                                                                                 .ordinal()] ? 1 : 2)), 16 / 16D), wire);
        }

        if (typeB != null) { // Supported
            renderer.setColor(WireHelper.getColorForPowerLevel(typeB, powerB));

            EnumFacing dir = EnumFacing.WEST;
            for (int i = 0; i < getRotation(); i++)
                dir = dir.rotateAround(getFace().getOpposite().getAxis());
            dir = Vec3dHelper.toEnumFacing(VectorHelper.rotateUndo(new BlockPos(0, 0, 0).offset(dir), getFace(), Vec3dHelper.CENTER));

            if (!nullcells[dir.ordinal()])
                renderer.renderBox(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 10 / 16D, 9 / 16D), wire);
            if (!nullcells[dir.getOpposite().ordinal()])
                renderer.renderBox(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 10 / 16D, 9 / 16D), wire);
            renderer.renderBox(new Vec3dCube(0 / 16D, 10 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D), wire);
        }

        renderer.setColor(0xFFFFFF);

        renderer.resetTransformations();
        return true;
    }

    // Wire types and combinations

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> l = new ArrayList<ItemStack>();

        l.add(getStackWithData(new GateNullCell(inWorldA ? null : typeA, inWorldA ? false : bundledA, inWorldB ? null : typeB,
                inWorldB ? false : bundledB)));
        if (inWorldA && typeA != null)
            l.add(typeA.getPartInfo(MinecraftColor.NONE, bundledA).getStack());
        if (inWorldB && typeB != null)
            l.add(typeB.getPartInfo(MinecraftColor.NONE, bundledB).getStack());

        return l;
    }

    @Override
    public ItemStack getPickedItem(QRayTraceResult mop) {

        if (BluePower.proxy.getPlayer().isSneaking()) {
            return getStackWithData(new GateNullCell(null, false, null, false));
        } else {
            return getStackWithData(this);
        }
    }

    @Override
    public void addTooltip(ItemStack item, List<String> tip) {

        try {
            GateNullCell gnc = (GateNullCell) ((ItemPart) item.getItem()).createPart(item, BluePower.proxy.getPlayer(), null, null);

            tip.add(gnc.typeA + (gnc.bundledA ? " (bundled)" : "") + " x " + gnc.typeB + (gnc.bundledB ? " (bundled)" : ""));

            if (gnc.bundledA || gnc.bundledB)
                tip.add(MinecraftColor.RED + "Disabled temporarily. Still not fully working.");
        } catch (Exception ex) {
        }
    }

    @Override
    public List<ItemStack> getSubItems() {

        List<ItemStack> l = new ArrayList<ItemStack>();

        l.addAll(super.getSubItems());
        for (int i = 0; i < 2; i++)
            for (RedwireType t : RedwireType.values())
                l.add(getStackWithData(new GateNullCell(t, i == 1, t, i == 1)));

        return l;
    }

    public static ItemStack getStackWithData(GateNullCell gate) {

        ItemStack is = PartManager.getPartInfo("nullcell").getStack();
        if (is.getTagCompound() == null)
            is.setTagCompound(new NBTTagCompound());

        NBTTagCompound tag = new NBTTagCompound();
        gate.writeToNBT(tag);
        is.getTagCompound().setTag("tileData", tag);
        is.getTagCompound().setBoolean("hideSilkyTooltip", true);

        return is;
    }

    @Override
    public boolean preSilkyRemoval(World world, BlockPos pos) {
        return true;
    }


    @Override
    public void postSilkyRemoval(World world, BlockPos pos) {

    }

    @Override
    public boolean writeSilkyData(World world, BlockPos pos, NBTTagCompound tag) {

        if (typeA != null) {
            tag.setInteger("typeA", typeA.ordinal());
            tag.setBoolean("bundledA", bundledA);
        }
        if (typeB != null) {
            tag.setInteger("typeB", typeB.ordinal());
            tag.setBoolean("bundledB", bundledB);
        }

        return true;
    }

    @Override
    public void readSilkyData(World world,  BlockPos pos, NBTTagCompound tag) {

        if (tag.hasKey("typeA")) {
            typeA = RedwireType.values()[tag.getInteger("typeA")];
            bundledA = tag.getBoolean("bundledA");
        } else {
            typeA = null;
            bundledA = false;
        }
        if (tag.hasKey("typeB")) {
            typeB = RedwireType.values()[tag.getInteger("typeB")];
            bundledB = tag.getBoolean("bundledB");
        } else {
            typeB = null;
            bundledB = false;
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        if (typeA != null) {
            tag.setInteger("typeA", typeA.ordinal());
            tag.setBoolean("bundledA", bundledA);
            tag.setBoolean("inWorldA", inWorldA);
        }
        if (typeB != null) {
            tag.setInteger("typeB", typeB.ordinal());
            tag.setBoolean("bundledB", bundledB);
            tag.setBoolean("inWorldB", inWorldB);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        if (tag.hasKey("typeA")) {
            typeA = RedwireType.values()[tag.getInteger("typeA")];
            bundledA = tag.getBoolean("bundledA");
            inWorldA = tag.getBoolean("inWorldA");
        } else {
            typeA = null;
            bundledA = false;
            inWorldA = false;
        }
        if (tag.hasKey("typeB")) {
            typeB = RedwireType.values()[tag.getInteger("typeB")];
            bundledB = tag.getBoolean("bundledB");
            inWorldB = tag.getBoolean("inWorldB");
        } else {
            typeB = null;
            bundledB = false;
            inWorldB = false;
        }
    }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        super.writeUpdateData(buffer, channel);

        if (channel == -1) {
            buffer.writeBoolean(typeA != null);
            if (typeA != null) {
                buffer.writeInt(typeA.ordinal());
                buffer.writeBoolean(bundledA);
            }
            buffer.writeBoolean(typeB != null);
            if (typeB != null) {
                buffer.writeInt(typeB.ordinal());
                buffer.writeBoolean(bundledB);
            }
        }

        if (channel == 1 || channel == -1) {
            buffer.writeByte(powerA);
        }
        if (channel == 2 || channel == -1) {
            buffer.writeByte(powerB);
        }

        if (channel == 3 || channel == -1) {
            for (EnumFacing d : EnumFacing.VALUES) {
                IConnection<IRedstoneDevice> c = redstoneConnections.getConnectionOnSide(d);
                buffer.writeBoolean(c != null && c.getB() instanceof GateNullCell
                        && (getType(d) == 1 || ((BPPartFaceRotate) c.getB()).getRotation() % 2 == getRotation() % 2));
            }
        }
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);

        if (channel == -1) {
            if (buffer.readBoolean()) {
                typeA = RedwireType.values()[buffer.readInt()];
                bundledA = buffer.readBoolean();
            } else {
                typeA = null;
                bundledA = false;
            }
            if (buffer.readBoolean()) {
                typeB = RedwireType.values()[buffer.readInt()];
                bundledB = buffer.readBoolean();
            } else {
                typeB = null;
                bundledB = false;
            }
        }
        if (channel == 1 || channel == -1) {
            powerA = buffer.readByte();
        }
        if (channel == 2 || channel == -1) {
            powerB = buffer.readByte();
        }

        if (channel == 3 || channel == -1)
            for (int i = 0; i < 6; i++)
                nullcells[i] = buffer.readBoolean();

        if (getParent() != null && getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    // Connectivity and propagation

    @Override
    public void onUpdate() {

        // Don't to anything if propagation-related stuff is going on
        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;

        // Do not do anything if we're on the client
        if (getWorld().isRemote)
            return;

        // Refresh connections
        redstoneConnections.recalculateConnections();
        // bundledConnections.recalculateConnections();

        EnumFacing d1 = null;
        EnumFacing d2 = null;
        for (EnumFacing d : EnumFacing.VALUES) {
            if (d != getFace() && d != getFace().getOpposite()) {
                if (d1 == null)
                    d1 = d;
                else if (d2 == null && d != d1.getOpposite())
                    d2 = d;
            }
        }
        RedstoneApi.getInstance().getRedstonePropagator(this, d1).propagate();
        RedstoneApi.getInstance().getRedstonePropagator(this, d2).propagate();
    }

    private RedstoneConnectionCache redstoneConnections = new RedstoneConnectionCache(this);
    private boolean updatedA = false;
    private boolean updatedB = false;

    @Override
    public boolean canConnect(EnumFacing side, IRedstoneDevice device, ConnectionType type) {

        if (type == ConnectionType.OPEN_CORNER && device instanceof IGate<?, ?, ?, ?, ?, ?>)
            return false;

        if (type == ConnectionType.STRAIGHT)
            if ((side == getFace().getOpposite() || side == null) && device instanceof DummyRedstoneDevice)
                return false;
        if (type == ConnectionType.CLOSED_CORNER) {
            if (side == getFace())
                return false;
            if (side == getFace().getOpposite())
                return false;
            if (side == null)
                return false;
        }

        if (device instanceof IRedwire) {
            RedwireType rwt = getRedwireType(side);
            if (rwt == null)
                return false;
            RedwireType rwt_ = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
            if (rwt_ == null)
                return false;

            return rwt.canConnectTo(rwt_);
        }

        return true;
    }

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {

        return redstoneConnections;
    }

    @Override
    public void setRedstonePower(EnumFacing side, byte power) {

        int type = getType(side);

        if (type == 1 && typeA != null) {
            updatedA |= powerA != power;
            powerA = power;
        }
        if (type == 2 && typeB != null) {
            updatedB |= powerB != power;
            powerB = power;
        }
        // powerA = power;
        // powerB = power;
        // updatedA = true;
        // updatedB = true;
        // sendUpdatePacket(-1);
    }

    @Override
    public byte getRedstonePower(EnumFacing side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
            return 0;

        int type = getType(side);

        if (type == 1 && typeA != null)
            return (byte) (typeA.isAnalogue() ? ((powerA & 0xFF) > 0 ? 255 : 0) : powerA);
        if (type == 2 && typeB != null)
            return (byte) (typeB.isAnalogue() ? ((powerB & 0xFF) > 0 ? 255 : 0) : powerB);

        return 0;
    }

    @Override
    public void onRedstoneUpdate() {

        if (updatedA) {
            sendUpdatePacket(1);
            updatedA = false;
        }
        if (updatedB) {
            sendUpdatePacket(2);
            updatedB = false;
        }
    }

    private int getType(EnumFacing side) {

        EnumFacing d = Vec3dHelper.toEnumFacing(Vec3dHelper.rotate(VectorHelper.rotateUndo(new BlockPos(0, 0, 0).offset(side), getFace(), new Vec3d(0, 0, 0)), 0, 90 * -getRotation(), 0));
        if (d == EnumFacing.NORTH || d == EnumFacing.SOUTH)
            return 1;
        if (d == EnumFacing.WEST || d == EnumFacing.EAST)
            return 2;
        return 0;
    }

    @Override
    public RedwireType getRedwireType(EnumFacing side) {

        int type = getType(side);

        return type == 0 ? null : (type == 1 ? typeA : typeB);
    }

    @Override
    public boolean hasLoss(EnumFacing side) {

        int type = getType(side);

        if (type == 1 && typeA != null)
            return typeA.hasLoss();
        if (type == 2 && typeB != null)
            return typeB.hasLoss();

        return false;
    }

    @Override
    public boolean isAnalogue(EnumFacing side) {

        int type = getType(side);

        if (type == 1 && typeA != null)
            return typeA.isAnalogue();
        if (type == 2 && typeB != null)
            return typeB.isAnalogue();

        return false;
    }

    @Override
    public boolean canPropagateFrom(EnumFacing fromSide) {

        return fromSide != getFace() && fromSide != getFace().getOpposite();
    }

    @Override
    public Collection<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(EnumFacing fromSide) {

        if (getParent() instanceof FakeMultipartTileIC)
            ((FakeMultipartTileIC) getParent()).getIC().loadWorld();

        List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

        // // System.out.println("Propagating at (" + getX() + " " + getY() + " " + getZ() + ")");
        // for (EnumFacing d : EnumFacing.VALUES) {
        // IConnection<IRedstoneDevice> c = redstoneConnections.getConnectionOnSide(d);
        // if (c != null) {
        // l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c, c.getB() instanceof IRedwire
        // && ((IRedwire) c.getB()).getRedwireType(c.getSideB()) != getRedwireType(c.getSideA())));
        // // System.out.println(" - " + c.getA() + "  " + c.getB());
        // }
        // }

        if (!canPropagateFrom(fromSide))
            return l;

        IConnection<IRedstoneDevice> c1 = getRedstoneConnectionCache().getConnectionOnSide(fromSide);
        IConnection<IRedstoneDevice> c2 = getRedstoneConnectionCache().getConnectionOnSide(fromSide.getOpposite());

        if (c1 != null)
            l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c1, false));
        if (c2 != null)
            l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c2, false));

        return l;
    }

    @Override
    public void onConnect(IConnection<?> connection) {

        sendUpdatePacket(3);
    }

    @Override
    public void onDisconnect(IConnection<?> connection) {

        sendUpdatePacket(3);
    }

    // Placement disabling

    @Override
    public IPartPlacement getPlacement(IPart part, World world, BlockPos location, EnumFacing face, RayTraceResult mop,
            EntityPlayer player) {

        if (bundledA || bundledB)
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    // Collision/selection boxes

    @Override
    protected void addBoxes(List<Vec3dCube> boxes) {

        super.addBoxes(boxes);

        double height = 2 / 16D;

        if (typeA != null) {
            boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D));
            boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + (height / 2), 1 / 16D));
            boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / 2), 16 / 16D));
        }

        if (typeB != null) {
            boxes.add(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 12 / 16D, 9 / 16D));
            boxes.add(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D));
            boxes.add(new Vec3dCube(2 / 16D, 10 / 16D, 7 / 16D, 14 / 16D, 12 / 16D, 9 / 16D));
        }
    }

    @Override
    public QRayTraceResult rayTrace(Vec3d start, Vec3d end) {

        QRayTraceResult mop = super.rayTrace(start, end);

        // EntityPlayer player = BluePower.proxy.getPlayer();

        // if (mop != null
        // && (player == null || (player != null && player.getHeldItemMainhand() != null && !(player.getHeldItemMainhand()
        // .getItem() instanceof IScrewdriver))))
        if (mop != null)
            mop = new QRayTraceResult(mop, mop.getPart(), Vec3dCube.merge(getSelectionBoxes()));

        return mop;
    }

    @Override
    public boolean drawHighlight(QRayTraceResult mop, EntityPlayer player, float frame) {

        Vec3d hit = VectorHelper.rotateUndo(new BlockPos(mop.hitVec).subtract(mop.getBlockPos()), getFace(), Vec3dHelper.CENTER);
        BlockPos pos = player.getPosition();

        ItemStack held = player.getHeldItemMainhand();
        if (held == null)
            return false;
        if (held.getItem() instanceof ItemPart) {
            IPart part = ((ItemPart) held.getItem()).createPart(held, player, null, null);
            if (part == null)
                return false;
            if (!(part instanceof PartRedwireFaceUninsulated))
                return false;
            PartRedwireFace wire = (PartRedwireFace) part;

            RenderHelper renderer = RenderHelper.instance;
            renderer.fullReset();
            renderer.setRenderCoords(getWorld(), getPos().getX(), getPos().getY(), getPos().getZ());

            double height = 2 / 16D;

            TextureAtlasSprite wireIcon = IconSupplier.wire;

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            Tessellator.getInstance().getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            Tessellator.getInstance().getBuffer().setTranslation((float) -pos.getX(), (float) -pos.getY(), (float) -pos.getZ());
            {
                switch (getFace()) {
                case DOWN:
                    break;
                case UP:
                    renderer.addTransformation(new Rotation(180, 180, 0, Vec3dHelper.CENTER));
                    break;
                case NORTH:
                    renderer.addTransformation(new Rotation(90, 0, 0, Vec3dHelper.CENTER));
                    break;
                case SOUTH:
                    renderer.addTransformation(new Rotation(-90, 0, 0, Vec3dHelper.CENTER));
                    break;
                case WEST:
                    renderer.addTransformation(new Rotation(0, 0, -90, Vec3dHelper.CENTER));
                    break;
                case EAST:
                    renderer.addTransformation(new Rotation(0, 0, 90, Vec3dHelper.CENTER));
                    break;
                default:
                    break;
                }

                int rotation = getRotation();
                if (rotation != -1)
                    renderer.addTransformation(new Rotation(0, 90 * -rotation, 0));

                renderer.setOpacity(0.5);
                renderer.setColor(WireHelper.getColorForPowerLevel(wire.getRedwireType(null), (byte) (255 / 2)));

                EnumFacing dir = EnumFacing.NORTH;
                if (getRotation() % 2 == 1)
                    dir = dir.rotateAround(getFace().getAxis());

                if (hit.yCoord > 2 / 16D) {
                    if (typeB == null) {
                        renderer.renderBox(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 10 / 16D, 9 / 16D), wireIcon);
                        renderer.renderBox(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 10 / 16D, 9 / 16D), wireIcon);
                        renderer.renderBox(new Vec3dCube(0 / 16D, 10 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D), wireIcon);
                    }
                } else {
                    if (typeA == null)
                        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + height, 16 / 16D), wireIcon);
                }

                renderer.fullReset();
            }
            Tessellator.getInstance().getBuffer().setTranslation((float) pos.getX(), (float) pos.getY(), (float) pos.getZ());
            Tessellator.getInstance().draw();

            GL11.glDisable(GL11.GL_BLEND);

            return true;
        } else if (held.getItem() instanceof IScrewdriver) {
            // List<Vec3dCube> l = new ArrayList<Vec3dCube>();
            // super.addBoxes(l);
            // boolean def = false;
            // for (Vec3dCube c : l)
            // if (mop.getCube().equals(c.clone().rotate(getFace(), Vec3dHelper.CENTER).rotate(0, 90 * -getRotation(), 0, Vec3dHelper.CENTER)))
            // def = true;
            // if (def || hit.getY() <= 2 / 16D) {
            // Vec3dCube c = Vec3dCube.merge(getSelectionBoxes()).expand(0.001);
            //
            // GL11.glEnable(GL11.GL_BLEND);
            // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            // GL11.glDisable(GL11.GL_TEXTURE_2D);
            // GL11.glColor4f(0, 0, 0, 0.4F);
            // GL11.glLineWidth(2);
            // GL11.glDepthMask(true);
            // GL11.glPushMatrix();
            //
            // Tessellator var2 = Tessellator.instance;
            // var2.startDrawing(3);
            // Tessellator.instance.addTranslation((float) -pos.xCoord + getX(), (float) -pos.yCoord + getY(), (float) -pos.zCoord
            // + getZ());
            // var2.addVertex(c.getMinX(), c.getMinY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMinY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMinY(), c.getMaxZ());
            // var2.addVertex(c.getMinX(), c.getMinY(), c.getMaxZ());
            // var2.addVertex(c.getMinX(), c.getMinY(), c.getMinZ());
            // var2.draw();
            // var2.startDrawing(3);
            // var2.addVertex(c.getMinX(), c.getMaxY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMaxY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMaxY(), c.getMaxZ());
            // var2.addVertex(c.getMinX(), c.getMaxY(), c.getMaxZ());
            // var2.addVertex(c.getMinX(), c.getMaxY(), c.getMinZ());
            // var2.draw();
            // var2.startDrawing(1);
            // var2.addVertex(c.getMinX(), c.getMinY(), c.getMinZ());
            // var2.addVertex(c.getMinX(), c.getMaxY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMinY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMaxY(), c.getMinZ());
            // var2.addVertex(c.getMaxX(), c.getMinY(), c.getMaxZ());
            // var2.addVertex(c.getMaxX(), c.getMaxY(), c.getMaxZ());
            // var2.addVertex(c.getMinX(), c.getMinY(), c.getMaxZ());
            // var2.addVertex(c.getMinX(), c.getMaxY(), c.getMaxZ());
            // Tessellator.instance.addTranslation((float) pos.xCoord - getX(), (float) pos.yCoord - getY(), (float) pos.zCoord - getZ());
            // var2.draw();
            //
            // GL11.glPopMatrix();
            // GL11.glDepthMask(false);
            // GL11.glEnable(GL11.GL_TEXTURE_2D);
            // GL11.glDisable(GL11.GL_BLEND);
            //
            // return true;
            // }
            //
            // return true;
        }

        return false;
    }

    // In-world customization

    @Override
    public boolean onActivated(EntityPlayer player, QRayTraceResult mop, ItemStack item) {

        Vec3d hit = VectorHelper.rotateUndo(new BlockPos(mop.hitVec).subtract(mop.getBlockPos()), getFace(), Vec3dHelper.CENTER);

        if (item != null) {
            if (item.getItem() instanceof ItemPart) {
                IPart part = ((ItemPart) item.getItem()).createPart(item, player, null, null);
                if (part != null && part instanceof PartRedwireFaceUninsulated) {
                    PartRedwireFace wire = (PartRedwireFace) part;

                    if (hit.yCoord > 2 / 16D) {
                        if (typeB == null) {
                            if (getWorld().isRemote)
                                return true;

                            typeB = wire.getRedwireType(null);
                            bundledB = false;
                            inWorldB = true;

                            getRedstoneConnectionCache().recalculateConnections();

                            sendUpdatePacket();

                            if (!player.capabilities.isCreativeMode)
                                item.setCount(item.getCount() - 1);
                            return true;
                        }
                    } else {
                        if (typeA == null) {
                            if (getWorld().isRemote)
                                return true;

                            typeA = wire.getRedwireType(null);
                            bundledA = false;
                            inWorldA = true;

                            getRedstoneConnectionCache().recalculateConnections();

                            sendUpdatePacket();

                            if (!player.capabilities.isCreativeMode)
                                item.setCount(item.getCount() - 1);
                            return true;
                        }
                    }
                }
            } else if (item.getItem() instanceof IScrewdriver && player.isSneaking()) {
                if (hit.yCoord > 2 / 16D
                        && ((hit.yCoord <= 4 / 16D && hit.xCoord > 0.5 - 1 / 16D && hit.xCoord > 0.5 + 1 / 16D) || hit.yCoord > 4 / 16D)) {
                    if (typeB != null) {
                        if (getWorld().isRemote)
                            return true;

                        IOHelper.spawnItemInWorld(getWorld(), typeB.getPartInfo(MinecraftColor.NONE, bundledB).getStack(), getPos().add(0.5,0.5,0.5));

                        typeB = null;
                        bundledB = false;
                        inWorldB = false;

                        ((IScrewdriver) item.getItem()).damage(item, 1, player, false);

                        getRedstoneConnectionCache().recalculateConnections();

                        sendUpdatePacket();
                        return true;
                    }
                } else if (hit.yCoord > 2 / 16D) {
                    if (typeA != null) {
                        if (getWorld().isRemote)
                            return true;

                        IOHelper.spawnItemInWorld(getWorld(), typeA.getPartInfo(MinecraftColor.NONE, bundledA).getStack(), getPos().add(0.5,0.5,0.5));

                        typeA = null;
                        bundledA = false;
                        inWorldA = false;

                        ((IScrewdriver) item.getItem()).damage(item, 1, player, false);

                        getRedstoneConnectionCache().recalculateConnections();

                        sendUpdatePacket();
                        return true;
                    }
                }
            }
        }

        return super.onActivated(player, mop, item);
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof GateNullCell))
            return false;

        if (getParent() == null && getWorld() == null) {
            GateNullCell g = (GateNullCell) obj;
            return g.typeA == typeA && g.bundledA == bundledA && g.typeB == typeB && g.bundledB == bundledB;
        }

        return super.equals(obj);
    }

}
