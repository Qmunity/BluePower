package com.bluepowermod.part;

import java.lang.reflect.Constructor;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.init.BPItems;

public class PartInfo {

    private final String type;
    private final BPPart example;
    private final ItemStack item;

    private Constructor<? extends BPPart> constructor;
    private Object[] arguments;

    public PartInfo(Class<? extends BPPart> clazz, Object... arguments) {

        this.arguments = arguments;

        generateConstructor(clazz, arguments);

        example = create();
        type = example.getType();

        item = new ItemStack(BPItems.multipart);
        item.stackTagCompound = new NBTTagCompound();
        item.stackTagCompound.setString("id", type);
    }

    private void generateConstructor(Class<? extends BPPart> clazz, Object... arguments) {

        try {
            Class<?>[] argsClasses = new Class<?>[arguments.length];
            for (int i = 0; i < arguments.length; i++)
                argsClasses[i] = arguments[i].getClass();

            constructor = clazz.getConstructor(argsClasses);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public BPPart create() {

        try {
            return constructor.newInstance(arguments);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String getType() {

        return type;
    }

    public BPPart getExample() {

        return example;
    }

    public ItemStack getItem() {

        return item;
    }

}
