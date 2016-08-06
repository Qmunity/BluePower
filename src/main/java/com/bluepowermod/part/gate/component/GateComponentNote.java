package com.bluepowermod.part.gate.component;

import com.bluepowermod.part.gate.GateBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @author soniex2
 */
public class GateComponentNote extends GateComponentCubes {

    private String sound = "note.harp";
    private byte pitch = 0;

    /* maybe not doing it right */
    private ShiftingBuffer<Boolean> play = new ShiftingBuffer<Boolean>(1, 2, false);

    public GateComponentNote(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {
        super(gate, color);
        double yPos = 0;
        double xPos = 0.25;
        double zPos = 0.25;
        double scale = 1.0 / 2.0;
        getCubes().add(new Vec3dCube(xPos, yPos, zPos, xPos + scale, yPos + scale, zPos + scale, gate));
    }

    @Override
    public IIcon getIcon() {
        return Blocks.noteblock.getIcon(0, 0);
    }

    @Override
    public void tick() {

        super.tick();

        play.shift();

        GateBase<?, ?, ?, ?, ?, ?> gate = getGate();

        if (!play.get(0))
            return;
        play.set(0, false);

        Vec3d v = getCubes().get(0).getCenter();

        double x = gate.getX() + v.getX();
        double y = gate.getY() + v.getY();
        double z = gate.getZ() + v.getZ();

        float f = (float) Math.pow(2.0D, (double) (pitch - 12) / 12.0D);

        gate.getWorld().playSoundEffect(x, y, z, sound, 3.0F, f);
        gate.getWorld().spawnParticle("note", gate.getX() + v.getX(), gate.getY() + v.getY(), gate.getZ() + v.getZ(), (double) pitch / 24.0D, 0.0D, 0.0D);
    }

    public void playNote() {

        play.set(0, true);
        setNeedsSyncing(true);
    }

    public void setSound(String sound) {

        if (!this.sound.equals(sound)) {
            this.sound = sound;
            setNeedsSyncing(true);
        }
    }

    public void setPitch(byte pitch) {

        if (pitch < 0) pitch = 0;
        if (pitch > 24) pitch = 24;
        if (this.pitch != pitch) {
            this.pitch = pitch;
            setNeedsSyncing(true);
        }
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeUTF(sound);
        buffer.writeByte(pitch);
        buffer.writeBoolean(play.get(0));
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        sound = buffer.readUTF();
        pitch = buffer.readByte();
        if (pitch < 0) pitch = 0;
        if (pitch > 24) pitch = 24;
        play.set(0, buffer.readBoolean());
    }
}
