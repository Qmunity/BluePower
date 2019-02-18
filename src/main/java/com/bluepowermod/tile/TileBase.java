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

package com.bluepowermod.tile;

import com.bluepowermod.block.BlockContainerFacingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MineMaarten
 */
public class TileBase extends TileEntity implements IRotatable, ITickable {

    private boolean isRedstonePowered;
    private int outputtingRedstone;
    private int ticker = 0;

    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {

        super.readFromNBT(tCompound);
        isRedstonePowered = tCompound.getBoolean("isRedstonePowered");
        readFromPacketNBT(tCompound);
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tCompound) {

        super.writeToNBT(tCompound);
        tCompound.setBoolean("isRedstonePowered", isRedstonePowered);

        writeToPacketNBT(tCompound);
        return  tCompound;
    }

    /**
     * Tags written in here are synced upon markBlockForUpdate.
     * 
     * @param tCompound
     */
    protected void writeToPacketNBT(NBTTagCompound tCompound) {
        tCompound.setByte("outputtingRedstone", (byte) outputtingRedstone);
    }

    protected void readFromPacketNBT(NBTTagCompound tCompound) {
        outputtingRedstone = tCompound.getByte("outputtingRedstone");
        if (world != null)
            markForRenderUpdate();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tCompound = new NBTTagCompound();
        writeToPacketNBT(tCompound);
        return new SPacketUpdateTileEntity(pos, 0, tCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {

        readFromPacketNBT(pkt.getNbtCompound());
    }

    protected void sendUpdatePacket() {

        if (!world.isRemote)
        world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
    }

    protected void markForRenderUpdate() {

        if (world != null)
            world.markBlockRangeForRenderUpdate(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    protected void notifyNeighborBlockUpdate() {

        //world.notifyBlocksOfNeighborChange(pos, getBlockType());
    }

    /**
     * Function gets called every tick.
     */
    @Override
    public void update() {

        if (ticker == 0) {
            onTileLoaded();
        }
        ticker++;
    }

    /**
     * ************** ADDED FUNCTIONS ****************
     */

    public void onBlockNeighbourChanged() {

        checkRedstonePower();
    }

    /**
     * Checks if redstone has changed.
     */
    public void checkRedstonePower() {

        boolean isIndirectlyPowered = (getWorld().getRedstonePowerFromNeighbors(pos) != 0);
        if (isIndirectlyPowered && !getIsRedstonePowered()) {
            redstoneChanged(true);
        } else if (getIsRedstonePowered() && !isIndirectlyPowered) {
            redstoneChanged(false);
        }
    }

    /**
     * Before being able to use this, remember to mark the block as redstone emitter by calling BlockContainerBase#emitsRedstone()
     * 
     * @param newValue
     */
    public void setOutputtingRedstone(boolean newValue) {

        setOutputtingRedstone(newValue ? 15 : 0);
    }

    /**
     * Before being able to use this, remember to mark the block as redstone emitter by calling BlockContainerBase#emitsRedstone()
     * 
     * @param value
     */
    public void setOutputtingRedstone(int value) {

        value = Math.max(0, value);
        value = Math.min(15, value);
        if (outputtingRedstone != value) {
            outputtingRedstone = value;
            notifyNeighborBlockUpdate();
        }
    }

    public int getOutputtingRedstone() {

        return outputtingRedstone;
    }

    /**
     * This method can be overwritten to get alerted when the redstone level has changed.
     * 
     * @param newValue
     *            The redstone level it is at now
     */
    protected void redstoneChanged(boolean newValue) {

        isRedstonePowered = newValue;
    }

    /**
     * Check whether or not redstone level is high
     */
    public boolean getIsRedstonePowered() {

        return isRedstonePowered;
    }

    /**
     * Returns the ticker of the Tile, this number wll increase every tick
     * 
     * @return the ticker
     */
    public int getTicker() {

        return ticker;
    }

    /**
     * Gets called when the TileEntity ticks for the first time, the world is accessible and updateEntity() has not been ran yet
     */
    protected void onTileLoaded() {

        if (!world.isRemote)
            onBlockNeighbourChanged();
    }

    public List<ItemStack> getDrops() {

        return new ArrayList<ItemStack>();
    }

    @Override
    public void setFacingDirection(EnumFacing dir) {
        if(world.getBlockState(pos).getBlock() instanceof BlockContainerFacingBase) {
            BlockContainerFacingBase.setState(dir, world, pos);
            if (world != null) {
                sendUpdatePacket();
                notifyNeighborBlockUpdate();
            }
        }
    }

    @Override
    public EnumFacing getFacingDirection() {
        if(world.getBlockState(pos).getBlock() instanceof BlockContainerFacingBase) {
            return world.getBlockState(pos).getValue(BlockContainerFacingBase.FACING);
        }
        return EnumFacing.UP;
    }

    public boolean canConnectRedstone() {

        return false;
    }
}
