package com.bluepowermod.part.gate.ic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.transform.Scale;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec2d;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IGate;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.connection.GateConnectionBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateIntegratedCircuit extends
GateBase<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>
implements IGateLogic<GateIntegratedCircuit> {

    private int size;
    private GateBase<?, ?, ?, ?, ?, ?>[][] gates = null;
    private TileMultipart[][] tiles = null;

    private double border = 1 / 16D;

    public GateIntegratedCircuit(Integer size) {

        this.size = size;
        gates = new GateBase<?, ?, ?, ?, ?, ?>[size][size];
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

    public int getSize() {

        return size;
    }

    public GateBase<?, ?, ?, ?, ?, ?>[][] getGates() {

        return gates;
    }

    public GateBase<?, ?, ?, ?, ?, ?> getGate(int x, int z) {

        if (x >= 0 && z >= 0 && x < getSize() && z < getSize())
            return gates[x][z];

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

    private void loadWorld() {

        FakeWorldIC.getInstance().setIC(this);
    }

    private void unloadWorld() {

        FakeWorldIC.getInstance().setIC(null);
    }

    private List<GateBase<?, ?, ?, ?, ?, ?>> allGates() {

        List<GateBase<?, ?, ?, ?, ?, ?>> l = new ArrayList<GateBase<?, ?, ?, ?, ?, ?>>();

        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                GateBase<?, ?, ?, ?, ?, ?> g = gates[x][z];
                if (g != null)
                    l.add(g);
            }
        }

        return l;
    }

    public void setGate(int x, int z, GateBase<?, ?, ?, ?, ?, ?> gate) {

        loadWorld();

        TileMultipart tmp = new TileMultipart();
        tmp.setWorldObj(FakeWorldIC.getInstance());
        tmp.xCoord = x;
        tmp.yCoord = 64;
        tmp.xCoord = z;

        tiles[x][z] = tmp;
        gates[x][z] = gate;

        gate.setFace(ForgeDirection.DOWN);

        tmp.addPart(gate);

        unloadWorld();
    }

    // Logic and interaction

    @Override
    public void doLogic() {

        loadWorld();

        for (IGate<?, ?, ?, ?, ?, ?> gate : allGates())
            gate.logic().doLogic();

        unloadWorld();
    }

    @Override
    public void tick() {

        loadWorld();

        for (IGate<?, ?, ?, ?, ?, ?> gate : allGates())
            gate.logic().tick();

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
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition mop = super.rayTrace(start, end);

        if (mop != null) {
            if (mop.getPart() == this)
                return new QMovingObjectPosition(mop, this, new Vec3dCube(0, 0, 0, 1, 2 / 16D, 1));
        }

        return mop;
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        Vec2d v = new Vec2d(hit.hitVec.xCoord - hit.blockX, hit.hitVec.zCoord - hit.blockZ).sub(0.5, 0.5).rotate(90 * -getRotation())
                .add(0.5, 0.5);

        int x = (int) (v.getX() * getSize());
        int z = (int) (v.getY() * getSize());

        if (getGate(x, z) == null && item != null && item.getItem() instanceof ItemPart) {
            IPart part = ((ItemPart) item.getItem()).createPart(item, player, getWorld(), hit);
            if (part instanceof GateBase<?, ?, ?, ?, ?, ?>) {
                setGate(x, z, (GateBase<?, ?, ?, ?, ?, ?>) part);
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

        IIcon[] icons = new IIcon[] { getIcon(ForgeDirection.DOWN), getIcon(ForgeDirection.UP), getIcon(ForgeDirection.WEST),
                getIcon(ForgeDirection.EAST), getIcon(ForgeDirection.NORTH), getIcon(ForgeDirection.SOUTH) };

        renderer.renderBox(new Vec3dCube(0, 0, 0, 1, 1 / 16D, 1), icons);

        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1, 2 / 16D, 1 / 16D), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 15 / 16D, 1, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(0, 1 / 16D, 0, 1 / 16D, 2 / 16D, 1), icons);
        renderer.renderBox(new Vec3dCube(15 / 16D, 1 / 16D, 0, 1, 2 / 16D, 1), icons);

        double scale = (1 - border * 2D) / getSize();

        renderer.addTransformation(new Scale(scale, scale, scale));

        for (int x = 0; x < getSize(); x++) {
            for (int z = 0; z < getSize(); z++) {
                GateBase<?, ?, ?, ?, ?, ?> gate = getGate(x, z);
                if (gate == null)
                    continue;

                int s = getSize();
                if (s % 2 == 1)
                    s -= 1;

                renderer.addTransformation(new Translation(-(s - x - 1), -1, -(s - z - 1)));
                gate.renderStatic(translation, renderer, renderBlocks, pass);
                renderer.addTransformation(new Translation(s - x - 1, 1, s - z - 1));
            }
        }

        return true;
    }

}
