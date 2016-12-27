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
 */

package com.bluepowermod.client.gui;

import com.bluepowermod.container.*;
import com.bluepowermod.container.inventory.InventoryItem;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.item.ItemCanvasBag;
import com.bluepowermod.item.ItemFloppyDisk;
import com.bluepowermod.item.ItemScrewdriver;
import com.bluepowermod.item.ItemSeedBag;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import com.bluepowermod.tile.tier2.TileRegulator;
import com.bluepowermod.tile.tier2.TileRetriever;
import com.bluepowermod.tile.tier2.TileSortingMachine;
import com.bluepowermod.tile.tier3.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GUIHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        // This function creates a container
        TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));
        // ID is the GUI ID
        switch (GuiIDs.values()[ID]) {
        case ALLOY_FURNACE:
            return new ContainerAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
        case BUFFER:
            return new ContainerBuffer(player.inventory, (TileBuffer) ent);
        case SORTING_MACHINE:
            return new ContainerSortingMachine(player.inventory, (TileSortingMachine) ent);
        case SEEDBAG:
            if ((!player.getHeldItemMainhand().isEmpty()) && player.getHeldItemMainhand().getItem() instanceof ItemSeedBag) {
                return new ContainerSeedBag(player.getHeldItemMainhand(), player.inventory, InventoryItem.getItemInventory(player,
                        player.getHeldItemMainhand(), "Seed Bag", 9));
            }
            break;
        case CANVAS_BAG:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemCanvasBag) {
                return new ContainerCanvasBag(player.getHeldItemMainhand(), player.inventory, InventoryItem.getItemInventory(player,
                        player.getHeldItemMainhand(), "Canvas Bag", 27));
            }
            break;
        case CPU:
            return new ContainerCPU(player.inventory, (TileCPU) ent);
        case MONITOR:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemScrewdriver) {
                return null;
            }
            return new ContainerMonitor(player.inventory, (TileMonitor) ent);
        case DISK_DRIVE: // TODO: this conditional will always be false (for fabricator77)
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemScrewdriver
            && player.getHeldItemMainhand().getItem() instanceof ItemFloppyDisk) {
                return null;
            }
            return new ContainerDiskDrive(player.inventory, (TileDiskDrive) ent);
        case IO_EXPANDER:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemScrewdriver) {
                return null;
            }
            return new ContainerIOExpander(player.inventory, (TileIOExpander) ent);

        case REDBUS_ID:
            return new ContainerRedbusID(player.inventory, (IRedBusWindow) ent);

        case KINETICGENERATOR_ID:
            return new ContainerKinect(player.inventory, (TileKinectGenerator) ent);
        case DEPLOYER_ID:
            return new ContainerDeployer(player.inventory, (TileDeployer) ent);
        case RELAY_ID:
            return new ContainerRelay(player.inventory, (TileRelay) ent);
        case EJECTOR_ID:
            return new ContainerEjector(player.inventory, (TileEjector) ent);
        case FILTER_ID:
            return new ContainerFilter(player.inventory, (TileFilter) ent);
        case RETRIEVER_ID:
            return new ContainerRetriever(player.inventory, (TileRetriever) ent);
        case REGULATOR_ID:
            return new ContainerRegulator(player.inventory, (TileRegulator) ent);
        case ITEMDETECTOR_ID:
            return new ContainerItemDetector(player.inventory, (TileItemDetector) ent);
        case PROJECTTABLE_ID:
            return new ContainerProjectTable(player.inventory, (TileProjectTable) ent);
        case CIRCUITTABLE_ID:
            return new ContainerCircuitTable(player.inventory, (TileCircuitTable) ent);
        case MANAGER_ID:
            return new ContainerManager(player.inventory, (TileManager) ent);
        case CIRCUITDATABASE_SHARING_ID:
            return new ContainerCircuitDatabaseSharing(player.inventory, (TileCircuitDatabase) ent);
        case CIRCUITDATABASE_MAIN_ID:
            return new ContainerCircuitDatabaseMain(player.inventory, (TileCircuitDatabase) ent);
        default:
            break;
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        TileEntity ent = world.getTileEntity(new BlockPos(x, y, z));
        // ID is the GUI ID
        switch (GuiIDs.values()[ID]) {
        case ALLOY_FURNACE:
            return new GuiAlloyFurnace(player.inventory, (TileAlloyFurnace) ent);
        case BUFFER:
            return new GuiBuffer(player.inventory, (TileBuffer) ent);
        case SORTING_MACHINE:
            return new GuiSortingMachine(player.inventory, (TileSortingMachine) ent);
        case SEEDBAG:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemSeedBag) {
                return new GuiSeedBag(player.getHeldItemMainhand(), player.inventory, InventoryItem.getItemInventory(player,
                        player.getHeldItemMainhand(), BPItems.seed_bag.getUnlocalizedName(), 9));
            }
        case CANVAS_BAG:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemCanvasBag) {
                return new GuiCanvasBag(player.getHeldItemMainhand(), player.inventory, InventoryItem.getItemInventory(player,
                        player.getHeldItemMainhand(), BPItems.canvas_bag.getUnlocalizedName(), 27));
            }
            break;
        case CPU:
            return new GuiCPU(player.inventory, (TileCPU) ent);
        case MONITOR:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemScrewdriver) {
                return null;
            }
            return new GuiMonitor(player.inventory, (TileMonitor) ent);
        case DISK_DRIVE: // TODO: this conditional will always be false (for fabricator77)
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemScrewdriver
            && player.getHeldItemMainhand().getItem() instanceof ItemFloppyDisk) {
                return null;
            }
            return new GuiDiskDrive(player.inventory, (TileDiskDrive) ent);
        case IO_EXPANDER:
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof ItemScrewdriver) {
                return null;
            }
            return new GuiIOExpander(player.inventory, (TileIOExpander) ent);
        case REDBUS_ID:
            return new GuiRedbusID(player.inventory, (IRedBusWindow) ent);
        case KINETICGENERATOR_ID:
            return new GuiKinect(player.inventory, (TileKinectGenerator) ent);
        case DEPLOYER_ID:
            return new GuiDeployer(player.inventory, (TileDeployer) ent);
        case RELAY_ID:
            return new GuiRelay(player.inventory, (TileRelay) ent);
        case EJECTOR_ID:
            return new GuiEjector(player.inventory, (TileEjector) ent);
        case FILTER_ID:
            return new GuiFilter(player.inventory, (TileFilter) ent);
        case RETRIEVER_ID:
            return new GuiRetriever(player.inventory, (TileRetriever) ent);
        case REGULATOR_ID:
            return new GuiRegulator(player.inventory, (TileRegulator) ent);
        case ITEMDETECTOR_ID:
            return new GuiItemDetector(player.inventory, (TileItemDetector) ent);
        case PROJECTTABLE_ID:
            return new GuiProjectTable(player.inventory, (TileProjectTable) ent);
        case CIRCUITTABLE_ID:
            return new GuiCircuitTable(player.inventory, (TileCircuitTable) ent);
        case MANAGER_ID:
            return new GuiManager(player.inventory, (TileManager) ent);
        case CIRCUITDATABASE_MAIN_ID:
            return new GuiCircuitDatabaseMain(player.inventory, (TileCircuitDatabase) ent);
        case CIRCUITDATABASE_SHARING_ID:
            return new GuiCircuitDatabaseSharing(player.inventory, (TileCircuitDatabase) ent);
        default:
            break;
        }
        return null;
    }
}
