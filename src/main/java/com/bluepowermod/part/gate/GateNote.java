package com.bluepowermod.part.gate;

import com.bluepowermod.api.block.IAdvancedSilkyRemovable;
import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentNote;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.world.NoteBlockEvent;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.misc.ShiftingBuffer;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.util.Dir;

import java.util.List;

/**
 * @author soniex2
 */
public class GateNote extends GateBase<GateConnectionDigital, GateConnectionDigital, GateConnectionDigital, GateConnectionDigital, GateConnectionDigital, GateConnectionAnalogue>
        implements IGateLogic<GateNote>, IAdvancedSilkyRemovable {

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
    public List<ItemStack> getDrops() {

        List<ItemStack> l = super.getDrops();

        if (instrument != null)
            l.add(instrument);

        return l;
    }

    @Override
    public boolean preSilkyRemoval(World world, int x, int y, int z) {

        return true;
    }

    @Override
    public void postSilkyRemoval(World world, int x, int y, int z) {

    }

    @Override
    public boolean writeSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {

        NBTTagCompound i = new NBTTagCompound();
        if (instrument != null)
            instrument.writeToNBT(i);
        tag.setTag("instrument", i);
        return false;
    }

    @Override
    public void readSilkyData(World world, int x, int y, int z, NBTTagCompound tag) {
        instrument = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("instrument"));
        updateNote();
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
        updateNote();
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

    private void updateNote() {
        if (instrument == null) {
            noteblock.setSound("note.harp");
            return;
        }

        Block b = Block.getBlockFromItem(instrument.getItem());

        Material m = b.getMaterial();

        if (m == Material.rock) {
            noteblock.setSound("note.bd");
        } else if (m == Material.sand) {
            noteblock.setSound("note.snare");
        } else if (m == Material.glass) {
            noteblock.setSound("note.hat");
        } else if (m == Material.wood) {
            noteblock.setSound("note.bassattack");
        } else if (m == Material.iron) { // why not?
            noteblock.setSound("note.pling");
        } else if (m == Material.cloth) { // heh
            noteblock.setSound("note.bass");
        } else if (m == Material.anvil) { // GET YOUR EARS READY
            noteblock.setSound("random.anvil_land");
        } else {
            noteblock.setSound("note.harp");
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition mop, ItemStack item) {

        if (item != null) {
            if (item.getItem() instanceof IScrewdriver && player.isSneaking()) {
                if (instrument != null) {
                    if (getWorld().isRemote)
                        return true;

                    IOHelper.spawnItemInWorld(getWorld(), instrument, getX() + 0.5, getY() + 0.5, getZ() + 0.5);

                    instrument = null;

                    noteblock.setSound("note.harp");

                    ((IScrewdriver) item.getItem()).damage(item, 1, player, false);

                    sendUpdatePacket();
                    return true;
                }
            } else {
                if (instrument == null) {
                    if (getWorld().isRemote)
                        return true;

                    instrument = item.splitStack(1);

                    updateNote();

                    if (player.capabilities.isCreativeMode)
                        item.stackSize++;

                    sendUpdatePacket();
                    return true;
                }
            }
        }

        return super.onActivated(player, mop, item);
    }

    private String noteToString(NoteBlockEvent.Octave o, NoteBlockEvent.Note n) {
        // REALLY lazy hack
        int i = 0;
        switch (o) {
            case LOW:
                i = 3;
                break;
            case MID:
                i = 4;
                break;
            case HIGH:
                i = 5;
                break;
        }
        if (n.ordinal() >= NoteBlockEvent.Note.C.ordinal()) {
            i++;
        }
        return n.name().charAt(0) + (n.name().indexOf("SHARP") > 0 ? "#" : "") + i;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> text) {
        // TODO
        text.add(noteblock.getSound());
        // lazy hack
        NoteBlockEvent dummy = new NoteBlockEvent.Change(null, 0, 0, 0, 0, 0, noteblock.getPitch());
        text.add(noteToString(dummy.getOctave(), dummy.getNote()));
    }
}
