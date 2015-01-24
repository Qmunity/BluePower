package com.bluepowermod.item;

import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IChargable;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;

/**
 * @author Koen Beckers (K4Unl)
 */
public class ItemBattery extends ItemBase implements IChargable{

    //TODO: Make me have damage
    private int ampStored;


    public ItemBattery(){
        super();

        setUnlocalizedName(Refs.BATTERY_ITEM_NAME);
        setCreativeTab(BPCreativeTabs.power);
        this.setTextureName(Refs.MODID + ":" + Refs.BATTERY_ITEM_NAME);
        setMaxStackSize(1);
    }

    @Override
    public BluePowerTier getTier() {

        return BluePowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public float getAmpStored() {

        return ampStored;
    }

    @Override
    public float getMaxAmp() {

        return 0;
    }

    @Override
    public void removeEnergy(float amp) {

    }

    @Override
    public void addEnergy(float amp) {

    }
}
