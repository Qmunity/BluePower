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

import com.bluepowermod.api.misc.MinecraftColor;
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

        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);

        ItemStack itemstack1 = getFireworkCharge();

        CompoundNBT nbttagcompound = new CompoundNBT();
        CompoundNBT nbttagcompound1 = new CompoundNBT();
        ListNBT nbttaglist = new ListNBT();

        if (!itemstack1.isEmpty() && itemstack1.getItem() == Items.FIREWORK_STAR && itemstack1.hasTag()
                && itemstack1.getTag().hasUniqueId("Explosion")) {
            nbttaglist.add(itemstack1.getTag().getCompound("Explosion"));
        }

        nbttagcompound1.put("Explosions", nbttaglist);
        nbttagcompound1.putByte("Flight", (byte) 2);
        nbttagcompound.put("Fireworks", nbttagcompound1);

        rocket.setTag(nbttagcompound);

        FireworkRocketEntity entity = new FireworkRocketEntity(world, x, y, z, rocket);
        world.addEntity(entity);
    }

    private static ItemStack getFireworkCharge() {

        ItemStack charge = new ItemStack(Items.FIREWORK_STAR);
        CompoundNBT nbttagcompound = new CompoundNBT();
        CompoundNBT nbttagcompound1 = new CompoundNBT();
        byte b0 = 0;
        ArrayList<Integer> arraylist = new ArrayList<Integer>();
        arraylist.add( MinecraftColor.values()[rand.nextInt(16)].getHex());

        if (rand.nextBoolean())
            nbttagcompound1.putBoolean("Flicker", true);

        if (rand.nextBoolean())
            nbttagcompound1.putBoolean("Trail", true);

        b0 = (byte) rand.nextInt(5);

        int[] aint = new int[arraylist.size()];

        for (int j2 = 0; j2 < aint.length; ++j2) {
            aint[j2] = arraylist.get(j2);
        }

        nbttagcompound1.putIntArray("Colors", aint);
        nbttagcompound1.putByte("Type", b0);
        nbttagcompound.put("Explosion", nbttagcompound1);
        charge.setTag(nbttagcompound);
        return charge;
    }
}
