package com.bluepowermod.part.tube;

import net.minecraft.util.IIcon;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.client.renderers.IconSupplier;

public class PneumaticTubeOpaque extends PneumaticTube {
    @Override
    public String getType() {

        return "pneumaticTubeOpaque";
    }

    @Override
    public String getUnlocalizedName() {

        return "pneumaticTubeOpaque";
    }

    @Override
    protected IIcon getSideIcon() {

        return IconSupplier.pneumaticTubeOpaqueSide;
    }

    @Override
    protected IIcon getNodeIcon() {

        return IconSupplier.pneumaticTubeOpaqueNode;
    }

    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {

    }
}
