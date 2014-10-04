package com.bluepowermod.part.tube;

import net.minecraft.util.IIcon;

import com.bluepowermod.client.renderers.IconSupplier;
import com.qmunity.lib.vec.Vec3d;

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
    public void renderDynamic(Vec3d loc, int pass, float frame) {

    }
}
