package com.bluepowermod.part.gate.supported;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.wire.redstone.WireCommons;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateNullCell
extends
GateSupported<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>
implements IAdvancedSilkyRemovable, IAdvancedRedstoneConductor {

    private RedwireType typeA = null, typeB = null;
    private boolean bundledA = false, bundledB = false;
    private boolean inWorldA = false, inWorldB = false;
    private byte powerA, powerB;
    private boolean[] nullcells = new boolean[6];

    public GateNullCell() {

    }

    public GateNullCell(RedwireType typeA, boolean bundledA, RedwireType typeB, boolean bundledB) {

        this.typeA = typeA;
        this.bundledA = bundledA;

        this.typeB = typeB;
        this.bundledB = bundledB;
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
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        double height = 2 / 16D;

        IIcon wire = IconSupplier.wire;

        if (typeA != null) { // Flat
            renderer.setColor(WireCommons.getColorForPowerLevel(typeA.getColor(), powerA));

            ForgeDirection dir = ForgeDirection.NORTH;
            if (getRotation() % 2 == 1)
                dir = dir.getRotation(getFace());

            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D), wire);
            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D,
                    2 / 16D + (height / /* (nullcells[dir.ordinal()] ? 1 : */2/* ) */), 1 / 16D), wire);
            renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / (nullcells[dir.getOpposite()
                                                                                                                 .ordinal()] ? 1 : 2)), 16 / 16D), wire);
        }

        if (typeB != null) { // Supported
            renderer.setColor(WireCommons.getColorForPowerLevel(typeB.getColor(), powerB));

            ForgeDirection dir2 = ForgeDirection.WEST;
            if (getRotation() % 2 == 1)
                dir2 = dir2.getRotation(getFace());

            // if (!nullcells[dir2.ordinal()])
            renderer.renderBox(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 10 / 16D, 9 / 16D), wire);
            // if (!nullcells[dir2.getOpposite().ordinal()])
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
    public ItemStack getPickedItem(QMovingObjectPosition mop) {

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
                tip.add(MinecraftColor.RED + "Disabled temporarily.");
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
    public boolean preSilkyRemoval(World world, int x, int y, int z) {

        return true;
    }

    @Override
    public void postSilkyRemoval(World world, int x, int y, int z) {

    }

    @Override
    public boolean writeSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

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
    public void readSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

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
        } else if (channel == 1) {
            buffer.writeByte(powerA);
        } else if (channel == 2) {
            buffer.writeByte(powerB);
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
        } else if (channel == 1) {
            powerA = buffer.readByte();
        } else if (channel == 2) {
            powerB = buffer.readByte();
        }

        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    // Connectivity and propagation

    private RedstoneConnectionCache redstoneConnections = new RedstoneConnectionCache(this);
    private boolean updatedA = false;
    private boolean updatedB = false;

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        if (type == ConnectionType.STRAIGHT)
            if ((side == getFace().getOpposite() || side == ForgeDirection.UNKNOWN) && device instanceof DummyRedstoneDevice)
                return false;
        if (type == ConnectionType.CLOSED_CORNER) {
            if (side == getFace())
                return false;
            if (side == getFace().getOpposite())
                return false;
            if (side == ForgeDirection.UNKNOWN)
                return false;
        }

        if (device instanceof IRedwire) {
            int t = getType(side);

            if (t == 1 && typeA != null)
                return typeA.canConnectTo(((IRedwire) device).getRedwireType());
            if (t == 2 && typeB != null)
                return typeB.canConnectTo(((IRedwire) device).getRedwireType());
        }

        return false;
    }

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {

        return redstoneConnections;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        int type = getType(side);

        if (type == 1 && typeA != null) {
            updatedA = true;
            powerA = power;
        }
        if (type == 2 && typeB != null) {
            updatedB = true;
            powerB = power;
        }
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

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
            updatedA = false;
        }
    }

    private int getType(ForgeDirection side) {

        ForgeDirection d = new Vec3d(0, 0, 0).add(side).rotateUndo(getFace(), new Vec3d(0, 0, 0)).rotate(0, 90 * -getRotation(), 0)
                .toForgeDirection();
        if (d == ForgeDirection.NORTH || d == ForgeDirection.SOUTH)
            return 1;
        if (d == ForgeDirection.WEST || d == ForgeDirection.EAST)
            return 2;
        return 0;
    }

    @Override
    public boolean hasLoss(ForgeDirection side) {

        int type = getType(side);

        if (type == 1 && typeA != null)
            return typeA.hasLoss();
        if (type == 2 && typeB != null)
            return typeB.hasLoss();

        return false;
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        int type = getType(side);

        if (type == 1 && typeA != null)
            return typeA.isAnalogue();
        if (type == 2 && typeB != null)
            return typeB.isAnalogue();

        return false;
    }

    @Override
    public boolean canPropagateFrom(ForgeDirection fromSide) {

        return fromSide != getFace() && fromSide != getFace().getOpposite();
    }

    @Override
    public Collection<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(ForgeDirection fromSide) {

        List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

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

    // Placement disabling

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        if (bundledA || bundledB)
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

}
