package com.bluepowermod.part.gate.supported;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.wire.redstone.WireCommons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateNullCell
        extends
        GateSupported<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase> {

    private RedwireType typeA, typeB;
    private byte powerA, powerB;
    private boolean[] nullcells = new boolean[6];

    public GateNullCell(RedwireType typeA, RedwireType typeB) {

        this.typeA = typeA;
        this.typeB = typeB;
    }

    @Override
    protected String getGateType() {

        return "nullcell." + typeA.getName() + "." + typeB.getName();
    }

    // Redwire connectivity

    // Rendering

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        double height = 2 / 16D;

        IIcon wire = IconSupplier.wire;

        renderer.setColor(WireCommons.getColorForPowerLevel(typeA.getColor(), powerA));

        ForgeDirection dir = ForgeDirection.NORTH;
        if (getRotation() % 2 == 1)
            dir = dir.getRotation(getFace());

        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D), wire);
        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D,
                2 / 16D + (height / /* (nullcells[dir.ordinal()] ? 1 : */2/* ) */), 1 / 16D), wire);
        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D,
                2 / 16D + (height / (nullcells[dir.getOpposite().ordinal()] ? 1 : 2)), 16 / 16D), wire);

        renderer.setColor(WireCommons.getColorForPowerLevel(typeB.getColor(), powerB));

        ForgeDirection dir2 = ForgeDirection.WEST;
        if (getRotation() % 2 == 1)
            dir2 = dir2.getRotation(getFace());

        // if (!nullcells[dir2.ordinal()])
        renderer.renderBox(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 10 / 16D, 9 / 16D), wire);
        // if (!nullcells[dir2.getOpposite().ordinal()])
        renderer.renderBox(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 10 / 16D, 9 / 16D), wire);
        renderer.renderBox(new Vec3dCube(0 / 16D, 10 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D), wire);

        renderer.setColor(0xFFFFFF);

        renderer.resetTransformations();

        return true;
    }

}
