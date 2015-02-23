package com.bluepowermod.part.gate.digital;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.texture.Layout;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;

public class GateRSLatch extends GateSimpleDigital {

    private int mode = 0;

    private GateComponentTorch t1, t2;
    private GateComponentWire w1, w2;

    public GateRSLatch() {

    }

    @Override
    protected void initializeConnections() {

        front().reset().enable().setOutput(true);
        left().reset().enable();
        back().reset().enable();
        right().reset().enable();

        if (mode == 0)
            right().setOutput(true);
        else if (mode == 1)
            left().setOutput(true);
    }

    @Override
    protected void initComponents() {

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true));
        addComponent(t2 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, true).setState(true));

        addComponent(w1 = new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).setPower((byte) (mode == 3 || mode == 1 ? 255 : 0)));
        addComponent(w2 = new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).setPower((byte) (mode == 2 || mode == 0 ? 255 : 0)));
        addComponent(new GateComponentWire(this, 0xC600FF, RedwireType.BLUESTONE).bind(right()));
        addComponent(new GateComponentWire(this, 0x00FF00, RedwireType.BLUESTONE).bind(left()));

        addComponent(new GateComponentBorder(this, 0x7D7D7D));
    }

    @Override
    protected String getGateType() {

        return "rs";
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        GateConnectionDigital c1 = mode == 0 || mode == 3 ? left() : right();
        GateConnectionDigital c2 = mode == 0 || mode == 3 ? right() : left();

        boolean s1 = c2.getInput() || t2.getState();
        boolean s2 = c1.getInput() || t1.getState();

        t1.setState(!s1);
        t2.setState(!s2);

        (mode == 0 || mode == 2 ? w1 : w2).setPower((byte) (s2 ? 255 : 0));
        (mode == 0 || mode == 2 ? w2 : w1).setPower((byte) (s1 ? 255 : 0));

        front().setOutput(t2.getState());
        back().setOutput(t1.getState());
        if (mode == 0 || mode == 1) {
            c1.setOutput(t1.getState());
            c2.setOutput(t2.getState());
        } else {
            c1.setOutput(false);
            c2.setOutput(false);
        }
        c1.refresh();
        c2.refresh();
    }

    @Override
    public Layout getLayout() {

        Layout layout = super.getLayout();
        if (layout == null)
            return null;
        return layout.getSubLayout(mode);
    }

    @Override
    public boolean changeMode() {

        if (!getWorld().isRemote) {
            mode++;
            if (mode >= 4)
                mode = 0;

            getComponents().clear();
            initConnections();
            initComponents();
            doLogic();
            sendUpdatePacket();
        }

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("mode", mode);

        super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        int lastMode = mode;
        mode = tag.getInteger("mode");
        if (lastMode != mode) {
            getComponents().clear();
            initComponents();
        }

        super.readFromNBT(tag);
    }

    @Override
    public void writeUpdateData(DataOutput buffer, int channel) throws IOException {

        buffer.writeInt(mode);

        super.writeUpdateData(buffer, channel);
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        int lastMode = 0;
        mode = buffer.readInt();
        if (lastMode != mode) {
            getComponents().clear();
            initComponents();
        }

        super.readUpdateData(buffer, channel);
    }

}
