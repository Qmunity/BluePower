package com.bluepowermod;

import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.power.IPowerBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Koen Beckers (K4Unl)
 */
public class PowerHandler implements IPowerBase {

    private boolean      isMultipart;
    private BPPart       pTarget;
    private TileEntity   tTarget;
    private IBluePowered iTarget;
    private World        tWorld;

    private int ampStored = 0;
    private BluePowerTier tier;


    public void init(TileEntity _target) {

        tTarget = _target;
        iTarget = (IBluePowered) _target;
        tWorld = _target.getWorldObj();
    }

    public void init(BPPart _target) {

        pTarget = _target;
        iTarget = (IBluePowered) _target;
        tWorld = _target.getWorld();
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


    public int getAmpStored(){
        return ampStored;
    }

    public void removeEnergy(int amp){
        ampStored -= amp;
    }

    public void addEnergy(int amp){
        ampStored += amp;
    }
}
