package com.bluepowermod;

import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.api.BPApi.IBPApi;
import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.cast.ICastRegistry;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.IPartRegistry;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.api.tube.IPneumaticTube;
import com.bluepowermod.cast.CastRegistry;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.cable.bluestone.BluestoneApi;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.recipe.AlloyCrucibleRegistry;
import com.bluepowermod.util.Dependencies;

public class BluePowerAPI implements IBPApi {

    @Override
    public IMultipartCompat getMultipartCompat() {

        return (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
    }

    @Override
    public IPartRegistry getPartRegistry() {

        return PartRegistry.getInstance();
    }

    @Override
    public IPneumaticTube getPneumaticTube(TileEntity te) {

        PneumaticTube tube = getMultipartCompat().getBPPart(te, PneumaticTube.class);
        return tube != null ? tube.getLogic() : null;
    }

    @Override
    public IAlloyFurnaceRegistry getAlloyFurnaceRegistry() {

        return AlloyCrucibleRegistry.getInstance();
    }

    @Override
    public IBluestoneApi getBluestoneApi() {

        return BluestoneApi.getInstance();
    }

    @Override
    public ICastRegistry getCastRegistry() {

        return CastRegistry.getInstance();
    }

}
