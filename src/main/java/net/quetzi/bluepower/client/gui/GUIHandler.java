package net.quetzi.bluepower.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.quetzi.bluepower.containers.ContainerAlloyFurnace;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;

public class GUIHandler implements IGuiHandler {
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    
        // This function creates a container
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent != null) {
            // ID is the GUI ID
            if (ID == GuiIDs.ALLOY_FURNACE.ordinal()) {
                if (ent instanceof TileAlloyFurnace) { // Just a sanity check.
                    return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
                }
            }
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
    
        TileEntity ent = world.getTileEntity(x, y, z);
        if (ent != null) {
            // ID is the GUI ID
            if (ID == GuiIDs.ALLOY_FURNACE.ordinal()) {
                if (ent instanceof TileAlloyFurnace) { // Just a sanity check.
                    return new GuiAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
                }
            }
        }
        return null;
    }
    
}
