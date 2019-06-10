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

package com.bluepowermod.util;

import net.minecraft.entity.item.FireworkRocketEntity;
import net.minecraft.item.Items;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DateEventHandler {

    private static Random rand = new Random();

    public static enum Event {
        HALLOWEEN(31, 10), NEW_YEAR(1, 1);

        private final int month;
        private final int day;

        Event(int day, int month) {

            this.month = month;
            this.day = day;
        }

        public boolean isEvent(Calendar calendar) {

            return calendar.get(2) + 1 == month && calendar.get(5) == day;
        }
    }

    public static boolean isEvent(Event event) {

        Calendar calendar = Calendar.getInstance();
        return event.isEvent(calendar);
    }

    public static void spawnFirework(World world, double x, double y, double z) {

        ItemStack rocket = new ItemStack(Items.FIREWORKS);

        ItemStack itemstack1 = getFireworkCharge();

        CompoundNBT nbttagcompound = new CompoundNBT();
        CompoundNBT nbttagcompound1 = new CompoundNBT();
        ListNBT nbttaglist = new ListNBT();

        if (!itemstack1.isEmpty() && itemstack1.getItem() == Items.FIREWORK_CHARGE && itemstack1.hasTagCompound()
                && itemstack1.getTagCompound().hasKey("Explosion")) {
            nbttaglist.appendTag(itemstack1.getTagCompound().getCompoundTag("Explosion"));
        }

        nbttagcompound1.setTag("Explosions", nbttaglist);
        nbttagcompound1.setByte("Flight", (byte) 2);
        nbttagcompound.setTag("Fireworks", nbttagcompound1);

        rocket.setTagCompound(nbttagcompound);

        FireworkRocketEntity entity = new FireworkRocketEntity(world, x, y, z, rocket);
        world.spawnEntity(entity);
    }

    private static ItemStack getFireworkCharge() {

        ItemStack charge = new ItemStack(Items.FIREWORK_CHARGE);
        CompoundNBT nbttagcompound = new CompoundNBT();
        CompoundNBT nbttagcompound1 = new CompoundNBT();
        byte b0 = 0;
        ArrayList<Integer> arraylist = new ArrayList<Integer>();

        arraylist.add(DyeItem.DYE_COLORS[rand.nextInt(16)]);

        if (rand.nextBoolean())
            nbttagcompound1.setBoolean("Flicker", true);

        if (rand.nextBoolean())
            nbttagcompound1.setBoolean("Trail", true);

        b0 = (byte) rand.nextInt(5);

        int[] aint = new int[arraylist.size()];

        for (int j2 = 0; j2 < aint.length; ++j2) {
            aint[j2] = arraylist.get(j2).intValue();
        }

        nbttagcompound1.setIntArray("Colors", aint);
        nbttagcompound1.setByte("Type", b0);
        nbttagcompound.setTag("Explosion", nbttagcompound1);
        charge.setTagCompound(nbttagcompound);
        return charge;
    }
}
