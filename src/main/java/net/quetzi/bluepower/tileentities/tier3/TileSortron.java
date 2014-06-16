package net.quetzi.bluepower.tileentities.tier3;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.quetzi.bluepower.tileentities.TileMachineBase;

import java.util.HashSet;
import java.util.Set;

public class TileSortron extends TileMachineBase implements IPeripheral {

    private Set<IComputerAccess> connectedComputers = new HashSet<IComputerAccess>();

    @Override
    public String getType() {

        return "BluePower.Sortron";
    }

    @Override
    public String[] getMethodNames() {

        return new String[]{"SortSlots", "SortPull", "SortSlot", };
    }

    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {

        return new Object[0];
    }

    @Override
    public void attach(IComputerAccess computer) {

        connectedComputers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {

        connectedComputers.remove(computer);
    }

    @Override
    public boolean equals(IPeripheral other) {

        return false;
    }
}
