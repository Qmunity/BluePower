package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map.Entry;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.raytrace.RayTracer;

import com.bluepowermod.part.gate.ic.GateIntegratedCircuit;
import com.bluepowermod.part.gate.ic.ICRegistry;
import com.bluepowermod.util.BPFakePlayer;

public class MessageICPlace extends LocatedPacket<MessageICPlace> {

    private String id;
    private int slot;
    private double xPos, zPos;

    public MessageICPlace(GateIntegratedCircuit gate, int slot, double xPos, double zPos) {

        super(gate);
        for (Entry<String, IPart> e : gate.getParent().getPartMap().entrySet()) {
            if (e.getValue() == gate) {
                id = e.getKey();
                break;
            }
        }
        this.slot = slot;
        this.xPos = xPos;
        this.zPos = zPos;
    }

    public MessageICPlace() {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        super.write(buffer);
        buffer.writeUTF(id);
        buffer.writeInt(slot);
        buffer.writeDouble(xPos);
        buffer.writeDouble(zPos);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        super.read(buffer);
        id = buffer.readUTF();
        slot = buffer.readInt();
        xPos = buffer.readDouble();
        zPos = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        GateIntegratedCircuit gate = (GateIntegratedCircuit) MultipartCompatibility.getPartHolder(player.worldObj, x, y, z).getPartMap().get(id);
        ItemStack stack = player.inventory.getStackInSlot(slot);// PartManager.getPartInfo("and").getStack();

        if (stack == null)
            return;

        EntityPlayer fakePlayer = FakePlayerFactory.get((WorldServer) gate.load(), BPFakePlayer.INTEGRATED_CIRCUIT);
        fakePlayer.worldObj = gate.load();
        fakePlayer.posX = xPos;
        fakePlayer.posY = 70;
        fakePlayer.posZ = zPos;
        fakePlayer.rotationPitch = 90;
        fakePlayer.rotationYaw = 0;
        fakePlayer.setCurrentItemOrArmor(0, stack);
        MovingObjectPosition mop = fakePlayer.worldObj.rayTraceBlocks(RayTracer.getCorrectedHeadVector(fakePlayer),
                RayTracer.getEndVector(fakePlayer, 10).toVec3());

        if (mop == null)
            return;

        try {
            System.out.println(ICRegistry.instance.placeOnIC(gate.load(), mop, fakePlayer, stack));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
