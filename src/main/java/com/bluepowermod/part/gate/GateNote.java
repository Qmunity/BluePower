package com.bluepowermod.part.gate;

import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentNote;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.util.Dir;

/**
 * @author soniex2
 */
public class GateNote extends GateBase<GateConnectionDigital, GateConnectionDigital, GateConnectionDigital, GateConnectionDigital, GateConnectionDigital, GateConnectionAnalogue>
        implements IGateLogic<GateNote> {

    private ShiftingBuffer<Byte> notebuf = new ShiftingBuffer<Byte>(1, 2, (byte) 0);
    private ShiftingBuffer<Boolean> signalbuf = new ShiftingBuffer<Boolean>(3, 2, false);

    private ItemStack instrument;
    private GateComponentNote noteblock;
    private boolean played;

    @Override
    public GateNote getGate() {

        return this;
    }

    @Override
    public void doLogic() {

        notebuf.set(0, back().getInput());

        signalbuf.set(0, front().getInput());
        signalbuf.set(1, left().getInput());
        signalbuf.set(2, right().getInput());
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        notebuf.shift();
        signalbuf.shift();

        noteblock.setPitch((byte) MathHelper.map(notebuf.get(0) & 255, 0, 256, 0, 25));

        boolean state = signalbuf.get(0) || signalbuf.get(1) || signalbuf.get(2);

        if (state && !played) {
            played = true;
            noteblock.playNote();
        }
        if (!state && played) {
            played = false;
        }
    }

    @Override
    protected String getGateType() {
        return "note";
    }

    @Override
    protected void initConnections() {

        top(new GateConnectionDigital(this, Dir.TOP));
        bottom(new GateConnectionDigital(this, Dir.BOTTOM));
        front(new GateConnectionDigital(this, Dir.FRONT));
        back(new GateConnectionAnalogue(this, Dir.BACK));
        left(new GateConnectionDigital(this, Dir.LEFT));
        right(new GateConnectionDigital(this, Dir.RIGHT));

        front().enable();
        back().enable();
        left().enable();
        right().enable();
    }

    @Override
    protected void initComponents() {

        addComponent(noteblock = new GateComponentNote(this, 0x0000FF));

        addComponent(new GateComponentWire(this, 0x18FF00, RedwireType.BLUESTONE).bind(front()));
        addComponent(new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.RED_ALLOY).bind(back()));
        addComponent(new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    public IGateLogic<GateNote> logic() {

        return this;
    }

    @Override
    public boolean changeMode() {

        return false;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        instrument = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("instrument"));
        notebuf.readFromNBT(tag, "notebuffer");
        signalbuf.readFromNBT(tag, "signalbuffer");
        played = tag.getBoolean("played");
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        NBTTagCompound i = new NBTTagCompound();
        if (instrument != null)
            instrument.writeToNBT(i);
        tag.setTag("instrument", i);
        notebuf.writeToNBT(tag, "notebuffer");
        signalbuf.writeToNBT(tag, "signalbuffer");
        tag.setBoolean("played", played);
    }
}
