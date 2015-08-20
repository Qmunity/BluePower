package com.bluepowermod.tile.tier2;


import com.bluepowermod.BluePower;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.reference.PowerConstants;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * @author Koen Beckers (K-4U)
 */
public class TileThermopile extends TileMachineBase implements IPowered {

    private int updateTemperatureTicks = 0;
    private double temperatureDifference;

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!getWorldObj().isRemote) {
            if(updateTemperatureTicks == 3){
                temperatureDifference = getTemperatureDifference();
                updateTemperatureTicks++;
            }else if(updateTemperatureTicks < 3){
                updateTemperatureTicks ++;
            }
            double addedEnergy = temperatureDifference * PowerConstants.THERMOPILE_MULTIPLIER;
            getPowerHandler(ForgeDirection.UNKNOWN).addEnergy(addedEnergy, false);
        }

    }

    @Override
    public void onNeighborBlockChanged() {
        super.onNeighborBlockChanged();
        updateTemperatureTicks = 0;
    }

    /**
     * @author Koen Beckers (K-4U)
     * @return double of the difference in temperature there exists between each block
     */
    private int getTemperatureDifference() {
        //Temperature is in kelvin.
        int lowestTemperature = Integer.MAX_VALUE;
        int temperature = 0;
        int amountOfBlocks = 0;
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            int x = xCoord + dir.offsetX;
            int y = yCoord + dir.offsetY;
            int z = zCoord + dir.offsetZ;

            Block toCheck = getWorld().getBlock(x, y, z);
            Fluid blockFluid = FluidRegistry.lookupFluidForBlock(toCheck);
            if(toCheck == Blocks.flowing_lava && blockFluid == null){
                blockFluid = FluidRegistry.LAVA;
            }
            if(toCheck == Blocks.flowing_water && blockFluid == null){
                blockFluid = FluidRegistry.WATER;
            }

            if(blockFluid != null){
                lowestTemperature = Math.min(blockFluid.getTemperature(getWorld(), x, y, z), lowestTemperature);
                temperature += blockFluid.getTemperature(getWorld(), x, y, z);
                amountOfBlocks++;
            }
            if(toCheck == Blocks.ice || toCheck == Blocks.packed_ice || toCheck == Blocks.snow){
                lowestTemperature = Math.min(273, lowestTemperature);
                temperature += 273;
                amountOfBlocks++;
            }
        }

        BluePower.log.info("We found " + amountOfBlocks + " L:" + lowestTemperature + " T:" + temperature);
        temperature = temperature - (lowestTemperature * amountOfBlocks);

        return temperature;
    }

}
