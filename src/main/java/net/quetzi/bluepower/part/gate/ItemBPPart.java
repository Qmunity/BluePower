package net.quetzi.bluepower.part.gate;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemBPPart extends Item {
    
    public ItemBPPart() {
    
        super();
        setUnlocalizedName(Refs.MODID + ".part");
        setCreativeTab(CustomTabs.tabBluePowerCircuits);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack item) {
    
        return getUnlocalizedName_(item);
    }
    
    public static String getUnlocalizedName_(ItemStack item) {
    
        return Refs.MODID + ".part";// TODO Unlocalized names for parts
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w,
            int x, int y, int z, int side, float f, float f2, float f3) {
    
        boolean flag = true;
        
        if (flag) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5,
                    Block.soundTypeWood.soundName,
                    Block.soundTypeWood.getVolume(),
                    Block.soundTypeWood.getPitch());
            
            // TODO Place part without FMP
            return true;
        }
        return false;
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {
    
        getSubItems(l);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void getSubItems(List l) {
    
        for (String id : PartRegistry.getRegisteredParts())
            l.add(PartRegistry.getItemForPart(id));
    }
    
    @Override
    public boolean getHasSubtypes() {
    
        return true;
    }
    
}
