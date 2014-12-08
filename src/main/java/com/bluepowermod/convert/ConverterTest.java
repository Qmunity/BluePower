package com.bluepowermod.convert;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.PartBase;

public class ConverterTest implements IPartConverter {

    @Override
    public boolean matches(String id) {

        return id.equals("mcr_face");
    }

    @Override
    public IPart convert(NBTTagCompound old) {

        System.out.println("Converting " + old);

        return new PartBase() {

            @Override
            public String getType() {

                return "TestPart";
            }

            @Override
            public ItemStack getItem() {

                return null;
            }

            @Override
            public void writeToNBT(NBTTagCompound tag) {

                super.writeToNBT(tag);

                tag.setString("TestString", "TestValue");
            }
        };
    }

}
