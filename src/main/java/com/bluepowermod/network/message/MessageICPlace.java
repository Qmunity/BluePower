package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.part.gate.ic.GateIntegratedCircuit;

public class MessageICPlace extends LocatedPacket<MessageICPlace> {

    private String id;
    private double xPos, zPos;

    public MessageICPlace(GateIntegratedCircuit gate, double xPos, double zPos) {

        super(gate);
        for (Entry<String, IPart> e : gate.getParent().getPartMap().entrySet()) {
            if (e.getValue() == gate) {
                id = e.getKey();
                break;
            }
        }
        this.xPos = xPos;
        this.zPos = zPos;
    }

    public MessageICPlace() {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        super.write(buffer);
        buffer.writeUTF(id);
        buffer.writeDouble(xPos);
        buffer.writeDouble(zPos);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        super.read(buffer);
        id = buffer.readUTF();
        xPos = buffer.readDouble();
        zPos = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        GateIntegratedCircuit gate = (GateIntegratedCircuit) MultipartCompatibility.getPartHolder(player.worldObj, x, y, z).getPartMap().get(id);
        gate.activate(player, player.getCurrentEquippedItem(), xPos, zPos);
    }
}
