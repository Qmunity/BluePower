package com.bluepowermod.part.wire;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IMicroblock;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.IPartPlacementFlat;
import uk.co.qmunity.lib.part.IPartRenderPlacement;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.util.Dir;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.ic.IIntegratedCircuitPart;
import com.bluepowermod.part.BPPartFaceRotate;
import com.bluepowermod.part.PartPlacementFaceRotateFlat;
import com.bluepowermod.part.PartRotationHelper;

public class PartSeparator extends BPPartFaceRotate implements IMicroblock, IPartRenderPlacement, IIntegratedCircuitPart {

    @Override
    public String getType() {

        return "separator";
    }

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    @Override
    public boolean canStay() {

        return true;
    }

    @Override
    public MicroblockShape getShape() {

        return MicroblockShape.EDGE;
    }

    @Override
    public int getSize() {

        return 1;
    }

    @Override
    public int getPosition() {

        return 0;
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return Arrays.asList(OcclusionHelper.getEdgeMicroblockBox(getSize(), getFace(), Dir.getDirection(ForgeDirection.NORTH, getFace(), 0)
                .toForgeDirection(getFace(), getRotation())));
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        ForgeDirection face = getFace();
        ForgeDirection side = ForgeDirection.NORTH;
        if (face == ForgeDirection.NORTH)
            side = ForgeDirection.UP;
        if (face == ForgeDirection.SOUTH)
            side = ForgeDirection.DOWN;

        return Arrays.asList(OcclusionHelper.getEdgeMicroblockBox(getSize(), face,
                Dir.getDirection(side, face, 0).toForgeDirection(face, getRotation())).expand(0.001));
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        for (Vec3dCube c : getSelectionBoxes())
            renderer.renderBox(c, Blocks.anvil.getIcon(0, 0));

        return true;
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop, EntityPlayer player) {

        int rot = PartRotationHelper.getPlacementRotation(mop);

        if (face == ForgeDirection.UP || face == ForgeDirection.NORTH || face == ForgeDirection.WEST)
            rot += 2;
        if (face == ForgeDirection.DOWN || face == ForgeDirection.EAST)
            if (rot == 0 || rot == 2)
                rot += 2;
        if (face == ForgeDirection.SOUTH)
            if (rot == 1 || rot == 3)
                rot += 2;

        return new PartPlacementSeparator(face.getOpposite(), rot % 4);
    }

    @Override
    public IPartPlacementFlat getFlatPlacement(IPart part, double hitX, double hitZ) {

        return new PartPlacementFaceRotateFlat((PartRotationHelper.getPlacementRotation(hitX, hitZ) + 2) % 4);
    }

    @Override
    public boolean canPlaceOnIntegratedCircuit() {

        return true;
    }

}
