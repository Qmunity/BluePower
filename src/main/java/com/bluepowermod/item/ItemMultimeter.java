package com.bluepowermod.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

/**
 * @author Koen Beckers (K4Unl)
 */
public class ItemMultimeter extends ItemBase {

    public ItemMultimeter() {

        super();

        setUnlocalizedName(Refs.MULTIMETER_NAME);
        setCreativeTab(BPCreativeTabs.power);
        setTextureName(Refs.MODID + ":" + Refs.MULTIMETER_NAME);
        setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float xC, float yC, float zC) {

        if (!world.isRemote) {
            TileEntity ent = world.getTileEntity(x, y, z);
            Block block = world.getBlock(x, y, z);
            IPowered part = MultipartCompatibility.getPart(world, x, y, z, IPowered.class);
            if (ent != null) {
                if (ent instanceof IPowered || part != null) { // TODO: Add multipart checking
                    IPowered machine = null;

                    if (part == null) {
                        machine = (IPowered) ent;
                    } else {
                        machine = part;
                    }

                    List<String> messages = new ArrayList<String>();
                    if (machine.getPowerHandler(ForgeDirection.UNKNOWN) != null) {
                        messages.add(String.format("Charge: %.1f/%.1fV", machine.getPowerHandler(ForgeDirection.UNKNOWN).getVoltage(), machine
                                .getPowerHandler(ForgeDirection.UNKNOWN).getMaxVoltage()));
                    } else {
                        messages.add("No handler found!");
                    }

                    if (messages.size() > 0) {
                        for (String msg : messages) {
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
