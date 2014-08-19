package com.bluepowermod.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.cast.ICast;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;

public class ItemCast extends ItemBase {

    public ItemCast() {

        setUnlocalizedName(Refs.CAST_NAME);
        setCreativeTab(CustomTabs.tabBluePowerItems);
        setTextureName(Refs.MODID + ":" + Refs.CAST_NAME);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {

        ICast cast = BPApi.getInstance().getCastRegistry().getCastFromStack(stack);

        if (cast == null)
            return null;

        return cast.getIcon();
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {

        return getIconIndex(stack);
    }

    @Override
    public boolean getHasSubtypes() {

        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {

        for (ICast c : BPApi.getInstance().getCastRegistry().getRegisteredCasts())
            list.add(createCast(c));
    }

    public static ItemStack createCast(ICast cast) {

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", cast.getCastType());
        ItemStack is = new ItemStack(BPItems.cast);
        is.stackTagCompound = tag;
        return is;
    }

}
