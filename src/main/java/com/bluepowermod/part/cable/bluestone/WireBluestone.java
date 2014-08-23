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
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.TMultiPart;
import codechicken.multipart.TileMultipart;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluestone.IBluestoneWire;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.helper.RedstoneHelper;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.ICableSize;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.api.part.redstone.IBPRedstonePart;
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
    protected static Vector3Cube OCCLUSION_BOX = new Vector3Cube(7 / 16D, 0 / 16D, 7 / 16D, 9 / 16D, 1 / 16D, 9 / 16D);
    protected static Vector3Cube SELECTION_BOX_INSULATED = new Vector3Cube(0, 0, 0, 1, 3 / 16D, 1);
    protected static Vector3Cube OCCLUSION_BOX_INSULATED = new Vector3Cube(7 / 16D, 0 / 16D, 7 / 16D, 9 / 16D, 3 / 16D, 9 / 16D);

    private static ResourceLocation textureOn;
    private static ResourceLocation textureOff;
    private static ResourceLocation textureInsulationFB;
    private static ResourceLocation textureInsulationLR;

    private int power = 0;
    private int powerSelf = 0;

    private static boolean updateState;

    private boolean isItemRenderer = false;

    private int color = -1;
    private String colorName = null;
    private boolean isBundled = false;

    public WireBluestone(Integer color, String colorName) {

        this(color, colorName, false);
    }

    public WireBluestone(Integer color, String colorName, Boolean bundled) {

        this.color = color.intValue();
        this.colorName = colorName;
        isBundled = bundled.booleanValue();
    }

    public WireBluestone() {

        this(-1, null);
    }

    @Override
    public String getType() {

        return "bluestoneWire" + (colorName != null ? "." + colorName : "");
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestoneWire" + (colorName != null ? "." + colorName : "");
    }

    @Override
    public int getRotation() {

        return 0;
    }

    @Override
    public boolean canConnectToCable(CableWall cable) {

        if (cable != null && cable instanceof WireBluestone) {
            WireBluestone w = (WireBluestone) cable;

            if (isBundled) {
                if (w.isBundled) {
                    if (color == w.color || color == -1 || w.color == -1)
                        return true;
                } else {
                    if (w.color != -1)
                        return true;
                }
            } else {
                if (w.isBundled) {
                    if (color != -1)
                        return true;
                } else {
                    if (color == w.color || color == -1 || w.color == -1)
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean canConnectToBlock(Block block, Vector3 location) {

        if (Loader.isModLoaded(Dependencies.FMP))
            if (location.hasTileEntity() && isFMPTile(location.getTileEntity()))
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
    private boolean isFMPPart(Object o) {

        return o != null && o instanceof TMultiPart;
    }

    @Optional.Method(modid = Dependencies.FMP)
    private int getFMPPower(Object o, ForgeDirection side) {

        TMultiPart part = (TMultiPart) o;

        if (part instanceof MultipartBPPart) {
            if (((MultipartBPPart) part).getPart() instanceof IBPRedstonePart) {
                return ((IBPRedstonePart) ((MultipartBPPart) part).getPart()).getWeakOutput(side);
            }
        }
        if (part instanceof IRedstonePart) {
            return ((IRedstonePart) part).weakPowerLevel(ForgeDirectionUtils.getSide(side));
        }

        return 0;
    }

    @Optional.Method(modid = Dependencies.FMP)
    public static TMultiPart getFMPPartOnSide(IBluestoneWire wire, ForgeDirection dir) {

        IMultipartCompat compat = BPApi.getInstance().getMultipartCompat();

        Vector3 vec = wire.getLocation().getRelative(dir);
        if (!vec.hasTileEntity())
            return null;
        if (!(vec.getTileEntity() instanceof TileMultipart))
            return null;

        TileMultipart te = (TileMultipart) wire.getLocation().getTileEntity();

        if (te == null)
            return null;

        if (compat.isOccupied(te, getStripHitboxForSide(ForgeDirection.getOrientation(wire.getFace()), dir)))
            return null;

        // Check for parts in the same block
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

        // ForgeDirection dir2 = ForgeDirection.getOrientation(wire.getFace());

        // Check for parts next to this one
        if (vec.hasTileEntity() && vec.getTileEntity() instanceof TileMultipart) {
            te = ((TileMultipart) vec.getTileEntity());
            l = te.jPartList();
            for (TMultiPart p : l)
                if (p instanceof IFaceRedstonePart) {
                    // ForgeDirection d = dir2;
                    if (p instanceof MultipartBPPart) {
                        if (((MultipartBPPart) p).getPart() instanceof WireBluestone)
                            continue;
                        // d = d.getOpposite();
                    }
                    // if ((p instanceof MultipartBPPart && ((MultipartBPPart) p).getPart() instanceof GateBase)
                    // || !compat.isOccupied(
                    // te,
                    // getStripHitboxForSide(ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()).getOpposite(),
                    // dir.getOpposite())))
                    // if (ForgeDirection.getOrientation(((IFaceRedstonePart) p).getFace()) == d)
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

        boxes.add((color == -1 ? SELECTION_BOX : SELECTION_BOX_INSULATED).clone().toAABB());
    }

    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add((color == -1 ? OCCLUSION_BOX : OCCLUSION_BOX_INSULATED).clone().toAABB());
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return CustomTabs.tabBluePowerCircuits;
    }

    @Override
    public boolean renderStatic(Vector3 loc, int pass) {

        if (pass == 0) {
            Tessellator t = Tessellator.instance;
            t.draw();

            if (textureOn == null)
                textureOn = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOn.png");
            if (textureOff == null)
                textureOff = new ResourceLocation(Refs.MODID + ":textures/base/bluestoneOff.png");
            if (textureInsulationFB == null)
                textureInsulationFB = new ResourceLocation(Refs.MODID + ":textures/blocks/bluestone/insulation_fb.png");
            if (textureInsulationLR == null)
                textureInsulationLR = new ResourceLocation(Refs.MODID + ":textures/blocks/bluestone/insulation_lr.png");

            GL11.glPushMatrix();
            {
                rotateAndTranslateDynamic(loc, pass, 0);

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

                    if (!isItemRenderer)
                        len = BPApi.getInstance().getBluestoneApi().getExtraLength(v, this, d);
                    int val = 1 + (v.distanceTo(this.loc) > 1 ? 1 : len);

                    if (!isItemRenderer) {
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

                if (power > 0)
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureOn);
                else
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureOff);

                if (color == -1) {
                    // Render center
                    renderBox(7, 0, 7, 9, 1, 9);
                }
                if (sides[3] > 0)// East
                    renderBox(0 - (sides[3] - 1), 0, 7, 7, 1, 9);
                if (sides[2] > 0)// West
                    renderBox(9, 0, 7, 16 + (sides[2] - 1), 1, 9);
                if (sides[1] > 0)// South
                    renderBox(7, 0, 0 - (sides[1] - 1), 9, 1, 7);
                if (sides[0] > 0)// North
                    renderBox(7, 0, 9, 9, 1, 16 + (sides[0] - 1));

                if (color >= 0) {
                    double r = ((color >> 16) & 0xFF) / 255D;
                    double g = ((color >> 8) & 0xFF) / 255D;
                    double b = (color & 0xFF) / 255D;
                    GL11.glColor4d((r * 0.7) + 0.1, (g * 0.7) + 0.1, (b * 0.7) + 0.1, 1);
                    Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                    renderBox(7, 0, 7, 9, 3, 9);
                    if (sides[3] > 0) {// East
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                        renderBox(0, 0, 6, 7, 3, 10);
                    } else {
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                        renderBox(6, 0, 7 - (sides[1] == 0 ? 1 : 0), 7, 3, 9 + (sides[0] == 0 ? 1 : 0));
                    }
                    if (sides[2] > 0) {// West
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                        renderBox(9, 0, 6, 16, 3, 10);
                    } else {
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                        renderBox(9, 0, 7 - (sides[1] == 0 ? 1 : 0), 10, 3, 9 + (sides[0] == 0 ? 1 : 0));
                    }
                    if (sides[1] > 0) {// South
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                        renderBox(6, 0, 0, 10, 3, 7);
                    } else {
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                        renderBox(7, 0, 6, 9, 3, 7);
                    }
                    if (sides[0] > 0) {// North
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationFB);
                        renderBox(6, 0, 9, 10, 3, 16);
                    } else {
                        Minecraft.getMinecraft().renderEngine.bindTexture(textureInsulationLR);
                        renderBox(7, 0, 9, 9, 3, 10);
                    }
                    GL11.glColor4d(1D, 1D, 1D, 1D);
                }
            }
            GL11.glPopMatrix();

            t.startDrawingQuads();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        isItemRenderer = true;

        if (connections[ForgeDirectionUtils.getSide(ForgeDirection.EAST)] == null) {
            loc = new Vector3(0, -1, 0);
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (d == ForgeDirection.UP || d == ForgeDirection.DOWN)
                    continue;
                connections[ForgeDirectionUtils.getSide(d)] = new Vector3(0, 0, 0).add(d);
            }
            power = 15;
        }
        GL11.glPushMatrix();
        {
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(180, 0, 0, -1);
            GL11.glTranslated(-0.5, -0.5, -0.5);
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
                GL11.glTranslated(-0.125, -0.625, 0);
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

    private void recalculatePower() {

        if (loc == null)
            loc = new Vector3(getX(), getY(), getZ(), getWorld());
        loc.setX(getX());
        loc.setY(getY());
        loc.setZ(getZ());
        loc.setWorld(getWorld());

        powerSelf = 0;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {

            ForgeDirection dir = d;
            if (dir == ForgeDirection.UP || dir == ForgeDirection.DOWN)
                dir = dir.getOpposite();
            if (dir == ForgeDirection.getOrientation(getFace()).getOpposite())// If it's in the opposite face, don't do anything
                continue;

            Vector3 l = null;// Get the block in that set of coords

            // If it's either disconnected on that side OR connected and it's not bluestone wire
            if (dir == ForgeDirection.getOrientation(getFace())
                    || (connections[ForgeDirectionUtils.getSide(d)] != null && !(connections[ForgeDirectionUtils.getSide(d)] instanceof WireBluestone))) {
                if (isFMPPart(connections[ForgeDirectionUtils.getSide(d)])) {
                    powerSelf = Math.max(powerSelf, getFMPPower(connections[ForgeDirectionUtils.getSide(d)], d.getOpposite()));
                } else {
                    if (connections[ForgeDirectionUtils.getSide(d)] instanceof Vector3) {
                        l = (Vector3) connections[ForgeDirectionUtils.getSide(d)];
                    } else if (connections[ForgeDirectionUtils.getSide(d)] instanceof BPPart) {
                        BPPart part = (BPPart) connections[ForgeDirectionUtils.getSide(d)];
                        l = new Vector3(part.getX(), part.getY(), part.getZ(), part.getWorld());
                    } else {
                        if (connections[ForgeDirectionUtils.getSide(d)] == null)
                            l = loc.getRelative(d);
                        else
                            continue;
                    }
                    if (l.getBlock(true) == null)
                        continue;
                    if (l.getBlock() instanceof BlockRedstoneWire)
                        continue;
                    if (l.getBlock().isOpaqueCube()) {
                        for (ForgeDirection di : ForgeDirection.VALID_DIRECTIONS) {
                            if (di == d.getOpposite())
                                continue;
                            if (BPApi.getInstance().getMultipartCompat()
                                    .getBPPartOnFace(l.getRelative(di).getTileEntity(), WireBluestone.class, di.getOpposite()) != null)
                                continue;
                            int p = RedstoneHelper.getInput(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ(), di, null, true, false,
                                    dir != ForgeDirection.getOrientation(getFace()));
                            powerSelf = Math.max(powerSelf, p);
                        }
                    } else {
                        powerSelf = Math.max(powerSelf, RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d));
                    }
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
    }

    @Override
    public void onUpdate() {

        if (!updateState) {
            updateState = true;
            super.onUpdate();

            try {
                propagate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            updateState = false;
        }
    }

    private void propagate() {

        if (getWorld() == null)
            return;
        if (getWorld().isRemote)
            return;

        List<WireBluestone> wires = new ArrayList<WireBluestone>();
        int[] power = new int[] { 0 };

        propagate(wires, power);

        for (WireBluestone wire : wires) {
            wire.power = power[0];
            wire.sendUpdatePacket();

            if (wire.hasSetFace()) {
                ForgeDirection face = ForgeDirection.getOrientation(wire.getFace());
                if (face == ForgeDirection.UP || face == ForgeDirection.DOWN)
                    face = face.getOpposite();
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    RedstoneConnection con = wire.getConnection(d);
                    if (con != null)
                        con.setPower(0, false);
                }
                for (int i = 0; i < 6; i++) {
                    Object o = wire.connections[i];
                    RedstoneConnection con = wire.getConnection(ForgeDirection.getOrientation(i));
                    if (con != null) {
                        if (o != null) {
                            if (!(o instanceof WireBluestone)) {
                                con.setPower(power[0], false);
                            } else {
                                con.setPower(0, false);
                            }
                        } else {
                            con.setPower(0, false);
                        }
                    }
                }
                if (wire.loc != null) {
                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        Vector3 v = wire.loc.getRelative(d);
                        wire.getWorld().notifyBlockChange(v.getBlockX(), v.getBlockY(), v.getBlockZ(), wire.loc.getBlock());
                        wire.getWorld().markBlockForUpdate(v.getBlockX(), v.getBlockY(), v.getBlockZ());
                    }
                }
            }
        }
    }

    private void propagate(List<WireBluestone> wires, int[] power) {

        recalculatePower();

        wires.add(this);

        power[0] = Math.max(power[0], powerSelf);

        for (int i = 0; i < 6; i++)
            if (connections[i] != null)
                if (connections[i] instanceof WireBluestone)
                    if (!wires.contains(connections[i]))
                        ((WireBluestone) connections[i]).propagate(wires, power);
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

        return 2 + (color >= 0 ? 2 : 0);
    }

    @Override
    public int getCableHeight() {

        return 1 + (color >= 0 ? 2 : 0);
    }

    @Override
    public void onConnect(Object o) {

        if (o instanceof WireBluestone)
            ((WireBluestone) o).onUpdate();
    }

    @Override
    public void onDisconnect(Object o) {

        if (o instanceof WireBluestone)
            ((WireBluestone) o).onUpdate();
    }

    @Override
    public int getRedstonePower() {

        return power;
    }

    @Override
    public int getStrongOutput(ForgeDirection side) {

        return getWeakOutput(side);
    }

    @Override
    public int getWeakOutput(ForgeDirection side) {

        return super.getWeakOutput(side);
    }

    @Override
    public float getHardness() {

        return 0.25F;
    }

    @Override
    public boolean canStay() {

        return super.canStay();
    }

}
