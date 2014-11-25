package com.bluepowermod.part.tube;

import uk.co.qmunity.lib.vec.Vec3d;
import net.minecraft.util.IIcon;

import com.bluepowermod.client.renderers.IconSupplier;

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
    public void renderDynamic(Vec3d loc, double delta, int pass) {

    }
}
