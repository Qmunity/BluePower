package com.bluepowermod.part.gate.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import com.bluepowermod.part.gate.GateBase;

public class GateComponentBorderDark extends GateComponentCubes {

    public GateComponentBorderDark(GateBase<?, ?, ?, ?, ?, ?> gate, int color) {

        super(gate, color);
    }

    @Override
    public int getColor() {

        return 0x555555;
    }

    @Override
    public TextureAtlasSprite getIcon() {

        return getGate().getDarkTop();
    }

    @Override
    public double getHeight() {

        return 1 / 48D;
    }

}
