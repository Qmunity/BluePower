package com.bluepowermod.power;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.bluepower.IBluePowered;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Koen Beckers (K4Unl)
 */
public class PowerNetwork {

    public static class networkEntry {
        private Vec3i blockLocation;

        public networkEntry(Vec3i _location){

            blockLocation = _location;
        }

        public Vec3i getLocation(){

            return blockLocation;
        }
    }

    private int randomNumber = 0;

    private float currentStored = 0.0F;
    private List<networkEntry> machines;
    private IBlockAccess world;

    public PowerNetwork(IBluePowered machine, float beginCurrent){

        randomNumber = new Random().nextInt();

        machines = new ArrayList<networkEntry>();
        machines.add(new networkEntry(machine.getHandler().getBlockLocation()));

        currentStored = beginCurrent;
        world = ((PowerHandler)machine.getHandler()).getWorld();
    }

    public int getRandomNumber(){
        return randomNumber;
    }

    private int contains(IBluePowered machine){

        int i = 0;
        for(i=0; i < machines.size(); i++){
            if(machines.get(i).getLocation().equals(machine.getHandler().getBlockLocation())){
                return i;
            }
        }
        return -1;
    }

    public void addMachine(IBluePowered machine, float currentToAdd){

        if(contains(machine) == -1){
            float oAmp = currentStored * machines.size();
            oAmp += currentToAdd;
            machines.add(new networkEntry(machine.getHandler().getBlockLocation()));
            currentStored = oAmp / machines.size();

            if(world == null){
                world = ((PowerHandler)machine.getHandler()).getWorld();
            }
        }
    }

    public void removeMachine(IBluePowered machine){

        int machineIndex = contains(machine);
        if(machineIndex != -1){
            ((PowerHandler)machine.getHandler()).setNetwork(null);
            machines.remove(machineIndex);
        }

        //TODO: Redo me for multiparts
        for(networkEntry entry : machines){
            Vec3i loc = entry.getLocation();
            TileEntity ent = loc.getTileEntity();
            if(ent instanceof IBluePowered){
                IBluePowered target = ((IBluePowered) ent);
                ((PowerHandler)target.getHandler()).setNetwork(null);
                ((PowerHandler)machine.getHandler()).updateNetworkOnNextTick(getCurrentStored());
            }
        }
    }

    public void mergeNetwork(PowerNetwork toMerge){
        BluePower.log.info("Trying to merge network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
        if(toMerge.equals(this)) return;

        float newCurrent = ((currentStored - toMerge.getCurrentStored()) / 2) + toMerge.getCurrentStored();
        setCurrentStored(newCurrent);

        List<networkEntry> otherList = toMerge.getMachines();

        for(networkEntry entry : otherList){
            Vec3i loc = entry.getLocation();
            TileEntity ent = loc.getTileEntity();
            if(ent instanceof IBluePowered){
                IBluePowered machine = (IBluePowered) ent;
                ((PowerHandler)machine.getHandler()).setNetwork(this);
                this.addMachine(machine, newCurrent);
            }
        }

        BluePower.log.info("Merged network " + toMerge.getRandomNumber() + " into " + getRandomNumber());
    }

    public float getCurrentStored(){
        return currentStored;
    }

    public void setCurrentStored(float newCurrent){
        currentStored = newCurrent;
    }

    public List<networkEntry> getMachines(){
        return machines;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setFloat("currentStored", currentStored);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        currentStored = tagCompound.getFloat("currentStored");
    }

    public static PowerNetwork getNetworkInDir(Vec3i loc, ForgeDirection dir){

        TileEntity t = loc.getTileEntity();
        if(t instanceof IBluePowered){ //Just to be safe
            IBluePowered mEnt = (IBluePowered) t;


            PowerNetwork foundNetwork = null;
            Vec3i locationInDir = loc.getRelative(dir);
            if(locationInDir.getTileEntity() instanceof IBluePowered){
                foundNetwork = ((PowerHandler)((IBluePowered)locationInDir.getTileEntity()).getHandler()).getNetwork();
                return foundNetwork;
            }
        }

        return null;
    }


}
