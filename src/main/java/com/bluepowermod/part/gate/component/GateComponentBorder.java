package com.bluepowermod.part.gate.component;

import net.minecraft.util.IIcon;

import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateComponentBorder extends GateComponentCubes {

    public GateComponentBorder(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {

        return getGate().getDarkTop();
    }

    @Override
    public double getHeight() {

        return 1 / 64D;
    }

}
