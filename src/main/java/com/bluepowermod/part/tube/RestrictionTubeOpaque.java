package com.bluepowermod.part.tube;

import net.minecraft.util.IIcon;

import com.bluepowermod.client.renderers.IconSupplier;
import com.qmunity.lib.vec.Vec3d;

public class RestrictionTubeOpaque extends RestrictionTube {

    @Override
    public String getType() {

        return "restrictionTubeOpaque";
    }

    @Override
    public String getUnlocalizedName() {

        return "restrictionTubeOpaque";
    }

    @Override
    protected IIcon getSideIcon() {

        return IconSupplier.restrictionTubeSideOpaque;
    }

    @Override
    protected IIcon getNodeIcon() {

        return IconSupplier.restrictionTubeNodeOpaque;
    }

    @Override
    public void renderDynamic(Vec3d loc, int pass, float frame) {

    }
}
