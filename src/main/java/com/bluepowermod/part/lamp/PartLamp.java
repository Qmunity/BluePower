/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.lamp;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.BPPartFace;
import com.bluepowermod.part.wire.redstone.PartRedwireFreestanding;
import com.bluepowermod.part.wire.redstone.WireCommons;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Base class for the lamps that are multiparts.
 *
 * @author Koen Beckers (K4Unl), Amadornes
 *
 */
public abstract class PartLamp extends BPPartFace implements IPartRedstone, IFaceRedstoneDevice {

    protected final MinecraftColor color;
    protected final boolean inverted;

    protected byte power = 0;
    private byte[] input = new byte[6];

    private IRedstoneDevice[] devices = new IRedstoneDevice[6];

    /**
     * @author amadornes
     * @param colorName
     * @param colorVal
     * @param inverted
     *            TODO
     */
    public PartLamp(MinecraftColor color, Boolean inverted) {

        this.color = color;
        this.inverted = inverted;
    }

    @Override
    public String getType() {

        return getLampType() + "." + color.name().toLowerCase() + (inverted ? ".inverted" : "");
    }

    protected abstract String getLampType();

    /**
     * @author amadornes
     */
    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    /**
     * @author amadornes
     */
    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        boxes.addAll(getSelectionBoxes());
    }

    /**
     * @author amadornes
     */
    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return getSelectionBoxes();
    }

    /**
     * @author amadornes
     */

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return Arrays.asList(new Vec3dCube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    }

    /**
     * @author Koen Beckers (K4Unl)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = (byte) 255;

        RenderHelper rh = RenderHelper.instance;
        rh.setRenderCoords(null, 0, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        Tessellator.instance.startDrawingQuads();
        renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 0);
        Tessellator.instance.draw();

        rh.reset();

        GL11.glPushMatrix();
        renderGlow(1);
        GL11.glPopMatrix();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
    }

    /**
     * @author Koen Beckers (K4Unl), Amadornes
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        RenderHelper renderer = RenderHelper.instance;
        renderer.reset();

        switch (getFace().ordinal()) {
        case 0:
            break;
        case 1:
            renderer.addTransformation(new Rotation(180, 0, 0, Vec3d.center));
            break;
        case 2:
            renderer.addTransformation(new Rotation(90, 0, 0, Vec3d.center));
            break;
        case 3:
            renderer.addTransformation(new Rotation(-90, 0, 0, Vec3d.center));
            break;
        case 4:
            renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
            break;
        case 5:
            renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
            break;
        }

        renderGlow(pass);

        renderer.resetTransformations();
    }

    /**
     * This render method gets called whenever there's a block update in the chunk. You should use this to remove load from the renderer if a part of
     * the rendering code doesn't need to get called too often or just doesn't change at all. To call a render update to re-render this just call
     * {@link com.bluepowermod.part.BPPart#markPartForRenderUpdate()}
     *
     * @param loc
     *            Distance from the player's position
     * @param pass
     *            Render pass (0 or 1)
     * @return Whether or not it rendered something
     */

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i loc, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        switch (getFace().ordinal()) {
        case 0:
            break;
        case 1:
            renderer.addTransformation(new Rotation(180, 0, 0, Vec3d.center));
            break;
        case 2:
            renderer.addTransformation(new Rotation(90, 0, 0, Vec3d.center));
            break;
        case 3:
            renderer.addTransformation(new Rotation(-90, 0, 0, Vec3d.center));
            break;
        case 4:
            renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
            break;
        case 5:
            renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
            break;
        }

        // Render base
        renderLamp(renderer);

        renderer.resetTransformations();

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderOnPass(int pass) {

        return true;
    }

    /**
     * Code to render the base portion of the lamp. Will not be colored
     *
     * @author Koen Beckers (K4Unl)
     * @param renderer
     * @param pass
     */
    @SideOnly(Side.CLIENT)
    public void renderLamp(RenderHelper renderer) {

    }

    /**
     * Code to render the actual lamp portion of the lamp. Will be colored
     *
     * @author Koen Beckers (K4Unl)
     * @param pass
     *            The pass that is rendered now. Pass 1 for solids. Pass 2 for transparents
     */
    @SideOnly(Side.CLIENT)
    public void renderGlow(int pass) {

    }

    @Override
    public int getLightValue() {

        return (inverted ? 15 - power : power);
    }

    @Override
    public void onAdded() {

        super.onAdded();

        WireCommons.refreshConnectionsRedstone(this);

        onUpdate();
    }

    /**
     * @author amadornes
     */
    @Override
    public void onUpdate() {

        recalculatePower();
    }

    private void recalculatePower() {

        if (getWorld().isRemote)
            return;

        int old = power;

        int pow = 0;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            IRedstoneDevice dev = getDeviceOnSide(d);
            if (dev != null) {
                pow = Math.max(pow, input[d.ordinal()] & 0xFF);
            } else {
                pow = Math.max(pow, MathHelper.map(RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d), 0, 15, 0, 255));
            }
        }
        power = (byte) pow;

        if (old != power)
            sendUpdatePacket();
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        return 0;
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        return 0;
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        return true;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("power", power);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        power = tag.getByte("power");
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setByte("power", power);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        power = tag.getByte("power");

        try {
            getWorld().updateLightByType(EnumSkyBlock.Block, getX(), getY(), getZ());
        } catch (Exception ex) {
        }
    }

    @Override
    public boolean canStay() {

        Vec3i loc = new Vec3i(this).add(getFace());

        if (MultipartCompatibility.getPartHolder(getWorld(), loc) != null)
            return MultipartCompatibility.getPart(getWorld(), loc, PartRedwireFreestanding.class) != null;

        return super.canStay();
    }

    /**
     * @author amadornes
     */
    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.lighting;
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return true;
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return true;
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side == getFace())
            return false;
        if (side == getFace().getOpposite())
            return false;
        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return true;
    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        return 0;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        input[side.ordinal()] = power;
    }

    @Override
    public void onRedstoneUpdate() {

        recalculatePower();
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        devices[side.ordinal()] = device;
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

}
