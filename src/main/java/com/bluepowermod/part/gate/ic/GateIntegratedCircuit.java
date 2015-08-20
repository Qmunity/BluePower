package com.bluepowermod.part.gate.ic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacementFlat;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.IPartPlacementFlat;
import uk.co.qmunity.lib.part.IPartRenderPlacement;
import uk.co.qmunity.lib.part.IPartSelectable;
import uk.co.qmunity.lib.part.IPartSelectableCustom;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.PartRegistry;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.raytrace.RayTracer;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.transform.Scale;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.util.Dir;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.gate.IGateComponent;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.gate.ic.IIntegratedCircuitPart;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.util.ItemStackUtils;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateIntegratedCircuit extends GateBase implements IGateLogic<GateIntegratedCircuit>, IPartSelectableCustom {

    public static final double border = 1 / 16D;
    public static boolean shouldActivate = false;

    private int size;
    protected TileMultipart[][] tiles = null;
    protected RSTileIC[][] rsTiles = null;
    protected int[][] modes = null;
    private boolean showBG = true;

    public GateIntegratedCircuit(Integer size) {

        this.size = size;
        tiles = new TileMultipart[size][size];
        modes = new int[4][size];

        rsTiles = new RSTileIC[4][size];
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < size; j++)
                rsTiles[i][j] = new RSTileIC(this, Dir.values()[i], j);
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

    // Gate methods

    @Override
    public boolean changeMode() {

        if (!getWorld().isRemote) {
            showBG = !showBG;
            sendUpdatePacket();
        }

        return true;
    }

    @Override
    public IGateLogic<? extends GateBase> logic() {

        return this;
    }

    @Override
    public GateIntegratedCircuit getGate() {

        return this;
    }

    private boolean propagating = false;

    @Override
    public void doLogic() {

        if (propagating)
            return;

        propagating = true;
        for (int i = 0; i < 4; i++)
            rsTiles[i][(getSize() - 1) / 2].repropagate();
        propagating = false;
    }

    @Override
    public void tick() {

        for (int x = 0; x < getSize(); x++)
            for (int z = 0; z < getSize(); z++)
                if (tiles[x][z] != null)
                    for (IPart p : tiles[x][z].getParts())
                        if (p instanceof IPartTicking)
                            ((IPartTicking) p).update();
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();

        for (int x = 0; x < getSize(); x++)
            for (int z = 0; z < getSize(); z++)
                if (tiles[x][z] != null)
                    for (IPart p : tiles[x][z].getParts())
                        drops.addAll(p.getDrops());

        return drops;
    }

    // Misc IC methods

    @Override
    public boolean canPlaceOnIntegratedCircuit() {

        return false;
    }

    public int getSize() {

        return size;
    }

    @Override
    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        if (showBG)
            boxes.add(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1));
        else
            boxes.add(new Vec3dCube(0, 0, 0, 1, 0, 1));

        boxes.add(new Vec3dCube(0, showBG ? border : 0, 0, 1, showBG ? 2 * border : border, 1 / 16D));
        boxes.add(new Vec3dCube(0, showBG ? border : 0, 15 / 16D, 1, showBG ? 2 * border : border, 1));
        boxes.add(new Vec3dCube(0, showBG ? border : 0, 0, 1 / 16D, showBG ? 2 * border : border, 1));
        boxes.add(new Vec3dCube(15 / 16D, showBG ? border : 0, 0, 1, showBG ? 2 * border : border, 1));
    }

    // @Override
    // public void addCollisionBoxes(List<Vec3dCube> boxes, Entity entity) {
    //
    // double s = (1 - border * 2) / size;
    // double t = ((size - 1) / 2D);
    // Rotation r1 = new Rotation(0, 90 * -getRotation(), 0);
    // Rotation r2 = new Rotation(getFace());
    // Scale sc = new Scale(s, s, s);
    // Translation tr = new Translation(-t, -t, -t);
    // for (int x = 0; x < size; x++)
    // for (int y = 0; y < size; y++)
    // if (cachedBlocks[x][y] != null)
    // boxes.add(new Vec3dCube(0, 0, 0, 1, 1, 1).transform(tr).transform(new Translation(x, 0, y)).transform(sc).transform(r2)
    // .transform(r1));
    // }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition mop = super.rayTrace(start, end);
        double dist = mop != null ? mop.hitVec.distanceTo(start.toVec3()) : Double.MAX_VALUE;
        List<Vec3dCube> cubes = new ArrayList<Vec3dCube>();
        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                if (tiles[x][z] != null) {
                    for (IPart p : tiles[x][z].getParts()) {
                        if (p instanceof IPartSelectable) {
                            cubes.clear();
                            for (Vec3dCube c : ((IPartSelectable) p).getSelectionBoxes()) {
                                Vec3dCube box = c.clone().add(x, 0, z);
                                box.getMin().div(getSize()).mul(14 / 16D);
                                box.getMax().div(getSize()).mul(14 / 16D);
                                box.add(border, showBG ? border : 0, border).rotate(0, getRotation() * -90, 0, Vec3d.center)
                                        .rotate(getFace(), Vec3d.center);
                                cubes.add(box);
                            }
                            QMovingObjectPosition hit = RayTracer.instance().rayTraceCubes(cubes, start, end, new Vec3i(this));
                            if (hit != null) {
                                double d = hit.hitVec.distanceTo(start.toVec3());
                                if (d < dist) {
                                    mop = new QMovingObjectPosition(hit, p, hit.getCube().clone().expand(-0.001));
                                    dist = d;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (mop != null && mop.getPart() == this) {
            return new QMovingObjectPosition(mop, this, new Vec3dCube(0, 0, 0, 1, showBG ? 2 * border : border, 1).expand(-0.001).rotate(
                    getFace(), Vec3d.center));
        }

        return mop;
    }

    // Rendering

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        Rotation r = null, r1 = null;

        switch (getFace()) {
        case DOWN:
            break;
        case UP:
            renderer.addTransformation(r = new Rotation(180, 180, 0, Vec3d.center));
            break;
        case NORTH:
            renderer.addTransformation(r = new Rotation(90, 0, 0, Vec3d.center));
            break;
        case SOUTH:
            renderer.addTransformation(r = new Rotation(-90, 0, 0, Vec3d.center));
            break;
        case WEST:
            renderer.addTransformation(r = new Rotation(0, 0, -90, Vec3d.center));
            break;
        case EAST:
            renderer.addTransformation(r = new Rotation(0, 0, 90, Vec3d.center));
            break;
        default:
            break;
        }

        int rotation = getRotation();
        if (rotation != -1)
            renderer.addTransformation(r1 = new Rotation(0, 90 * -rotation, 0));

        IIcon[] icons = new IIcon[] { getIcon(ForgeDirection.DOWN), getIcon(ForgeDirection.UP), getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH) };
        if (!showBG) {
            Vec3i vec = new Vec3i(this).add(getFace());
            icons = new IIcon[] { vec.getBlock().getIcon(getWorld(), vec.getX(), vec.getY(), vec.getZ(), 0),
                    vec.getBlock().getIcon(getWorld(), vec.getX(), vec.getY(), vec.getZ(), 1),
                    vec.getBlock().getIcon(getWorld(), vec.getX(), vec.getY(), vec.getZ(), 4),
                    vec.getBlock().getIcon(getWorld(), vec.getX(), vec.getY(), vec.getZ(), 5),
                    vec.getBlock().getIcon(getWorld(), vec.getX(), vec.getY(), vec.getZ(), 2),
                    vec.getBlock().getIcon(getWorld(), vec.getX(), vec.getY(), vec.getZ(), 3) };
            renderer.setColor(vec.getBlock().colorMultiplier(getWorld(), vec.getX(), vec.getY(), vec.getZ()));
        }

        if (showBG)
            renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1), getIcon(ForgeDirection.DOWN), null, getIcon(ForgeDirection.WEST),
                    getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH));

        renderer.renderBox(new Vec3dCube(0, showBG ? border : 0, 0, 1, showBG ? 2 * border : border, 1 / 16D), icons);
        renderer.renderBox(new Vec3dCube(0, showBG ? border : 0, 15 / 16D, 1, showBG ? 2 * border : border, 1), icons);
        renderer.renderBox(new Vec3dCube(0, showBG ? border : 0, 0, 1 / 16D, showBG ? 2 * border : border, 1), icons);
        renderer.renderBox(new Vec3dCube(15 / 16D, showBG ? border : 0, 0, 1, showBG ? 2 * border : border, 1), icons);

        renderer.addTransformation(new Scale(1 / 16D, 1 / 16D, 1 / 16D, new Vec3d(0, 0, 0)));

        for (int rot = 0; rot < 4; rot++) {
            renderer.addTransformation(new Rotation(0, -90 * rot, 0, new Vec3d(8, 8, 8)));
            renderer.addTransformation(new Translation(7.5 + 1 / 16D, 0.001 + (showBG ? 1 : 0), 0));

            for (int i = -getSize() / 2; i < (getSize() + 1) / 2; i++) {
                int m = modes[rot][i + -(-getSize() / 2)];
                if (m == 0)
                    continue;

                renderer.addTransformation(new Translation(i * (14D / getSize()), 0, 0));
                renderer.addTransformation(new Scale(1.75, 1, 1.75));
                renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), null, m == 1 ? IconSupplier.icArrowIn
                        : (m == 2 ? IconSupplier.icArrowOut : (m == 3 ? IconSupplier.icArrowInBundled : IconSupplier.icArrowOutBundled)),
                        null, null, null, null);
                renderer.removeTransformations(2);
            }

            renderer.removeTransformations(2);
        }

        renderer.removeTransformation();

        renderer.setColor(0xFFFFFF);

        for (IGateComponent c : getComponents())
            c.renderStatic(translation, renderer, pass);

        renderer.resetTransformations();

        double s = (1 - border * 2) / size;
        Translation t = new Translation(border, showBG ? border : 0, border);
        Scale st = new Scale(s, s, s, new Vec3d(0, 0, 0));

        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                if (r != null)
                    renderer.addTransformation(r);
                if (r1 != null)
                    renderer.addTransformation(r1);

                if (showBG) {
                    renderer.setColor((x + z) % 2 == 1 ? 0xEEEEEE : 0xBBBBBB);
                    renderer.renderBox(new Vec3dCube(border + x * s, border, border + z * s, border + (x + 1) * s, border, border + (z + 1)
                            * s), null, getIcon(ForgeDirection.UP), null, null, null, null);
                }
                renderer.setColor(0xFFFFFF);

                TileMultipart te = tiles[x][z];
                if (te != null) {
                    renderer.addTransformation(t);
                    renderer.addTransformation(st);
                    renderer.addTransformation(new Translation(x, 0, z));
                    for (IPart p : te.getParts())
                        if (p.shouldRenderOnPass(pass))
                            p.renderStatic(new Vec3i(0, 0, 0), renderer, renderBlocks, pass);
                }

                renderer.resetTransformations();
            }
        }

        return true;
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        super.renderDynamic(translation, delta, pass);

        GL11.glPushMatrix();
        {
            GL11.glTranslated(translation.getX(), translation.getY(), translation.getZ());

            GL11.glTranslated(0.5, 0.5, 0.5);
            {
                ForgeDirection face = getFace();
                if (face == ForgeDirection.UP) {
                    GL11.glRotated(180, 0, 1, 0);
                    GL11.glRotated(180, 1, 0, 0);
                } else if (face == ForgeDirection.NORTH) {
                    GL11.glRotated(90, 1, 0, 0);
                } else if (face == ForgeDirection.SOUTH) {
                    GL11.glRotated(90, -1, 0, 0);
                } else if (face == ForgeDirection.WEST) {
                    GL11.glRotated(90, 0, 0, -1);
                } else if (face == ForgeDirection.EAST) {
                    GL11.glRotated(90, 0, 0, 1);
                }
                int rotation = getRotation();
                GL11.glRotated(-90 * rotation, 0, 1, 0);
            }
            GL11.glTranslated(-0.5, -0.5, -0.5);

            GL11.glTranslated(border, showBG ? border : 0, border);
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    GL11.glPushMatrix();
                    {
                        GL11.glScaled((1D / getSize()) * 14 / 16D, (1D / getSize()) * 14 / 16D, (1D / getSize()) * 14 / 16D);
                        GL11.glTranslated(x, 0, z);
                        TileMultipart te = tiles[x][z];
                        if (te != null)
                            for (IPart p : te.getParts())
                                if (p.shouldRenderOnPass(pass))
                                    p.renderDynamic(new Vec3d(0, 0, 0), delta, pass);
                    }
                    GL11.glPopMatrix();
                }
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public boolean drawHighlight(QMovingObjectPosition mop, EntityPlayer player, float frame) {

        ItemStack item = player.getCurrentEquippedItem();
        Vec3d click = new Vec3d(mop.hitVec).sub(mop.blockX, mop.blockY, mop.blockZ).rotateUndo(getFace(), Vec3d.center).sub(0.5, 0.5, 0.5)
                .rotate(0, getRotation() * 90, 0).add(0.5, 0.5, 0.5).sub(border, border, border).div(14 / 16D).mul(getSize());
        double x = click.getX(), z = click.getZ();
        int x_ = (int) Math.floor(x), z_ = (int) Math.floor(z);

        if (x >= 0 && z >= 0 && x < getSize() && z < getSize()) {
            BPPart part = getPlacedPart(item, x, z);
            if (part == null || !(part instanceof IPartRenderPlacement))
                return false;
            TileMultipart tile = tiles[x_][z_];
            if (tile != null && !tile.canAddPart(part))
                return false;
            GL11.glPushMatrix();
            {
                Vec3 pos = player.getPosition(frame);
                GL11.glTranslated(getX() - pos.xCoord, getY() - pos.yCoord, getZ() - pos.zCoord);

                GL11.glTranslated(0.5, 0.5, 0.5);
                {
                    ForgeDirection face = getFace();
                    if (face == ForgeDirection.UP) {
                        GL11.glRotated(180, 0, 1, 0);
                        GL11.glRotated(180, 1, 0, 0);
                    } else if (face == ForgeDirection.NORTH) {
                        GL11.glRotated(90, 1, 0, 0);
                    } else if (face == ForgeDirection.SOUTH) {
                        GL11.glRotated(90, -1, 0, 0);
                    } else if (face == ForgeDirection.WEST) {
                        GL11.glRotated(90, 0, 0, -1);
                    } else if (face == ForgeDirection.EAST) {
                        GL11.glRotated(90, 0, 0, 1);
                    }
                    int rotation = getRotation();
                    GL11.glRotated(-90 * rotation, 0, 1, 0);
                }
                GL11.glTranslated(-0.5, -0.5, -0.5);

                GL11.glTranslated(border, showBG ? border : 0, border);
                GL11.glScaled((1D / getSize()) * 14 / 16D, (1D / getSize()) * 14 / 16D, (1D / getSize()) * 14 / 16D);
                GL11.glTranslated(x_, 0, z_);

                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

                RenderHelper rh = RenderHelper.instance;
                rh.fullReset();
                rh.setRenderCoords(getWorld(), getX(), getY(), getZ());
                rh.setOpacity(0.5);

                boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                Tessellator.instance.startDrawingQuads();
                Tessellator.instance.addTranslation(-getX(), -getY(), -getZ());
                if (part.shouldRenderOnPass(0))
                    part.renderStatic(new Vec3i(this), rh, null, 0);
                if (part.shouldRenderOnPass(1))
                    part.renderStatic(new Vec3i(this), rh, null, 1);
                Tessellator.instance.addTranslation(getX(), getY(), getZ());
                Tessellator.instance.draw();

                if (!blend)
                    GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glPopMatrix();
        } else if (!((x_ < 0 && z_ < 0) || (x_ < 0 && z_ >= getSize()) || (x_ >= getSize() && z_ < 0) || (x_ >= getSize() && z_ >= getSize()))
                && ItemStackUtils.isScrewdriver(item)) {
            Vec3 pos = player.getPosition(frame);
            GL11.glPushMatrix();
            {
                GL11.glTranslated(getX() - pos.xCoord, getY() - pos.yCoord, getZ() - pos.zCoord);

                GL11.glTranslated(0.5, 0.5, 0.5);
                {
                    ForgeDirection face = getFace();
                    if (face == ForgeDirection.UP) {
                        GL11.glRotated(180, 0, 1, 0);
                        GL11.glRotated(180, 1, 0, 0);
                    } else if (face == ForgeDirection.NORTH) {
                        GL11.glRotated(90, 1, 0, 0);
                    } else if (face == ForgeDirection.SOUTH) {
                        GL11.glRotated(90, -1, 0, 0);
                    } else if (face == ForgeDirection.WEST) {
                        GL11.glRotated(90, 0, 0, -1);
                    } else if (face == ForgeDirection.EAST) {
                        GL11.glRotated(90, 0, 0, 1);
                    }
                    int rotation = getRotation();
                    GL11.glRotated(-90 * rotation, 0, 1, 0);
                }
                GL11.glTranslated(-0.5, -0.5, -0.5);

                GL11.glTranslated(border, showBG ? border : 0, border);
                GL11.glTranslated(x_ * (14 / 16D) / getSize(), 0, z_ * (14 / 16D) / getSize());

                Vec3 min = Vec3.createVectorHelper(x > 0 ? 0 : (14 / 16D) / getSize() - border, border + 0.001, z > 0 ? 0 : (14 / 16D)
                        / getSize() - border);
                Vec3 max = Vec3.createVectorHelper(x < getSize() ? (14 / 16D) / getSize() : border, border + 0.001,
                        z < getSize() ? (14 / 16D) / getSize() : border);

                RenderGlobal.drawOutlinedBoundingBox(
                        AxisAlignedBB.getBoundingBox(min.xCoord, min.yCoord, min.zCoord, max.xCoord, max.yCoord, max.zCoord), 0);
            }
            GL11.glPopMatrix();
            return true;
        }

        return false;
    }

    @Override
    public boolean renderBreaking(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass, QMovingObjectPosition mop) {

        if (mop.getPart() != this) {
            double s = (14 / 16D) / getSize();

            renderer.addTransformation(new Rotation(getFace()));
            renderer.addTransformation(new Rotation(0, getRotation() * -90, 0));
            renderer.addTransformation(new Scale(s, s, s, new Vec3d(0, 0, 0)));
            renderer.addTransformation(new Translation(mop.getPart().getX(), 0, mop.getPart().getZ()));
            renderer.addTransformation(new Translation(border / s, showBG ? border / s : 0, border / s));

            boolean result = mop.getPart().renderBreaking(translation, renderer, renderBlocks, pass, mop);

            renderer.removeTransformations(Integer.MAX_VALUE);

            return result;
        }

        return super.renderBreaking(translation, renderer, renderBlocks, pass, mop);
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        // if (!DebugHelper.isDebugModeEnabled())
        // return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    @Override
    public void addTooltip(ItemStack item, List<String> tip) {

        // tip.add((!DebugHelper.isDebugModeEnabled() ? MinecraftColor.RED : MinecraftColor.CYAN)
        // + I18n.format("Disabled temporarily. Still not fully working."));
        tip.add(MinecraftColor.CYAN + I18n.format("Can only be placed on the ground and connected to directly"));
        tip.add(MinecraftColor.CYAN + I18n.format("from the side as of now. This is something we're working on."));
    }

    // TODO: Have a look at this!
    // @Override
    // public IBundledDevice getBundledDeviceOnSide(ForgeDirection side) {
    //
    // Dir dir = Dir.getDirection(side, getFace(), getRotation());
    // TileMultipart te = null;
    // if (dir == Dir.FRONT && tiles[(getSize() - 1) / 2][0] != null)
    // te = tiles[(getSize() - 1) / 2][0];
    // if (dir == Dir.BACK && tiles[(getSize() - 1) / 2][getSize() - 1] != null)
    // te = tiles[(getSize() - 1) / 2][getSize() - 1];
    // if (dir == Dir.LEFT && tiles[0][(getSize() - 1) / 2] != null)
    // te = tiles[0][(getSize() - 1) / 2];
    // if (dir == Dir.RIGHT && tiles[getSize() - 1][(getSize() - 1) / 2] != null)
    // te = tiles[getSize() - 1][(getSize() - 1) / 2];
    // if (te == null)
    // return null;
    // for (IPart p : te.getParts())
    // if (p instanceof IFace && ((IFace) p).getFace() == ForgeDirection.DOWN && p instanceof IBundledDevice)
    // return (IBundledDevice) p;
    // return null;
    // }
    //
    // @Override
    // public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {
    //
    // Dir dir = Dir.getDirection(side, getFace(), getRotation());
    // TileMultipart te = null;
    // if (dir == Dir.FRONT && tiles[(getSize() - 1) / 2][0] != null)
    // te = tiles[(getSize() - 1) / 2][0];
    // if (dir == Dir.BACK && tiles[(getSize() - 1) / 2][getSize() - 1] != null)
    // te = tiles[(getSize() - 1) / 2][getSize() - 1];
    // if (dir == Dir.LEFT && tiles[0][(getSize() - 1) / 2] != null)
    // te = tiles[0][(getSize() - 1) / 2];
    // if (dir == Dir.RIGHT && tiles[getSize() - 1][(getSize() - 1) / 2] != null)
    // te = tiles[getSize() - 1][(getSize() - 1) / 2];
    // if (te == null)
    // return null;
    // for (IPart p : te.getParts())
    // if (p instanceof IFace && ((IFace) p).getFace() == ForgeDirection.DOWN && p instanceof IRedstoneDevice)
    // return (IRedstoneDevice) p;
    // return null;
    // }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition mop, ItemStack item) {

        Vec3d click = new Vec3d(mop.hitVec).sub(mop.blockX, mop.blockY, mop.blockZ).rotateUndo(getFace(), Vec3d.center).sub(0.5, 0.5, 0.5)
                .rotate(0, getRotation() * 90, 0).add(0.5, 0.5, 0.5).sub(border, showBG ? border : 0, border).div(14 / 16D).mul(getSize());
        double x = click.getX(), z = click.getZ();

        if (activate(player, item, x, z))
            return true;

        return super.onActivated(player, mop, item);
    }

    public boolean activate(EntityPlayer player, ItemStack item, double x, double z) {

        int x_ = (int) Math.floor(x), z_ = (int) Math.floor(z);

        // Toggling modes
        if ((((x_ < 0 || x_ >= getSize()) && (z_ >= 0 && z_ < getSize())) || ((z_ < 0 || z_ >= getSize()) && (x_ >= 0 && x_ < getSize())))
                && ItemStackUtils.isScrewdriver(item)) {
            int s = 0;
            int p = (getSize() - 1) / 2;

            if (z_ < 0) {
                s = 0;
                p = x_;
            } else if (z_ >= getSize()) {
                s = 2;
                p = getSize() - 1 - x_;
            } else if (x_ < 0) {
                s = 3;
                p = getSize() - 1 - z_;
            } else if (x_ >= getSize()) {
                s = 1;
                p = z_;
            }

            int i = modes[s][p];

            if (!player.isSneaking())
                i++;
            else
                i--;

            if (i > 4)
                i = 0;
            if (i < 0)
                i = 4;
            modes[s][p] = i;

            if (!getWorld().isRemote) {
                rsTiles[s][p].notifyUpdate();
                notifyUpdate();
                sendUpdatePacket();
            }

            return true;
        }

        // Placement
        if (x >= 0 && z >= 0 && x < getSize() && z < getSize()) {
            BPPart part = getPlacedPart(item, x, z);
            if (part == null)
                return false;
            TileMultipart tile = tiles[x_][z_];
            if (tile == null)
                tile = mktile(x_, z_);
            if (tile.canAddPart(part)) {
                tiles[x_][z_] = tile;
                if (!getWorld().isRemote) {
                    tile.addPart(part);
                    sendUpdatePacket(x_ * getSize() + z_ + 1);
                    if (!player.capabilities.isCreativeMode)
                        item.stackSize--;
                }
                return true;
            }
        }

        return false;
    }

    private BPPart getPlacedPart(ItemStack item, double x, double z) {

        if (item == null)
            return null;
        if (!(item.getItem() instanceof ItemPart))
            return null;
        BPPart part = ((ItemPart) item.getItem()).createPart(item);
        if (!(part instanceof IIntegratedCircuitPart))
            return null;
        if (!((IIntegratedCircuitPart) part).canPlaceOnIntegratedCircuit())
            return null;
        IPartPlacementFlat placement = ((IPartCustomPlacementFlat) part).getFlatPlacement(part, x % 1, z % 1);
        if (placement == null)
            return null;
        if (!placement.placePart(part, x, z))
            return null;
        return part;
    }

    protected FakeWorldIC load() {

        return FakeWorldIC.load(this);
    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return false;

        int m = modes[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2];
        return m == 1 || m == 2;
    }

    @Override
    public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return false;

        int m = modes[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2];
        return m == 3 || m == 4;
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return false;

        int m = modes[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2];
        return m == 1 || m == 2;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return 0;

        if (modes[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2] == 2)
            return rsTiles[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2].rsFW;
        return 0;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        rsTiles[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2].rsRW = power;
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return new byte[16];

        return rsTiles[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2].bRW;
    }

    @Override
    public byte[] getBundledOutput(ForgeDirection side) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return new byte[16];

        if (modes[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2] == 4)
            return rsTiles[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2].bFW;
        return new byte[16];
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        if (side == ForgeDirection.UNKNOWN || side == getFace() || side == getFace().getOpposite())
            return;

        rsTiles[Dir.getDirection(side, getFace(), getRotation()).ordinal()][(getSize() - 1) / 2].bRW = power;
    }

    @Override
    public void onRedstoneUpdate() {

        doLogic();
    }

    @Override
    public void onBundledUpdate() {

        doLogic();
    }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        super.writeUpdateData(buffer, channel);

        if (channel == -1) {
            buffer.writeBoolean(showBG);
            for (int x = 0; x < getSize(); x++)
                for (int z = 0; z < getSize(); z++)
                    writeTileUpdate(buffer, x, z);
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < getSize(); j++)
                    buffer.writeInt(modes[i][j]);
        }

        // Send tile updates if needed
        if (channel >= 1 && channel < getSize() * getSize() + 1)
            writeTileUpdate(buffer, ((channel - 1) - ((channel - 1) % getSize())) / getSize(), (channel - 1) % getSize());
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);

        if (channel == -1) {
            showBG = buffer.readBoolean();
            for (int x = 0; x < getSize(); x++)
                for (int z = 0; z < getSize(); z++)
                    readTileUpdate(buffer, x, z);
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < getSize(); j++)
                    modes[i][j] = buffer.readInt();
        }

        // Read tile updates if needed
        if (channel >= 1 && channel < getSize() * getSize() + 1)
            readTileUpdate(buffer, ((channel - 1) - ((channel - 1) % getSize())) / getSize(), (channel - 1) % getSize());

        if (getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    protected void writeTileUpdate(DataOutput buffer, int x, int z) throws IOException {

        TileMultipart te = tiles[x][z];
        buffer.writeBoolean(te != null && !te.getParts().isEmpty());
        if (te == null || te.getParts().isEmpty())
            return;
        buffer.writeInt(te.getPartMap().size());
        for (Entry<String, IPart> e : te.getPartMap().entrySet()) {
            buffer.writeUTF(e.getKey());
            buffer.writeUTF(e.getValue().getType());
            e.getValue().writeUpdateData(buffer, -1);
        }
    }

    protected void readTileUpdate(DataInput buffer, int x, int z) throws IOException {

        if (!buffer.readBoolean()) {
            tiles[x][z] = null;
            return;
        }
        TileMultipart te = tiles[x][z];
        if (te == null)
            tiles[x][z] = te = mktile(x, z);
        int amt = buffer.readInt();
        for (int i = 0; i < amt; i++) {
            String id = buffer.readUTF();
            String type = buffer.readUTF();
            IPart part = te.getPartMap().containsKey(id) ? te.getPartMap().get(id) : null;
            if (part == null)
                te.getPartMap().put(id, part = PartRegistry.createPart(type, true));
            part.setParent(te);
            part.readUpdateData(buffer, -1);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                TileMultipart te = tiles[x][z];
                if (te != null && !te.getParts().isEmpty()) {
                    NBTTagCompound t = new NBTTagCompound();
                    te.writeToNBT(t);
                    tag.setTag("tile_" + x + "_" + z, t);
                }
            }
        }

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < getSize(); j++)
                tag.setInteger("mode_" + i + "_" + j, modes[i][j]);

        tag.setBoolean("showBG", showBG);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        for (int x = 0; x < getSize(); x++)
            for (int z = 0; z < getSize(); z++)
                if (tag.hasKey("tile_" + x + "_" + z))
                    (tiles[x][z] = mktile(x, z)).readFromNBT(tag.getCompoundTag("tile_" + x + "_" + z));

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < getSize(); j++)
                modes[i][j] = tag.getInteger("mode_" + i + "_" + j);

        showBG = tag.getBoolean("showBG");
    }

    private static boolean registered = false;

    protected TileMultipart mktile(int x, int z) {

        TileMultipart tile = new TileMultipart() {

            @Override
            public World getWorldObj() {

                return load();
            }

            @Override
            public void sendUpdatePacket(IPart part, int channel) {

                GateIntegratedCircuit.this.sendUpdatePacket(getX() * getSize() + getZ() + 1);
            }

            @Override
            public boolean canAddPart(IPart part) {

                return OcclusionHelper.occlusionTest(this, part);
            }

            @Override
            public void markDirty() {

                // GateIntegratedCircuit.this.getParent().markDirty();
            }

            @Override
            public boolean removePart(IPart part) {

                if (!super.removePart(part))
                    return false;
                GateIntegratedCircuit.this.sendUpdatePacket();
                return true;
            }
        };
        if (!registered) {
            GameRegistry.registerTileEntity(tile.getClass(), "qlmultipartic");
            registered = true;
        }
        tile.xCoord = x;
        tile.yCoord = 64;
        tile.zCoord = z;
        return tile;
    }
}
