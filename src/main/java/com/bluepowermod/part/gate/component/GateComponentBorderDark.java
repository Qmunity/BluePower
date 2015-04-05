package com.bluepowermod.part.gate.component;

import net.minecraft.util.IIcon;

import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateComponentBorderDark extends GateComponentCubes {

    public GateComponentBorderDark(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    @Override
    public int getColor() {

        return 0x555555;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon() {

        return getGate().getDarkTop();
    }

    @Override
    public double getHeight() {

        return 1 / 48D;
    }

}
