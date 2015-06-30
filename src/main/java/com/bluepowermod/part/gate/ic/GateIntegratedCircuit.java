package com.bluepowermod.part.gate.ic;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.QmunityLib;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.network.PacketHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IGateComponent;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IBundledDeviceWrapper;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDeviceWrapper;
import com.bluepowermod.client.gui.gate.GuiGateIC;
import com.bluepowermod.client.render.TessellatorHelper;
import com.bluepowermod.client.render.TessellatorHelper.TessellatorVertex;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.util.CachedBlock;
import com.bluepowermod.util.DebugHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateIntegratedCircuit extends
        GateBase<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase> implements
        IGateLogic<GateIntegratedCircuit>, IRedstoneDeviceWrapper, IBundledDeviceWrapper {// , IPartSelectableCustom {

    public static boolean shouldActivate = false;

    private int size;
    private ICCachedBlock[][] cachedBlocks = null;

    public static final double border = 1 / 16D;

    public GateIntegratedCircuit(Integer size) {

        this.size = size;
        cachedBlocks = new ICCachedBlock[size][size];
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

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;
        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                if (cachedBlocks[x][z] != null)
                    cachedBlocks[x][z].block().onNeighborBlockChange(load(), x, 64, z, cachedBlocks[x][z].block());
            }
        }
        sendUpdatePacket();
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

        boxes.add(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1));
        boxes.add(new Vec3dCube(0, 1 / 16D, 0, 1, 2 / 16D, 1 / 16D));
        boxes.add(new Vec3dCube(0, 1 / 16D, 1 - 1 / 16D, 1, 2 / 16D, 1));
        boxes.add(new Vec3dCube(0, 1 / 16D, 0, 1 / 16D, 2 / 16D, 1));
        boxes.add(new Vec3dCube(1 - 1 / 16D, 1 / 16D, 0, 1, 2 / 16D, 1));
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
    //
    // @Override
    // public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {
    //
    // QMovingObjectPosition mop = super.rayTrace(start, end);
    //
    // {
    // MovingObjectPosition mop2 = rayTraceCircuit(start, end);
    // if (mop2 != null && mop2.blockY == 64) {
    // World w = load();
    // AxisAlignedBB aabb = w.getBlock(mop2.blockX, mop2.blockY, mop2.blockZ).getSelectedBoundingBoxFromPool(w, mop2.blockX, mop2.blockY,
    // mop2.blockZ);
    // if (aabb != null) {
    // double s = (1 - border * 2) / size;
    // double t = ((size - 1) / 2D);
    // Rotation r1 = new Rotation(0, 90 * -getRotation(), 0);
    // Rotation r2 = new Rotation(getFace());
    // Scale sc = new Scale(s, s, s);
    // Translation tr = new Translation(-t, -t, -t);
    // QMovingObjectPosition mop3 = RayTracer.instance().rayTraceCubes(
    // Arrays.asList(new Vec3dCube(aabb).add(-mop2.blockX, -mop2.blockY, -mop2.blockZ).transform(tr)
    // .transform(new Translation(mop2.blockX, 0, mop2.blockZ)).transform(sc).transform(r2).transform(r1)
    // .setData(new Pair<Integer, Integer>(mop2.blockX, mop2.blockZ))), start, end, new Vec3i(this));
    // if (mop3 != null) {
    // mop = mop3;
    // }
    // }
    // }
    // }
    //
    // if (mop != null) {
    // if (mop.getPart() == this) {
    // return new QMovingObjectPosition(mop, this, new Vec3dCube(0, 0, 0, 1, 2 / 16D, 1).expand(-0.001).rotate(getFace(), Vec3d.center));
    // } else {
    // return new QMovingObjectPosition(mop, this, mop.getCube());
    // }
    // }
    //
    // return mop;
    // }

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

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1), getIcon(ForgeDirection.DOWN), null, getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH));

        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1, 2 / 16D, 1 / 16D), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 15 / 16D, 1, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1 / 16D, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(15 / 16D, 1 / 16D, 0, 1, 2 / 16D, 1), icons);

        for (IGateComponent c : getComponents())
            c.renderStatic(translation, renderer, pass);

        renderer.resetTransformations();

        double s = (1 - border * 2) / size;

        ICFakeWorld.load(this, true);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (r != null)
                    renderer.addTransformation(r);
                if (r1 != null)
                    renderer.addTransformation(r1);

                renderer.setColor((x + y) % 2 == 1 ? 0xEEEEEE : 0xBBBBBB);
                renderer.renderBox(new Vec3dCube(border + x * s, border, border + y * s, border + (x + 1) * s, border, border + (y + 1) * s), null,
                        getIcon(ForgeDirection.UP), null, null, null, null);
                renderer.setColor(0xFFFFFF);

                CachedBlock b = cachedBlocks[x][y];
                if (b != null) {
                    TessellatorHelper.saveCurrent();

                    TessellatorHelper.saveTranslation();
                    Tessellator.instance.setTranslation(0, 0, 0);

                    RenderHelper rh = RenderHelper.instance;
                    RenderHelper.instance = new RenderHelper();
                    ICRegistry.instance.renderStatic(b, renderer, pass);
                    RenderHelper.instance = rh;

                    TessellatorHelper.resetTranslation();

                    List<TessellatorVertex> vertices = TessellatorHelper.findDifferences();

                    for (TessellatorVertex v : vertices) {
                        Vec3d pos = new Vec3d(v.x(), v.y(), v.z()).sub(0.5, 64.5, 0.5).mul(s).rotate(0, -90 * getRotation(), 0)
                                .add(0.5, 0.5 - (1 - s) / 2 + border, 0.5).rotate(getFace(), Vec3d.center)
                                .add(TessellatorHelper.oldX + getX(), TessellatorHelper.oldY + getY(), TessellatorHelper.oldZ + getZ());
                        v.setX(pos.getX());
                        v.setY(pos.getY());
                        v.setZ(pos.getZ());
                    }
                }

                renderer.resetTransformations();
            }
        }

        return true;
    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        super.renderDynamic(translation, delta, pass);
    }

    public void renderStatic_do(Vec3i translation, RenderHelper renderer, int pass) {

        IIcon[] icons = new IIcon[] { getIcon(ForgeDirection.DOWN), getIcon(ForgeDirection.UP), getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH) };

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1), getIcon(ForgeDirection.DOWN), null, getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH));

        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1, 2 / 16D, 1 / 16D), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 15 / 16D, 1, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1 / 16D, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(15 / 16D, 1 / 16D, 0, 1, 2 / 16D, 1), icons);

        for (IGateComponent c : getComponents())
            c.renderStatic(translation, renderer, pass);

        renderer.resetTransformations();

        double s = (1 - border * 2) / size;

        ICFakeWorld.load(this, true);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                renderer.setColor((x + y) % 2 == 1 ? 0xEEEEEE : 0xBBBBBB);
                renderer.renderBox(new Vec3dCube(border + x * s, border, border + y * s, border + (x + 1) * s, border, border + (y + 1) * s), null,
                        getIcon(ForgeDirection.UP), null, null, null, null);
                renderer.setColor(0xFFFFFF);

                CachedBlock b = cachedBlocks[x][y];
                if (b != null) {
                    TessellatorHelper.saveCurrent();

                    TessellatorHelper.saveTranslation();
                    Tessellator.instance.setTranslation(0, 0, 0);

                    RenderHelper rh = RenderHelper.instance;
                    RenderHelper.instance = new RenderHelper();
                    ICRegistry.instance.renderStatic(b, renderer, pass);
                    RenderHelper.instance = rh;

                    TessellatorHelper.resetTranslation();
                    List<TessellatorVertex> vertices = TessellatorHelper.findDifferences();

                    for (TessellatorVertex v : vertices) {
                        Vec3d pos = new Vec3d(v.x(), 0, v.z()).sub(0.5, 0.5, 0.5).mul(s).add(0.5, 0.5, 0.5)
                                .add(TessellatorHelper.oldX, TessellatorHelper.oldY - (1 - s) / 2 + border, TessellatorHelper.oldZ);
                        v.setX(pos.getX());
                        v.setY(pos.getY());
                        v.setZ(pos.getZ());
                    }
                }

                renderer.resetTransformations();
            }
        }
    }

    // @SuppressWarnings("unchecked")
    // @Override
    // public boolean renderBreaking(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass, QMovingObjectPosition mop) {
    //
    // Object data = mop.getCube().getData();
    // if (data == null)
    // return super.renderBreaking(translation, renderer, renderBlocks, pass, mop);
    // if (!(data instanceof Pair<?, ?>))
    // return super.renderBreaking(translation, renderer, renderBlocks, pass, mop);
    //
    // int x = ((Pair<Integer, Integer>) data).getKey();
    // int y = ((Pair<Integer, Integer>) data).getValue();
    //
    // switch (getFace()) {
    // case DOWN:
    // break;
    // case UP:
    // renderer.addTransformation(new Rotation(180, 180, 0, Vec3d.center));
    // break;
    // case NORTH:
    // renderer.addTransformation(new Rotation(90, 0, 0, Vec3d.center));
    // break;
    // case SOUTH:
    // renderer.addTransformation(new Rotation(-90, 0, 0, Vec3d.center));
    // break;
    // case WEST:
    // renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
    // break;
    // case EAST:
    // renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
    // break;
    // default:
    // break;
    // }
    //
    // int rotation = getRotation();
    // if (rotation != -1)
    // renderer.addTransformation(new Rotation(0, 90 * -rotation, 0));
    //
    // double s = (1 - border * 2) / size;
    // double t = ((size - 1) / 2D);
    //
    // renderer.addTransformation(new Scale(s, s, s));
    // renderer.addTransformation(new Translation(-t, -t, -t));
    // if (cachedBlocks[x][y] != null) {
    // renderer.addTransformation(new Translation(x, 0, y));
    // CachedBlock b = cachedBlocks[x][y];
    // renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), b.block().getIcon(0, b.meta()), b.block().getIcon(1, b.meta()),
    // b.block().getIcon(2, b.meta()), b.block().getIcon(3, b.meta()), b.block().getIcon(4, b.meta()), b.block().getIcon(5, b.meta()));
    // renderer.removeTransformation();
    // }
    // renderer.removeTransformations(2);
    //
    // return true;
    // }
    //
    // private boolean drawing = false;
    //
    // @Override
    // public boolean drawHighlight(QMovingObjectPosition mop, EntityPlayer player, float frame) {
    //
    // if (drawing)
    // return false;
    //
    // boolean result = false;
    //
    // do {
    // World fw = load();
    // fw.isRemote = true;
    //
    // MovingObjectPosition hit = rayTraceCircuit(RayTracer.getStartVector(player), RayTracer.getEndVector(player));
    //
    // if (hit == null)
    // break;
    //
    // int x = hit.blockX;
    // int z = hit.blockZ;
    //
    // if (x < 0 || z < 0 || x >= size || z >= size || (hit.blockY != 64 && hit.blockY != 63))
    // break;
    //
    // Vec3d pos = RayTracer.getStartVector(player).clone().sub(getX(), getY(), getZ()).sub(border, border, border).div(1 - 2 * border)
    // .mul(getSize()).add(0, 64, 0);
    //
    // World w = player.worldObj;
    // double px = player.posX, py = player.posY, pz = player.posZ;
    // double lpx = player.lastTickPosX, lpy = player.lastTickPosY, lpz = player.lastTickPosZ;
    // Vec3 plpos = player.getPosition(frame).addVector(-player.posX, -player.posY, -player.posZ);
    //
    // player.worldObj = fw;
    // player.posX = player.lastTickPosX = pos.getX();
    // player.posY = player.lastTickPosY = pos.getY();
    // player.posZ = player.lastTickPosZ = pos.getZ();
    //
    // RayTracer.overrideReachDistance = (RayTracer.getBlockReachDistance(player) / (1 - 2 * border)) * getSize();
    // drawing = true;
    // double s = (1 - border * 2) / size;
    //
    // GL11.glPushMatrix();
    // {
    // // GL11.glRotated(90 * -getRotation(), 0, 1, 0);
    //
    // GL11.glTranslated(-plpos.xCoord, -plpos.yCoord, -plpos.zCoord);
    //
    // GL11.glScaled(s, s, s);
    // GL11.glScaled(0.998, 0.998, 0.998);
    //
    // try {
    // DrawBlockHighlightEvent ev = new DrawBlockHighlightEvent(Minecraft.getMinecraft().renderGlobal, player, hit, 0,
    // player.getCurrentEquippedItem(), frame);
    // MinecraftForge.EVENT_BUS.post(ev);
    //
    // result = ev.isCanceled();
    // } catch (Exception ex) {
    // }
    // }
    // GL11.glPopMatrix();
    //
    // drawing = false;
    // RayTracer.overrideReachDistance = -1;
    //
    // player.worldObj = w;
    // player.posX = px;
    // player.posY = py;
    // player.posZ = pz;
    // player.lastTickPosX = lpx;
    // player.lastTickPosY = lpy;
    // player.lastTickPosZ = lpz;
    //
    // if (!result)
    // RenderUtils.drawSelectionBox(
    // new Vec3dCube(0, 0, 0, (1D - 2 * border) / getSize(), 0, (1D - 2 * border) / getSize()).add(
    // border + ((1D - 2 * border) / getSize()) * (x), 1 / 16D + 0.001, border + ((1D - 2 * border) / getSize()) * (z))
    // .rotate(getFace(), Vec3d.center), getX(), getY(), getZ(), player, frame);
    //
    // // return result;
    // } while (false);
    //
    // RenderUtils.drawSelectionBox(
    // new Vec3dCube(1 / 16D, 1 / 16D, 1 / 16D, 15 / 16D, 2 / 16D + 0.002, 15 / 16D).expand(-0.002).rotate(getFace(), Vec3d.center), getX(),
    // getY(), getZ(), player, frame);
    // if (result)
    // RenderUtils.drawSelectionBox(new Vec3dCube(0, 0, 0, 1, 2 / 16D, 1).expand(0.001).rotate(getFace(), Vec3d.center), getX(), getY(), getZ(),
    // player, frame);
    //
    // return result;
    // }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop, EntityPlayer player) {

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

    @Override
    public IBundledDevice getBundledDeviceOnSide(ForgeDirection side) {

        return null;
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return null;
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }

    @Override
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateIC(this, player);
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition mop, ItemStack item) {

        return super.onActivated(player, mop, item);
        // if (!shouldActivate && !getWorld().isRemote)
        // return false;
        //
        // World fw = load();
        //
        // MovingObjectPosition hit = rayTraceCircuit(RayTracer.getStartVector(player), RayTracer.getEndVector(player));
        //
        // if (hit == null)
        // return false;
        //
        // int x = hit.blockX;
        // int z = hit.blockZ;
        //
        // if (x < 0 || z < 0 || x >= size || z >= size)
        // return false;
        //
        // boolean action = false;
        //
        // if (item != null) {
        // Vec3d pos = RayTracer.getStartVector(player).clone().sub(getX(), getY(), getZ()).sub(border, border, border).div(1 - 2 * border)
        // .mul(getSize()).add(0, 64, 0);
        //
        // World w = player.worldObj;
        // double px = player.posX, py = player.posY, pz = player.posZ;
        // double lpx = player.lastTickPosX, lpy = player.lastTickPosY, lpz = player.lastTickPosZ;
        //
        // player.worldObj = fw;
        // if (getWorld().isRemote) {
        // player.posX = player.lastTickPosX = pos.getX();
        // player.posY = player.lastTickPosY = pos.getY();
        // player.posZ = player.lastTickPosZ = pos.getZ();
        // }
        //
        // RayTracer.overrideReachDistance = (RayTracer.getBlockReachDistance(player) / (1 - 2 * border)) * getSize();
        // action = ICRegistry.instance.placeOnIC(fw, hit, player, item);
        // RayTracer.overrideReachDistance = -1;
        //
        // if (!getWorld().isRemote)
        // for (int i = 0; i < getSize(); i++) {
        // for (int j = 0; j < getSize(); j++) {
        // System.out.println(cachedBlocks[i][j]);
        // }
        // }
        //
        // if (getWorld().isRemote) {
        // player.worldObj = w;
        // player.posX = px;
        // player.posY = py;
        // player.posZ = pz;
        // player.lastTickPosX = lpx;
        // player.lastTickPosY = lpy;
        // player.lastTickPosZ = lpz;
        // }
        //
        // sendUpdatePacket(1);
        // }
        //
        // // if (item.getItem() instanceof ItemBlock && cachedBlocks[x][y] == null) {
        // // if (!getWorld().isRemote) {
        // // ItemBlock ib = (ItemBlock) item.getItem();
        // // CachedBlock b = cachedBlocks[x][y] = new CachedBlock();
        // // b.setBlock(ib.field_150939_a);
        // // b.setMeta(ib.getMetadata(item.getItemDamage()));
        // // if (b.block() instanceof ITileEntityProvider)
        // // b.setTile(b.block().createTileEntity(load(), b.meta()));
        // //
        // // sendUpdatePacket(1);
        // // }
        // // return true;
        // // }
        //
        // if (action && getWorld().isRemote)
        // BPNetworkHandler.INSTANCE.sendToServer(new MessageICInteract(this, player));
        //
        // return action;
    }

    // @Override
    // public boolean breakAndDrop(EntityPlayer player, QMovingObjectPosition mop) {
    //
    // if (player == null)
    // return super.breakAndDrop(player, mop);
    //
    // MovingObjectPosition hit = rayTraceCircuit(RayTracer.getStartVector(player), RayTracer.getEndVector(player));
    // if (hit == null)
    // return super.breakAndDrop(player, mop);
    //
    // int x = hit.blockX;
    // int y = hit.blockZ;
    //
    // if (x >= 0 && y >= 0 && x < size && y < size && cachedBlocks[x][y] != null && !getWorld().isRemote) {
    // cachedBlocks[x][y] = null;
    // sendUpdatePacket(1);
    // return false;
    // }
    //
    // return super.breakAndDrop(player, mop);// super.breakAndDrop(player, mop);
    // }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        super.writeUpdateData(buffer, channel);

        if (channel == -1 || channel == 1) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    CachedBlock block = cachedBlocks[x][y];
                    buffer.writeBoolean(block != null);
                    if (block == null)
                        continue;
                    buffer.writeUTF(Block.blockRegistry.getNameForObject(block.block()));
                    buffer.writeInt(block.meta());
                    buffer.writeBoolean(block.tile() != null);
                    if (block.tile() == null)
                        continue;
                    NBTTagCompound tag = new NBTTagCompound();
                    block.tile().writeToNBT(tag);
                    PacketHelper.writeNBT(buffer, tag);
                }
            }
        }
    }

    private World playerWorld;

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);

        if (channel == -1 || channel == 1) {
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (!buffer.readBoolean()) {
                        cachedBlocks[x][y] = null;
                        continue;
                    }
                    CachedBlock b = getOrCreateBlock(x, y);
                    b.setBlock(Block.getBlockFromName(buffer.readUTF()));
                    b.setMeta(buffer.readInt());
                    if (b.block() instanceof ITileEntityProvider)
                        b.setTile(b.block().createTileEntity(load(), b.meta()));
                    if (!buffer.readBoolean())
                        continue;
                    NBTTagCompound tag = PacketHelper.readNBT(buffer);
                    if (b.tile() == null)
                        continue;
                    b.tile().readFromNBT(tag);
                }
            }
        }

        if (channel == 2) {
            playerWorld = QmunityLib.proxy.getPlayer().worldObj;
            QmunityLib.proxy.getPlayer().worldObj = load();
        }
        if (channel == 3) {
            QmunityLib.proxy.getPlayer().worldObj = playerWorld;
        }

        if (getWorld() != null)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    public MovingObjectPosition rayTraceCircuit(Vec3d start_, Vec3d end_) {

        return load().rayTraceBlocks(start_.toVec3(), end_.toVec3());
    }

    public Pair<Integer, Integer> getCurrentPos(Vec3d start, Vec3d end) {

        MovingObjectPosition mop = rayTraceCircuit(start, end);
        if (mop == null)
            return null;

        return new Pair<Integer, Integer>(mop.blockX, mop.blockZ);
    }

    public CachedBlock getCurrent(Vec3d start, Vec3d end) {

        MovingObjectPosition mop = rayTraceCircuit(start, end);
        if (mop == null)
            return null;

        if (mop.blockY > 63)
            return getBlock(mop.blockX, mop.blockZ);

        return null;
    }

    public CachedBlock[][] getCachedBlocks() {

        return cachedBlocks;
    }

    public CachedBlock getBlock(int x, int z) {

        try {
            return cachedBlocks[x][z];
        } catch (Exception ex) {
        }

        return null;
    }

    public CachedBlock getOrCreateBlock(int x, int z) {

        CachedBlock b = getBlock(x, z);

        if (b == null) {
            try {
                int d = (getSize() - 1) / 2;
                b = cachedBlocks[x][z] = new ICCachedBlock(this, x - d, 64, z - d);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return b;
    }

    public World load() {

        return ICFakeWorld.load(this);
    }
}
