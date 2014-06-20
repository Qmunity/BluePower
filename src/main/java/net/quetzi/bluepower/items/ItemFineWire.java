/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 *     
 *     @author Lumien
 */

package net.quetzi.bluepower.items;

import java.util.List;

import com.google.common.base.Preconditions;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.quetzi.bluepower.init.BPItems;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemFineWire extends Item {
    
    public enum WireType {
        IRON("iron", "ingotIron", 16777215), COPPER("copper", "ingotCopper", 13137203);
        
        String           name;
        int              colorModifier;
        String           material;
        public ItemStack is;
        
        WireType(String name, String material, int colorModifier) {
        
            this.name = name;
            this.colorModifier = colorModifier;
            this.material = material;
        }
    }
    
    IIcon[] icons;
    
    public ItemFineWire(String name) {
    
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setUnlocalizedName(name);
        this.setHasSubtypes(true);
        
        GameRegistry.registerItem(this, name);
    }
    
    public static void registerRecipes() {
    
        for (int i = 0; i < WireType.values().length; i++) {
            WireType wt = WireType.values()[i];
            wt.is = new ItemStack(BPItems.fine_wire, 1, i);
            GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BPItems.fine_wire, 1, i), new ItemStack(BPItems.diamond_drawPlate, 1,
                    OreDictionary.WILDCARD_VALUE), wt.material));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean requiresMultipleRenderPasses() {
    
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
    
        return icons[Preconditions.checkElementIndex(renderPass, 2)];
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister par1IconRegister) {
    
        icons = new IIcon[2];
        icons[0] = par1IconRegister.registerIcon(Refs.MODID + ":" + Refs.FINEWIRE_NAME + "_coil");
        icons[1] = par1IconRegister.registerIcon(Refs.MODID + ":" + Refs.FINEWIRE_NAME + "_wire");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack is, int renderPass) {
    
        if (renderPass == 1) {
            
            WireType wt = WireType.values()[Preconditions.checkElementIndex(is.getItemDamage(), WireType.values().length)];
            return wt.colorModifier;
        }
        return super.getColorFromItemStack(is, renderPass);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack is) {
    
        WireType wt = WireType.values()[Preconditions.checkElementIndex(is.getItemDamage(), WireType.values().length)];
        return this.getUnlocalizedName() + "." + wt.name;
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List itemList) {
    
        for (int i = 0; i < WireType.values().length; i++) {
            itemList.add(new ItemStack(this, 1, i));
        }
    }
}
