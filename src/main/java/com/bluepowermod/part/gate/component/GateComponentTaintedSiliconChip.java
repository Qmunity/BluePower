package com.bluepowermod.part.gate.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;

public class GateComponentTaintedSiliconChip extends GateComponentButton {

    public GateComponentTaintedSiliconChip(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    public GateComponentTaintedSiliconChip(GateBase<?, ?, ?, ?, ?, ?> gate, double x, double z) {

        super(gate, x, z);
    }

    @Override
    protected TextureAtlasSprite getIcon() {

        return getState() ? IconSupplier.taintedSiliconChipOn : IconSupplier.taintedSiliconChipOff;
    }

}
