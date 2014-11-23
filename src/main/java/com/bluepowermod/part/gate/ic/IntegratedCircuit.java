/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.gate.ic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.block.ISilkyRemovable;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.RedstoneConnection;
import com.bluepowermod.part.bluestone.WireBluestone;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.GateWire;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.ITilePartHolder;
import com.qmunity.lib.part.PartRegistry;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.util.Dir;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

public abstract class IntegratedCircuit extends GateBase implements ISilkyRemovable, ITilePartHolder {

    private GateBase[][] gates;
    private static double BORDER_WIDTH = 1 / 16D;

    @Override
    public void initializeConnections() {

        clearGateArray();
    }

    private void clearGateArray() {

        gates = new GateBase[getCircuitWidth()][];
        for (int i = 0; i < gates.length; i++) {
            gates[i] = new GateBase[gates.length];
        }
    }

    @Override
    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        super.addSelectionBoxes(boxes);
        double minY = 1 / 16D;
        double maxY = 2 / 16D + 0.01;

        double gateWidth = (1.0 - 2 * BORDER_WIDTH) / getCircuitWidth();
        for (int x = 0; x < getCircuitWidth(); x++) {
            for (int y = 0; y < getCircuitWidth(); y++) {
                boxes.add(new Vec3dCube(BORDER_WIDTH + x * gateWidth, minY, BORDER_WIDTH + y * gateWidth, BORDER_WIDTH + (x + 1) * gateWidth, maxY,
                        BORDER_WIDTH + (y + 1) * gateWidth));
            }
        }
    }

    public int getGateIndex(GateBase part) {

        for (int i = 0; i < getCircuitWidth(); i++) {
            for (int j = 0; j < getCircuitWidth(); j++) {
                if (part == gates[i][j]) {
                    return i * getCircuitWidth() + j;
                }
            }
        }
        throw new IllegalArgumentException("Part not found in Integrated Circuit");
    }

    public GateBase getPartForIndex(int index) {

        return gates[index / getCircuitWidth()][index % getCircuitWidth()];
    }

    @Override
    public String getId() {

        return "integratedCircuit" + getCircuitWidth() + "x" + getCircuitWidth();
    }

    @Override
    protected String getTextureName() {

        return "integrated";
    }

    protected abstract int getCircuitWidth();

    @Override
    protected void renderTop(float frame) {

        renderTop("front", front());
        renderTop("left", left());
        renderTop("back", back());
        renderTop("right", right());
        Vec3d loc = new Vec3d(0, 0, 0);

        GL11.glPushMatrix();
        {
            GL11.glTranslated(BORDER_WIDTH, 2 / 16D + 0.001D, BORDER_WIDTH);
            GL11.glScaled((1 - 2 * BORDER_WIDTH) / 1, 1, (1 - 2 * BORDER_WIDTH) / 1);

            GL11.glScaled(1.0 / getCircuitWidth(), 1.0 / getCircuitWidth(), 1.0 / getCircuitWidth());
            GL11.glTranslated(0, -2 / 16D, 0);
            for (GateBase[] gateArray : gates) {
                GL11.glPushMatrix();
                for (GateBase gate : gateArray) {
                    if (gate != null) {
                        GL11.glPushMatrix();
                        /* if (gate instanceof GateBase) {
                             gate.transformDynamic(loc);
                             gate.renderTop(frame);
                         } else {*/
                        gate.renderDynamic(loc, frame, 0);
                        //}
                        GL11.glPopMatrix();
                    }
                    GL11.glTranslated(0, 0, 1);
                }
                GL11.glPopMatrix();
                GL11.glTranslated(1, 0, 0);
            }
        }
        GL11.glPopMatrix();
    }

    @Override
    public void doLogic() {
        updateWires();
        for (int i = 0; i < gates.length; i++) {
            for (int j = 0; j < gates[i].length; j++) {
                GateBase gate = gates[i][j];
                if (gate != null) {
                    for (Dir dir : Dir.values()) {
                        if (dir == Dir.TOP || dir == Dir.BOTTOM)
                            continue;
                        GateBase neighbor = getNeighbor(i, j, dir);

                        ForgeDirection forgeDir = dir.getFD().getOpposite();

                        if (neighbor != null) {
                            Dir neighborDir = Dir.getDirection(ForgeDirection.UP, forgeDir, neighbor.getRotation()).getOpposite();
                            Dir gateDir = Dir.getDirection(ForgeDirection.UP, forgeDir, gate.getRotation());

                            if (neighbor.getConnection(neighborDir).isEnabled() && neighbor instanceof GateWire) {
                                if (gate.getConnection(gateDir).isEnabled() && !gate.getConnection(gateDir).isOutputOnly()) {
                                    gate.getConnection(gateDir).setInput(neighbor.getConnection(neighborDir).getOutput());
                                }
                            }
                        }
                    }
                    gate.update();
                }
            }
        }
        reflectGates();
    }

    @Override
    public void tick() {
        super.tick();
        for (GateBase[] gateArray : gates) {
            for (GateBase gate : gateArray) {
                if (gate != null) {
                    gate.tick();
                }
            }
        }
    }

    private GateBase getNeighbor(int x, int y, Dir faceDir) {

        if (faceDir == Dir.RIGHT && x > 0)
            return gates[x - 1][y];
        if (faceDir == Dir.LEFT && x < getCircuitWidth() - 1)
            return gates[x + 1][y];
        if (faceDir == Dir.BACK && y > 0)
            return gates[x][y - 1];
        if (faceDir == Dir.FRONT && y < getCircuitWidth() - 1)
            return gates[x][y + 1];
        return null;
    }

    private int getOffsetX(Dir faceDir) {

        return faceDir == Dir.RIGHT ? -1 : faceDir == Dir.LEFT ? 1 : 0;
    }

    private int getOffsetY(Dir faceDir) {

        return faceDir == Dir.BACK ? -1 : faceDir == Dir.FRONT ? 1 : 0;
    }

    private void updateWires() {

        List<GateWire> traversedWires = new ArrayList<GateWire>();
        for (int i = 0; i < getCircuitWidth(); i++) {
            for (int j = 0; j < getCircuitWidth(); j++) {
                if (gates[i][j] instanceof GateWire && !traversedWires.contains(gates[i][j])) {
                    int startSize = traversedWires.size();
                    int wirePowaah = traverseWire(traversedWires, i, j);// POWAHH!
                    /*
                     * for (int k = startSize; k < traversedWires.size(); k++) {//check for interfaces with the outside world GateWire wire =
                     * traversedWires.get(k); int index = getGateIndex(wire); int x = index / getCircuitWidth(); int y = index % getCircuitWidth();
                     * if(x == 0 ) }
                     */
                    for (int k = startSize; k < traversedWires.size(); k++) {
                        GateWire wire = traversedWires.get(k);
                        for (Dir dir : Dir.values()) {
                            wire.getConnection(dir).setOutput(wirePowaah);
                        }
                    }
                }
            }
        }
    }

    private int traverseWire(List<GateWire> traversedWires, int x, int y) {

        if (x < 0 || y < 0 || x >= getCircuitWidth() || y >= getCircuitWidth())
            return 0;
        int maxPowaah = 0;
        GateWire curWire = (GateWire) gates[x][y];
        traversedWires.add(curWire);
        for (Dir dir : Dir.values()) {

            GateBase neighbor = getNeighbor(x, y, dir);
            if (neighbor != null) {
                ForgeDirection forgeDir = null;
                switch (dir) {
                case FRONT:
                    forgeDir = ForgeDirection.SOUTH;
                    break;
                case BACK:
                    forgeDir = ForgeDirection.NORTH;
                    break;
                case LEFT:
                    forgeDir = ForgeDirection.EAST;
                    break;
                case RIGHT:
                    forgeDir = ForgeDirection.WEST;
                    break;
                }
                Dir neighborDir = Dir.getDirection(ForgeDirection.UP, forgeDir, neighbor.getRotation()).getOpposite();
                Dir gateDir = Dir.getDirection(ForgeDirection.UP, forgeDir, curWire.getRotation());
                if (curWire.getConnection(gateDir).isEnabled()) {
                    if (neighbor instanceof GateWire) {
                        if (!traversedWires.contains(neighbor) && neighbor.getConnection(neighborDir).isEnabled()) {
                            maxPowaah = Math.max(maxPowaah, traverseWire(traversedWires, x + getOffsetX(dir), y + getOffsetY(dir)));
                        }
                    } else {
                        RedstoneConnection connection = neighbor.getConnection(neighborDir);
                        if (connection.isEnabled())
                            maxPowaah = Math.max(maxPowaah, connection.getOutput());
                    }
                }
            }
        }
        return maxPowaah;
    }

    private void reflectGates() {

        int mid = getCircuitWidth() / 2;
        if (gates[0][mid] != null) {
            RedstoneConnection connection = gates[0][mid].getConnection(ForgeDirection.WEST);
            if (connection.isEnabled()) {
                left().enable();
            } else {
                left().disable();
            }
            connection.setInput(left().getInput());
            left().setOutput(connection.getOutput());
        } else {
            left().disable();
        }
        if (gates[getCircuitWidth() - 1][mid] != null) {
            RedstoneConnection connection = gates[getCircuitWidth() - 1][mid].getConnection(ForgeDirection.EAST);
            if (connection.isEnabled()) {
                right().enable();
            } else {
                right().disable();
            }
            connection.setInput(right().getOutput());
            right().setInput(connection.getOutput());
        } else {
            right().disable();
        }
        if (gates[mid][0] != null) {
            RedstoneConnection connection = gates[mid][0].getConnection(ForgeDirection.NORTH);
            if (connection.isEnabled()) {
                front().enable();
            } else {
                front().disable();
            }
            connection.setInput(front().getOutput());
            front().setInput(connection.getOutput());
        } else {
            front().disable();
        }
        if (gates[mid][getCircuitWidth() - 1] != null) {
            RedstoneConnection connection = gates[mid][getCircuitWidth() - 1].getConnection(ForgeDirection.SOUTH);
            if (connection.isEnabled()) {
                back().enable();
            } else {
                back().disable();
            }
            connection.setInput(back().getOutput());
            back().setInput(connection.getOutput());
        } else {
            back().disable();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        writeUpdateToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        readUpdateFromNBT(tag);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        for (int k = 0; k < gates.length; k++) {
            for (int j = 0; j < gates[k].length; j++) {
                if (gates[k][j] != null) {
                    GateBase gate = gates[k][j];
                    NBTTagCompound gateTag = new NBTTagCompound();
                    gateTag.setString("type", gate.getType());
                    gate.writeToNBT(gateTag);
                    tag.setTag("gate" + k + "" + j, gateTag);
                }
            }
        }
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        for (int k = 0; k < gates.length; k++) {
            for (int j = 0; j < gates[k].length; j++) {
                if (tag.hasKey("gate" + k + "" + j)) {
                    NBTTagCompound gateTag = tag.getCompoundTag("gate" + k + "" + j);

                    String type = gateTag.getString("type");
                    GateBase gate;
                    if (gates[k][j] != null && gates[k][j].getType().equals(type)) {
                        gate = gates[k][j];
                    } else {
                        if (type.equals(GateWire.ID)) {
                            gate = new GateWire();
                        } else {
                            gate = (GateBase) PartRegistry.createPart(type, false);
                        }
                        gates[k][j] = gate;
                        gates[k][j].parentCircuit = this;
                        gate.setParent(this);
                    }
                    gate.readFromNBT(gateTag);
                } else {
                    gates[k][j] = null;
                }
            }
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        List<Vec3dCube> aabbs = getSelectionBoxes();
        int subPartHit = aabbs.indexOf(hit.getCube());

        if (subPartHit <= 0)
            return super.onActivated(player, hit, item);

        subPartHit--;
        int x = getCircuitWidth() - 1 - subPartHit / getCircuitWidth();
        int y = getCircuitWidth() - 1 - subPartHit % getCircuitWidth();
        if (gates[x][y] != null) {
            if (gates[x][y].onActivated(player, hit, item)) {
                sendUpdatePacket();
                return true;
            } else {
                return false;
            }
        } else {
            return tryPlaceGate(player, x, y, item, hit);
        }
    }

    @Override
    public void onClicked(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        List<Vec3dCube> aabbs = getSelectionBoxes();
        int subPartHit = aabbs.indexOf(hit.getCube());

        if (subPartHit <= 0)
            return;

        subPartHit--;
        int x = getCircuitWidth() - 1 - subPartHit / getCircuitWidth();
        int y = getCircuitWidth() - 1 - subPartHit % getCircuitWidth();
        if (gates[x][y] != null) {
            if (!getWorld().isRemote) {
                ItemStack partStack = PartManager
                        .getPartInfo(gates[x][y] instanceof GateWire ? new WireBluestone().getType() : gates[x][y].getType()).getItem();
                getWorld().spawnEntityInWorld(new EntityItem(getWorld(), getX() + 0.5, getY() + 0.5, getZ() + 0.5, partStack));
                gates[x][y] = null;
                sendUpdatePacket();
            }
            gates[x][y] = null;

        }
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (GateBase[] gateArray : gates) {
            for (GateBase gate : gateArray) {
                if (gate != null) {
                    if (gate instanceof GateWire) {
                        //  gate = new WireBluestone(); TODO
                    }
                    ItemStack partStack = PartManager.getPartInfo(gate.getType()).getItem();
                    drops.add(partStack);
                }
            }
        }

        return drops;
    }

    private boolean tryPlaceGate(EntityPlayer player, int x, int y, ItemStack stack, MovingObjectPosition mop) {

        if (stack != null && stack.getItem() == BPItems.multipart) {
            BPPart part = PartManager.createPart(stack);
            if (part instanceof WireBluestone) {
                part = new GateWire();
            }
            if (part instanceof GateBase && !(part instanceof IntegratedCircuit)) {
                gates[x][y] = (GateBase) part;
                ((GateBase) part).setFace(ForgeDirection.DOWN);
                ;
                ((GateBase) part).parentCircuit = this;
                part.setParent(this);
                if (!player.capabilities.isCreativeMode)
                    stack.stackSize--;
                if (!getWorld().isRemote)
                    notifyUpdate();
                return true;
            }
        }
        return false;
    }

    @Override
    public void addWailaInfo(List<String> info) {

        // TODO Auto-generated method stub

    }

    /**
     * Return true if the ItemStack that's being 'injected' with info is a stack that can be injected. This method is only called when
     * itemStack.isItemEqual(otherStack) returned true.
     *
     * @param outputStack
     * @return false to disallow copying.
     */
    @Override
    public boolean canGoInCopySlot(ItemStack stack) {

        return true;
    }

    /**
     * Items that contain items (an Integrated Circuit with gates on it) need to compare the input and output, and tell which items are required. With
     * this method you can tell the Circuit Database what items the item carries, so it can calculate which items it needs.
     *
     * @param templateStack
     * @param outputStack
     * @return null is a valid return.
     */
    @Override
    public List<ItemStack> getItemsOnStack(ItemStack stack) {

        List<ItemStack> items = getDrops();
        items.remove(0); // remove the part itself
        return items;
    }

    @Override
    public List<IPart> getParts() {
        return null;
    }

    @Override
    public boolean canAddPart(IPart part) {
        return false;
    }

    @Override
    public void addPart(IPart part) {
    }

    @Override
    public boolean removePart(IPart part) {
        return false;
    }

    //@Override
    //TODO: Check me
    public void sendPartUpdate(IPart part) {
        sendUpdatePacket();
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, AxisAlignedBB bounds, Entity entity) {

    }

}
