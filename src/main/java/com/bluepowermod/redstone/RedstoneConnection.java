package com.bluepowermod.redstone;

import net.minecraft.util.EnumFacing;;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

public class RedstoneConnection implements IConnection<IRedstoneDevice> {

    private IRedstoneDevice a, b;
    private EnumFacing sideA, sideB;
    private ConnectionType type;
    private IConnection<IRedstoneDevice> complementary;

    public RedstoneConnection(IRedstoneDevice a, IRedstoneDevice b, EnumFacing sideA, EnumFacing sideB, ConnectionType type) {

        this.a = a;
        this.b = b;
        this.sideA = sideA;
        this.sideB = sideB;
        this.type = type;
    }

    public RedstoneConnection(IRedstoneDevice a, IRedstoneDevice b, EnumFacing sideA, EnumFacing sideB, ConnectionType type,
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
    public EnumFacing getSideA() {

        return sideA;
    }

    @Override
    public EnumFacing getSideB() {

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

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;
        if (!(obj instanceof RedstoneConnection))
            return false;

        RedstoneConnection c = (RedstoneConnection) obj;

        return getA().equals(c.getA()) && getB().equals(c.getB()) && getSideA() == c.getSideA() && getSideB() == c.getSideB()
                && getType() == c.getType();
    }

}
