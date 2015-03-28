package com.bluepowermod.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.wire.redstone.IBundledDevice;

public class BundledConnection implements IConnection<IBundledDevice> {

    private IBundledDevice a, b;
    private ForgeDirection sideA, sideB;
    private ConnectionType type;
    private IConnection<IBundledDevice> complementary;

    public BundledConnection(IBundledDevice a, IBundledDevice b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type) {

        this.a = a;
        this.b = b;
        this.sideA = sideA;
        this.sideB = sideB;
        this.type = type;
    }

    public BundledConnection(IBundledDevice a, IBundledDevice b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type,
            IConnection<IBundledDevice> complementary) {

        this(a, b, sideA, sideB, type);
        this.complementary = complementary;
    }

    @Override
    public IBundledDevice getA() {

        return a;
    }

    @Override
    public IBundledDevice getB() {

        return b;
    }

    @Override
    public ForgeDirection getSideA() {

        return sideA;
    }

    @Override
    public ForgeDirection getSideB() {

        return sideB;
    }

    @Override
    public ConnectionType getType() {

        return type;
    }

    @Override
    public IConnection<IBundledDevice> getComplementaryConnection() {

        if (complementary != null)
            return complementary;

        BundledConnection c = new BundledConnection(getB(), getA(), getSideB(), getSideA(), getType());

        complementary = c;
        c.complementary = this;

        return c;
    }

    @Override
    public void setComplementaryConnection(IConnection<IBundledDevice> con) {

        complementary = con;
    }

}
