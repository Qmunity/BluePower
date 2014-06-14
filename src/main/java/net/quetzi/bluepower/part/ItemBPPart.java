/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.part;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.references.Refs;

public class ItemBPPart extends Item {
    
    public ItemBPPart() {
    
        super();
        setUnlocalizedName(Refs.MODID + ".part");
    }
    
    public static String getUnlocalizedName_(ItemStack item) {
    
        return Refs.MODID + ".part." + PartRegistry.getPartIdFromItem(item);// TODO Unlocalized names for parts
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void getSubItems(List l) {
    
        for (String id : PartRegistry.getRegisteredParts())
            l.add(PartRegistry.getItemForPart(id));
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w, int x, int y, int z, int side, float f, float f2, float f3) {
    
        boolean flag = true;
        
        if (flag) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Block.soundTypeWood.soundName, Block.soundTypeWood.getVolume(),
                    Block.soundTypeWood.getPitch());
            
            // TODO Place part without FMP
            return true;
        }
        return false;
    }
    
    @Override
    public boolean getHasSubtypes() {
    
        return true;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack item) {
    
        return getUnlocalizedName_(item);
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {
    
        getSubItems(l);
    }
    
}
