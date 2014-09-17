/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.items;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author MineMaarten
 */
public class ItemColorableOverlay extends ItemBase {
    
    private IIcon overlayTexture;
    
    public ItemColorableOverlay(String name) {
    
        setUnlocalizedName(name);
        setCreativeTab(CustomTabs.tabBluePowerItems);
        setTextureName(Refs.MODID + ":" + name);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
    
        super.registerIcons(iconRegister);
        overlayTexture = iconRegister.registerIcon(getIconString() + "_overlay");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item par1, CreativeTabs tab, List subItems) {
    
        for (int i = 0; i < 16; i++) {
            subItems.add(new ItemStack(this, 1, i));
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
    
        return renderPass == 0 || itemStack.getItemDamage() >= 16 ? super.getColorFromItemStack(itemStack, renderPass) : ItemDye.field_150922_c[itemStack.getItemDamage()];
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
    
        return true;
    }
    
    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int meta, int renderPass) {
    
        return renderPass == 0 || meta >= 16 ? itemIcon : overlayTexture;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
    
        return super.getUnlocalizedName() + "." + (stack.getItemDamage() >= 16 ? "empty" : ItemDye.field_150923_a[stack.getItemDamage()]);
    }
}
