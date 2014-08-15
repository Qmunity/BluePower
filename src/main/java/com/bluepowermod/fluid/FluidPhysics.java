package com.bluepowermod.fluid;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class FluidPhysics {

    public static void applyPhyisics(TileEntity te, int maxOut) {

        if (te == null)
            return;
        if (!(te instanceof IFluidHandler))
            return;

        IFluidHandler h = (IFluidHandler) te;

        TileEntity te2 = te.getWorldObj().getTileEntity(te.xCoord, te.yCoord - 1, te.zCoord);
        if (te2 == null)
            return;
        if (!(te2 instanceof IFluidHandler))
            return;
        IFluidHandler h2 = (IFluidHandler) te2;

        for (FluidTankInfo i : h.getTankInfo(ForgeDirection.DOWN)) {
            if (i.fluid == null || i.fluid.amount <= 0)
                continue;
            if (!h.canDrain(ForgeDirection.DOWN, i.fluid.getFluid()))
                continue;
            if (!h2.canFill(ForgeDirection.UP, i.fluid.getFluid()))
                continue;

            h.drain(ForgeDirection.DOWN, h2.fill(ForgeDirection.UP, new FluidStack(i.fluid.getFluid(), Math.min(i.fluid.amount, maxOut)), true), true);
        }
    }

}
