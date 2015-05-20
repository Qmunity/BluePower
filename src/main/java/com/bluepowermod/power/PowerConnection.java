package com.bluepowermod.power;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.power.IPowerBase;

public class PowerConnection implements IConnection<IPowerBase> {

    private IPowerBase a, b;
    private ForgeDirection sideA, sideB;
    private ConnectionType type;
    private IConnection<IPowerBase> complementary;

    public PowerConnection(IPowerBase a, IPowerBase b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type) {

        this.a = a;
        this.b = b;
        this.sideA = sideA;
        this.sideB = sideB;
        this.type = type;
    }

    public PowerConnection(IPowerBase a, IPowerBase b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type,
            IConnection<IPowerBase> complementary) {

        this(a, b, sideA, sideB, type);
        this.complementary = complementary;
    }

    @Override
    public IPowerBase getA() {

        return a;
    }

    @Override
    public IPowerBase getB() {

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
    public IConnection<IPowerBase> getComplementaryConnection() {

        if (complementary != null)
            return complementary;

        PowerConnection c = new PowerConnection(getB(), getA(), getSideB(), getSideA(), getType());

        complementary = c;
        c.complementary = this;

        return c;
    }

    @Override
    public void setComplementaryConnection(IConnection<IPowerBase> con) {

        complementary = con;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null)
            return false;
        if (!(obj instanceof PowerConnection))
            return false;

        PowerConnection c = (PowerConnection) obj;

        return getA().equals(c.getA()) && getB().equals(c.getB()) && getSideA() == c.getSideA() && getSideB() == c.getSideB()
                && getType() == c.getType();
    }

}
