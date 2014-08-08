package com.bluepowermod.part.cable.bluestone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluestone.IBluestoneWire;
import com.bluepowermod.api.helper.RedstoneHelper;
import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.ICableSize;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;
import com.bluepowermod.compat.fmp.MultipartBPPart;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.part.cable.CableWall;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;

public class WireBluestone extends CableWall implements IBluestoneWire, ICableSize {

    protected static Vector3Cube SELECTION_BOX = new Vector3Cube(0, 0, 0, 1, 1 / 16D, 1);
    protected static Vector3Cube OCCLUSION_BOX = new Vector3Cube(1 / 8D, 0, 1 / 8D, 15 / 16D, 1 / 8D, 7 / 8D);

    private static ResourceLocation textureOn = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOn.png");
    private static ResourceLocation textureOff = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOff.png");

    private int power = 0;
    private int powerSelf = 0;

    private boolean isSamplePart = false;
    private boolean shouldUpdate;

    @Override
    public String getType() {

        return "bluestoneWire";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestoneWire";
    }

    @Override
    public int getRotation() {

        return 0;
    }

    @Override
    public boolean canConnectToCable(CableWall cable) {

        return cable != null && cable instanceof WireBluestone;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        if (Loader.isModLoaded(Dependencies.FMP))
            if (location.hasTileEntity() && isFMPTile(location.getTileEntity()) && !canConnectToTileEntityFMP(location.getTileEntity()))
                return false;

        boolean cc = BPApi.getInstance().getBluestoneApi().canConnect(location, this, loc.getDirectionTo(location));
        try {
            return cc
                    || block.canConnectRedstone(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(),
                            ForgeDirectionUtils.getSide(loc.getDirectionTo(location)));
        } catch (Exception ex) {
        }
        return cc;
    }

    @Override
    public boolean canConnectToTileEntity(TileEntity tile) {

        return false;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private boolean isFMPTile(TileEntity tile) {

        return tile instanceof TileMultipart;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private boolean canConnectToTileEntityFMP(TileEntity tile) {

        // ForgeDirection dir = new Vector3(tile).getDirectionTo(loc);

        return false;
    }

    @Optional.Method(modid = Dependencies.FMP)
    public static TMultiPart getFMPPartOnSide(IBluestoneWire wire, ForgeDirection dir) {

        Vector3 vec = wire.getLocation().getRelative(dir);
        TileMultipart te = null;

        // Check for parts in the same block
        if (wire.getLocation().getTileEntity() instanceof TileMultipart) {
            te = (TileMultipart) wire.getLocation().getTileEntity();
            List<TMultiPart> l = te.jPartList();
            for (TMultiPart p : l) {
                if (p instanceof IFaceRedstonePart) {
                    ForgeDirection d = dir;
                    if (p instanceof MultipartBPPart) {
                        if (((MultipartBPPart) p).getPart() instanceof WireBluestone)
                            continue;
                        d = d.getOpposite();
                    }
                    if (ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()) == d)
                        return p;
                }

            }
        }

        ForgeDirection dir2 = ForgeDirection.getOrientation(wire.getFace());

        // Check for parts next to this one
        if (vec.hasTileEntity() && vec.getTileEntity() instanceof TileMultipart) {
            te = ((TileMultipart) vec.getTileEntity());
            List<TMultiPart> l = te.jPartList();
            for (TMultiPart p : l)
                if (p instanceof IFaceRedstonePart) {
                    ForgeDirection d = dir2;
                    if (p instanceof MultipartBPPart) {
                        if (((MultipartBPPart) p).getPart() instanceof WireBluestone)
                            continue;
                        d = d.getOpposite();
                    }
                    if (ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()) == d)
                        return p;
                }
        }

        return null;
    }

    @Override
    protected TMultiPart getPartOnSide(ForgeDirection dir) {

        return getFMPPartOnSide(this, dir);
    }

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(SELECTION_BOX.clone().toAABB());
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(OCCLUSION_BOX.clone().toAABB());
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerCircuits;
    }

    @Override
    public boolean renderStatic(Vector3 loc, int pass) {

        Tessellator t = Tessellator.instance;
        t.draw();

        GL11.glPushMatrix();
        {
            rotateAndTranslateDynamic(loc, pass, 0);

            if (power > 0)
                Minecraft.getMinecraft().renderEngine.bindTexture(textureOn);
            else
                Minecraft.getMinecraft().renderEngine.bindTexture(textureOff);

            // Render center
            renderBox(7, 0, 7, 9, 1, 9);

            int[] sides = new int[] { 0, 0, 0, 0 };

            ForgeDirection f = ForgeDirection.getOrientation(getFace());
            int id = 0;
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == f || d == f.getOpposite())
                    continue;

                Vector3 v = (Vector3) connections[ForgeDirectionUtils.getSide(d)];
                if (v == null) {
                    id++;
                    continue;
                }

                int len = 0;

                if (!isSamplePart)
                    len = BPApi.getInstance().getBluestoneApi().getExtraLength(v, this, d);
                int val = 1 + (v.distanceTo(this.loc) > 1 ? 1 : len);

                if (!isSamplePart) {
                    GL11.glPushMatrix();
                    {
                        int times = 0;
                        switch (id) {
                        case 0:
                            times = 1;
                            break;
                        case 1:
                            times = 3;
                            break;
                        case 2:
                            times = 2;
                            break;
                        case 3:
                            break;
                        }
                        GL11.glTranslated(0.5, 0.5, 0.5);
                        GL11.glRotated(90 * times, 0, 1, 0);
                        GL11.glTranslated(-0.5, -0.5, -0.5);
                        BPApi.getInstance().getBluestoneApi().renderExtraCables(v, this, d);
                    }
                    GL11.glPopMatrix();
                }

                switch (f) {
                case UP:
                    switch (d) {
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    case NORTH:
                        sides[0] = val;
                        break;
                    case SOUTH:
                        sides[1] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case DOWN:
                    switch (d) {
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    case NORTH:
                        sides[1] = val;
                        break;
                    case SOUTH:
                        sides[0] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case EAST:
                    switch (d) {
                    case UP:
                        sides[3] = val;
                        break;
                    case DOWN:
                        sides[2] = val;
                        break;
                    case NORTH:
                        sides[0] = val;
                        break;
                    case SOUTH:
                        sides[1] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case WEST:
                    switch (d) {
                    case UP:
                        sides[2] = val;
                        break;
                    case DOWN:
                        sides[3] = val;
                        break;
                    case NORTH:
                        sides[0] = val;
                        break;
                    case SOUTH:
                        sides[1] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case NORTH:
                    switch (d) {
                    case UP:
                        sides[0] = val;
                        break;
                    case DOWN:
                        sides[1] = val;
                        break;
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                case SOUTH:
                    switch (d) {
                    case UP:
                        sides[1] = val;
                        break;
                    case DOWN:
                        sides[0] = val;
                        break;
                    case EAST:
                        sides[3] = val;
                        break;
                    case WEST:
                        sides[2] = val;
                        break;
                    default:
                        break;
                    }
                    break;
                default:
                    break;
                }
                id++;
            }

            if (sides[3] > 0)// East
                renderBox(0 - (sides[3] - 1), 0, 7, 7, 1, 9);
            if (sides[2] > 0)// West
                renderBox(9, 0, 7, 16 + (sides[2] - 1), 1, 9);
            if (sides[1] > 0)// South
                renderBox(7, 0, 0 - (sides[1] - 1), 9, 1, 7);
            if (sides[0] > 0)// North
                renderBox(7, 0, 9, 9, 1, 16 + (sides[0] - 1));
        }
        GL11.glPopMatrix();

        t.startDrawingQuads();

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        isSamplePart = true;

        if (connections[ForgeDirectionUtils.getSide(ForgeDirection.EAST)] == null) {
            loc = new Vector3(0, -1, 0);
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN)
                    continue;
                connections[ForgeDirectionUtils.getSide(d)] = new Vector3(0, 0, 0).add(d);
            }
        }
        GL11.glPushMatrix();
        {
            switch (type) {
            case ENTITY:
                if (item.getItemFrame() != null) {
                    GL11.glTranslated(0.5, 0.5, 0.5);
                    GL11.glRotated(90, 0, 0, -1);
                    GL11.glTranslated(-0.375, -1, -0.5);
                }
                break;
            case EQUIPPED:
                GL11.glTranslated(0, -0.5, 0);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(-0.5, 0, 0);
                break;
            case INVENTORY:
                GL11.glTranslated(0, -0.625, 0);
                GL11.glScaled(1.125, 1.125, 1.125);
                break;
            default:
                break;
            }
            Tessellator.instance.startDrawingQuads();
            renderStatic(new Vector3(0, 0, 0), 0);
            Tessellator.instance.draw();
        }
        GL11.glPopMatrix();
    }

    private void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz) {

        BPApi.getInstance().getBluestoneApi().renderBox(minx, miny, minz, maxx, maxy, maxz);
    }

    public void propagate(List<WireBluestone> wires, int[] power) {

        if (getWorld() == null)
            return;

        wires.add(this);

        power[0] = Math.max(power[0], powerSelf);

        for (Object o : connections) {
            if (wires.contains(o))
                continue;
            if (o instanceof WireBluestone)
                ((WireBluestone) o).propagate(wires, power);
        }
    }

    public void propagate(WireBluestone... extras) {

        if (getWorld() == null)
            return;

        if (!getWorld().isRemote) {
            int oldPS = powerSelf;

            powerSelf = 0;
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == ForgeDirection.getOrientation(getFace()) || d == ForgeDirection.getOrientation(getFace()).getOpposite())
                    continue;
                if (!(new Vector3(getX(), getY(), getZ(), getWorld()).add(d).getBlock() instanceof BlockRedstoneWire)
                        && !(connections[ForgeDirectionUtils.getSide(d)] != null && connections[ForgeDirectionUtils.getSide(d)] instanceof WireBluestone))
                    powerSelf = Math.max(powerSelf,
                            RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d, ForgeDirection.getOrientation(getFace())));
            }

            if (powerSelf != oldPS || shouldUpdate) {
                List<WireBluestone> l = new ArrayList<WireBluestone>();
                int[] power = new int[1];
                power[0] = 0;
                propagate(l, power);
                for (WireBluestone w : extras) {
                    w.propagate(l, power);
                    if (!l.contains(w))
                        l.add(w);
                }
                for (WireBluestone w : l) {
                    w.power = power[0];
                    w.sendUpdatePacket();

                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        int val = 0;
                        if (w.isConnectedOnSide(d))
                            val = w.power;
                        RedstoneConnection c = w.getConnection(d);
                        if (c != null)
                            c.setPower(val);
                    }
                    w.getWorld().notifyBlockChange(w.getX(), w.getY(), w.getZ(), w.getWorld().getBlock(w.getX(), w.getY(), w.getZ()));
                }
            }
        }
    }

    @Override
    public void onFirstTick() {

        super.onFirstTick();

        for (FaceDirection d : FaceDirection.values()) {
            RedstoneConnection c = getConnectionOrCreate(d);
            c.enable();
            c.setOutput();
        }
    }

    @Override
    public void update() {

        super.update();

        propagate();

        shouldUpdate = false;
    }

    @Override
    public void writeUpdatePacket(NBTTagCompound tag) {

        super.writeUpdatePacket(tag);

        tag.setInteger("power", power);
    }

    @Override
    public void readUpdatePacket(NBTTagCompound tag) {

        super.readUpdatePacket(tag);

        power = tag.getInteger("power");

        onUpdate();
    }

    public int getPower() {

        return power;
    }

    @Override
    public Vector3 getLocation() {

        return loc;
    }

    @Override
    public int getCableWidth() {

        return 4;
    }

    @Override
    public int getCableHeight() {

        return 2;
    }

    @Override
    public void onConnect(Object o) {

        shouldUpdate = true;
    }

    @Override
    public void onDisconnect(Object o) {

        shouldUpdate = true;
    }

    @Override
    public void onUpdate() {

        super.onUpdate();
    }

}
