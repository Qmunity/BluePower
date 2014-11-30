package com.bluepowermod.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import net.minecraft.entity.item.EntityFireworkRocket;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

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
        ItemStack rocket = new ItemStack(Items.fireworks);

        ItemStack itemstack1 = getFireworkCharge();

        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();

        if (itemstack1 != null && itemstack1.getItem() == Items.firework_charge && itemstack1.hasTagCompound()
                && itemstack1.getTagCompound().hasKey("Explosion")) {
            nbttaglist.appendTag(itemstack1.getTagCompound().getCompoundTag("Explosion"));
        }

        nbttagcompound1.setTag("Explosions", nbttaglist);
        nbttagcompound1.setByte("Flight", (byte) 2);
        nbttagcompound.setTag("Fireworks", nbttagcompound1);

        rocket.setTagCompound(nbttagcompound);

        EntityFireworkRocket entity = new EntityFireworkRocket(world, x, y, z, rocket);
        world.spawnEntityInWorld(entity);
    }

    private static ItemStack getFireworkCharge() {
        ItemStack charge = new ItemStack(Items.firework_charge);
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        byte b0 = 0;
        ArrayList arraylist = new ArrayList();

        arraylist.add(Integer.valueOf(ItemDye.field_150922_c[rand.nextInt(16)]));

        if (rand.nextBoolean())
            nbttagcompound1.setBoolean("Flicker", true);

        if (rand.nextBoolean())
            nbttagcompound1.setBoolean("Trail", true);

        b0 = (byte) rand.nextInt(5);

        int[] aint = new int[arraylist.size()];

        for (int j2 = 0; j2 < aint.length; ++j2) {
            aint[j2] = ((Integer) arraylist.get(j2)).intValue();
        }

        nbttagcompound1.setIntArray("Colors", aint);
        nbttagcompound1.setByte("Type", b0);
        nbttagcompound.setTag("Explosion", nbttagcompound1);
        charge.setTagCompound(nbttagcompound);
        return charge;
    }
}
