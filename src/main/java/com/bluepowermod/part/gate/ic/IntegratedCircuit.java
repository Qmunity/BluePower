package com.bluepowermod.part.gate.ic;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.GateStateCell;

public abstract class IntegratedCircuit extends GateBase {
    
    private GateBase[][]  gates;
    private static double BORDER_WIDTH = 1 / 16D;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        clearGateArray();
    }
    
    private void clearGateArray() {
    
        gates = new GateBase[getCircuitWidth()][];
        for (int i = 0; i < gates.length; i++) {
            gates[i] = new GateBase[gates.length];
            
            for (int j = 0; j < gates[i].length; j++) {
                gates[i][j] = new GateStateCell();
            }
        }
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
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
    
    protected abstract int getCircuitWidth();
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
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
                        gate.renderTop(frame);
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
                GateBase gate = gates[i][j];
                for (FaceDirection dir : FaceDirection.values()) {
                    GateBase neighbor = getNeighbor(i, j, dir);
                    if (neighbor != null && neighbor.getConnection(dir.getOpposite()).isEnabled() && neighbor.getConnection(dir.getOpposite()).isOutput()) {
                        if (gate.getConnection(dir).isEnabled() && gate.getConnection(dir).isInput()) {
                            gate.getConnection(dir).setPower(neighbor.getConnection(dir.getOpposite()).getPower());
                        }
                    }
                }
                if (gate != null) {
                    gate.update();
                }
            }
        }
        reflectGates(front, left, back, right);
    }
    
    private GateBase getNeighbor(int x, int y, FaceDirection side) {
    
        if (side == FaceDirection.LEFT && x > 0) return gates[x - 1][y];
        if (side == FaceDirection.RIGHT && x < getCircuitWidth() - 1) return gates[x + 1][y];
        if (side == FaceDirection.FRONT && y > 0) return gates[x][y - 1];
        if (side == FaceDirection.BACK && y < getCircuitWidth() - 1) return gates[x][y + 1];
        return null;
    }
    
    private void reflectGates(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        int mid = getCircuitWidth() / 2;
        if (gates[mid][0] != null) {
            RedstoneConnection connection = gates[mid][0].getConnection(FaceDirection.FRONT);
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
            RedstoneConnection connection = gates[mid][getCircuitWidth() - 1].getConnection(FaceDirection.BACK);
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
        if (gates[0][mid] != null) {
            RedstoneConnection connection = gates[0][mid].getConnection(FaceDirection.LEFT);
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
            RedstoneConnection connection = gates[getCircuitWidth() - 1][mid].getConnection(FaceDirection.RIGHT);
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
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        for (int k = 0; k < gates.length; k++) {
            for (int j = 0; j < gates[k].length; j++) {
                if (gates[k][j] != null) {
                    GateBase gate = gates[k][j];
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
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        // TODO Auto-generated method stub
        
    }
    
}
