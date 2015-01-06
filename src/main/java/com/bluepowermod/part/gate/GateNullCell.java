/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.wire.redstone.RedstoneApi;
import com.bluepowermod.part.wire.redstone.RedwireType;
import com.bluepowermod.part.wire.redstone.WireCommons;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateNullCell extends GateBase implements IFaceRedstoneDevice, IRedstoneConductor {

    private boolean analog = false;

    private byte powerA = 0, powerB = 0;
    private IRedstoneDevice[] devices = new IRedstoneDevice[6];
    private boolean[] nullcells = new boolean[6];

    public GateNullCell(Boolean analog) {

        this.analog = analog;
    }

    @Override
    public void initializeConnections() {

    }

    @Override
    public String getId() {

        return "nullcell" + (analog ? ".analog" : "");
    }

    @Override
    protected String getTextureName() {

        return "nullcell";
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void renderTop(float frame) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        double height = 2 / 16D;

        IIcon planks = Blocks.planks.getIcon(0, 0);
        IIcon wire = IconSupplier.wire;

        int col = isAnalog() ? RedwireType.INFUSED_TESLATITE.getColor() : RedwireType.BLUESTONE.getColor();

        renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D), planks);
        renderer.renderBox(new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D), planks);
        renderer.renderBox(new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D), planks);

        renderer.renderBox(new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D), planks);
        renderer.renderBox(new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D), planks);
        renderer.renderBox(new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D), planks);

        renderer.setColor(WireCommons.getColorForPowerLevel(col, getRotation() % 2 == 0 ? powerA : powerB));

        ForgeDirection dir = ForgeDirection.NORTH;
        if (getRotation() % 2 == 1)
            dir = dir.getRotation(getFace());

        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D), wire);
        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D,
                2 / 16D + (height / /* (nullcells[dir.ordinal()] ? 1 : */2/* ) */), 1 / 16D), wire);
        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D,
                2 / 16D + (height / (nullcells[dir.getOpposite().ordinal()] ? 1 : 2)), 16 / 16D), wire);

        renderer.setColor(WireCommons.getColorForPowerLevel(col, getRotation() % 2 == 0 ? powerB : powerA));

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

    @Override
    public void doLogic() {

    }

    @Override
    public void onUpdate() {

        if (RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;

        super.onUpdate();

        WireCommons.refreshConnectionsRedstone(this);

        for (ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
            if (s != getFace()) {
                WirePropagator.INSTANCE.onPowerLevelChange(this, s, (byte) 0, (byte) -1);
                break;
            }
        }
    }

    @Override
    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        super.addSelectionBoxes(boxes);
        addBoxes(boxes);
    }

    @Override
    public void addCollisionBoxes(List<Vec3dCube> boxes, Entity entity) {

        super.addCollisionBoxes(boxes, entity);
        addBoxes(boxes);
    }

    private void addBoxes(List<Vec3dCube> boxes) {

        double height = 2 / 16D;

        boxes.add(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D));

        boxes.add(new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D));
        boxes.add(new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D));

        boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D));
        boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + (height / 2), 1 / 16D));
        boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / 2), 16 / 16D));

        boxes.add(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 12 / 16D, 9 / 16D));
        boxes.add(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 10 / 16D, 7 / 16D, 14 / 16D, 12 / 16D, 9 / 16D));
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition mop = super.rayTrace(start, end);

        if (mop != null)
            mop = new QMovingObjectPosition(mop, mop.getPart(), Vec3dCube.merge(getSelectionBoxes()));

        return mop;
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        return true;
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return true;
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        return true;
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

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (d == getFace() || d == getFace().getOpposite())
                continue;
            if (d == side || d == side.getOpposite())
                return powerA;
            return powerB;
        }

        return 0;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (d == getFace() || d == getFace().getOpposite())
                continue;
            if (d == side || d == side.getOpposite()) {
                powerA = power;
                return;
            }
            powerB = power;
            return;
        }
    }

    @Override
    public void onRedstoneUpdate() {

        sendUpdatePacket();
    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public boolean hasLoss() {

        return false;
    }

    @Override
    public boolean isAnalog() {

        return analog;
    }

    @Override
    public Collection<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        if (fromSide == ForgeDirection.UNKNOWN)
            return Arrays.asList();

        IRedstoneDevice dev = getDeviceOnSide(fromSide.getOpposite());
        if (dev == null)
            return Arrays.asList();

        return Arrays.asList(new Pair<IRedstoneDevice, ForgeDirection>(dev, fromSide.getOpposite()));
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);

        tag.setByte("powerA", powerA);
        tag.setByte("powerB", powerB);

        for (int i = 0; i < 6; i++)
            tag.setBoolean("nullcell_" + i, devices[i] != null && devices[i] instanceof GateNullCell);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

        powerA = tag.getByte("powerA");
        powerB = tag.getByte("powerB");

        for (int i = 0; i < 6; i++)
            nullcells[i] = tag.getBoolean("nullcell_" + i);
    }

}