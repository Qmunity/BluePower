/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.api.part;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.helper.RedstoneHelper;
import com.bluepowermod.api.part.redstone.IBPRedstonePart;
import com.bluepowermod.api.util.ForgeDirectionUtils;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.api.vec.Vector3Cube;

public abstract class BPPartFace extends BPPart implements IBPFacePart, IBPRedstonePart {

    private int face = 0;
    private int rotation = 0;

    protected RedstoneConnection[] connections = new RedstoneConnection[4];

    private final List<Vector3Cube> selectionBoxes = new ArrayList<Vector3Cube>();
    private final List<Vector3Cube> collisionBoxes = new ArrayList<Vector3Cube>();
    private final List<Vector3Cube> occlusionBoxes = new ArrayList<Vector3Cube>();

    @Override
    public int getFace() {

        return face;
    }

    @Override
    public void setFace(int face) {

        this.face = face;
        notifyUpdate();
    }

    @Override
    public int getRotation() {

        return rotation;
    }

    @Override
    public void setRotation(int rotation) {

        this.rotation = rotation % 4;
        notifyUpdate();
    }

    @Override
    public boolean canPlacePart(ItemStack is, EntityPlayer player, Vector3 block, MovingObjectPosition mop) {

        World world = block.getWorld();
        int x = block.getBlockX();
        int y = block.getBlockY();
        int z = block.getBlockZ();

        ForgeDirection dir = ForgeDirection.getOrientation(mop.sideHit).getOpposite();
        if (dir == ForgeDirection.DOWN) {
            dir = ForgeDirection.UP;
        } else {
            if (dir == ForgeDirection.UP)
                dir = ForgeDirection.DOWN;
        }

        setFace(ForgeDirectionUtils.getSide(dir));

        int rotation = 0;

        {
            double s1 = 0;
            double s2 = 0;

            switch (dir) {
            case WEST:
            case EAST:
                s1 = -(mop.hitVec.yCoord - player.posY - player.height);
                s2 = mop.hitVec.zCoord - player.posZ;
                break;
            case DOWN:
            case UP:
                s1 = mop.hitVec.xCoord - player.posX;
                s2 = mop.hitVec.zCoord - player.posZ;
                break;
            case NORTH:
            case SOUTH:
                s1 = mop.hitVec.xCoord - player.posX;
                s2 = -(mop.hitVec.yCoord - player.posY - player.height);
                break;
            default:
                break;
            }

            if (Math.abs(s1) >= Math.abs(s2)) {
                if (s1 >= 0) {
                    rotation = 3;
                } else {
                    rotation = 1;
                }
            } else {
                if (s2 >= 0) {
                    rotation = 0;
                } else {
                    rotation = 2;
                }
            }
        }

        setRotation(rotation);

        return world.isSideSolid(x, y, z, dir);
    }

    @Override
    public boolean canStay() {

        if (getWorld() == null)
            return true;

        ForgeDirection d = ForgeDirection.getOrientation(getFace());

        if (d == ForgeDirection.UP || d == ForgeDirection.DOWN)
            d = d.getOpposite();

        return getWorld().isSideSolid(getX() + d.offsetX, getY() + d.offsetY, getZ() + d.offsetZ, d.getOpposite());
    }

    @Override
    public final boolean canConnect(ForgeDirection side) {

        RedstoneConnection con = getConnection(FaceDirection.getDirection(ForgeDirection.getOrientation(getFace()), side, rotation));
        if (con == null)
            return false;

        return con.isEnabled();
    }

    @Override
    public final int getStrongOutput(ForgeDirection side) {

        return 0;
    }

    @Override
    public final int getWeakOutput(ForgeDirection side) {

        RedstoneConnection con = getConnection(side);
        if (con == null)
            return 0;

        ForgeDirection face = ForgeDirection.getOrientation(getFace()).getOpposite();

        int p = RedstoneHelper.setOutput(getWorld(), getX(), getY(), getZ(), side, face, con.getPower());

        return con.isOutput() ? p : 5;
    }

    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {

    }

    public void rotateAndTranslateDynamic(Vector3 loc, int pass, float frame) {

        GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());

        GL11.glTranslated(0.5, 0.5, 0.5);
        {

            switch (getFace()) {
            case 0:
                GL11.glRotated(180, 1, 0, 0);
                break;
            case 1:
                break;
            case 2:
                GL11.glRotated(90, 1, 0, 0);
                break;
            case 3:
                GL11.glRotated(90, -1, 0, 0);
                break;
            case 4:
                GL11.glRotated(90, 0, 0, -1);
                break;
            case 5:
                GL11.glRotated(90, 0, 0, 1);
                break;
            }
            GL11.glRotated(90 * (rotation == 0 || rotation == 2 ? (rotation + 2) % 4 : rotation), 0, 1, 0);
        }
        GL11.glTranslated(-0.5, -0.5, -0.5);
    }

    public void translateStatic(Vector3 loc, int pass) {

        Tessellator tess = Tessellator.instance;

        tess.addTranslation((float) loc.getX(), (float) loc.getY(), (float) loc.getZ());
    }

    public void undoTranslateStatic(Vector3 loc, int pass) {

        Tessellator tess = Tessellator.instance;

        tess.addTranslation((float) -loc.getX(), (float) -loc.getY(), (float) -loc.getZ());
    }

    public RedstoneConnection getConnection(FaceDirection dir) {

        if (dir == null)
            return null;

        try {
            return connections[dir.getDirection()];
        } catch (Exception ex) {
        }
        return null;
    }

    public RedstoneConnection getConnectionOrCreate(FaceDirection dir) {

        if (dir == null)
            return null;

        RedstoneConnection c = getConnection(dir);

        if (c == null)
            c = connections[dir.getDirection()] = new RedstoneConnection(this, dir.getDirection() + "");

        return c;
    }

    public RedstoneConnection getConnection(ForgeDirection dir) {

        return getConnection(FaceDirection.getDirection(ForgeDirection.getOrientation(getFace()), dir, rotation));
    }

    @Override
    public void update() {

        super.update();

        ForgeDirection face = ForgeDirection.getOrientation(getFace()).getOpposite();

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            RedstoneConnection rc = getConnection(d);
            if (rc != null && rc.isInput()) {
                rc.setPower(RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d, face));
            }
        }
    }

    @Override
    public final List<AxisAlignedBB> getCollisionBoxes() {

        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();

        ForgeDirection d = ForgeDirection.getOrientation(getFace());

        collisionBoxes.clear();

        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        addCollisionBoxes(boxes);
        for (AxisAlignedBB b : boxes) {
            Vector3Cube c = new Vector3Cube(b);

            c.getMin().subtract(0.5, 0.5, 0.5);
            c.getMax().subtract(0.5, 0.5, 0.5);
            c.getMin().rotate(0, Math.PI / 2 * getRotation(), 0);
            c.getMax().rotate(0, Math.PI / 2 * getRotation(), 0);
            c.getMin().add(0.5, 0.5, 0.5);
            c.getMax().add(0.5, 0.5, 0.5);

            c = c.rotate90Degrees(d);

            collisionBoxes.add(new Vector3Cube(c.getMin(), c.getMax()));
        }

        for (Vector3Cube c : collisionBoxes)
            aabbs.add(c.toAABB());

        return aabbs;
    }

    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {

    }

    @Override
    public final List<AxisAlignedBB> getSelectionBoxes() {

        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();

        ForgeDirection d = ForgeDirection.getOrientation(getFace());

        selectionBoxes.clear();

        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        addSelectionBoxes(boxes);
        for (AxisAlignedBB b : boxes) {
            Vector3Cube c = new Vector3Cube(b);

            c.getMin().subtract(0.5, 0.5, 0.5);
            c.getMax().subtract(0.5, 0.5, 0.5);
            c.getMin().rotate(0, Math.PI / 2 * getRotation(), 0);
            c.getMax().rotate(0, Math.PI / 2 * getRotation(), 0);
            c.getMin().add(0.5, 0.5, 0.5);
            c.getMax().add(0.5, 0.5, 0.5);

            c = c.rotate90Degrees(d);

            selectionBoxes.add(new Vector3Cube(c.getMin(), c.getMax()));
        }

        for (Vector3Cube c : selectionBoxes)
            aabbs.add(c.toAABB());

        return aabbs;
    }

    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

    }

    @Override
    public final List<AxisAlignedBB> getOcclusionBoxes() {

        List<AxisAlignedBB> aabbs = new ArrayList<AxisAlignedBB>();

        ForgeDirection d = ForgeDirection.getOrientation(getFace());

        occlusionBoxes.clear();
        List<AxisAlignedBB> boxes = new ArrayList<AxisAlignedBB>();
        addOcclusionBoxes(boxes);
        for (AxisAlignedBB b : boxes) {
            Vector3Cube c = new Vector3Cube(b);

            c.getMin().subtract(0.5, 0.5, 0.5);
            c.getMax().subtract(0.5, 0.5, 0.5);
            c.getMin().rotate(0, Math.PI / 2 * getRotation(), 0);
            c.getMax().rotate(0, Math.PI / 2 * getRotation(), 0);
            c.getMin().add(0.5, 0.5, 0.5);
            c.getMax().add(0.5, 0.5, 0.5);

            c = c.rotate90Degrees(d);

            occlusionBoxes.add(new Vector3Cube(c.getMin(), c.getMax()));
        }

        for (Vector3Cube c : occlusionBoxes)
            aabbs.add(c.toAABB());

        return aabbs;
    }

    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {

    }

}
