package net.quetzi.bluepower.compat.fmp;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.PartRegistry;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.part.gate.ItemBPPart;
import net.quetzi.bluepower.references.Refs;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.JItemMultiPart;
import codechicken.multipart.TMultiPart;

public class ItemBPMultipart extends JItemMultiPart {
    
    public ItemBPMultipart() {
    
        super();
        setUnlocalizedName(Refs.MODID + ".part");
        setCreativeTab(CustomTabs.tabBluePowerCircuits);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack item) {
    
        return super.getUnlocalizedName(item);
    }
    
    @Override
    public TMultiPart newPart(ItemStack is, EntityPlayer player, World w,
            BlockCoord b, int unused, Vector3 unused1) {
    
        BPPart part = PartRegistry.createPartFromItem(is);
        
        if (part == null) return null;
        
        return new MultipartBPPart(part);
    }
    
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World w,
            int x, int y, int z, int side, float f, float f2, float f3) {
    
        if (super.onItemUse(stack, player, w, x, y, z, side, f, f2, f3)) {
            w.playSoundEffect(x + 0.5, y + 0.5, z + 0.5,
                    Block.soundTypeWood.soundName,
                    Block.soundTypeWood.getVolume(),
                    Block.soundTypeWood.getPitch());
            return true;
        }
        return false;
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public void getSubItems(Item unused, CreativeTabs tab, List l) {
    
        ItemBPPart.getSubItems(l);
    }
    
    @Override
    public boolean getHasSubtypes() {
    
        return true;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public void addInformation(ItemStack item, EntityPlayer par2EntityPlayer,
            List l, boolean par4) {
    }
    
}
