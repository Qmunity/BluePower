package com.bluepowermod.convert.part;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.convert.IPartConverter;
import com.bluepowermod.part.BPPartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.GateBase;

public class PartConverterGate implements IPartConverter {

    @Override
    public boolean matches(String id) {

        return id.startsWith("bluepower_");
    }

    @Override
    public IPart convert(NBTTagCompound old) {

        String id = old.getString("part_id");
        BPPartInfo info = PartManager.getPartInfo(id);
        if (info == null)
            return null;

        IPart p = info.create();

        if (!(p instanceof GateBase<?, ?, ?, ?, ?, ?>))
            return null;

        GateBase<?, ?, ?, ?, ?, ?> part = (GateBase<?, ?, ?, ?, ?, ?>) p;
        NBTTagCompound data = old.getCompoundTag("partData");
        part.readFromNBT(data);
        part.setRotation(data.getInteger("rotation") + 2);

        return part;
    }

}
