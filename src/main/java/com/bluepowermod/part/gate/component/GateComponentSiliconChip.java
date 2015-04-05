package com.bluepowermod.part.gate.component;

import net.minecraft.util.IIcon;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateComponentSiliconChip extends GateComponentButton {

    public GateComponentSiliconChip(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    public GateComponentSiliconChip(GateBase<?, ?, ?, ?, ?, ?> gate, double x, double z) {

        super(gate, x, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getIcon() {

        return getState() ? IconSupplier.siliconChipOn : IconSupplier.siliconChipOff;
    }

}
