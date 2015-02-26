package com.bluepowermod.item;

import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.iRechargeable;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.util.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Koen Beckers (K4Unl)
 */
public class ItemBattery extends ItemBase implements iRechargeable {

    private float ampStored;
    private int maxAmp;


    public ItemBattery(int maxAmp_){
        super();

        setUnlocalizedName(Refs.BATTERY_ITEM_NAME);
        setCreativeTab(BPCreativeTabs.power);
        this.setTextureName(Refs.MODID + ":" + Refs.BATTERY_ITEM_NAME);
        setMaxStackSize(1);
        setMaxDamage(maxAmp);
        maxAmp = maxAmp_;
        setNoRepair();
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

        return maxAmp;
    }

    @Override
    public float removeEnergy(float amp) {
        float oldAmp = ampStored;
        if(getAmpStored() - amp > 0){
            ampStored = ampStored - amp;
        }else{
            ampStored = 0;
        }
        return ampStored - oldAmp;
    }

    @Override
    public float addEnergy(float amp) {
        float oldAmp = ampStored;
        if(amp + getAmpStored() < getMaxAmp()){
            ampStored = ampStored + amp;
        }else{
            ampStored = getMaxAmp();
        }
        return ampStored - oldAmp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List){
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, getMaxDamage()/10));
    }
}
