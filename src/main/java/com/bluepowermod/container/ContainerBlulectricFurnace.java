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

package com.bluepowermod.container;

import com.bluepowermod.container.slot.SlotMachineInput;
import com.bluepowermod.tile.tier2.TileRegulator;
import com.bluepowermod.tile.tier3.TileBlulectricFurnace;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.FurnaceContainer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MoreThanHidden
 */
public class ContainerBlulectricFurnace extends FurnaceContainer {
    private IIntArray fields;

    public ContainerBlulectricFurnace(int windowId, PlayerInventory invPlayer, IInventory inventory, IIntArray fields) {
        super(windowId, invPlayer, inventory, fields);
        this.fields = fields;

        inventorySlots.remove(new SlotMachineInput(inventory, 0, 21, 35));
    }

    public ContainerBlulectricFurnace( int id, PlayerInventory player )    {
        this( id, player, new Inventory( 2 ), new IntArray(6));
    }

    //fields.get(1) = Max | fields.get(0) = Amount
    @OnlyIn(Dist.CLIENT)
    public float getBufferPercentage() {

        if (fields.get(1) > 0) {
            return (float) fields.get(0) / (float) fields.get(1);
        } else {
            return 0;
        }
    }

    //fields.get(5) = Max | fields.get(4) = Amount
    @OnlyIn(Dist.CLIENT)
    public float getEnergyPercentage() {

        if (fields.get(5) > 0) {
            return (float) fields.get(4) / (float) fields.get(5);
        } else {
            return 0;
        }
    }

    //fields.get(6) | 0 = Both Disabled | 1 = Active | 2 = Both Enabled | 3 = Charged
    @OnlyIn(Dist.CLIENT)
    public boolean getActive() {
        return fields.get(6) == 1 || fields.get(6) == 2;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean getCharged() {
        return fields.get(6) == 2 || fields.get(6) == 3;
    }

}
