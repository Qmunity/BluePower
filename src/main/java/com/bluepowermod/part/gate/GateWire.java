package com.bluepowermod.part.gate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.wire.redstone.RedwireType;

public class GateWire {

    private ItemStack stack;
    private RedwireType type;

    public GateWire(ItemStack stack, RedwireType type) {

        this.stack = stack;
        this.type = type;
    }

    public GateWire() {

    }

    public ItemStack getStack() {

        return stack;
    }

    public RedwireType getType() {

        return type;
    }

    public void writeToNBT(NBTTagCompound tag) {

        stack.writeToNBT(tag);
        tag.setInteger("type", type.ordinal());
    }

    public void readFromNBT(NBTTagCompound tag) {

        if (stack == null)
            stack = new ItemStack(Blocks.stone);
        stack.readFromNBT(tag);
        type = RedwireType.values()[tag.getInteger("type")];
    }

    public void writeUpdate(DataOutput buffer) throws IOException {

        buffer.writeInt(type.ordinal());
    }

    public void readUpdate(DataInput buffer) throws IOException {

        type = RedwireType.values()[buffer.readInt()];
    }

}
