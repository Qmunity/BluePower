package com.bluepowermod.convert.part;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.convert.IPartConverter;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.wire.redstone.PartRedwireFace;

public class PartConverterWire implements IPartConverter {

    @Override
    public boolean matches(String id) {

        return id.startsWith("bluepower_bluestoneWire");
    }

    @Override
    public IPart convert(NBTTagCompound old) {

        String id = old.getString("part_id").replace("bluestoneWire", "wire.bluestone").replace("silver", "light_gray");
        PartInfo info = PartManager.getPartInfo(id);
        if (info == null)
            return null;
        PartRedwireFace part = (PartRedwireFace) info.create();

        NBTTagCompound data = old.getCompoundTag("partData");

        part.setFace(ForgeDirection.getOrientation(data.getInteger("face")));

        return part;
    }

}
