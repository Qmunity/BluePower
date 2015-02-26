package com.bluepowermod.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.BluePower;
import com.bluepowermod.api.misc.IScrewdriver;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.item.ItemPart;
import com.bluepowermod.item.ItemScrewdriver;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.supported.GateNullCell;
import com.bluepowermod.part.wire.redstone.PartRedwireFace;
import com.bluepowermod.part.wire.redstone.PartRedwireFace.PartRedwireFaceUninsulated;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class RecipeNullCell implements IRecipe {

    public static final RecipeNullCell instance = new RecipeNullCell();

    private RecipeNullCell() {

        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public boolean matches(InventoryCrafting inv, World w) {

        return getCraftingResult(inv) != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {

        return getCraftingResult(inv, null, false);
    }

    @Override
    public int getRecipeSize() {

        return 3 * 3;
    }

    @Override
    public ItemStack getRecipeOutput() {

        return GateNullCell.getStackWithData(new GateNullCell(null, false, null, false));
    }

    @SubscribeEvent
    public void onCraft(ItemCraftedEvent event) {

        getCraftingResult(event.craftMatrix, event.player, true);
    }

    private ItemStack getItemAt(IInventory inv, int x, int y) {

        return inv.getStackInSlot(y * 3 + x);
    }

    private ItemStack setItemAt(IInventory inv, int x, int y, ItemStack item) {

        inv.setInventorySlotContents(y * 3 + x, item);
        return item;
    }

    private ItemStack getCraftingResult(IInventory inv, EntityPlayer player, boolean isCrafting) {

        if (inv.getSizeInventory() < 4)
            return null;

        int centerX = 0;
        int centerY = 0;
        GateNullCell gnc = null;

        // Find the null cell
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ItemStack item = getItemAt(inv, x, y);
                if (item == null)
                    continue;
                if (!(item.getItem() instanceof ItemPart))
                    continue;
                IPart p = ((ItemPart) item.getItem()).createPart(item, BluePower.proxy.getPlayer(), null, null);
                if (p != null && p instanceof GateNullCell) {
                    gnc = (GateNullCell) p;
                    centerX = x;
                    centerY = y;
                    break;
                }
            }
            if (gnc != null)
                break;
        }

        // If there's no null cell, return null
        if (gnc == null)
            return null;

        // Make this recipe invalid if there's items where there shouldn't be
        {
            // To the right
            if (centerX < 2)
                for (int x = centerX + 1; x < 3; x++)
                    for (int y = 0; y < 3; y++)
                        if (getItemAt(inv, x, y) != null)
                            return null;
            // To the left - 1
            for (int x = 0; x < centerX - 1; x++)
                for (int y = 0; y < 3; y++)
                    if (getItemAt(inv, x, y) != null)
                        return null;

            // To the left
            if (centerX > 0) {
                if (getItemAt(inv, centerX - 1, 0) != null)
                    return null;
                if (getItemAt(inv, centerX - 1, 2) != null)
                    return null;
            }
        }

        // Determine what kind of crafting operation this is
        // Removing > adding
        {
            // Removing
            {
                // Screwdriver on top
                if (centerY > 0) {
                    ItemStack sd = getItemAt(inv, centerX, centerY - 1);
                    if (sd != null && sd.getItem() instanceof ItemScrewdriver) {
                        RedwireType t = gnc.getTypeB();
                        boolean bundled = gnc.isBundledB();
                        if (t != null) {
                            boolean can = true;
                            if (centerY < 2)
                                if (getItemAt(inv, centerX, centerY + 1) != null)
                                    can = false;
                            if (centerX > 0)
                                if (getItemAt(inv, centerX - 1, centerY) != null)
                                    can = false;
                            if (!((IScrewdriver) sd.getItem()).damage(sd, getItemAt(inv, centerX, centerY).stackSize, null, true))
                                can = false;

                            if (can) {
                                ItemStack wire = PartManager.getPartInfo("wire." + t.getName() + (bundled ? ".bundled" : "")).getStack(
                                        getItemAt(inv, centerX, centerY).stackSize);
                                if (isCrafting) {
                                    gnc = new GateNullCell(gnc.getTypeA(), gnc.isBundledA(), null, false);
                                    ItemStack nullCellStack = GateNullCell.getStackWithData(gnc);
                                    nullCellStack.stackSize = getItemAt(inv, centerX, centerY).stackSize + 1;
                                    setItemAt(inv, centerX, centerY, nullCellStack);
                                    sd.stackSize++;
                                    ((IScrewdriver) sd.getItem()).damage(sd, nullCellStack.stackSize - 1, null, false);
                                }
                                return wire;
                            }
                        }
                    }
                }

                // Screwdriver below
                if (centerY < 2) {
                    ItemStack sd = getItemAt(inv, centerX, centerY + 1);
                    if (sd != null && sd.getItem() instanceof ItemScrewdriver) {
                        RedwireType t = gnc.getTypeA();
                        boolean bundled = gnc.isBundledA();
                        if (t != null) {
                            boolean can = true;
                            if (centerY > 0)
                                if (getItemAt(inv, centerX, centerY - 1) != null)
                                    can = false;
                            if (centerX > 0)
                                if (getItemAt(inv, centerX - 1, centerY) != null)
                                    can = false;
                            if (!((IScrewdriver) sd.getItem()).damage(sd, getItemAt(inv, centerX, centerY).stackSize, null, true))
                                can = false;

                            if (can) {
                                ItemStack wire = PartManager.getPartInfo("wire." + t.getName() + (bundled ? ".bundled" : "")).getStack(
                                        getItemAt(inv, centerX, centerY).stackSize);
                                if (isCrafting) {
                                    gnc = new GateNullCell(null, false, gnc.getTypeB(), gnc.isBundledB());
                                    ItemStack nullCellStack = GateNullCell.getStackWithData(gnc);
                                    nullCellStack.stackSize = getItemAt(inv, centerX, centerY).stackSize + 1;
                                    setItemAt(inv, centerX, centerY, nullCellStack);
                                    sd.stackSize++;
                                    ((IScrewdriver) sd.getItem()).damage(sd, nullCellStack.stackSize - 1, null, false);
                                }
                                return wire;
                            }
                        }
                    }
                }

                // Screwdriver on the left
                if (centerX > 0) {
                    ItemStack sd = getItemAt(inv, centerX - 1, centerY);
                    if (sd != null && sd.getItem() instanceof ItemScrewdriver) {
                        RedwireType tA = gnc.getTypeA();
                        boolean bundledA = gnc.isBundledA();
                        RedwireType tB = gnc.getTypeB();
                        boolean bundledB = gnc.isBundledB();
                        if (tA != null || tB != null) {
                            boolean can = true;
                            if (centerY > 0)
                                if (getItemAt(inv, centerX, centerY - 1) != null)
                                    can = false;
                            if (centerY < 2)
                                if (getItemAt(inv, centerX, centerY + 1) != null)
                                    can = false;
                            int amt = 0;
                            if (tA != null && (tB == null || tB == tA))
                                amt += getItemAt(inv, centerX, centerY).stackSize;
                            if (tB != null)
                                amt += getItemAt(inv, centerX, centerY).stackSize;
                            if (amt > 64)
                                can = false;
                            if (!((IScrewdriver) sd.getItem()).damage(sd, amt, null, true))
                                can = false;

                            if (can) {
                                ItemStack wire = null;
                                if (tB != null) {
                                    wire = PartManager.getPartInfo("wire." + tB.getName() + (bundledB ? ".bundled" : "")).getStack(amt);
                                } else {
                                    wire = PartManager.getPartInfo("wire." + tA.getName() + (bundledA ? ".bundled" : "")).getStack(amt);
                                }
                                if (isCrafting) {
                                    if (tA != null && tB != null && tA == tB) {
                                        gnc = new GateNullCell(null, false, null, false);
                                    } else if (tB != null) {
                                        gnc = new GateNullCell(gnc.getTypeA(), gnc.isBundledA(), null, false);
                                    } else if (tA != null) {
                                        gnc = new GateNullCell(null, false, gnc.getTypeB(), gnc.isBundledB());
                                    }
                                    ItemStack nullCellStack = GateNullCell.getStackWithData(gnc);
                                    nullCellStack.stackSize = getItemAt(inv, centerX, centerY).stackSize + 1;
                                    setItemAt(inv, centerX, centerY, nullCellStack);
                                    sd.stackSize++;
                                    System.out.println(((IScrewdriver) sd.getItem()).damage(sd, amt, null, false) + " " + player);
                                }
                                return wire;
                            }
                        }
                    }
                }
            }

            // Adding
            {
                // Wire on top
                if (centerY > 0 && gnc.getTypeB() == null) {
                    ItemStack wire = getItemAt(inv, centerX, centerY - 1);
                    if (wire != null && wire.getItem() instanceof ItemPart) {
                        IPart p = ((ItemPart) wire.getItem()).createPart(wire, BluePower.proxy.getPlayer(), null, null);

                        if (p != null && p instanceof PartRedwireFaceUninsulated) {
                            RedwireType t = ((PartRedwireFace) p).getRedwireType(ForgeDirection.UNKNOWN);
                            boolean bundled = false;
                            boolean can = true;
                            if (centerY < 2)
                                if (getItemAt(inv, centerX, centerY + 1) != null)
                                    can = false;
                            if (centerX > 0)
                                if (getItemAt(inv, centerX - 1, centerY) != null)
                                    can = false;

                            if (can)
                                return GateNullCell.getStackWithData(new GateNullCell(gnc.getTypeA(), gnc.isBundledA(), t, bundled));
                        }
                    }
                }
                // Wire below
                if (centerY < 2 && gnc.getTypeA() == null) {
                    ItemStack wire = getItemAt(inv, centerX, centerY + 1);
                    if (wire != null && wire.getItem() instanceof ItemPart) {
                        IPart p = ((ItemPart) wire.getItem()).createPart(wire, BluePower.proxy.getPlayer(), null, null);

                        if (p != null && p instanceof PartRedwireFaceUninsulated) {
                            RedwireType t = ((PartRedwireFace) p).getRedwireType(ForgeDirection.UNKNOWN);
                            boolean bundled = false;
                            boolean can = true;
                            if (centerY > 0)
                                if (getItemAt(inv, centerX, centerY - 1) != null)
                                    can = false;
                            if (centerX > 0)
                                if (getItemAt(inv, centerX - 1, centerY) != null)
                                    can = false;

                            if (can)
                                return GateNullCell.getStackWithData(new GateNullCell(t, bundled, gnc.getTypeB(), gnc.isBundledB()));
                        }
                    }
                }
                // Wire below
                if (centerY == 1 && gnc.getTypeA() == null && gnc.getTypeB() == null) {
                    ItemStack wireA = getItemAt(inv, centerX, centerY + 1);
                    ItemStack wireB = getItemAt(inv, centerX, centerY - 1);
                    if (wireA != null && wireA.getItem() instanceof ItemPart && wireB != null && wireB.getItem() instanceof ItemPart) {
                        IPart pA = ((ItemPart) wireA.getItem()).createPart(wireA, BluePower.proxy.getPlayer(), null, null);
                        IPart pB = ((ItemPart) wireB.getItem()).createPart(wireB, BluePower.proxy.getPlayer(), null, null);

                        if (pA != null && pA instanceof PartRedwireFaceUninsulated && pB != null
                                && pB instanceof PartRedwireFaceUninsulated) {
                            RedwireType tA = ((PartRedwireFace) pA).getRedwireType(ForgeDirection.UNKNOWN);
                            boolean bundledA = false;
                            RedwireType tB = ((PartRedwireFace) pB).getRedwireType(ForgeDirection.UNKNOWN);
                            boolean bundledB = false;
                            boolean can = true;
                            if (centerX > 0)
                                if (getItemAt(inv, centerX - 1, centerY) != null)
                                    can = false;

                            if (can)
                                return GateNullCell.getStackWithData(new GateNullCell(tA, bundledA, tB, bundledB));
                        }
                    }
                }
            }
        }

        return null;
    }

}
