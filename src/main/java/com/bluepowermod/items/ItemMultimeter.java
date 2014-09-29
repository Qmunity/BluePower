package com.bluepowermod.items;

import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Koen Beckers (K4Unl)
 */
public class ItemMultimeter extends ItemBase {

    public ItemMultimeter(){

        super();

        setUnlocalizedName(Refs.MULTIMETER_NAME);
        setCreativeTab(CustomTabs.tabBluePowerPower);
        this.setTextureName(Refs.MODID + ":" + Refs.MULTIMETER_NAME);
        setMaxStackSize(1);
    }

    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float xC, float yC, float zC){

        if(!world.isRemote){
            TileEntity ent = world.getTileEntity(x, y, z);
            Block block = world.getBlock(x, y, z);
            if(ent != null){
                if(ent instanceof IBluePowered || block == BPBlocks.multipart){
                    IBluePowered machine = null;
                    if(block == BPBlocks.multipart){
                        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
                        BPPart part = compat.getClickedPart(new Vector3(x, y, z, world), new Vector3(xC, yC, zC), player, null);
                        if(part instanceof IBluePowered){
                            machine = (IBluePowered) part;
                        }else{
                            return false;
                        }
                    }else{
                        machine = (IBluePowered) ent;
                    }

                    List<String> messages = new ArrayList<String>();
                    messages.add("Charge: " + machine.getHandler().getAmpStored() + "/" + machine.getHandler().getMaxAmp() + "mA");



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
