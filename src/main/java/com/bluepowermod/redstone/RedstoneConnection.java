package com.bluepowermod.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

public class RedstoneConnection implements IConnection<IRedstoneDevice> {

    private IRedstoneDevice a, b;
    private ForgeDirection sideA, sideB;
    private ConnectionType type;
    private IConnection<IRedstoneDevice> complementary;

    public RedstoneConnection(IRedstoneDevice a, IRedstoneDevice b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type) {

        this.a = a;
        this.b = b;
        this.sideA = sideA;
        this.sideB = sideB;
        this.type = type;
    }

    public RedstoneConnection(IRedstoneDevice a, IRedstoneDevice b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type,
            IConnection<IRedstoneDevice> complementary) {

        this(a, b, sideA, sideB, type);
        this.complementary = complementary;
    }

    @Override
    public IRedstoneDevice getA() {

        return a;
    }

    @Override
    public IRedstoneDevice getB() {

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
    public IConnection<IRedstoneDevice> getComplementaryConnection() {

        if (complementary != null)
            return complementary;

        RedstoneConnection c = new RedstoneConnection(getB(), getA(), getSideB(), getSideA(), getType());

        complementary = c;
        c.complementary = this;

        return c;
    }

    @Override
    public void setComplementaryConnection(IConnection<IRedstoneDevice> con) {

        complementary = con;
    }

}
