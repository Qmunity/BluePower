package net.quetzi.bluepower.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileBase extends TileEntity
{

    private boolean isRedstonePowered;

    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound)
    {
        super.readFromNBT(tCompound);
        isRedstonePowered = tCompound.getBoolean("isRedstonePowered");
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound)
    {
        super.writeToNBT(tCompound);
        tCompound.setBoolean("isRedstonePowered", isRedstonePowered);
    }

    /**
     * Function gets called every tick.
     * Do not forget to call the super method!
     */
    @Override
    public void updateEntity()
    {
        super.updateEntity();
    }

    /***************** ADDED FUNCTIONS *****************/

    /**
     * Checks if redstone has changed.
     */
    public void checkRedstonePower()
    {
        boolean isIndirectlyPowered = getWorldObj().isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
        if (isIndirectlyPowered && !isRedstonePowered) {
            isRedstonePowered = true;
            this.redstoneChanged(isRedstonePowered);
        } else if (isRedstonePowered && !isIndirectlyPowered) {
            isRedstonePowered = false;
            this.redstoneChanged(isRedstonePowered);
        }
    }

    /**
     * This method can be overwritten to get alerted when the redstone level has changed.
     *
     * @param newValue The redstone level it is at now
     */
    protected void redstoneChanged(boolean newValue)
    { }

    /**
     * Check whether or not redstone level is high
     */
    public boolean getIsRedstonePowered()
    {
        return isRedstonePowered;
    }
}