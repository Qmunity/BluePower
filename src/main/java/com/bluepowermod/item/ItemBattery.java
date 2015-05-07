package com.bluepowermod.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.bluepowermod.api.power.IChargeable;
import com.bluepowermod.api.power.PowerTier;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 */
public class ItemBattery extends ItemBase implements IChargeable {

    private int maxAmp;

    public ItemBattery(int maxAmp_) {

        super();

        setUnlocalizedName(Refs.BATTERY_ITEM_NAME);
        setCreativeTab(BPCreativeTabs.power);
        this.setTextureName(Refs.MODID + ":" + Refs.BATTERY_ITEM_NAME);
        setMaxStackSize(1);
        maxAmp = maxAmp_;
        setMaxDamage(maxAmp);
        setNoRepair();
    }

    @Override
    public PowerTier getTier() {

        return PowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public float getAmpStored(ItemStack stack) {

        return maxAmp - stack.getItemDamage();
    }

    @Override
    public float getMaxAmp() {

        return maxAmp;
    }

    @Override
    public float drainEnergy(ItemStack stack, float amp, boolean simulated) {

        float oldAmp = getAmpStored(stack);
        if (getAmpStored(stack) - amp > 0) {
            stack.setItemDamage(getMaxDamage() - (int) (getAmpStored(stack) - amp));
        } else {
            stack.setItemDamage(maxAmp);
        }
        return getAmpStored(stack) - oldAmp;
    }

    @Override
    public float injectEnergy(ItemStack stack, float amp, boolean simulated) {

        float oldAmp = getAmpStored(stack);
        if (amp + getAmpStored(stack) < getMaxAmp()) {
            stack.setItemDamage(getMaxDamage() - (int) (getAmpStored(stack) + amp));
        } else {
            stack.setItemDamage(getMaxDamage());
        }
        return getAmpStored(stack) - oldAmp;
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {

        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, getMaxDamage()));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List infoList, boolean par4) {

        infoList.add("Power: " + Math.round(getAmpStored(stack)) + " mA");
    }
}
