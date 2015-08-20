package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.RayTracer;

import com.bluepowermod.part.gate.ic.GateIntegratedCircuit;

public class MessageICInteract extends LocatedPacket<MessageICInteract> {

    private String id;
    private double px, py, pz, lx, ly, lz;
    private float eh, p, yaw;

    public MessageICInteract(GateIntegratedCircuit gate, EntityPlayer player) {

        super(gate);
        for (Entry<String, IPart> e : gate.getParent().getPartMap().entrySet()) {
            if (e.getValue() == gate) {
                id = e.getKey();
                break;
            }
        }
        px = player.posX;
        py = player.posY;
        pz = player.posZ;
        lx = player.lastTickPosX;
        ly = player.lastTickPosY;
        lz = player.lastTickPosZ;
        eh = player.eyeHeight;
        p = player.rotationPitch;
        yaw = player.rotationYaw;
    }

    public MessageICInteract() {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        super.write(buffer);
        buffer.writeUTF(id);
        buffer.writeDouble(px);
        buffer.writeDouble(py);
        buffer.writeDouble(pz);
        buffer.writeDouble(lx);
        buffer.writeDouble(ly);
        buffer.writeDouble(lz);
        buffer.writeFloat(eh);
        buffer.writeFloat(p);
        buffer.writeFloat(yaw);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        super.read(buffer);
        id = buffer.readUTF();
        px = buffer.readDouble();
        py = buffer.readDouble();
        pz = buffer.readDouble();
        lx = buffer.readDouble();
        ly = buffer.readDouble();
        lz = buffer.readDouble();
        eh = buffer.readFloat();
        p = buffer.readFloat();
        yaw = buffer.readFloat();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        GateIntegratedCircuit gate = (GateIntegratedCircuit) MultipartCompatibility.getPartHolder(player.worldObj, x, y, z).getPartMap().get(id);
        double opx = player.posX;
        double opy = player.posY;
        double opz = player.posZ;
        double olx = player.lastTickPosX;
        double oly = player.lastTickPosY;
        double olz = player.lastTickPosZ;
        float oeh = player.eyeHeight;
        float op = player.rotationPitch;
        float oyaw = player.rotationYaw;
        player.posX = px;
        player.posY = py;
        player.posZ = pz;
        player.lastTickPosX = lx;
        player.lastTickPosY = ly;
        player.lastTickPosZ = lz;
        player.eyeHeight = eh;
        player.rotationPitch = p;
        player.rotationYaw = yaw;

        GateIntegratedCircuit.shouldActivate = true;
        gate.onActivated(player, gate.rayTrace(RayTracer.getStartVector(player), RayTracer.getEndVector(player)), player.getCurrentEquippedItem());
        GateIntegratedCircuit.shouldActivate = false;

        player.posX = opx;
        player.posY = opy;
        player.posZ = opz;
        player.lastTickPosX = olx;
        player.lastTickPosY = oly;
        player.lastTickPosZ = olz;
        player.eyeHeight = oeh;
        player.rotationPitch = op;
        player.rotationYaw = oyaw;
    }
}
