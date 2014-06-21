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
 *     @author Quetzi
 */

package net.quetzi.bluepower.tileentities.tier1;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.helper.IOHelper;
import net.quetzi.bluepower.tileentities.TileMachineBase;

public class TileTransposer extends TileMachineBase {

    private boolean isPowered;

    @Override
    public void updateEntity() {

        super.updateEntity();

    }

    @Override
    protected void redstoneChanged(boolean newValue) {

        if (newValue) {
            suckItems();
            pullItem();
        }

    }

    private void pullItem() {

        if(isBufferEmpty()) {
            ForgeDirection dir = getOutputDirection().getOpposite();
            TileEntity inputTE = getTileCache()[dir.ordinal()].getTileEntity();

            if (inputTE instanceof IInventory) {
                IInventory inputInv = (IInventory) inputTE;
                int[] accessibleSlots;
                if (inputInv instanceof ISidedInventory) {
                    accessibleSlots = ((ISidedInventory) inputInv).getAccessibleSlotsFromSide(dir.ordinal());
                } else {
                    accessibleSlots = new int[inputInv.getSizeInventory()];
                    for (int i = 0; i < accessibleSlots.length; i++) {
                        accessibleSlots[i] = i;
                    }
                }
                for (int slot : accessibleSlots) {
                    ItemStack stack = inputInv.getStackInSlot(slot);
                    if (stack != null && IOHelper.canExtractItemFromInventory(inputInv, stack, slot, dir.ordinal())) {
                        stack.stackSize = 1;
                        addItemToOutputBuffer(stack);
                        IOHelper.extractOneItem(inputTE, dir);
                        break;
                    }
                }
            }
        }
    }

    private void suckItems() {

    }
}
