package com.bluepowermod.item;

import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K4Unl)
 */
public class ItemMultimeter extends ItemBase {

    public ItemMultimeter(){

        super();

        setUnlocalizedName(Refs.MULTIMETER_NAME);
        setCreativeTab(BPCreativeTabs.power);
        this.setTextureName(Refs.MODID + ":" + Refs.MULTIMETER_NAME);
        setMaxStackSize(1);
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float xC, float yC, float zC){

        if(!world.isRemote){
            TileEntity ent = world.getTileEntity(x, y, z);
            Block block = world.getBlock(x, y, z);
            IBluePowered part = MultipartCompatibility.getPart(world, x, y, z, IBluePowered.class);
            if(ent != null){
                if(ent instanceof IBluePowered || part != null){ //TODO: Add multipart checking
                    IBluePowered machine = null;

                    if(part == null) {
                        machine = (IBluePowered) ent;
                    }else{
                        machine = part;
                    }

                    List<String> messages = new ArrayList<String>();
                    if(machine.getHandler() != null) {
                        messages.add("Charge: " + machine.getHandler().getAmpHourStored() + "/" + machine.getHandler().getMaxAmpHour() + "mAh");
                    }else{
                        messages.add("No handler found!");
                    }


                    if(messages.size() > 0){
                        for(String msg : messages){
                            player.addChatComponentMessage(new ChatComponentText(msg));
                        }
                    }

                    return true;
                }

            }
        }
        return false;
    }
}
