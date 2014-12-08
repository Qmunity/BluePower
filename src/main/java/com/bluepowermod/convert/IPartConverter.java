package com.bluepowermod.convert;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.part.IPart;

public interface IPartConverter {

    public boolean matches(String id);

    public IPart convert(NBTTagCompound old);

}
