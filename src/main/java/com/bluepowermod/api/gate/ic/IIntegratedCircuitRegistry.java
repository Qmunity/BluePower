package com.bluepowermod.api.gate.ic;

import net.minecraft.item.ItemStack;

public interface IIntegratedCircuitRegistry {

    public void registerHandler(IIntegratedCircuitHandler handler);

    public boolean canPlaceOnIntegratedCircuit(ItemStack stack);

}
