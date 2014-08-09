package com.bluepowermod.part.gate.ic;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.util.Dependencies;

public abstract class IntegratedCircuit extends GateBase {
    
    private BPPartFace[][] gates;
    private static double  BORDER_WIDTH = 1 / 16D;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        clearGateArray();
    }
    
    private void clearGateArray() {
    
        gates = new GateBase[getCircuitWidth()][];
        for (int i = 0; i < gates.length; i++) {
            gates[i] = new GateBase[gates.length];
        }
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addSelectionBoxes(boxes);
        double minY = 2 / 16D;
        double maxY = 3 / 16D;
        
        double gateWidth = (1.0 - 2 * BORDER_WIDTH) / getCircuitWidth();
        for (int x = 0; x < getCircuitWidth(); x++) {
            for (int y = 0; y < getCircuitWidth(); y++) {
                boxes.add(AxisAlignedBB.getBoundingBox(BORDER_WIDTH + x * gateWidth, minY, BORDER_WIDTH + y * gateWidth, BORDER_WIDTH + (x + 1) * gateWidth, maxY, BORDER_WIDTH + (y + 1) * gateWidth));
            }
        }
    }
    
    @Override
    public String getGateID() {
    
        return "integratedCircuit" + getCircuitWidth() + "x" + getCircuitWidth();
    }
    
    @Override
    protected String getTextureName() {
    
        return "integrated";
    }
    
    protected abstract int getCircuitWidth();
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, front);
        renderTopTexture(FaceDirection.LEFT, left);
        renderTopTexture(FaceDirection.BACK, back);
        renderTopTexture(FaceDirection.RIGHT, right);
        Vector3 loc = new Vector3(0, 0, 0);
        
        GL11.glPushMatrix();
        {
            GL11.glTranslated(BORDER_WIDTH, 2 / 16D + 0.001D, BORDER_WIDTH);
            GL11.glScaled((1 - 2 * BORDER_WIDTH) / 1, 1, (1 - 2 * BORDER_WIDTH) / 1);
            
            GL11.glScaled(1.0 / getCircuitWidth(), 1.0 / getCircuitWidth(), 1.0 / getCircuitWidth());
            GL11.glTranslated(0, -2 / 16D, 0);
            for (BPPartFace[] gateArray : gates) {
                GL11.glPushMatrix();
                for (BPPartFace gate : gateArray) {
                    if (gate != null) {
                        GL11.glPushMatrix();
                        if (gate instanceof GateBase) {
                            ((GateBase) gate).rotateAndTranslateDynamic(loc, 0, frame);
                            ((GateBase) gate).renderTop(frame);
                        } else {
                            gate.renderDynamic(loc, 0, frame);
                        }
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
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        for (int i = 0; i < gates.length; i++) {
            for (int j = 0; j < gates[i].length; j++) {
                BPPartFace gate = gates[i][j];
                if (gate != null) {
                    for (FaceDirection dir : FaceDirection.values()) {
                        BPPartFace neighbor = getNeighbor(i, j, dir);
                        
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
                        if (neighbor != null) {
                            FaceDirection neighborDir = FaceDirection.getDirection(ForgeDirection.UP, forgeDir, neighbor.getRotation()).getOpposite();
                            FaceDirection gateDir = FaceDirection.getDirection(ForgeDirection.UP, forgeDir, gate.getRotation());
                            
                            if (neighbor.getConnection(neighborDir).isEnabled() && neighbor.getConnection(neighborDir).isOutput()) {
                                if (gate.getConnection(gateDir).isEnabled() && gate.getConnection(gateDir).isInput()) {
                                    gate.getConnection(gateDir).setPower(neighbor.getConnection(neighborDir).getPower());
                                }
                            }
                        }
                    }
                    gate.update();
                }
            }
        }
        reflectGates(front, left, back, right);
    }
    
    private BPPartFace getNeighbor(int x, int y, FaceDirection faceDir) {
    
        if (faceDir == FaceDirection.RIGHT && x > 0) return gates[x - 1][y];
        if (faceDir == FaceDirection.LEFT && x < getCircuitWidth() - 1) return gates[x + 1][y];
        if (faceDir == FaceDirection.BACK && y > 0) return gates[x][y - 1];
        if (faceDir == FaceDirection.FRONT && y < getCircuitWidth() - 1) return gates[x][y + 1];
        return null;
    }
    
    private void reflectGates(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        int mid = getCircuitWidth() / 2;
        if (gates[0][mid] != null) {
            RedstoneConnection connection = gates[0][mid].getConnection(ForgeDirection.WEST);
            if (connection.isEnabled()) {
                left.enable();
            } else {
                left.disable();
            }
            if (connection.isInput()) {
                left.setInput();
                connection.setPower(left.getPower());
            } else {
                left.setOutput();
                left.setPower(connection.getPower());
            }
        } else {
            left.disable();
        }
        if (gates[getCircuitWidth() - 1][mid] != null) {
            RedstoneConnection connection = gates[getCircuitWidth() - 1][mid].getConnection(ForgeDirection.EAST);
            if (connection.isEnabled()) {
                right.enable();
            } else {
                right.disable();
            }
            if (connection.isInput()) {
                right.setInput();
                connection.setPower(right.getPower());
            } else {
                right.setOutput();
                right.setPower(connection.getPower());
            }
        } else {
            right.disable();
        }
        if (gates[mid][0] != null) {
            RedstoneConnection connection = gates[mid][0].getConnection(ForgeDirection.NORTH);
            if (connection.isEnabled()) {
                front.enable();
            } else {
                front.disable();
            }
            if (connection.isInput()) {
                front.setInput();
                connection.setPower(front.getPower());
            } else {
                front.setOutput();
                front.setPower(connection.getPower());
            }
        } else {
            front.disable();
        }
        if (gates[mid][getCircuitWidth() - 1] != null) {
            RedstoneConnection connection = gates[mid][getCircuitWidth() - 1].getConnection(ForgeDirection.SOUTH);
            if (connection.isEnabled()) {
                back.enable();
            } else {
                back.disable();
            }
            if (connection.isInput()) {
                back.setInput();
                connection.setPower(back.getPower());
            } else {
                back.setOutput();
                back.setPower(connection.getPower());
            }
        } else {
            back.disable();
        }
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        for (int k = 0; k < gates.length; k++) {
            for (int j = 0; j < gates[k].length; j++) {
                if (gates[k][j] != null) {
                    BPPartFace gate = gates[k][j];
                    NBTTagCompound gateTag = new NBTTagCompound();
                    
                    gateTag.setString("part_id", gate.getType());
                    
                    NBTTagCompound t = new NBTTagCompound();
                    gate.save(t);
                    gateTag.setTag("partData", t);
                    
                    gateTag.setInteger("face", gate.getFace());
                    gateTag.setInteger("rotation", gate.getRotation());
                    for (int i = 0; i < 4; i++) {
                        RedstoneConnection c = gate.getConnection(FaceDirection.getDirection(i));
                        if (c != null) {
                            gateTag.setTag("con_" + i, c.getNBTTag());
                        } else {
                            NBTTagCompound ta = new NBTTagCompound();
                            ta.setBoolean("___error", true);
                            gateTag.setTag("con_" + i, ta);
                        }
                    }
                    
                    tag.setTag("gate" + k + "" + j, gateTag);
                }
            }
        }
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        clearGateArray();
        for (int k = 0; k < gates.length; k++) {
            for (int j = 0; j < gates[k].length; j++) {
                if (tag.hasKey("gate" + k + "" + j)) {
                    NBTTagCompound gateTag = tag.getCompoundTag("gate" + k + "" + j);
                    
                    String type = gateTag.getString("part_id");
                    GateBase gate = (GateBase) PartRegistry.getInstance().createPart(type);
                    gates[k][j] = gate;
                    gates[k][j].isAttachedToIC = true;
                    
                    NBTTagCompound t = gateTag.getCompoundTag("partData");
                    gate.load(t);
                    
                    gate.setFace(gateTag.getInteger("face"));
                    gate.setRotation(gateTag.getInteger("rotation"));
                    for (int i = 0; i < 4; i++) {
                        RedstoneConnection c = gate.getConnection(FaceDirection.getDirection(i));
                        NBTTagCompound ta = gateTag.getCompoundTag("con_" + i);
                        if (ta.hasKey("___error") && ta.getBoolean("___error")) {
                            continue;
                        } else {
                            if (c == null) {
                                c = gate.getConnectionOrCreate(FaceDirection.getDirection(i));
                            }
                            c.load(ta);
                        }
                    }
                }
            }
        }
        if (getWorld() != null) {
            setWorld(getWorld());//Make sure the attached gates get all the info.
            setX(getX());
            setY(getY());
            setZ(getZ());
        }
    }
    
    @Override
    public boolean onActivated(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        int subPartHit = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getMOPData(mop);
        if (subPartHit == 0) return super.onActivated(player, mop, item);
        subPartHit--;
        int x = getCircuitWidth() - 1 - subPartHit / getCircuitWidth();
        int y = getCircuitWidth() - 1 - subPartHit % getCircuitWidth();
        if (gates[x][y] != null) {
            if (gates[x][y].onActivated(player, mop, item)) {
                return true;
            } else {
                return false;
            }
        } else {
            return tryPlaceGate(player, x, y, item);
        }
    }
    
    @Override
    public void click(EntityPlayer player, MovingObjectPosition mop, ItemStack item) {
    
        int subPartHit = ((IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP)).getMOPData(mop);
        if (subPartHit == 0) return;
        subPartHit--;
        int x = getCircuitWidth() - 1 - subPartHit / getCircuitWidth();
        int y = getCircuitWidth() - 1 - subPartHit % getCircuitWidth();
        if (gates[x][y] != null) {
            if (!getWorld().isRemote) {
                ItemStack partStack = PartRegistry.getInstance().getItemForPart(gates[x][y].getType());
                getWorld().spawnEntityInWorld(new EntityItem(getWorld(), getX() + 0.5, getY() + 0.5, getZ() + 0.5, partStack));
                gates[x][y] = null;
                sendUpdatePacket();
            }
            gates[x][y] = null;
            
        }
    }
    
    private boolean tryPlaceGate(EntityPlayer player, int x, int y, ItemStack stack) {
    
        if (stack != null && stack.getItem() == BPItems.multipart) {
            BPPart part = PartRegistry.getInstance().createPartFromItem(stack);
            if (part instanceof BPPartFace && !(part instanceof IntegratedCircuit)) {
                gates[x][y] = (BPPartFace) part;
                ((BPPartFace) part).setFace(1);
                ((BPPartFace) part).isAttachedToIC = true;
                part.setWorld(getWorld());
                part.setX(getX());
                part.setY(getY());
                part.setZ(getZ());
                if (!player.capabilities.isCreativeMode) stack.stackSize--;
                if (!getWorld().isRemote) notifyUpdate();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void setWorld(World world) {
    
        super.setWorld(world);
        for (BPPartFace[] gateArray : gates) {
            for (BPPartFace gate : gateArray) {
                if (gate != null) gate.setWorld(world);
            }
        }
    }
    
    @Override
    public void setX(int x) {
    
        super.setX(x);
        for (BPPartFace[] gateArray : gates) {
            for (BPPartFace gate : gateArray) {
                if (gate != null) gate.setX(x);
            }
        }
    }
    
    @Override
    public void setY(int y) {
    
        super.setY(y);
        for (BPPartFace[] gateArray : gates) {
            for (BPPartFace gate : gateArray) {
                if (gate != null) gate.setY(y);
            }
        }
    }
    
    @Override
    public void setZ(int z) {
    
        super.setZ(z);
        for (BPPartFace[] gateArray : gates) {
            for (BPPartFace gate : gateArray) {
                if (gate != null) gate.setZ(z);
            }
        }
    }
}
