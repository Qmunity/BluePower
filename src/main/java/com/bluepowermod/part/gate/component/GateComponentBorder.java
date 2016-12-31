package com.bluepowermod.part.gate.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.bluepowermod.part.gate.GateBase;

public class GateComponentBorder extends GateComponentCubes {

    public GateComponentBorder(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    @Override
    public TextureAtlasSprite getIcon() {

        return getGate().getDarkTop();
    }

    @Override
    public double getHeight() {

        return 1 / 64D;
    }

}
