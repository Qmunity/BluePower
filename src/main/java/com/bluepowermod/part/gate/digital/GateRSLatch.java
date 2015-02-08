package com.bluepowermod.part.gate.digital;

import uk.co.qmunity.lib.misc.ShiftingBuffer;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.part.gate.component.GateComponentBorder;
import com.bluepowermod.part.gate.component.GateComponentTorch;
import com.bluepowermod.part.gate.component.GateComponentWire;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.util.Layout;

public class GateRSLatch extends GateSimpleDigital {

    private int mode = 0;

    private GateComponentTorch t1, t2;
    private GateComponentWire w1, w2;

    private ShiftingBuffer<Boolean> buf;

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

        addComponent(t1 = new GateComponentTorch(this, 0x0000FF, 4 / 16D, true).setState(mode == 1 || mode == 2));
        addComponent(t2 = new GateComponentTorch(this, 0x3e94dc, 4 / 16D, true).setState(mode == 0 || mode == 3));

        addComponent(w1 = new GateComponentWire(this, 0xFFF600, RedwireType.BLUESTONE).setPower((byte) (mode == 0 ? 255 : 0)));
        addComponent(w2 = new GateComponentWire(this, 0xFF0000, RedwireType.BLUESTONE).setPower((byte) (mode == 1 ? 255 : 0)));
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

        if (buf == null) {
            buf = new ShiftingBuffer<Boolean>(4, 2, false);
            buf.set(mode == 0 || mode == 3 ? 3 : 2, true);
        }

        buf.set(0, left().getInput());
        buf.set(1, right().getInput());
        w1.setPower((byte) (right().getInput() ? 255 : (t2.getState() ? 255 : 0)));
        w2.setPower((byte) (left().getInput() ? 255 : (t1.getState() ? 255 : 0)));
    }

    @Override
    public void tick() {

        if (getWorld().isRemote)
            return;

        if (buf == null) {
            buf = new ShiftingBuffer<Boolean>(4, 2, false);
            buf.set(mode == 0 || mode == 3 ? 3 : 2, true);
        }

        buf.shift();

        t1.setState(!buf.get(3) && !buf.get(1));
        t2.setState(!buf.get(2) && !buf.get(0));

        w1.setPower((byte) (right().getInput() ? 255 : (t2.getState() ? 255 : 0)));
        w2.setPower((byte) (left().getInput() ? 255 : (t1.getState() ? 255 : 0)));

        if (mode == 0 || mode == 1) {
            left().setOutput(t1.getState());
            right().setOutput(t2.getState());
        }

        front().setOutput((mode == 0 || mode == 3 ? t2 : t1).getState());
        back().setOutput((mode == 0 || mode == 3 ? t1 : t2).getState());

        buf.set(3, t2.getState());
        buf.set(2, t1.getState());
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

        mode++;
        if (mode >= 4)
            mode = 0;

        getComponents().clear();
        initConnections();
        initComponents();
        for (GateConnectionBase c : getConnections())
            if (c != null)
                c.notifyUpdate();
        buf = null;
        doLogic();

        return true;
    }

}
