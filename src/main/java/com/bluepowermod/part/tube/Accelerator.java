/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.tube;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.client.render.IconSupplier;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Accelerator extends PneumaticTube, as that's much easier routing wise.
 *
 * @author MineMaarten
 *
 */
public class Accelerator extends PneumaticTube implements IPartCustomPlacement {

    private ForgeDirection rotation = ForgeDirection.UP;

    @Override
    public String getType() {

        return "accelerator";
    }

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    public void setRotation(ForgeDirection rotation) {

        this.rotation = rotation;
    }

    /**
     * Gets all the occlusion boxes for this block
     *
     * @return A list with the occlusion boxes
     */
    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> aabbs = new ArrayList<Vec3dCube>();

        if (rotation == ForgeDirection.DOWN || rotation == ForgeDirection.UP) {
            aabbs.add(new Vec3dCube(0, 4 / 16D, 0, 1, 12 / 16D, 1));
        } else if (rotation == ForgeDirection.NORTH || rotation == ForgeDirection.SOUTH) {
            aabbs.add(new Vec3dCube(0, 0, 4 / 16D, 1, 1, 12 / 16D));
        } else {
            aabbs.add(new Vec3dCube(4 / 16D, 0, 0, 12 / 16D, 1, 1));
        }
        return aabbs;
    }

    @Override
    public void update() {

        super.update();
        TubeLogic logic = getLogic();
        for (TubeStack stack : logic.tubeStacks) {
            PneumaticTube tube = getPartCache(stack.heading);
            if (tube instanceof MagTube && isPowered()) {
                stack.setSpeed(1);
            } else {
                stack.setSpeed(TubeStack.ITEM_SPEED);
            }
        }

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("rotation", (byte) rotation.ordinal());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        rotation = ForgeDirection.getOrientation(tag.getByte("rotation"));
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(rotation.ordinal());
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        rotation = ForgeDirection.getOrientation(buffer.readInt());
    }

    @Override
    public boolean isConnected(ForgeDirection dir, PneumaticTube otherTube) {

        if (dir == rotation || dir.getOpposite() == rotation) {
            return getWorld() == null
                    || !MultipartCompatibility.checkOcclusion(getWorld(), getX(), getY(), getZ(), sideBB.clone().rotate(dir, Vec3d.center));
        } else {
            return false;
        }
    }

    private boolean isPowered() {

        return true;// TODO implement powah!
    }

    // @Override
    // @SideOnly(Side.CLIENT)
    // protected IIcon getSideIcon(ForgeDirection side) {
    //
    // return getPartCache(side) instanceof MagTube ? IconSupplier.magTubeSide : IconSupplier.pneumaticTubeSide;
    // }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon() {

        return IconSupplier.pneumaticTubeSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        GL11.glPushMatrix();
        {
            GL11.glTranslated(0, -0.125, 0);

            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            rotation = ForgeDirection.UP;
            renderDynamic(new Vec3d(0, 0, 0), 0, 0);
        }
        GL11.glPopMatrix();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d loc, double delta, int pass) {

        super.renderDynamic(loc, delta, pass);
        if (pass == 0) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            Tessellator t = Tessellator.instance;

            GL11.glPushMatrix();
            GL11.glTranslatef((float) loc.getX() + 0.5F, (float) loc.getY() + 0.5F, (float) loc.getZ() + 0.5F);
            if (rotation == ForgeDirection.NORTH || rotation == ForgeDirection.SOUTH) {
                GL11.glRotated(90, 1, 0, 0);
            } else if (rotation == ForgeDirection.EAST || rotation == ForgeDirection.WEST) {
                GL11.glRotated(90, 0, 0, 1);
            }
            GL11.glTranslatef((float) -loc.getX() - 0.5F, (float) -loc.getY() - 0.5F, (float) -loc.getZ() - 0.5F);

            t.startDrawingQuads();

            t.setColorOpaque_F(1, 1, 1);
            t.addTranslation((float) loc.getX(), (float) loc.getY(), (float) loc.getZ());

            IIcon icon = isPowered() ? IconSupplier.acceleratorFrontPowered : IconSupplier.acceleratorFront;

            double minX = icon.getInterpolatedU(0);
            double maxX = icon.getInterpolatedU(16);
            double minY = icon.getInterpolatedV(0);
            double maxY = icon.getInterpolatedV(16);

            t.setNormal(0, -1, 0);
            t.addVertexWithUV(0, 4 / 16D, 0, maxX, maxY);// minY
            t.addVertexWithUV(1, 4 / 16D, 0, minX, maxY);
            t.addVertexWithUV(1, 4 / 16D, 1, minX, minY);
            t.addVertexWithUV(0, 4 / 16D, 1, maxX, minY);

            t.setNormal(0, 1, 1);
            t.addVertexWithUV(0, 12 / 16D, 0, maxX, maxY);// maxY
            t.addVertexWithUV(0, 12 / 16D, 1, minX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 1, minX, minY);
            t.addVertexWithUV(1, 12 / 16D, 0, maxX, minY);

            icon = isPowered() ? IconSupplier.acceleratorSidePowered : IconSupplier.acceleratorSide;

            minX = icon.getInterpolatedU(4);
            maxX = icon.getInterpolatedU(12);
            minY = icon.getInterpolatedV(0);
            maxY = icon.getInterpolatedV(16);

            t.setNormal(0, 0, 1);
            t.addVertexWithUV(0, 4 / 16D, 1, maxX, minY);// maxZ
            t.addVertexWithUV(1, 4 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 1, minX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 1, minX, minY);

            t.setNormal(0, 0, -1);
            t.addVertexWithUV(0, 4 / 16D, 0, minX, maxY);// minZ
            t.addVertexWithUV(0, 12 / 16D, 0, maxX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 0, maxX, minY);
            t.addVertexWithUV(1, 4 / 16D, 0, minX, minY);

            t.setNormal(-1, 0, 0);
            t.addVertexWithUV(0, 4 / 16D, 0, maxX, minY);// minX
            t.addVertexWithUV(0, 4 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 1, minX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 0, minX, minY);

            t.setNormal(1, 0, 0);
            t.addVertexWithUV(1, 4 / 16D, 0, maxX, maxY);// maxX
            t.addVertexWithUV(1, 12 / 16D, 0, minX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 1, minX, minY);
            t.addVertexWithUV(1, 4 / 16D, 1, maxX, minY);

            icon = IconSupplier.acceleratorInside;

            minX = icon.getInterpolatedU(4);
            maxX = icon.getInterpolatedU(12);
            minY = icon.getInterpolatedV(4);
            maxY = icon.getInterpolatedV(12);

            t.addVertexWithUV(0, 4 / 16D, 6 / 16D, minX, minY);// inside maxZ
            t.addVertexWithUV(1, 4 / 16D, 6 / 16D, maxX, maxY);
            t.addVertexWithUV(1, 12 / 16D, 6 / 16D, maxX, maxY);
            t.addVertexWithUV(0, 12 / 16D, 6 / 16D, minX, minY);

            t.addVertexWithUV(0, 4 / 16D, 10 / 16D, minX, maxY);// inside minZ
            t.addVertexWithUV(0, 12 / 16D, 10 / 16D, minX, minY);
            t.addVertexWithUV(1, 12 / 16D, 10 / 16D, maxX, minY);
            t.addVertexWithUV(1, 4 / 16D, 10 / 16D, maxX, maxY);

            t.addVertexWithUV(10 / 16D, 4 / 16D, 0, minX, minY);// inside minX
            t.addVertexWithUV(10 / 16D, 4 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(10 / 16D, 12 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(10 / 16D, 12 / 16D, 0, minX, minY);

            t.addVertexWithUV(6 / 16D, 4 / 16D, 0, minX, minY);// inside maxX
            t.addVertexWithUV(6 / 16D, 12 / 16D, 0, minX, maxY);
            t.addVertexWithUV(6 / 16D, 12 / 16D, 1, maxX, maxY);
            t.addVertexWithUV(6 / 16D, 4 / 16D, 1, maxX, minY);

            t.addTranslation((float) -loc.getX(), (float) -loc.getY(), (float) -loc.getZ());
            t.draw();
            GL11.glPopMatrix();
        }

    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop, EntityPlayer player) {

        return new PartPlacementAccelerator(player);
    }

    @Override
    protected boolean shouldRenderFully() {

        return true;
    }

}
