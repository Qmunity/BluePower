package com.bluepowermod.power;

import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.vec.Vector3;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K4Unl)
 */
public class PowerNetwork {

    public static class networkEntry {
        private Vector3 blockLocation;

        public networkEntry(Vector3 _location){

            blockLocation = _location;
        }

        public Vector3 getLocation(){

            return blockLocation;
        }
    }


    private float currentStored = 0.0F;
    private List<networkEntry> machines;
    private IBlockAccess world;

    public PowerNetwork(IBluePowered machine, float beginCurrent){

        machines = new ArrayList<networkEntry>();
        machines.add(new networkEntry(machine.getHandler().getBlockLocation()));

        currentStored = beginCurrent;
        world = ((PowerHandler)machine.getHandler()).getWorld();
    }


}
