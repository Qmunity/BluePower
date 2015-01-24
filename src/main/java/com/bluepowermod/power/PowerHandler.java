package com.bluepowermod.power;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.bluepower.IPowerBase;
import com.bluepowermod.part.BPPart;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K4Unl)
 */
public class PowerHandler implements IPowerBase {

    private boolean      isMultipart;
    private BPPart       pTarget;
    private TileEntity   tTarget;
    private IBluePowered iTarget;
    private World        tWorld;

    private PowerNetwork pNetwork;

    private BluePowerTier tier;
    private boolean shouldUpdateNetworkOnNextTick = true;
    protected List<ForgeDirection> connectedSides;
    private   float                oldCurrent;

    public PowerHandler(TileEntity _target) {

        tTarget = _target;
        iTarget = (IBluePowered) _target;
        tWorld = _target.getWorldObj();
        connectedSides = new ArrayList<ForgeDirection>();
    }

    public PowerHandler(BPPart _target) {

        pTarget = _target;
        iTarget = (IBluePowered) _target;
        isMultipart = true;
        tWorld = _target.getWorld();
        connectedSides = new ArrayList<ForgeDirection>();
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

    }

    public World getWorld(){

        if(tWorld == null){
            if(isMultipart){
                tWorld = pTarget.getWorld();
            }else{
                if(tTarget != null){
                    tWorld = tTarget.getWorldObj();
                }
            }
        }
        return tWorld;
    }


    public float getAmpStored(){
        if(getNetwork() == null){
            //Fuck, we need to fix this..
            updateNetworkOnNextTick(0);
            return 0;
        }
        return getNetwork().getCurrentStored();
    }

    @Override
    public float getMaxAmp() {
        if(getNetwork() == null){
            //Fuck, we need to fix this..
            updateNetworkOnNextTick(0);
            return 0;
        }
        return getNetwork().getMaxStored();
    }

    @Override
    public void removeEnergy(float amp){

        if(getNetwork() == null) return;
        int compare = Float.compare(getNetwork().getCurrentStored() - amp, 0.0F);
        if(compare == 1) {
            getNetwork().setCurrentStored(getNetwork().getCurrentStored() - amp);
        }else{
            //Implode?

        }
    }

    @Override
    public void addEnergy(float amp){

        if(getNetwork() == null) return;
        int compare = Float.compare(getNetwork().getCurrentStored() + amp, getMaxAmp());
        if(compare == -1) {
            getNetwork().setCurrentStored(getNetwork().getCurrentStored() + amp);
        }else{
            //Explode?

        }
    }

    private void setEnergy(float amp){

        if(getNetwork() == null) return;
        getNetwork().setCurrentStored(amp);
    }

    @Override
    public Vec3i getBlockLocation() {

        if(isMultipart) {
            return new Vec3i(pTarget.getX(), pTarget.getY(), pTarget.getZ(), getWorld());
        }else{
            return new Vec3i(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord, getWorld());
        }
    }

    public void setNetwork(PowerNetwork newNetwork) {
        pNetwork = newNetwork;
    }

    public void updateNetworkOnNextTick(float _oldCurrent){
        if(getWorld() != null && !getWorld().isRemote){
            shouldUpdateNetworkOnNextTick = true;
            this.pNetwork = null;
            this.oldCurrent = _oldCurrent;
        }
    }

    @Override
    public void update(){
        if(shouldUpdateNetworkOnNextTick){
            shouldUpdateNetworkOnNextTick = false;
            updateNetwork(this.oldCurrent);
        }
    }

    public PowerNetwork getNetwork() {
        return pNetwork;
    }

    public void updateNetwork(float oldCurrent) {
        PowerNetwork newNetwork = null;
        PowerNetwork foundNetwork = null;
        PowerNetwork endNetwork = null;
        //This block can merge networks!
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            if(iTarget.canConnectTo(dir)){
                foundNetwork = PowerNetwork.getNetworkInDir(getBlockLocation(), dir);
                if(foundNetwork != null){
                    if(endNetwork == null){
                        endNetwork = foundNetwork;
                    }else{
                        newNetwork = foundNetwork;
                    }
                    connectedSides.add(dir);
                }

                if(newNetwork != null && endNetwork != null){
                    //Hmm.. More networks!? What's this!?
                    endNetwork.mergeNetwork(newNetwork);
                    newNetwork = null;
                }
            }
        }

        if(endNetwork != null){
            pNetwork = endNetwork;
            pNetwork.addMachine(iTarget, oldCurrent);
            BluePower.log.info("Found an existing network (" + pNetwork.getRandomNumber() + ") @ " + getBlockLocation().getX() + "," + getBlockLocation().getY() + "," + getBlockLocation().getZ());
        }else{
            pNetwork = new PowerNetwork(iTarget, oldCurrent);
            BluePower.log.info("Created a new network (" + pNetwork.getRandomNumber() + ") @ " + getBlockLocation().getX() + "," + getBlockLocation().getY() + "," + getBlockLocation().getZ());
        }
    }

    @Override
    public void invalidate(){
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER){
            getNetwork().removeMachine(iTarget);
        }
    }
}
