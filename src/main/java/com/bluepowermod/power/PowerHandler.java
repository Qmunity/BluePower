package com.bluepowermod.power;

import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.bluepower.IPowerBase;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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

    private float ampStored = 0;
    private float maxAmp    = 0;
    private BluePowerTier tier;

    private List<ForgeDirection> pushedFrom;

    public PowerHandler(TileEntity _target, float _maxAmp) {

        tTarget = _target;
        iTarget = (IBluePowered) _target;
        tWorld = _target.getWorldObj();
        maxAmp = _maxAmp;
    }

    public PowerHandler(BPPart _target, float _maxAmp) {

        pTarget = _target;
        iTarget = (IBluePowered) _target;
        isMultipart = true;
        tWorld = _target.getWorld();
        maxAmp = _maxAmp;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        ampStored = tagCompound.getFloat("ampStored");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        tagCompound.setFloat("ampStored", ampStored);
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
        return ampStored;
    }

    @Override
    public float getMaxAmp() {

        return maxAmp;
    }

    public void removeEnergy(float amp){

        int compare = Float.compare(ampStored - amp, 0.0F);
        if(compare == 1) {
            ampStored -= amp;
        }else{
            //Implode?

        }
    }

    public void addEnergy(float amp){

        int compare = Float.compare(ampStored + amp, maxAmp);
        if(compare == -1) {
            ampStored += amp;
        }else{
            //Explode?

        }
    }

    @Override
    public Vector3 getBlockLocation() {

        if(isMultipart) {
            return new Vector3(pTarget.getX(), pTarget.getY(), pTarget.getZ());
        }else{
            return new Vector3(tTarget.xCoord, tTarget.yCoord, tTarget.zCoord);
        }
    }

    public void propagate(){

        if(pushedFrom == null) return;
        List<ForgeDirection> connectedDirs = new ArrayList<ForgeDirection>();
        //First check where we are connected:
        for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
            if(!pushedFrom.contains(dir)){
                if((getBlockLocation().getRelative(dir).hasTileEntity() && getBlockLocation().getRelative(dir).getTileEntity() instanceof IBluePowered)) {
                    connectedDirs.add(dir);
                }
            }
        }

    }
}
