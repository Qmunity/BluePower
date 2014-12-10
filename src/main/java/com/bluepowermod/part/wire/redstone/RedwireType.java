package com.bluepowermod.part.wire.redstone;

public enum RedwireType {

    BLUESTONE(false, false, 0x0000FF), RED_ALLOY(true, true, 0xFF0000), INFUSED_TESLATITE(true, false, 0xFF00FF);

    private boolean analog, loss;
    private int color;

    private RedwireType(boolean analog, boolean loss, int color) {

        this.analog = analog;
        this.loss = loss;
        this.color = color;
    }

    public boolean isAnalog() {

        return analog;
    }

    public boolean hasLoss() {

        return loss;
    }

    public String getName() {

        return name().toLowerCase().replace("_", "");
    }

    public int getColor() {

        return color;
    }

}
